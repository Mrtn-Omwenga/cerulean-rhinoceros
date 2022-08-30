package org.zew.donations.commons.repository;

import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.UUID;
import java.util.stream.Stream;

public class EntityIdHelper {

    public static void setIdValue(Object entity, Class<? extends Entity> clazz) throws IllegalAccessException {
        var idField = getIdField(clazz);
        idField.setAccessible(true);
        idField.set(entity, UUID.randomUUID().toString());
    }

    public static Field getIdField(Class<? extends Entity> clazz) {
        var idFields = Stream.of(clazz.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Id.class))
                .toList();

        if (CollectionUtils.isEmpty(idFields)) {
            throw new RuntimeException("Entity without an Id field");
        }

        if (idFields.size() > 1) {
            throw new RuntimeException("Entity with more than one Id field");
        }

        return idFields.iterator().next();
    }

}
