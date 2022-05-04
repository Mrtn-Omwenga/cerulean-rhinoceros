package org.zew.donations;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.money.MonetaryAmount;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Map;

@AllArgsConstructor
@Builder
@Getter
public class DonationDto {
    private ZonedDateTime transactionTimestamp;
    private String transactionId;
    private String ownerId;
    private String crmOwnerId;
    private String source;
    private MonetaryAmount amount;
    private MonetaryAmount originalAmount;
    private BigDecimal currencyConversionRate;
    private MonetaryAmount overheadsDistribution;
    private MonetaryAmount developmentDistribution;
    private MonetaryAmount feesDistribution;
    private Map<String, MonetaryAmount> missionsDistribution;
}
