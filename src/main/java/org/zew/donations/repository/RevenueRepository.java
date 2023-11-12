package org.zew.donations.repository;

import java.util.List;

import org.zew.donations.commons.repository.QldbRepository;
import org.zew.donations.model.Revenue;

public interface RevenueRepository extends QldbRepository<Revenue> {

    boolean existsById(String revenueId);
    List<Revenue> findByOwnerId(String ownerId);

}
