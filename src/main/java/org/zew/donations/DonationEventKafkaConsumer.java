package org.zew.donations;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.kafka.retrytopic.TopicSuffixingStrategy;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DonationEventKafkaConsumer {
    private DonationPersistenceService donationPersistenceService;

    @Autowired
    public DonationEventKafkaConsumer(DonationPersistenceService donationPersistenceService) {
        this.donationPersistenceService = donationPersistenceService;
    }

    @RetryableTopic(
            attempts = "4",
            backoff = @Backoff(delay = 2000, multiplier = 2.0),
            autoCreateTopics = "false",
            topicSuffixingStrategy = TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE,
            dltStrategy = DltStrategy.FAIL_ON_ERROR)
    @KafkaListener(
            topics = "#{'${org.zew.kafka.topic.missions}'}",
            groupId = "#{'${org.zew.kafka.consumer.group}'}",
            containerFactory = "kafkaListenerContainerFactory")
    public void listen(@Payload DonationDto donation) {
        log.info("Received donation {}", donation);
        donationPersistenceService.saveDonation(donation);
    }
}
