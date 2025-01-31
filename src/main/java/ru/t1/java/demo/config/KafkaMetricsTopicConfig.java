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
import ru.t1.java.demo.kafka.KafkaTimeExceededProducer;
import ru.t1.java.demo.model.dto.DataSourceErrorLogDto;


@Slf4j
@Configuration
@RequiredArgsConstructor
public class KafkaMetricsTopicConfig {
    @Value("${t1.kafka.topic.metrics}")
    private String metricsTopic;

    ///  ---------KafkaDataSourceErrorProducer----------------
    @Bean("error")
    public  KafkaTemplate<String, DataSourceErrorLogDto> kafkaDataSourceErrorLogTemplate(
            ProducerFactory<String, DataSourceErrorLogDto> producerPatFactory) {
        return new KafkaTemplate<>(producerPatFactory);
    }

    @Bean
    @ConditionalOnProperty(value = "t1.kafka.producer.enable",
            havingValue = "true",
            matchIfMissing = true)
    public KafkaErrorProducer producerDataSourceError(
            @Qualifier("error") KafkaTemplate<String, DataSourceErrorLogDto> template) {
        template.setDefaultTopic(metricsTopic);
        return new KafkaErrorProducer(template);
    }


    ///  ---------KafkaTimeExceededProducer----------------
    @Bean("time_exceeded")
    public  KafkaTemplate<String, String> kafkaTimeExceededTemplate(ProducerFactory<String, String> producerPatFactory) {
        return new KafkaTemplate<>(producerPatFactory);
    }

    @Bean
    @ConditionalOnProperty(value = "t1.kafka.producer.enable",
            havingValue = "true",
            matchIfMissing = true)
    public KafkaTimeExceededProducer producerTimeExceeded(@Qualifier("time_exceeded") KafkaTemplate<String, String> template) {
        template.setDefaultTopic(metricsTopic);
        return new KafkaTimeExceededProducer(template);
    }
}
