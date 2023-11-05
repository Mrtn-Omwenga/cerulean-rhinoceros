package org.zew.donations.model.response;

import java.math.BigDecimal;

import org.zew.donations.commons.repository.Id;
import org.zew.donations.model.OwnerType;
import org.zew.donations.model.WalletType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class WalletResponse {
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
}
