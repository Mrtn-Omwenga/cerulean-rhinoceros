package org.zew.donations.repository;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Repository;
import org.zew.donations.commons.repository.AbstractQldbQldbRepository;
import org.zew.donations.model.Wallet;
import org.zew.donations.model.WalletType;

import java.util.List;

@Repository
public class WalletRepositoryImpl extends AbstractQldbQldbRepository<Wallet> implements WalletRepository {

    @Override
    public boolean existsByOwnerIdAndType(String ownerId, WalletType walletType) {
        return exists(Pair.of("ownerId", ownerId), Pair.of("walletType", walletType));
    }

    @Override
    public Wallet getByOwnerIdAndType(String ownerId, WalletType walletType) {
        if (existsByOwnerIdAndType(ownerId, walletType)) {
            return query(Pair.of("ownerId", ownerId), Pair.of("walletType", walletType)).get(0);
        }
        return null;
    }

    @Override
    public int getCountByMissionIdGroupByOwnerId(String missionId){
        return queryCount(
                "SELECT COUNT(*) FROM " + Wallet.class.getSimpleName().toUpperCase() + " WHERE missionId = " + missionId + "GROUP BY ownerId"
        );
    }


    public List getTotalAmountByOwnerId(String ownerId){
        return query(
                "SELECT * FROM" + Wallet.class.getSimpleName().toUpperCase() + " WHERE ownerId = " + ownerId +  " AND walletType = IN_MISSION"
        );
    }

}
