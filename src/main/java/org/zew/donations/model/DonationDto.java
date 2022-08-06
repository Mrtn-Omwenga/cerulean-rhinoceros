package org.zew.donations.model;

import lombok.*;

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
    private Distribution distribution;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Distribution {

        private Amount overheads;
        private Amount development;
        private Amount fees;
        private List<Mission> missions;

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Amount {
        private double amount;
        private String currency;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Mission {
        private String missionId;
        private double amount;
        private String currency;
    }
}
