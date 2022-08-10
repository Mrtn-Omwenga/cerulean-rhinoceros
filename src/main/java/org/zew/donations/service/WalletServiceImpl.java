package org.zew.donations.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zew.donations.model.Wallet;
import org.zew.donations.repository.WalletRepository;

@Service
public class WalletServiceImpl implements WalletService {

    @Autowired
    private WalletRepository walletRepository;

    @Override
    public Wallet create(Wallet wallet) {
        if (walletRepository.existsByOwnerIdAndType(wallet.getOwnerId(), wallet.getWalletType())) {
            throw new RuntimeException("Wallet already exists");
        }
        return walletRepository.save(wallet);
    }

    @Override
    public Wallet findById(String id) {
        return walletRepository.findById(id).orElseThrow();
    }
}
