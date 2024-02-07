package org.zew.donations.repository;

import org.zew.donations.commons.repository.QldbRepository;
import org.zew.donations.model.Wallet;
import org.zew.donations.model.WalletType;

import java.util.List;

public interface WalletRepository extends QldbRepository<Wallet> {

    boolean existsByOwnerIdAndType(String ownerId, WalletType walletType);

    Wallet getByOwnerIdAndType(String ownerId, WalletType walletType);

    int getCountByMissionIdGroupByOwnerId(String missionId);

}
