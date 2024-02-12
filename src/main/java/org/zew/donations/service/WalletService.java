package org.zew.donations.service;

import org.zew.donations.model.Wallet;
import org.zew.donations.model.request.WalletUpdateRequest;
import org.zew.donations.model.WalletType;

public interface WalletService {

    Wallet findById(String id);

    Wallet create(Wallet wallet);

    Wallet updateAmounts(String walletId, WalletUpdateRequest request);

    Wallet findByOwnerIdAndType(String ownerId, WalletType walletType);

    void update(Wallet wallet);

    int getCountByMissionIdGroupByOwnerId(String missionId);
}
