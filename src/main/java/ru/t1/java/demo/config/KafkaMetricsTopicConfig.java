package ru.t1.java.demo.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.*;
import ru.t1.java.demo.kafka.KafkaErrorProducer;


@Slf4j
@Configuration
@RequiredArgsConstructor
public class KafkaMetricsTopicConfig {
    @Value("${t1.kafka.topic.metrics}")
    private String metricsTopic;

    ///  ---------KafkaDataSourceErrorProducer----------------
    @Bean("error")
    public KafkaTemplate<String, Object> kafkaDataSourceErrorLogTemplate(ProducerFactory<String, Object> producerPatFactory) {
        return new KafkaTemplate<>(producerPatFactory);
    }

    @Bean
    @ConditionalOnProperty(value = "t1.kafka.producer.enable",
            havingValue = "true",
            matchIfMissing = true)
    public KafkaErrorProducer producerDataSourceError(@Qualifier("error") KafkaTemplate<String, Object> template) {
        template.setDefaultTopic(metricsTopic);
        return new KafkaErrorProducer(template);
    }
}
