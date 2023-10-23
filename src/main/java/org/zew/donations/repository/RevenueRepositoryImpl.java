package org.zew.donations.repository;

import java.util.List;

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
    
    
    @Override
    public List<Revenue> findByOwnerId(String ownerId) {
        Pair<String, Object> queryPair = Pair.of("ownerId", ownerId);
        return query(queryPair);
    }

}
