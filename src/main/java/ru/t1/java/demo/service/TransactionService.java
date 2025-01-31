package ru.t1.java.demo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.t1.java.demo.aop.annotations.LogDataSourceError;
import ru.t1.java.demo.model.Transaction;
import ru.t1.java.demo.model.dto.TransactionDto;
import ru.t1.java.demo.repository.TransactionRepository;
import ru.t1.java.demo.util.TransactionMapper;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository repository;
    private final TransactionMapper mapper;

    @LogDataSourceError
    public TransactionDto getTransactionById(Long id) {
        Transaction entity = repository.findById(id).orElseThrow();
        return mapper.toDto(entity);
    }

    @LogDataSourceError
    public void deleteTransactionById(Long id) {
        repository.deleteById(id);
    }

    @LogDataSourceError
    public void saveTransaction(Transaction transaction) {
        repository.save(transaction);
    }

    @LogDataSourceError
    public void saveTransactions(List<Transaction> transactions) {
        repository.saveAll(transactions);

    }
}
