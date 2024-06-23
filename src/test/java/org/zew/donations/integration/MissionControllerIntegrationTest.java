package org.zew.donations.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.zew.donations.Application;
import org.zew.donations.model.Revenue;
import org.zew.donations.model.SourceType;
import org.zew.donations.repository.RevenueRepository;

import java.math.BigDecimal;
import java.sql.Timestamp;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
@Transactional
public class MissionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RevenueRepository revenueRepository;

    @BeforeEach
    public void setup() {
        revenueRepository.deleteAll();

        Revenue revenue1 = Revenue.builder()
                .revenueId("rev1")
                .ownerId("contact1")
                .crmOwnerId("crm1")
                .toWalletId("mission1")
                .amount(new BigDecimal("50.00"))
                .currency("USD")
                .sourceType(SourceType.PAYPAL_EMAIL)
                .transactionId("txn1")
                .timestamp(new Timestamp(System.currentTimeMillis()))
                .invoiceId("inv1")
                .originalAmount(new BigDecimal("50.00"))
                .originalCurrency("USD")
                .currencyConversion(null)
                .invoiceReceiptURL(null)
                .missionId("mission1")
                .build();

        Revenue revenue2 = Revenue.builder()
                .revenueId("rev2")
                .ownerId("contact2")
                .crmOwnerId("crm2")
                .toWalletId("mission1")
                .amount(new BigDecimal("100.00"))
                .currency("USD")
                .sourceType(SourceType.PAYPAL_EMAIL)
                .transactionId("txn2")
                .timestamp(new Timestamp(System.currentTimeMillis()))
                .invoiceId("inv2")
                .originalAmount(new BigDecimal("100.00"))
                .originalCurrency("USD")
                .currencyConversion(null)
                .invoiceReceiptURL(null)
                .missionId("mission1")
                .build();

        revenueRepository.save(revenue1);
        revenueRepository.save(revenue2);
    }

    @Test
    public void testGetTotalMissionDonors() throws Exception {
        var response = mockMvc.perform(get("/mission/total-donors/mission1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        int actualDonors = Integer.parseInt(response.getContentAsString());
        assertThat(actualDonors).isEqualTo(2);
    }

    @Test
    public void testGetSupportedMissions() throws Exception {
        var response = mockMvc.perform(get("/mission/supported-missions")
                .param("contactId", "contact1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.supportedMissions[0].missionId").value("mission1"))
                .andExpect(jsonPath("$.supportedMissions[0].myDonationAmount").value(50.00))
                .andExpect(jsonPath("$.supportedMissions[0].myDonationCurrency").value("USD"))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        String responseBody = response.getContentAsString();
        assertThat(responseBody).contains("mission1");
        assertThat(responseBody).contains("50.00");
        assertThat(responseBody).contains("USD");
    }

    @Test
    public void testGetSupportedMissionsMultipleMissions() throws Exception {
        // Adding another mission for the same contact
        Revenue revenue3 = Revenue.builder()
                .revenueId("rev3")
                .ownerId("contact1")
                .crmOwnerId("crm3")
                .toWalletId("mission2")
                .amount(new BigDecimal("75.00"))
                .currency("USD")
                .sourceType(SourceType.PAYPAL_EMAIL)
                .transactionId("txn3")
                .timestamp(new Timestamp(System.currentTimeMillis()))
                .invoiceId("inv3")
                .originalAmount(new BigDecimal("75.00"))
                .originalCurrency("USD")
                .currencyConversion(null)
                .invoiceReceiptURL(null)
                .missionId("mission2")
                .build();

        revenueRepository.save(revenue3);

        var response = mockMvc.perform(get("/mission/supported-missions")
                .param("contactId", "contact1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.supportedMissions[0].missionId").value("mission1"))
                .andExpect(jsonPath("$.supportedMissions[0].myDonationAmount").value(50.00))
                .andExpect(jsonPath("$.supportedMissions[0].myDonationCurrency").value("USD"))
                .andExpect(jsonPath("$.supportedMissions[1].missionId").value("mission2"))
                .andExpect(jsonPath("$.supportedMissions[1].myDonationAmount").value(75.00))
                .andExpect(jsonPath("$.supportedMissions[1].myDonationCurrency").value("USD"))
                .andReturn()
                .getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        String responseBody = response.getContentAsString();
        assertThat(responseBody).contains("mission1");
        assertThat(responseBody).contains("50.00");
        assertThat(responseBody).contains("USD");
        assertThat(responseBody).contains("mission2");
        assertThat(responseBody).contains("75.00");
    }
}
