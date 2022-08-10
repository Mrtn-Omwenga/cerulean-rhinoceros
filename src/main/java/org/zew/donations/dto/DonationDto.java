package org.zew.donations.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.zew.donations.model.Amount;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class DonationDto {
    private String transactionTimestamp;
    private String transactionId;
    private String ownerId;
    private String crmOwnerId;
    private String source;
    private double totalAmount;
    private String currency;
    private double originalAmount;
    private String originalCurrency;
    private double currencyConversion;
    private Distribution distribution;

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class Distribution {
        private Amount overheads;
        private Amount development;
        private Amount fees;
        private List<Mission> missions;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    public static class Mission extends Amount {
        private String missionId;
    }
}
