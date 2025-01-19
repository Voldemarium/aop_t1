package ru.t1.java.demo.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.t1.java.demo.dto.TransactionDto;
import ru.t1.java.demo.model.Transaction;
import ru.t1.java.demo.repository.TransactionRepository;
import ru.t1.java.demo.util.TransactionMapper;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository repository;
    private final Map<Long, Transaction> cache;

    @PostConstruct
    void init() {
        getTransactionById(1L);
    }

    public TransactionDto getTransactionById(Long id) {
        log.debug("Call method getTransactionById with id {}", id);
        TransactionDto transactionDto;

        if (cache.containsKey(id)) {
            return TransactionMapper.toDto(cache.get(id));
        }

        Transaction entity = repository.findById(id).get();
        transactionDto = TransactionMapper.toDto(entity);
        cache.put(id, entity);
        return transactionDto;
    }

    public void deleteTransactionById(Long id) {
        repository.deleteById(id);
    }

}
