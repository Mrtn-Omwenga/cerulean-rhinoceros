package org.zew.donations;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;

import java.nio.charset.StandardCharsets;

@Slf4j
@NoArgsConstructor
public class DonationDtoDeserializer implements Deserializer<DonationDto> {
    private final ObjectMapper objectMapper = DonationObjectMapper.getInstance().getObjectMapper();

    @Override
    public DonationDto deserialize(String s, byte[] bytes) {
        try {
            return objectMapper.readValue(new String(bytes, StandardCharsets.UTF_8), DonationDto.class);
        } catch (Exception e) {
            log.error("Unable to deserialize message {}", bytes, e);
            return null;
        }
    }
}
