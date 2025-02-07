package ru.t1.java.service_2.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import ru.t1.java.library.dto.TransactionAcceptDto;
import ru.t1.java.library.dto.TransactionResultDto;
import ru.t1.java.service_2.kafka.KafkaTransactionResultProducer;

import java.util.HashMap;
import java.util.Map;

import static ru.t1.java.library.kafka.KafkaConsumerGenerator.buildKafkaListenerContainerFactory;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class KafkaConfig {
    private final KafkaCommonProperties kafkaCommonProperties;
    @Value("${t1.kafka.consumer.consumer1.group-id}")
    private String transactionAcceptGroupId;
    @Value("${t1.kafka.topic.client_transactions_result}")
    private String transactionsResultTopic;

    ///  ---------KafkaTransactionAcceptConsumer----------------
    @Bean
    public ConsumerFactory<String, TransactionAcceptDto> consumerListenerFactory() {
        Map<String, Object> props = new HashMap<>(kafkaCommonProperties.buildConsumerCommonProperties());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, transactionAcceptGroupId);
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "ru.t1.java.library.dto.TransactionAcceptDto");
        DefaultKafkaConsumerFactory<String, TransactionAcceptDto> factory = new DefaultKafkaConsumerFactory<>(props);
        factory.setKeyDeserializer(new StringDeserializer());
        return factory;
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, TransactionAcceptDto> kafkaListenerContainerFactory(
            @Qualifier("consumerListenerFactory") ConsumerFactory<String, TransactionAcceptDto> consumerFactory) {
        return buildKafkaListenerContainerFactory(consumerFactory);
    }


    ///  ---------KafkaTransactionResultProducer----------------
    @Bean("transaction_result")
    public KafkaTemplate<String, TransactionResultDto> kafkaTransactionResultTemplate(ProducerFactory<String,
                TransactionResultDto> producerPatFactory) {
        return new KafkaTemplate<>(producerPatFactory);
    }

    @Bean
    @ConditionalOnProperty(value = "t1.kafka.producer.enable",
            havingValue = "true",
            matchIfMissing = true)
    public KafkaTransactionResultProducer producerTransactionResult(
            @Qualifier("transaction_result") KafkaTemplate<String, TransactionResultDto> template) {
        template.setDefaultTopic(transactionsResultTopic);
        return new KafkaTransactionResultProducer(template);
    }


    @Bean
    public <T> ProducerFactory<String, T> producerFactory() {
        Map<String, Object> producerProperties = new HashMap<>(kafkaCommonProperties.buildProducerCommonProperties());
        return new DefaultKafkaProducerFactory<>(producerProperties);
    }
}
