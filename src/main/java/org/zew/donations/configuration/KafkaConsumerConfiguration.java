package org.zew.donations.configuration;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.Map;

import static java.util.Objects.requireNonNull;

@Configuration
public class KafkaConsumerConfiguration {

    @Autowired
    private Environment env;

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, String>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfig());
    }

    @Bean
    public Map<String, Object> consumerConfig() {
        return Map.of(
                ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, requireNonNull(env.getProperty("org.zew.kafka.bootstrap-servers", String.class)),
                ConsumerConfig.GROUP_ID_CONFIG, requireNonNull(env.getProperty("org.zew.kafka.consumer.group")),
                ConsumerConfig.MAX_POLL_RECORDS_CONFIG, requireNonNull(env.getProperty("org.zew.kafka.max-poll")),
                ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, requireNonNull(env.getProperty("org.zew.kafka.autocommit")),
                ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, requireNonNull(env.getProperty("org.zew.kafka.offset")),
                ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class,
                ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class,
                JsonDeserializer.TRUSTED_PACKAGES, "org.zew.donations.model",
                JsonDeserializer.VALUE_DEFAULT_TYPE, "org.zew.donations.model.DonationDto");
    }

}
