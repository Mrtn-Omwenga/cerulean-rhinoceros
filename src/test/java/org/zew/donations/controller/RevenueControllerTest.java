package org.zew.donations.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
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
import org.zew.donations.model.Revenue;
import org.zew.donations.repository.LocalRevenuesRepositoryImpl;

import com.fasterxml.jackson.core.json.JsonWriteFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.zew.donations.repository.RevenueRepository;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Import(RevenueControllerTest.Config.class)
@AutoConfigureMockMvc
public class RevenueControllerTest {

    @TestConfiguration
    static class Config {
        @Bean
        @Primary
        RevenueRepository localRevenueRepositoryImpl() {
            return new LocalRevenuesRepositoryImpl();
        }
    }

    @Autowired
    private LocalRevenuesRepositoryImpl revenueRepository;

    @Autowired
    private MockMvc mvc;

    private List<Revenue> revenues;

    @BeforeEach
    public void setup() throws IOException {
        revenueRepository.reset();

        ObjectMapper objectMapper = new ObjectMapper();
        InputStream inputStream = getClass().getResourceAsStream("/revenues.json");
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        revenues = objectMapper.readValue(reader, new TypeReference<List<Revenue>>() {});
    }

    @Test
    public void testCreateWallet() throws Exception {
        String revenueId = "revenue0";
        Revenue revenue = revenues.get(0).setRevenueId(revenueId);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(JsonWriteFeature.QUOTE_FIELD_NAMES.mappedFeature(), true);

        mvc.perform(MockMvcRequestBuilders.post("/revenues")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(revenue)))
                .andExpect(MockMvcResultMatchers.status().isOk());
        assertNotNull(revenueRepository.findById(revenueId));
    }

    @Test
    public void testCreateWalletAlreadyExists() throws Exception {
        String revenueId = "revenue0";
        Revenue revenue = revenues.get(0).setRevenueId(revenueId);

        revenueRepository.save(revenue);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(JsonWriteFeature.QUOTE_FIELD_NAMES.mappedFeature(), true);

        mvc.perform(MockMvcRequestBuilders.post("/revenues")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(revenue)))
                .andExpect(MockMvcResultMatchers.status().isConflict());
    }
}
