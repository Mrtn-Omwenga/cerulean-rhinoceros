package org.zew.donations.service;

import org.zew.donations.model.Wallet;
import org.zew.donations.model.request.WalletUpdateRequest;

public interface WalletService {

    Wallet findById(String id);

    Wallet create(Wallet wallet);

    Wallet updateAmounts(String walletId, WalletUpdateRequest request);

}
