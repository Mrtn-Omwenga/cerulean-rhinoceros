package org.zew.donations.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zew.donations.dto.DonationDto;
import org.zew.donations.dto.DonationDto.Mission;
import org.zew.donations.model.Revenue;
import org.zew.donations.model.Wallet;
import org.zew.donations.model.WalletType;
import org.zew.donations.repository.RevenuesRepository;

@Service
public class DonationServiceImpl implements DonationService {

    @Autowired
    private RevenuesRepository revenuesRepository;

    @Autowired
    private WalletService walletService;

    @Override
    public void saveDonation(DonationDto donationDto) {

        Wallet overheadsWallet = walletService.findByOwnerIdAndType(donationDto.getOwnerId(), WalletType.IN_Overheads);
        Revenue overheadsRevenue = Revenue
                .builder()
                .ownerId(donationDto.getOwnerId())
                .crmOwnerId(donationDto.getCrmOwnerId())
                .toWalletId(overheadsWallet.getId())
                .amount(donationDto.getDistribution().getOverheads().getAmount())
                .currency(donationDto.getCurrency())
                .source(donationDto.getSource())
                .transactionId(donationDto.getTransactionId())
                .timestamp(donationDto.getTransactionTimestamp())
                .originalAmount(new BigDecimal(donationDto.getOriginalAmount()))
                .originalCurrency(donationDto.getOriginalCurrency())
                .currencyConversion(donationDto.getCurrencyConversion())
                .build();
        revenuesRepository.save(overheadsRevenue);
        overheadsWallet.setAvailableAmount(overheadsWallet.getAvailableAmount().add(donationDto.getDistribution().getOverheads().getAmount()));
        walletService.update(overheadsWallet);

        Wallet developmentWallet = walletService.findByOwnerIdAndType(donationDto.getOwnerId(), WalletType.IN_Development);
        Revenue developmentRevenue = Revenue
                .builder()
                .ownerId(donationDto.getOwnerId())
                .crmOwnerId(donationDto.getCrmOwnerId())
                .toWalletId(overheadsWallet.getId())
                .amount(donationDto.getDistribution().getDevelopment().getAmount())
                .currency(donationDto.getCurrency())
                .source(donationDto.getSource())
                .transactionId(donationDto.getTransactionId())
                .timestamp(donationDto.getTransactionTimestamp())
                .originalAmount(new BigDecimal(donationDto.getOriginalAmount()))
                .originalCurrency(donationDto.getOriginalCurrency())
                .currencyConversion(donationDto.getCurrencyConversion())
                .build();
        revenuesRepository.save(developmentRevenue);
        developmentWallet.setAvailableAmount(developmentWallet.getAvailableAmount().add(donationDto.getDistribution().getDevelopment().getAmount()));
        walletService.update(developmentWallet);

        Wallet missionWallet = walletService.findByOwnerIdAndType(donationDto.getOwnerId(), WalletType.IN_Mission);
        BigDecimal missionsAmount = new BigDecimal(0);
        for (Mission mission : donationDto.getDistribution().getMissions()) {
            missionsAmount = missionsAmount.add(mission.getAmount());
        }
        Revenue missionRevenue = Revenue
                .builder()
                .ownerId(donationDto.getOwnerId())
                .crmOwnerId(donationDto.getCrmOwnerId())
                .toWalletId(overheadsWallet.getId())
                .amount(missionsAmount)
                .currency(donationDto.getCurrency())
                .source(donationDto.getSource())
                .transactionId(donationDto.getTransactionId())
                .timestamp(donationDto.getTransactionTimestamp())
                .originalAmount(new BigDecimal(donationDto.getOriginalAmount()))
                .originalCurrency(donationDto.getOriginalCurrency())
                .currencyConversion(donationDto.getCurrencyConversion())
                .build();
        revenuesRepository.save(missionRevenue);
        missionWallet.setAvailableAmount(missionWallet.getAvailableAmount().add(missionsAmount));
        walletService.update(missionWallet);
    }

}
