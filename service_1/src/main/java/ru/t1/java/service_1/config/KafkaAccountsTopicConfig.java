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
import ru.t1.java.service_1.model.dto.AccountDto;
import ru.t1.java.library.kafka.KafkaConsumerGenerator;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class KafkaAccountsTopicConfig {
    private final KafkaCommonProperties kafkaCommonProperties;
    @Value("${t1.kafka.consumer.consumer2.group-id}")
    private String account_groupId;

    ///  ---------KafkaAccountConsumer---------------
    @Bean
    public ConsumerFactory<String, AccountDto> consumer2ListenerFactory() {
        Map<String, Object> props = new HashMap<>(kafkaCommonProperties.buildConsumerCommonProperties());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, account_groupId);
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "ru.t1.java.service_1.model.dto.AccountDto");
        DefaultKafkaConsumerFactory<String, AccountDto> factory = new DefaultKafkaConsumerFactory<>(props);
        factory.setKeyDeserializer(new StringDeserializer());
        return factory;
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, AccountDto> kafkaListenerContainerFactory2(
            @Qualifier("consumer2ListenerFactory") ConsumerFactory<String, AccountDto> consumerFactory) {
        return KafkaConsumerGenerator.buildKafkaListenerContainerFactory(consumerFactory);
    }
}
