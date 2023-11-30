package org.zew.donations.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX");
        Date date;
        try {
            date = formatter.parse(donationDto.getTransactionTimestamp());
        } catch (ParseException e) {
            throw new RuntimeException(e.getMessage());
        }
        Wallet overheadsWallet = walletService.findByOwnerIdAndType(donationDto.getOwnerId(), WalletType.IN_Overheads);
        if (overheadsWallet == null) {
            overheadsWallet = Wallet
                                .builder()
                                .ownerId(donationDto.getOwnerId())
                                .crmOwnerId(donationDto.getCrmOwnerId())
                                .walletType(WalletType.IN_Overheads)
                                .availableAmount(new BigDecimal(0))
                                .totalAmount(new BigDecimal(0))
                                .currency(donationDto.getCurrency())
                                .build();
            walletService.create(overheadsWallet);
        }
        Revenue overheadsRevenue = Revenue
                .builder()
                .ownerId(donationDto.getOwnerId())
                .crmOwnerId(donationDto.getCrmOwnerId())
                .toWalletId(overheadsWallet.getId())
                .amount(donationDto.getDistribution().getOverheads().getAmount())
                .currency(donationDto.getCurrency())
                .transactionId(donationDto.getTransactionId())
                .timestamp(new Timestamp(date.getTime()))
                .originalAmount(new BigDecimal(donationDto.getOriginalAmount()))
                .originalCurrency(donationDto.getOriginalCurrency())
                .currencyConversion("" + donationDto.getCurrencyConversion())
                .build();
        revenuesRepository.save(overheadsRevenue);
        overheadsWallet.setAvailableAmount(overheadsWallet.getAvailableAmount().add(donationDto.getDistribution().getOverheads().getAmount()));
        walletService.update(overheadsWallet);

        Wallet developmentWallet = walletService.findByOwnerIdAndType(donationDto.getOwnerId(), WalletType.IN_Development);
        if (developmentWallet == null) {
            developmentWallet = Wallet
                                .builder()
                                .ownerId(donationDto.getOwnerId())
                                .crmOwnerId(donationDto.getCrmOwnerId())
                                .walletType(WalletType.IN_Development)
                                .availableAmount(new BigDecimal(0))
                                .totalAmount(new BigDecimal(0))
                                .currency(donationDto.getCurrency())
                                .build();
            walletService.create(developmentWallet);
        }
        Revenue developmentRevenue = Revenue
                .builder()
                .ownerId(donationDto.getOwnerId())
                .crmOwnerId(donationDto.getCrmOwnerId())
                .toWalletId(overheadsWallet.getId())
                .amount(donationDto.getDistribution().getDevelopment().getAmount())
                .currency(donationDto.getCurrency())
                .transactionId(donationDto.getTransactionId())
                .timestamp(new Timestamp(date.getTime()))
                .originalAmount(new BigDecimal(donationDto.getOriginalAmount()))
                .originalCurrency(donationDto.getOriginalCurrency())
                .currencyConversion("" + donationDto.getCurrencyConversion())
                .build();
        revenuesRepository.save(developmentRevenue);
        developmentWallet.setAvailableAmount(developmentWallet.getAvailableAmount().add(donationDto.getDistribution().getDevelopment().getAmount()));
        walletService.update(developmentWallet);

        Wallet missionWallet = walletService.findByOwnerIdAndType(donationDto.getOwnerId(), WalletType.IN_Mission);
        if (missionWallet == null) {
            missionWallet = Wallet
                                .builder()
                                .ownerId(donationDto.getOwnerId())
                                .crmOwnerId(donationDto.getCrmOwnerId())
                                .walletType(WalletType.IN_Mission)
                                .availableAmount(new BigDecimal(0))
                                .totalAmount(new BigDecimal(0))
                                .currency(donationDto.getCurrency())
                                .build();
            walletService.create(missionWallet);
        }
        BigDecimal missionsAmount = donationDto
			.getDistribution()
			.getMissions()
			.stream()
			.map(Mission::getAmount)
			.reduce(BigDecimal.ZERO, BigDecimal::add);
        Revenue missionRevenue = Revenue
                .builder()
                .ownerId(donationDto.getOwnerId())
                .crmOwnerId(donationDto.getCrmOwnerId())
                .toWalletId(overheadsWallet.getId())
                .amount(missionsAmount)
                .currency(donationDto.getCurrency())
                .transactionId(donationDto.getTransactionId())
                .timestamp(new Timestamp(date.getTime()))
                .originalAmount(new BigDecimal(donationDto.getOriginalAmount()))
                .originalCurrency(donationDto.getOriginalCurrency())
                .currencyConversion("" + donationDto.getCurrencyConversion())
                .build();
        revenuesRepository.save(missionRevenue);
        missionWallet.setAvailableAmount(missionWallet.getAvailableAmount().add(missionsAmount));
        walletService.update(missionWallet);
    }

}
