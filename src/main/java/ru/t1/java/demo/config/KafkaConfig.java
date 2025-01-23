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
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.util.backoff.FixedBackOff;
import ru.t1.java.demo.kafka.KafkaClientProducer;
import ru.t1.java.demo.kafka.KafkaErrorProducer;
import ru.t1.java.demo.model.dto.AccountDto;
import ru.t1.java.demo.model.dto.ClientDto;
import ru.t1.java.demo.model.dto.TransactionDto;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class KafkaConfig {
    private final KafkaCustomProperties kafkaCustomProperties;
    @Value("${t1.kafka.consumer.consumer1.group-id}")
    private String client_groupId;
    @Value("${t1.kafka.consumer.consumer2.group-id}")
    private String account_groupId;
    @Value("${t1.kafka.consumer.consumer3.group-id}")
    private String transaction_groupId;
    @Value("${t1.kafka.topic.client_id_registered}")
    private String clientTopic;
    @Value("${t1.kafka.topic.metrics}")
    private String metricsTopic;


    ///  ---------KafkaClientProducer----------------
    @Bean
    public ConsumerFactory<String, ClientDto> consumer1ListenerFactory() {
        Map<String, Object> props = new HashMap<>(kafkaCustomProperties.buildConsumerCommonProperties());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, client_groupId);
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "ru.t1.java.demo.model.dto.ClientDto");
        DefaultKafkaConsumerFactory<String, ClientDto> factory = new DefaultKafkaConsumerFactory<>(props);
        factory.setKeyDeserializer(new StringDeserializer());
        return factory;
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, ClientDto> kafkaListenerContainerFactory1(
            @Qualifier("consumer1ListenerFactory") ConsumerFactory<String, ClientDto> consumerFactory) {
        return buildKafkaListenerContainerFactory(consumerFactory);
    }

    ///  ---------KafkaAccountProducer----------------
    @Bean
    public ConsumerFactory<String, AccountDto> consumer2ListenerFactory() {
        Map<String, Object> props = new HashMap<>(kafkaCustomProperties.buildConsumerCommonProperties());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, account_groupId);
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "ru.t1.java.demo.model.dto.AccountDto");
        DefaultKafkaConsumerFactory<String, AccountDto> factory = new DefaultKafkaConsumerFactory<>(props);
        factory.setKeyDeserializer(new StringDeserializer());
        return factory;
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, AccountDto> kafkaListenerContainerFactory2(
            @Qualifier("consumer2ListenerFactory") ConsumerFactory<String, AccountDto> consumerFactory) {
        return buildKafkaListenerContainerFactory(consumerFactory);
    }

    ///  ---------KafkaTransactionProducer----------------
    @Bean
    public ConsumerFactory<String, TransactionDto> consumer3ListenerFactory() {
        Map<String, Object> props = new HashMap<>(kafkaCustomProperties.buildConsumerCommonProperties());
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

    public <T> ConcurrentKafkaListenerContainerFactory<String, T> buildKafkaListenerContainerFactory(
            ConsumerFactory<String, T> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, T> factory
                = new ConcurrentKafkaListenerContainerFactory<>();
        factoryBuilder(consumerFactory, factory);
        return factory;
    }

    private <T> void factoryBuilder(ConsumerFactory<String, T> consumerFactory,
                                    ConcurrentKafkaListenerContainerFactory<String, T> factory) {
        factory.setConsumerFactory(consumerFactory);
        factory.setBatchListener(true);
        factory.setConcurrency(1);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        factory.getContainerProperties().setPollTimeout(5000);
        factory.getContainerProperties().setMicrometerEnabled(true);
        factory.setCommonErrorHandler(errorHandler());
    }

    private CommonErrorHandler errorHandler() {
        DefaultErrorHandler handler = new DefaultErrorHandler(new FixedBackOff(1000, 3));
        handler.addNotRetryableExceptions(IllegalStateException.class);
        handler.setRetryListeners((record, ex, deliveryAttempt) ->
                log.error(" RetryListeners message = {}, offset = {} deliveryAttempt = {}",
                        ex.getMessage(), record.offset(), deliveryAttempt));
        return handler;
    }


    ///  ---------KafkaClientProducer----------------
    @Bean("client")
    public KafkaTemplate<String, Object> kafkaClientTemplate(ProducerFactory<String, Object> producerPatFactory) {
        return new KafkaTemplate<>(producerPatFactory);
    }

    @Bean
    @ConditionalOnProperty(value = "t1.kafka.producer.enable",
            havingValue = "true",
            matchIfMissing = true)
    public KafkaClientProducer producerClient(@Qualifier("client") KafkaTemplate<String, Object> template) {
        template.setDefaultTopic(clientTopic);
        return new KafkaClientProducer(template);
    }

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

    @Bean
    public ProducerFactory<String, Object> dataSourceErrorLogProducerFactory() {
        return producerFactory();
    }

    public ProducerFactory<String, Object> producerFactory() {
        Map<String, Object> producerProperties = new HashMap<>(kafkaCustomProperties.buildProducerCommonProperties());
        return new DefaultKafkaProducerFactory<>(producerProperties);
    }
}
