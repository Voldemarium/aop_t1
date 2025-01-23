package ru.t1.java.demo.config;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import ru.t1.java.demo.kafka.MessageDeserializer;

import java.util.*;

@Slf4j
@Configuration
@ConfigurationProperties(prefix = "kafka")
@Getter
@Setter
public class KafkaCustomProperties {
    @Value("${t1.kafka.bootstrap.server}")
    private String servers;
    @Value("${t1.kafka.session.timeout.ms:15000}")
    private String sessionTimeout;
    @Value("${t1.kafka.max.partition.fetch.bytes:300000}")
    private String maxPartitionFetchBytes;
    @Value("${t1.kafka.max.poll.records:1}")
    private String maxPollRecords;
    @Value("${t1.kafka.max.poll.interval.ms:3000}")
    private String maxPollIntervalsMs;

    public Map<String, Object> buildConsumerCommonProperties() {
        Map<String, Object> consumerProperties = new HashMap<>();
        consumerProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        consumerProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, MessageDeserializer.class);
        //        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        consumerProperties.put(JsonDeserializer.TRUSTED_PACKAGES, "ru.t1.java.demo.model.dto");
        consumerProperties.put(JsonDeserializer.USE_TYPE_INFO_HEADERS, false);
        consumerProperties.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, sessionTimeout);
        consumerProperties.put(ConsumerConfig.MAX_PARTITION_FETCH_BYTES_CONFIG, maxPartitionFetchBytes);
        consumerProperties.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maxPollRecords);
        consumerProperties.put(ConsumerConfig.MAX_POLL_INTERVAL_MS_CONFIG, maxPollIntervalsMs);
        consumerProperties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, Boolean.FALSE);
        consumerProperties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        consumerProperties.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, MessageDeserializer.class.getName());
        consumerProperties.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, MessageDeserializer.class);
        return consumerProperties;
    }

    public Map<String, Object> buildProducerCommonProperties() {
        Map<String, Object> producerProperties = new HashMap<>();
        producerProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, servers);
        producerProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        producerProperties.put(ProducerConfig.RETRIES_CONFIG, 3);
        producerProperties.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, 1000);
        producerProperties.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, false);
        return producerProperties;
    }
}
