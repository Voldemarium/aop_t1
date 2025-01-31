package ru.t1.java.demo.config;

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
import ru.t1.java.demo.kafka.KafkaTransactionProducer;
import ru.t1.java.demo.model.dto.TransactionDto;

import java.util.HashMap;
import java.util.Map;

import static ru.t1.java.demo.config.KafkaConsumerGenerator.buildKafkaListenerContainerFactory;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class KafkaTransactionsTopicConfig {
    private final KafkaCommonProperties kafkaCommonProperties;
    @Value("${t1.kafka.consumer.consumer3.group-id}")
    private String transaction_groupId;
    @Value("${t1.kafka.topic.client_transactions}")
    private String transactionsTopic;

    ///  ---------KafkaTransactionConsumer----------------
    @Bean
    public ConsumerFactory<String, TransactionDto> consumer3ListenerFactory() {
        Map<String, Object> props = new HashMap<>(kafkaCommonProperties.buildConsumerCommonProperties());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, transaction_groupId);
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "ru.t1.java.demo.model.dto.TransactionDto");
        DefaultKafkaConsumerFactory<String, TransactionDto> factory = new DefaultKafkaConsumerFactory<>(props);
        factory.setKeyDeserializer(new StringDeserializer());
        return factory;
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, TransactionDto> kafkaListenerContainerFactory3(
            @Qualifier("consumer3ListenerFactory") ConsumerFactory<String, TransactionDto> consumerFactory) {
        return buildKafkaListenerContainerFactory(consumerFactory);
    }


    ///  ---------KafkaTransactionProducer----------------
    @Bean("transaction")
    public KafkaTemplate<String, TransactionDto> kafkaTransactionTemplate(ProducerFactory<String, TransactionDto> producerPatFactory) {
        return new KafkaTemplate<>(producerPatFactory);
    }

    @Bean
    @ConditionalOnProperty(value = "t1.kafka.producer.enable",
            havingValue = "true",
            matchIfMissing = true)
    public KafkaTransactionProducer producerTransaction(@Qualifier("transaction") KafkaTemplate<String, TransactionDto> template) {
        template.setDefaultTopic(transactionsTopic);
        return new KafkaTransactionProducer(template);
    }

}
