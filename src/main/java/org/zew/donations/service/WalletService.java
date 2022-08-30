package org.zew.donations.service;

import org.zew.donations.model.Wallet;

public interface WalletService {

    Wallet create(Wallet wallet);

    Wallet findById(String id);

}
