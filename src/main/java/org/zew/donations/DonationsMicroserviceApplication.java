package org.zew.donations;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.kafka.retrytopic.TopicSuffixingStrategy;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.retry.annotation.Backoff;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
@Slf4j
@SpringBootApplication
public class DonationsMicroserviceApplication {

	private DonationPersistenceService donationPersistenceService;
	private final Environment env;

	public static void main(String[] args) {
		SpringApplication.run(DonationsMicroserviceApplication.class, args);
	}

	@Autowired
	public DonationsMicroserviceApplication(DonationPersistenceService donationPersistenceService, Environment env) {
		this.donationPersistenceService = donationPersistenceService;
		this.env = env;
	}

	@RetryableTopic(
			attempts = "4",
			backoff = @Backoff(delay = 2000, multiplier = 2.0),
			autoCreateTopics = "false",
			topicSuffixingStrategy = TopicSuffixingStrategy.SUFFIX_WITH_INDEX_VALUE,
			dltStrategy = DltStrategy.FAIL_ON_ERROR)
	@KafkaListener(
			topics = "#{'${org.zew.kafka.topic.donations}'}",
			groupId = "#{'${org.zew.kafka.consumer.group}'}",
			containerFactory = "kafkaListenerContainerFactory")
	public void listen(@Payload DonationDto donation) {
		log.info("Received donation {}", donation);
		donationPersistenceService.saveDonation(donation);
	}

	@Bean
	public ConsumerFactory<String, String> consumerFactory() {
		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, String.format("%s:%s", env.getProperty("org.zew.kafka.host"), env.getProperty("org.zew.kafka.port")));
		props.put(ConsumerConfig.GROUP_ID_CONFIG, env.getProperty("org.zew.kafka.consumer.group"));
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class.getName());
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, DonationDtoDeserializer.class.getName());
		props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, env.getProperty("org.zew.kafka.maxpoll"));
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, env.getProperty("org.zew.kafka.autocommit"));
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, env.getProperty("org.zew.kafka.offset"));
		return new DefaultKafkaConsumerFactory<>(props);
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
		ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory());
		return factory;
	}

}

