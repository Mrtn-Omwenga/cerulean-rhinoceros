package org.zew.donations.repository;

import org.zew.donations.commons.repository.QldbRepository;
import org.zew.donations.model.Revenue;

public interface RevenueRepository extends QldbRepository<Revenue> {

    boolean existsById(String revenueId);

}
