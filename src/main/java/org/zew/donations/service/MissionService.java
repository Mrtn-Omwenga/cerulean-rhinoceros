package org.zew.donations.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zew.donations.model.Revenue;
import org.zew.donations.dto.SupportedMissionResponse;
import org.zew.donations.repository.RevenueRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MissionService {

    @Autowired
    private RevenueRepository revenueRepository;

    public List<SupportedMissionResponse> getSupportedMissions(String contactId) {
        List<Revenue> revenues = revenueRepository.findByOwnerId(contactId);

        // Group by missionId and sum the amounts
        return revenues.stream()
            .collect(Collectors.groupingBy(Revenue::getToWalletId))
            .entrySet().stream()
            .map(entry -> {
                String missionId = entry.getKey();
                BigDecimal totalAmount = entry.getValue().stream()
                    .map(Revenue::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
                String currency = entry.getValue().get(0).getCurrency(); // Assuming all have the same currency

                return new SupportedMissionResponse(missionId, totalAmount, currency);
            })
            .collect(Collectors.toList());
    }
}
