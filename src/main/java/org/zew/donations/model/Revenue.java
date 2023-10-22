package org.zew.donations.model;

import lombok.*;
import lombok.experimental.Accessors;
import org.zew.donations.commons.repository.Entity;
import org.zew.donations.commons.repository.Id;

import java.math.BigDecimal;
import java.sql.Timestamp;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Accessors(chain = true)
public class Revenue implements Entity {
    @Id
    private String revenueId;
    private String ownerId;
    private String crmOwnerId;
    private String toWalletId;
    private BigDecimal amount;
    private String currency;
    private SourceType sourceType; // Assuming you have an enum called SourceType with PAYPAL_EMAIL, PAYPAL_CREDITORDEBID
    private String transactionId;
    private Timestamp timestamp;
    private String invoiceId;
    private BigDecimal originalAmount;
    private String originalCurrency;
    private String currencyConversion;

    @Override
    public String getId() {
        return revenueId;
    }
}
