package org.zew.donations.messaging;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.zew.donations.dto.DonationDto;
import org.zew.donations.service.DonationService;
import software.amazon.qldb.QldbDriver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@Disabled
@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = { "listeners=PLAINTEXT://localhost:9092", "port=9092" })
public class KafkaDonationListenerTest {

    @Autowired
    public KafkaTemplate<String, String> template;

    @Autowired
    private KafkaDonationConsumer consumer;

    @MockBean
    private DonationService donationService;

    @Captor
    private ArgumentCaptor<DonationDto> donationDtoCaptor;

    @Value("${org.zew.kafka.topic.donations}")
    private String topic;

    @MockBean
    private QldbDriver qldbDriver;

    @Test
    public void givenEmbeddedKafkaBroker_whenSendingWithATestDonation_thenMessageReceived() throws Exception {
        template.send(topic, getDefaultMessage());

        Thread.sleep(10_000);

        verify(donationService).saveDonation(donationDtoCaptor.capture());
        var donation = donationDtoCaptor.getValue();
        assertEquals("PAYME_visa", donation.getSource());
        assertEquals("u102john2021", donation.getOwnerId());
    }

    private String getDefaultMessage() {
        return """
                {
                    "transactionTimestamp": "2021-06-11T19:31:25.734-03:00",
                    "transactionId": "7MB27930VA981832YK2PHN7Q",
                    "ownerId": "u102john2021",
                    "crmOwnerId": "CRM111111john2021",
                    "source": "PAYME_visa",
                    "totalAmount": 120.168,
                    "currency": "EUR",
                    "originalAmount": 100.14,
                    "originalCurrency": "USD",
                    "currencyConversion": 1.2,
                    "distribution": {
                        "overheads": {
                            "amount": 9.5,
                            "currency": "EUR"
                        },
                        "development": {
                            "amount": 9.8,
                            "currency": "EUR"
                        },
                        "fees": {
                            "amount": 1.02,
                            "currency": "EUR"
                        },
                        "missions": [
                            {
                                "missionId": "M001",
                                "amount": 89.748,
                                "currency": "EUR"
                            },
                            {
                                "missionId": "M002",
                                "amount": 10.1,
                                "currency": "EUR"
                            }
                        ]
                    }
                }""";
    }

}
