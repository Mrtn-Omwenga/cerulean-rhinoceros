package org.zew.donations.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.zew.donations.commons.repository.Entity;
import org.zew.donations.commons.repository.Id;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Wallet implements Entity {
    @Id
    private String walletId;
    private String ownerId;
    private String crmOwnerId;
    private OwnerType ownerType;
    private String missionId;
    private WalletType walletType;
    private BigDecimal availableAmount;
    private BigDecimal totalAmount;
    private String currency;

    @Override
    public String getId() {
        return walletId;
    }
}
