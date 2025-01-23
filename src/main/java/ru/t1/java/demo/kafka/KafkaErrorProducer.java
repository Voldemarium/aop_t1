package ru.t1.java.demo.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.kafka.core.KafkaTemplate;
import ru.t1.java.demo.model.ErrorType;
import ru.t1.java.demo.model.dto.DataSourceErrorLogDto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
public class KafkaErrorProducer {
    private final KafkaTemplate<String, DataSourceErrorLogDto> kafkaTemplate;

    public boolean sendDataSourceErrorLog(String topic, DataSourceErrorLogDto errorLog) {
        try {
            List<Header> headers = new ArrayList<>();
            headers.add(new RecordHeader("error type", ErrorType.DATA_SOURCE.toString().getBytes()));
            ProducerRecord<String, DataSourceErrorLogDto> record =
                    new ProducerRecord<>(topic, null, UUID.randomUUID().toString(), errorLog, headers);
            return kafkaTemplate.send(record).get().getRecordMetadata().hasOffset();
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        } finally {
            kafkaTemplate.flush();
        }
        return false;
    }

}
