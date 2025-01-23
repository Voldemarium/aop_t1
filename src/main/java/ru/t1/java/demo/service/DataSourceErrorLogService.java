package ru.t1.java.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.t1.java.demo.kafka.KafkaErrorProducer;
import ru.t1.java.demo.mapper.DataSourceErrorLogMapper;
import ru.t1.java.demo.model.DataSourceErrorLog;
import ru.t1.java.demo.model.dto.DataSourceErrorLogDto;
import ru.t1.java.demo.repository.DataSourceErrorLogRepository;
import java.util.List;

@RequiredArgsConstructor
@Service
public class DataSourceErrorLogService {
    private final DataSourceErrorLogRepository repository;
    private final KafkaErrorProducer producer;
    private final DataSourceErrorLogMapper errorLogMapper;

    public void saveDataSourceErrorLog(DataSourceErrorLog log) {
        repository.save(log);
    }

    public boolean sendDataSourceErrorLog(String topic, DataSourceErrorLog errorLog) {
       return producer.sendDataSourceErrorLog(topic, errorLogMapper.toDto(errorLog));
    }

//    @LogDataSourceError
    public List<DataSourceErrorLogDto> getAllErrorLogs() {
        return errorLogMapper.toDataSourceErrorLogDto(repository.findAll());
    }

//    @LogDataSourceError
    public void deleteDataSourceErrorLogById(Long id) {
        repository.deleteById(id);
    }
}
