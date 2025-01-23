package ru.t1.java.demo.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.t1.java.demo.aop.annotations.LogDataSourceError;
import ru.t1.java.demo.aop.annotations.LogExecution;
import ru.t1.java.demo.model.dto.TransactionDto;
import ru.t1.java.demo.model.Transaction;
import ru.t1.java.demo.repository.TransactionRepository;
import ru.t1.java.demo.util.TransactionMapper;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository repository;
    private final Map<Long, Transaction> cache;

    @LogDataSourceError
    @LogExecution
    public TransactionDto getTransactionById(Long id) {
        if (cache.containsKey(id)) {
            return TransactionMapper.toDto(cache.get(id));
        }
        Transaction entity = repository.findById(id).orElseThrow();
        cache.put(id, entity);
        return TransactionMapper.toDto(entity);
    }

    @LogDataSourceError
    @LogExecution
    public void deleteTransactionById(Long id) {
        repository.deleteById(id);
    }

    @LogDataSourceError
    @LogExecution
    public void saveTransactions(List<Transaction> transactions) {
        repository.saveAll(transactions);
    }
}
