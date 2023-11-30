package org.zew.donations.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zew.donations.model.Operation;
import org.zew.donations.model.Wallet;
import org.zew.donations.model.request.WalletUpdateRequest;
import org.zew.donations.model.WalletType;
import org.zew.donations.repository.WalletRepository;

import java.math.BigDecimal;
import java.util.function.BiFunction;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

@Service
public class WalletServiceImpl implements WalletService {

    @Autowired
    private WalletRepository walletRepository;

    @Override
    public Wallet findById(String id) {
        return walletRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Wallet not found"));
    }

    @Override
    public Wallet create(Wallet wallet) {
        if (walletRepository.existsByOwnerIdAndType(wallet.getOwnerId(), wallet.getWalletType())) {
            throw new RuntimeException("Wallet already exists");
        }
        return walletRepository.save(wallet);
    }

    @Override
    public Wallet updateAmounts(String walletId, WalletUpdateRequest request) {
        var wallet = findById(walletId);
        BiFunction<BigDecimal, BigDecimal, BigDecimal> func = request.getOperation() == Operation.INCREMENT ? BigDecimal::add : BigDecimal::subtract;
        return walletRepository.save(wallet
                .setAvailableAmount(executeOperation(wallet.getAvailableAmount(), request.getAvailableAmount(), func))
                .setTotalAmount(executeOperation(wallet.getTotalAmount(), request.getTotalAmount(), func)));
    }

    private BigDecimal executeOperation(BigDecimal walletAmount, BigDecimal updateRequestAmount, BiFunction<BigDecimal, BigDecimal, BigDecimal> func) {
        return func.apply(walletAmount, defaultIfNull(updateRequestAmount, BigDecimal.ZERO));
    }

    @Override
    public Wallet findByOwnerIdAndType(String ownerId, WalletType walletType) {
        return walletRepository.getByOwnerIdAndType(ownerId, walletType);
    }

    @Override
    public void update(Wallet wallet) {
        if (!walletRepository.existsByOwnerIdAndType(wallet.getOwnerId(), wallet.getWalletType())) {
            throw new RuntimeException("Wallet does not exist");
        }
        walletRepository.save(wallet);
    }
}
