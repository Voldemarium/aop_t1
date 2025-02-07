package ru.t1.java.service_1.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class KafkaProducerFactoryConfig {
    private final KafkaCommonProperties kafkaCommonProperties;

    @Bean
    public <T> ProducerFactory<String, T> producerFactory() {
        Map<String, Object> producerProperties = new HashMap<>(kafkaCommonProperties.buildProducerCommonProperties());
        return new DefaultKafkaProducerFactory<>(producerProperties);
    }
}
