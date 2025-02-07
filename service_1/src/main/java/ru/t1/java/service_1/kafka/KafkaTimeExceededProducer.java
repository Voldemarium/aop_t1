package ru.t1.java.service_1.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.kafka.core.KafkaTemplate;
import ru.t1.java.service_1.model.ErrorType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class KafkaTimeExceededProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendOverMaxExecutionTimeMessage(String topic, String message) {
        try {
            List<Header> headers = new ArrayList<>();
            headers.add(new RecordHeader("error type", ErrorType.METRICS.toString().getBytes()));

            ProducerRecord<String, String> record =
                    new ProducerRecord<>(topic, null, UUID.randomUUID().toString(), message, headers);
            kafkaTemplate.send(record).get().getRecordMetadata();
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        } finally {
            kafkaTemplate.flush();
        }
    }
}
