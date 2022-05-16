package org.zew.donations;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DonationDtoDeserializerTests {
    @Test
    public void givenInputJsonAsString_thenItShouldBeDeserializedCorrectly() {
        DonationDto result = new DonationDtoDeserializer().deserialize("test_topic", getInputBytes());

        var expectedTransactionTimestamp = ZonedDateTime.of(
                LocalDateTime.of(2021, Month.JUNE, 11, 19, 31, 25, 734_000_000),
                ZoneId.of("-03:00")
        );
        assertEquals(expectedTransactionTimestamp, result.getTransactionTimestamp());
        assertEquals("7MB27930VA981832YK2PHN7Q", result.getTransactionId());
        assertEquals("u102john2021", result.getOwnerId());
        assertEquals("CRM111111john2021", result.getCrmOwnerId());
        assertEquals("PAYME_visa", result.getSource());
        assertEquals(Money.of(120.168d, "EUR"), result.getTotalAmount());
        assertEquals(Money.of(100.14d, "USD"), result.getOriginalAmount());
        assertEquals(BigDecimal.valueOf(1.2d), result.getCurrencyConversionRate());
        assertEquals(Money.of(9.5d, "EUR"), result.getOverheadsDistribution());
        assertEquals(Money.of(9.8d, "EUR"), result.getDevelopmentDistribution());
        assertEquals(Money.of(1.02d, "EUR"), result.getFeesDistribution());
        assertEquals(Money.of(89.748d, "EUR"), result.getMissionsDistribution().get("M001"));
        assertEquals(Money.of(10.1d, "EUR"), result.getMissionsDistribution().get("M002"));
    }

    private byte[] getInputBytes() {
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
                }""".getBytes(StandardCharsets.UTF_8);
    }
}
