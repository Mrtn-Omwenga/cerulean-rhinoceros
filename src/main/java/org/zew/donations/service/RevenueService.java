package org.zew.donations.service;

import java.util.List;
import java.util.Map;

import org.zew.donations.controller.exception.RevenueAlreadyExistsException;
import org.zew.donations.model.Revenue;

public interface RevenueService {

    Revenue findById(String id);

    Revenue create(Revenue revenue) throws RevenueAlreadyExistsException;

    List<Revenue> getAllRevenuesByOwnerId(String ownerId);

    Map<String, Object> getFinancialsByOwnerId(String ownerId, String currency);

}
