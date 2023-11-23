package org.zew.donations.commons.repository;

import com.amazon.ion.IonInt;
import com.amazon.ion.IonStruct;
import com.amazon.ion.IonValue;
import com.fasterxml.jackson.dataformat.ion.IonObjectMapper;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import software.amazon.qldb.QldbDriver;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public abstract class AbstractQldbQldbRepository<E extends Entity> implements QldbRepository<E> {

    @SuppressWarnings("unchecked")
    private final Class<E> clazz = (Class<E>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    private final String tableName = clazz.getSimpleName().toUpperCase();

    @Autowired
    private QldbDriver qldbDriver;

    @Autowired
    private IonObjectMapper ionObjectMapper;

    public Optional<E> findById(String id) {
        return query("SELECT * FROM " + tableName + " WHERE " + EntityHelper.getIdField(clazz).getName() + " = ?", id).stream().findFirst();
    }

    public List<E> findAll() {
        return query("SELECT * FROM " + tableName);
    }

    public E save(E entity) {
        return qldbDriver.execute(txn -> {
            try {
                if (ObjectUtils.isNotEmpty(entity.getId())) {
                    var query = "UPDATE " + tableName + " as e SET e = ? WHERE " + EntityHelper.getIdField(clazz).getName() + " = ?";
                    var bodyParams = ionObjectMapper.writeValueAsIonValue(entity);
                    var idParam = ionObjectMapper.writeValueAsIonValue(entity.getId());
                    txn.execute(query, bodyParams, idParam);
                    return entity;
                }
                EntityHelper.setIdValue(entity, clazz);
                txn.execute("INSERT INTO " + tableName + " ?", ionObjectMapper.writeValueAsIonValue(entity));
                return entity;
            } catch (IOException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        });
    }

    protected List<E> query(String query, Object... args) {
        return qldbDriver.execute(txn -> {
            var result = txn.execute(query, getParameters(args));
            return StreamSupport.stream(result.spliterator(), false).map(this::mapEntity).toList();
        });
    }

    @SafeVarargs
    protected final List<E> query(Pair<String, Object>... args) {
        var query = "SELECT * FROM " + tableName + " WHERE ";
        return qldbDriver.execute(txn -> {
            var result = txn.execute(query + getStringArguments(args), getParameters(args));
            return StreamSupport.stream(result.spliterator(), false).map(this::mapEntity).toList();
        });
    }

    @SafeVarargs
    protected final boolean exists(Pair<String, Object>... args) {
        var query = "SELECT COUNT(*) FROM " + tableName + " WHERE ";
        return qldbDriver.execute(txn -> {
            var result = txn.execute(query + getStringArguments(args), getParameters(args));
            return ((IonInt) ((IonStruct) result.iterator().next()).get("_1")).intValue() > 0;
        });
    }

    private static String getStringArguments(Pair<String, Object>[] args) {
        return String.join(" AND ", Arrays.stream(args).map(pair -> pair.getLeft() + " = ? ").toList());
    }

    private E mapEntity(IonValue entity) {
        try {
            return ionObjectMapper.readValue(entity, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<IonValue> getParameters(Object[] args) {
        return Stream.of(args).map(value -> {
            try {
                return ionObjectMapper.writeValueAsIonValue(value);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).toList();
    }

    private List<IonValue> getParameters(Pair<String, Object>[] args) {
        return getParameters(Arrays.stream(args).map(Pair::getRight).toArray());
    }

}
