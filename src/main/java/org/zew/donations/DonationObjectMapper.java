package org.zew.donations;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DonationObjectMapper {
    @Getter(lazy = true)
    private static final DonationObjectMapper instance = new DonationObjectMapper();

    @Getter
    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
}
