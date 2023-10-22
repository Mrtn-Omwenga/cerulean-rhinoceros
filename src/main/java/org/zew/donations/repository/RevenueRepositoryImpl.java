package org.zew.donations.repository;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Repository;
import org.zew.donations.commons.repository.AbstractQldbQldbRepository;
import org.zew.donations.model.Revenue;

@Repository
public class RevenueRepositoryImpl extends AbstractQldbQldbRepository<Revenue> implements RevenueRepository {

    @Override
    public boolean existsById(String revenueId) {
        return exists(Pair.of("revenueId", revenueId));
    }

}
