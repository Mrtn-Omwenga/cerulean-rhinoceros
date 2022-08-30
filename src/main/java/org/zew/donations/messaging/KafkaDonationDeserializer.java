package org.zew.donations.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;
import org.zew.donations.dto.DonationDto;

@Slf4j
public class KafkaDonationDeserializer implements Deserializer<DonationDto> {

    @Override
    public DonationDto deserialize(String topic, byte[] data) {
        try {
            return new ObjectMapper().readValue(data, DonationDto.class);
        } catch (Exception e) {
            log.error("Unable to deserialize message from topic {}\nThrown exception is: {}\nOriginal message bytes: {}", topic, e, data);
            return null;
        }
    }
}
