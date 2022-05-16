package org.zew.donations;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;
import org.javamoney.moneta.Money;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.money.MonetaryAmount;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

@Slf4j
@NoArgsConstructor
public class DonationDtoDeserializer implements Deserializer<DonationDto> {

    @Override
    public DonationDto deserialize(String topicName, byte[] bytes) {
        try {
            var json = new JSONObject(new String(bytes, StandardCharsets.UTF_8));
            JSONObject distribution = json.getJSONObject("distribution");
            JSONObject overheadsDistribution = distribution.getJSONObject("overheads");
            JSONObject developmentDistribution = distribution.getJSONObject("development");
            JSONObject feesDistribution = distribution.getJSONObject("fees");
            JSONArray missionsDistribution = distribution.getJSONArray("missions");

            var missionsDistributionMap = new HashMap<String, MonetaryAmount>();
            for(var i = 0; i < missionsDistribution.length(); i++) {
                JSONObject mission = missionsDistribution.getJSONObject(i);
                MonetaryAmount amount = Money.of(mission.getDouble("amount"), mission.getString("currency"));
                missionsDistributionMap.put(mission.getString("missionId"), amount);
            }

            var dto = DonationDto
                    .builder()
                    .transactionTimestamp(
                            ZonedDateTime.parse(
                                    json.getString("transactionTimestamp"),
                                    DateTimeFormatter.ISO_OFFSET_DATE_TIME
                            )
                    )
                    .transactionId(json.getString("transactionId"))
                    .ownerId(json.getString("ownerId"))
                    .crmOwnerId(json.getString("crmOwnerId"))
                    .source(json.getString("source"))
                    .totalAmount(Money.of(json.getDouble("totalAmount"), json.getString("currency")))
                    .originalAmount(Money.of(json.getDouble("originalAmount"), json.getString("originalCurrency")))
                    .currencyConversionRate(BigDecimal.valueOf(json.getDouble("currencyConversion")))
                    .overheadsDistribution(Money.of(overheadsDistribution.getDouble("amount"), overheadsDistribution.getString("currency")))
                    .developmentDistribution(Money.of(developmentDistribution.getDouble("amount"), developmentDistribution.getString("currency")))
                    .feesDistribution(Money.of(feesDistribution.getDouble("amount"), feesDistribution.getString("currency")))
                    .missionsDistribution(missionsDistributionMap)
                    .build();

            return dto;
        } catch (Exception e) {
            log.error("Unable to deserialize message from topic {}\nThrown exception is: {}\nOriginal message bytes: {}", topicName, e, bytes);
            return null;
        }
    }
}
