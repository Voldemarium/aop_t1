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
import ru.t1.java.demo.kafka.KafkaClientProducer;
import ru.t1.java.demo.model.dto.ClientDto;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class KafkaClientsTopicConfig {
    private final KafkaCommonProperties kafkaCommonProperties;
    @Value("${t1.kafka.consumer.consumer1.group-id}")
    private String client_groupId;
    @Value("${t1.kafka.topic.client_id_registered}")
    private String clientTopic;

    ///  ---------KafkaClientProducer----------------
    @Bean
    public ConsumerFactory<String, ClientDto> consumer1ListenerFactory() {
        Map<String, Object> props = new HashMap<>(kafkaCommonProperties.buildConsumerCommonProperties());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, client_groupId);
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "ru.t1.java.demo.model.dto.ClientDto");
        DefaultKafkaConsumerFactory<String, ClientDto> factory = new DefaultKafkaConsumerFactory<>(props);
        factory.setKeyDeserializer(new StringDeserializer());
        return factory;
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String, ClientDto> kafkaListenerContainerFactory1(
            @Qualifier("consumer1ListenerFactory") ConsumerFactory<String, ClientDto> consumerFactory) {
        return KafkaConsumerGenerator.buildKafkaListenerContainerFactory(consumerFactory);
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

}
