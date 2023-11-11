package org.zew.donations.repository;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Repository;
import org.zew.donations.commons.repository.AbstractQldbQldbRepository;
import org.zew.donations.model.Wallet;
import org.zew.donations.model.WalletType;

@Repository
public class WalletRepositoryImpl extends AbstractQldbQldbRepository<Wallet> implements WalletRepository {

    @Override
    public boolean existsByOwnerIdAndType(String ownerId, WalletType walletType) {
        return exists(Pair.of("ownerId", ownerId), Pair.of("walletType", walletType));
    }

    @Override
    public Wallet getByOwnerIdAndType(String ownerId, WalletType walletType) {
        return query(Pair.of("ownerId", ownerId), Pair.of("walletType", walletType)).get(0);
    }

}
