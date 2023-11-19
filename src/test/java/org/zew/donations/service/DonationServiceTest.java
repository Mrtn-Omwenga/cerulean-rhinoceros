package org.zew.donations.service;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.zew.donations.dto.DonationDto;
import org.zew.donations.dto.DonationDto.Distribution;
import org.zew.donations.dto.DonationDto.Mission;
import org.zew.donations.model.Amount;
import org.zew.donations.model.OwnerType;
import org.zew.donations.model.Wallet;
import org.zew.donations.model.WalletType;
import org.zew.donations.repository.LocalRevenuesRepositoryImpl;
import org.zew.donations.repository.LocalWalletRepositoryImpl;
import org.zew.donations.repository.RevenuesRepository;
import org.zew.donations.repository.WalletRepository;

@SpringBootTest
@Import(DonationServiceTest.Config.class)
public class DonationServiceTest {

  @TestConfiguration
  static class Config {
    @Bean
    @Primary
    WalletRepository localWalletRepositoryImpl() {
        return new LocalWalletRepositoryImpl();
    }

    @Bean
    @Primary
    RevenuesRepository localRevenuesRepositoryImpl() {
        return new LocalRevenuesRepositoryImpl();
    }
  }

  @Autowired
  private LocalWalletRepositoryImpl walletRepository;

  @Autowired
  private LocalRevenuesRepositoryImpl revenuesRepository;

  @Autowired
  private DonationService donationService;

  @BeforeEach
  public void setup() {
      walletRepository.reset();
      revenuesRepository.reset();
  }

  @Test
  public void testSaveDonation() throws Exception {
    walletRepository.save(
        Wallet.builder()
              .walletId("wallet0")
              .ownerId("u102john2021")
              .crmOwnerId("CRM111111john2021")
              .ownerType(OwnerType.IND)
              .missionId("")
              .walletType(WalletType.IN_Development)
              .availableAmount(new BigDecimal(0.00))
              .totalAmount(new BigDecimal(0.00))
              .currency("EUR")
              .build()
    );
    walletRepository.save(
        Wallet.builder()
              .walletId("wallet1")
              .ownerId("u102john2021")
              .crmOwnerId("CRM111111john2021")
              .ownerType(OwnerType.IND)
              .missionId("")
              .walletType(WalletType.IN_Overheads)
              .availableAmount(new BigDecimal(0.00))
              .totalAmount(new BigDecimal(0.00))
              .currency("EUR")
              .build()
    );
    walletRepository.save(
        Wallet.builder()
              .walletId("wallet2")
              .ownerId("u102john2021")
              .crmOwnerId("CRM111111john2021")
              .ownerType(OwnerType.IND)
              .missionId("")
              .walletType(WalletType.IN_Mission)
              .availableAmount(new BigDecimal(0.00))
              .totalAmount(new BigDecimal(0.00))
              .currency("EUR")
              .build()
    );
    DonationDto donation = DonationDto
      .builder()
      .transactionTimestamp("2021-06-11T19:31:25.734-03:00")
      .transactionId("7MB27930VA981832YK2PHN7Q")
      .ownerId("u102john2021")
      .crmOwnerId("CRM111111john2021")
      .source("PAYME_visa")
      .totalAmount(120.168)
      .currency("EUR")
      .originalAmount(100.14)
      .originalCurrency("EUR")
      .currencyConversion(1.2)
      .distribution(
          Distribution.builder()
                      .development(new Amount(new BigDecimal(9.8), "EUR"))
                      .overheads(new Amount(new BigDecimal(9.5), "EUR"))
                      .fees(new Amount(new BigDecimal(1.02), "EUR"))
                      .missions(List.of(
                          new Mission("M001", new BigDecimal(89.748), "EUR"),
                          new Mission("M002",new BigDecimal(1.02), "EUR")
                      ))
                      .build()
      )
      .build();
    donationService.saveDonation(donation);

    assertTrue(walletRepository.findById("wallet0").get().getAvailableAmount().compareTo(new BigDecimal(9.8)) == 0);
    assertTrue(walletRepository.findById("wallet1").get().getAvailableAmount().compareTo(new BigDecimal(9.5)) == 0);
    assertTrue(walletRepository.findById("wallet2").get().getAvailableAmount().setScale(5, RoundingMode.HALF_UP).compareTo(
                                                           new BigDecimal(90.768).setScale(5, RoundingMode.HALF_UP)) == 0);
  }
}
