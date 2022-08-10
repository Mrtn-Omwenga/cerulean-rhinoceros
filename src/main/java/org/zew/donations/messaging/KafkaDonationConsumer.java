package org.zew.donations.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.kafka.retrytopic.TopicSuffixingStrategy;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;
import org.zew.donations.dto.DonationDto;
import org.zew.donations.service.DonationService;

@Slf4j
@Component
public class KafkaDonationConsumer {

    @Autowired
    private DonationService donationService;

    @Autowired
    private ObjectMapper objectMapper;

    @RetryableTopic(
            attempts = "4",
            backoff = @Backoff(delay = 2000, multiplier = 2.0),
            autoCreateTopics = "false",
            topicSuffixingStrategy = TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE,
            dltStrategy = DltStrategy.FAIL_ON_ERROR)
    @KafkaListener(
            topics = "${org.zew.kafka.topic.donations}",
            groupId = "${org.zew.kafka.consumer.group}",
            containerFactory = "kafkaListenerContainerFactory")
    public void listener(@Payload DonationDto donationDto) {
        log.info("Received donation {}", donationDto);
        donationService.saveDonation(donationDto);
    }

}
