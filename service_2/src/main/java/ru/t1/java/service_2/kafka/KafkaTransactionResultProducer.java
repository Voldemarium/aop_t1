package ru.t1.java.service_2.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import ru.t1.java.library.dto.TransactionResultDto;

import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class KafkaTransactionResultProducer {
    private final KafkaTemplate<String, TransactionResultDto> template;

    public void send(TransactionResultDto t) {
        try {
            template.sendDefault(UUID.randomUUID().toString(), t).get();

        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        } finally {
            template.flush();
        }
    }
}
