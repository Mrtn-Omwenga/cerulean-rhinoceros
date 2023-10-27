package org.zew.donations.controller;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.zew.donations.model.OwnerType;
import org.zew.donations.model.Wallet;
import org.zew.donations.model.WalletType;
import org.zew.donations.repository.LocalWalletRepositoryImpl;
import org.zew.donations.repository.WalletRepository;
import org.zew.donations.service.WalletService;

import com.fasterxml.jackson.core.json.JsonWriteFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@Import(WalletControllerTest.Config.class)
@AutoConfigureMockMvc
public class WalletControllerTest {

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
  public void testCreateWallet() throws Exception {
    Wallet wallet = new Wallet(
        "wallet0",
        "u102john2021",
        "CRM111111john2021",
        OwnerType.IND,
        "",
        WalletType.IN_Overheads,
        new BigDecimal(0.00),
        new BigDecimal(0.00),
        "EUR"
    );
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.configure(JsonWriteFeature.QUOTE_FIELD_NAMES.mappedFeature(), true);
    mvc.perform(MockMvcRequestBuilders.post("/wallet")
                                      .contentType(MediaType.APPLICATION_JSON)
                                      .content(objectMapper.writeValueAsString(wallet)))
       .andExpect(MockMvcResultMatchers.status().isOk());
    assertNotNull(walletRepository.findById("wallet0"));
    assertTrue(walletRepository.existsByOwnerIdAndType("u102john2021", WalletType.IN_Overheads));

  }

  @Test
  public void testCreateWalletAlreadyExists() throws Exception {
    Wallet wallet = new Wallet(
        "wallet0",
        "u102john2021",
        "CRM111111john2021",
        OwnerType.IND,
        "",
        WalletType.IN_Overheads,
        new BigDecimal(0.00),
        new BigDecimal(0.00),
        "EUR"
    );
    walletRepository.save(wallet);
    assertTrue(walletRepository.existsByOwnerIdAndType("u102john2021", WalletType.IN_Overheads));
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.configure(JsonWriteFeature.QUOTE_FIELD_NAMES.mappedFeature(), true);
    mvc.perform(MockMvcRequestBuilders.post("/wallet")
                                      .contentType(MediaType.APPLICATION_JSON)
                                      .content(objectMapper.writeValueAsString(wallet)))
       .andExpect(MockMvcResultMatchers.status().isConflict());
  }

  @Test
  public void testGetById() throws Exception {
    Wallet wallet = new Wallet(
        "wallet0",
        "u102john2021",
        "CRM111111john2021",
        OwnerType.IND,
        "",
        WalletType.IN_Overheads,
        new BigDecimal(0.00),
        new BigDecimal(0.00),
        "EUR"
    );
    walletRepository.save(wallet);
    mvc.perform(MockMvcRequestBuilders.get("/wallet/wallet0"))
       .andExpect(MockMvcResultMatchers.status().isOk())
       .andExpect(MockMvcResultMatchers.jsonPath("walletId").value("wallet0"));
  }

  @Test
  public void testGetByIdDoesNotExist() throws Exception {
    mvc.perform(MockMvcRequestBuilders.get("/wallet/wallet0"))
       .andExpect(MockMvcResultMatchers.status().isNotFound());
  }
}