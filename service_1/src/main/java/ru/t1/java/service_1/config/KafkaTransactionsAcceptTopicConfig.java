package ru.t1.java.service_1.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import ru.t1.java.service_1.kafka.KafkaTransactionAcceptProducer;
import ru.t1.java.library.dto.TransactionAcceptDto;


@Slf4j
@Configuration
@RequiredArgsConstructor
public class KafkaTransactionsAcceptTopicConfig {
    @Value("${t1.kafka.topic.client_transactions_accept}")
    private String transactionsAcceptTopic;

    ///  ---------KafkaTransactionAcceptProducer----------------
    @Bean("transaction_accept")
    public KafkaTemplate<String, TransactionAcceptDto> kafkaTransactionAcceptTemplate(ProducerFactory<String,
            TransactionAcceptDto> producerPatFactory) {
        return new KafkaTemplate<>(producerPatFactory);
    }

    @Bean
    @ConditionalOnProperty(value = "t1.kafka.producer.enable",
            havingValue = "true",
            matchIfMissing = true)
    public KafkaTransactionAcceptProducer producerTransactionAccept(
            @Qualifier("transaction_accept") KafkaTemplate<String, TransactionAcceptDto> template) {
        template.setDefaultTopic(transactionsAcceptTopic);
        return new KafkaTransactionAcceptProducer(template);
    }
}
