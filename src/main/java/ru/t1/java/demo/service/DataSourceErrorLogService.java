package ru.t1.java.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.t1.java.demo.aop.annotations.LogDataSourceError;
import ru.t1.java.demo.model.DataSourceErrorLog;
import ru.t1.java.demo.repository.DataSourceErrorLogRepository;
import java.util.List;

@RequiredArgsConstructor
@Service
public class DataSourceErrorLogService {
    private final DataSourceErrorLogRepository repository;

    public void saveDataSourceErrorLog(DataSourceErrorLog log) {
        repository.save(log);
    }

//    @LogDataSourceError
    public List<DataSourceErrorLog> getAllErrorLogs() {
        return repository.findAll();
    }

//    @LogDataSourceError
    public void deleteDataSourceErrorLogById(Long id) {
        repository.deleteById(id);
    }
}
