package ru.t1.java.service_1.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import ru.t1.java.library.dto.TransactionResultDto;
import ru.t1.java.library.kafka.KafkaConsumerGenerator;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class KafkaTransactionsResultTopicConfig {
    private final KafkaCommonProperties kafkaCommonProperties;
    @Value("${t1.kafka.consumer.consumer4.group-id}")
    private String transaction_result_groupId;

    @Bean
    public ConsumerFactory<String, TransactionResultDto> consumer4ListenerFactory() {
        Map<String, Object> props = new HashMap<>(kafkaCommonProperties.buildConsumerCommonProperties());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, transaction_result_groupId);
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "ru.t1.java.library.dto.TransactionResultDto");
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "ru.t1.java.library.dto.");
        DefaultKafkaConsumerFactory<String, TransactionResultDto> factory = new DefaultKafkaConsumerFactory<>(props);
        factory.setKeyDeserializer(new StringDeserializer());
        return factory;
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, TransactionResultDto> kafkaListenerContainerFactory4(
            @Qualifier("consumer4ListenerFactory") ConsumerFactory<String, TransactionResultDto> consumerFactory) {
        return KafkaConsumerGenerator.buildKafkaListenerContainerFactory(consumerFactory);
    }
}
