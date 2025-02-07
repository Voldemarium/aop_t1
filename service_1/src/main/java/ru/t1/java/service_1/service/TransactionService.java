package ru.t1.java.service_1.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.t1.java.service_1.aop.annotations.LogDataSourceError;
import ru.t1.java.service_1.exception.AccountException;
import ru.t1.java.service_1.model.Transaction;
import ru.t1.java.service_1.model.dto.TransactionDto;
import ru.t1.java.service_1.repository.TransactionRepository;
import ru.t1.java.service_1.util.TransactionMapper;
import ru.t1.java.library.dto.TransactionStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository repository;
    private final Map<Long, Transaction> cache;
    private final TransactionMapper mapper;

    public TransactionDto getTransactionById(Long id) {
        if (cache.containsKey(id)) {
            return mapper.toDto(cache.get(id));
        }
        Transaction entity = repository.findById(id).orElseThrow();
        cache.put(id, entity);
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

    @LogDataSourceError
    public void updateStatusByTransactionId(String transactionId, TransactionStatus status) throws AccountException {
        int result = repository.updateStatusByTransactionId(transactionId, status);
        throwExceptionByResult(result);
    }

    @LogDataSourceError
    public BigDecimal getAmountByTransactionId(String transactionId) {
        return repository.findAmountByTransactionId(transactionId).orElseThrow();
    }

    private void throwExceptionByResult(int result) {
        if (result > 1) {
            throw new AccountException("More than one entity was updated!!!: " + result);
        }
    }

}
