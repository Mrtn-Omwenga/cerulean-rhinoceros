package org.zew.donations.service;

import org.zew.donations.model.Revenue;

public interface RevenueService {

    Revenue findById(String id);

    Revenue create(Revenue revenue);
}
