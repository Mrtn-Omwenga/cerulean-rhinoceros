package org.zew.donations.service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zew.donations.model.Revenue;
import org.zew.donations.model.SourceType;
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

    @Override
    public List<Revenue> getAllRevenuesByOwnerId(String ownerId) {
        return revenueRepository.findByOwnerId(ownerId);
    }

    @Override
    public Map<String, Object> getFinancialsByOwnerId(String ownerId, String currency) {
        List<Revenue> revenues = revenueRepository.findByOwnerId(ownerId);

        BigDecimal totalAmount = revenues.stream()
                .map(Revenue::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Revenue lastDonationRevenue = revenues.stream()
                .max(Comparator.comparing(Revenue::getTimestamp))
                .orElse(null);

        BigDecimal lastDonationAmount = BigDecimal.ZERO;
        String lastDonationDate = null;
        String lastDonationSource = null;

        if (lastDonationRevenue != null) {
            lastDonationAmount = revenues.stream()
                    .filter(r -> r.getTransactionId().equals(lastDonationRevenue.getTransactionId()))
                    .map(Revenue::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            
            lastDonationDate = lastDonationRevenue.getTimestamp().toLocalDateTime().toString();
            lastDonationSource = translateSourceType(lastDonationRevenue.getSourceType());
        }


        Map<String, Object> response = new HashMap<>();
        response.put("revenues", revenues);
        response.put("lastDonation", Map.of("amount", lastDonationAmount, "currency", currency,"date", lastDonationDate,
        "source", lastDonationSource));
        response.put("totals", Map.of("amount", totalAmount, "currency", currency));

        return response;
    }

    private String translateSourceType(SourceType sourceType) {
        switch (sourceType) {
            case PAYPAL_EMAIL:
            case PAYPAL_CREDITORDEBID:
                return "Paypal";
            // Add other cases as needed
            default:
                return sourceType.toString();
        }
    }

}
