package org.zew.donations.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zew.donations.model.Revenue;
import org.zew.donations.repository.RevenueRepository;

@Service
public class RevenueServiceImpl implements RevenueService {

    @Autowired
    private RevenueRepository revenueRepository;

    @Override
    public Revenue findById(String id) {
        return revenueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Revenue not found"));
    }

    @Override
    public Revenue create(Revenue revenue) {
        if (revenueRepository.existsById(revenue.getRevenueId())) {
            throw new RuntimeException("Revenue already exists");
        }
        return revenueRepository.save(revenue);
    }

    // Additional methods can be added as needed
}
