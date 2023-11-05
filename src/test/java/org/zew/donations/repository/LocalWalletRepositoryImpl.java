package org.zew.donations.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.zew.donations.model.Wallet;
import org.zew.donations.model.WalletType;

public class LocalWalletRepositoryImpl implements WalletRepository {

  private Map<String, Wallet> wallets = new HashMap<>();

  public void reset() {
    wallets.clear();
  }

  @Override
  public boolean existsByOwnerIdAndType(String ownerId, WalletType walletType) {
    for (Wallet wallet : wallets.values()) {
      if (wallet.getOwnerId().equals(ownerId) && wallet.getWalletType() == walletType) {
        return true;
      }
    }
    return false;
  }

  @Override
  public Optional<Wallet> findById(String id) {
    if (wallets.containsKey(id)) {
      return Optional.of(wallets.get(id));
    }
    return Optional.empty();
  }

  @Override
  public List<Wallet> findAll() {
    return new ArrayList<Wallet>(wallets.values());
  }

  @Override
  public Wallet save(Wallet entity) {
    wallets.put(entity.getId(), entity);
    return entity;
  }
  
}
