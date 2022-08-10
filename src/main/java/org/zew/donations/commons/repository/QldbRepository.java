package org.zew.donations.commons.repository;

import java.util.List;
import java.util.Optional;

public interface QldbRepository<E extends Entity> {

    Optional<E> findById(String id);

    List<E> findAll();

    E save(final E entity);

}
