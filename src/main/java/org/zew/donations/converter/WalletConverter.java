package org.zew.donations.converter;

import org.zew.donations.model.Wallet;
import org.zew.donations.model.response.WalletResponse;

public class WalletConverter {

    public static WalletResponse fromWalletToResponse(Wallet wallet) {
        return WalletResponse
                .builder()
                .walletId(wallet.getWalletId())
                .ownerId(wallet.getOwnerId())
                .crmOwnerId(wallet.getCrmOwnerId())
                .ownerType(wallet.getOwnerType())
                .missionId(wallet.getMissionId())
                .walletType(wallet.getWalletType())
                .availableAmount(wallet.getAvailableAmount())
                .totalAmount(wallet.getTotalAmount())
                .currency(wallet.getCurrency())
                .build();
    }

}
