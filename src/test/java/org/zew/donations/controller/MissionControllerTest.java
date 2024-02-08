package org.zew.donations.controller;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.zew.donations.model.OwnerType;
import org.zew.donations.model.Wallet;
import org.zew.donations.model.WalletType;
import org.zew.donations.repository.LocalWalletRepositoryImpl;
import org.zew.donations.repository.WalletRepository;

@SpringBootTest
@Import(MissionControllerTest.Config.class)
@AutoConfigureMockMvc
public class MissionControllerTest {

    @TestConfiguration
    static class Config {
        @Bean
        @Primary
        WalletRepository localWalletRepositoryImpl() {
            return new LocalWalletRepositoryImpl();
        }
    }

    @Autowired
    private LocalWalletRepositoryImpl walletRepository;

    @Autowired
    private MockMvc mvc;

    @BeforeEach
    public void setup() {
        walletRepository.reset();
    }

    @Test
    public void testGetTotalMissionDonors() throws Exception{
        Wallet wallet = new Wallet(
                "wallet0",
                "u102john2021",
                "CRM111111john2021",
                OwnerType.IND,
                "dummy_mission_id",
                WalletType.IN_Overheads,
                new BigDecimal(0.00),
                new BigDecimal(0.00),
                "EUR"
        );
        walletRepository.save(wallet);
        mvc.perform(MockMvcRequestBuilders.get("/mission/total-donors/dummy_mission_id"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(1));
    }
}
