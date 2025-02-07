package ru.t1.java.service_1.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import ru.t1.java.service_1.model.dto.ClientDto;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class KafkaClientProducer {
    private final KafkaTemplate<String, Object> template;

    // Метод без передачи топика (берется сконфигурированный топик KafkaTemplate из класс KafkaConfig)
    public void send(String clientId) {
        try {
            template.sendDefault(UUID.randomUUID().toString(), clientId);

        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        } finally {
            template.flush();
        }
    }

    public void sendTo(String topic, String key, ClientDto o) {
        try {
            template.send(topic, key, o);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        } finally {
            template.flush();
        }
    }
}
