package ru.t1.java.demo.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import ru.t1.java.demo.model.dto.ClientDto;
import ru.t1.java.demo.model.dto.TransactionDto;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class KafkaTransactionProducer {
    private final KafkaTemplate<String, TransactionDto> template;

    // Метод без передачи топика (берется сконфигурированный топик KafkaTemplate из класс KafkaConfig)
    public void send(TransactionDto t) {
        try {
            template.sendDefault(UUID.randomUUID().toString(), t);

        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        } finally {
            template.flush();
        }
    }
}
