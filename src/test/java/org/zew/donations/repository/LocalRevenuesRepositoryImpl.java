package org.zew.donations.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.zew.donations.model.Revenue;

public class LocalRevenuesRepositoryImpl implements RevenuesRepository {

  private Map<String, Revenue> revenues = new HashMap<>();

  public void reset() {
    revenues.clear();
  }

  @Override
  public Optional<Revenue> findById(String id) {
    if (revenues.containsKey(id)) {
      return Optional.of(revenues.get(id));
    }
    return Optional.empty();
  }

  @Override
  public List<Revenue> findAll() {
    return new ArrayList<Revenue>(revenues.values());
  }

  @Override
  public Revenue save(Revenue entity) {
    revenues.put(entity.getId(), entity);
    return entity;
  }
  
}
