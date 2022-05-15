package org.zew.donations;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;

@Slf4j
@NoArgsConstructor
public class DonationDtoDeserializer implements Deserializer<DonationDto> {

    @Override
    public DonationDto deserialize(String s, byte[] bytes) {
        try {
            return null;
        } catch (Exception e) {
            log.error("Unable to deserialize message {}", bytes, e);
            return null;
        }
    }
}
