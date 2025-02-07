package ru.t1.java.service_1.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.t1.java.service_1.aop.annotations.LogDataSourceError;
import ru.t1.java.service_1.exception.AccountException;
import ru.t1.java.service_1.model.Account;
import ru.t1.java.service_1.model.AccountStatus;
import ru.t1.java.service_1.model.dto.AccountDto;
import ru.t1.java.service_1.repository.AccountRepository;
import ru.t1.java.service_1.util.AccountMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
@Slf4j
public class AccountService {
    private final AccountRepository repository;
    private final Map<Long, Account> cache;
    private final AccountMapper mapper;

    public AccountDto getAccountDtoById(Long id) {
        if (cache.containsKey(id)) {
            return mapper.toDto(cache.get(id));
        }
        Account entity = repository.findById(id).orElseThrow();
        cache.put(id, entity);
        return mapper.toDto(entity);
    }

    @LogDataSourceError
    public AccountDto getAccountDtoByAccountId(String accountId) {
        return mapper.toDto(repository.findAccountByAccountId(accountId).orElseThrow());
    }

    @LogDataSourceError
    public void deleteAccountById(Long id) {
        repository.deleteById(id);
    }

    @LogDataSourceError
    public void saveAccounts(List<Account> accounts) {
        repository.saveAll(accounts);
    }

    @LogDataSourceError
    public Long getIdByAccountId(String accountId) {
        return repository.findIdByAccountId(accountId).orElseThrow();
    }

    @LogDataSourceError
    public String getAccountIdById(Long id) {
        return repository.findAccountIdById(id).orElseThrow();
    }

    @LogDataSourceError
    public int updateBalanceById(String accountId, BigDecimal updatedBalance) {
        int result = repository.updateBalanceById(accountId, updatedBalance);
        throwExceptionByResult(result);
        return result;
    }

    @LogDataSourceError
    public void updateBlockedBalanceByAccountId(String accountId, AccountStatus status, BigDecimal blockedAmount) {
        int result = repository.updateBlockedBalanceByAccountId(accountId, status, blockedAmount);
        throwExceptionByResult(result);
    }

    @LogDataSourceError
    public void updateRejectedBalanceByAccountId(String accountId, BigDecimal transactionAmount) {
        int result =  repository.updateRejectedBalanceByAccountId(accountId, transactionAmount);
        throwExceptionByResult(result);
    }

    private void throwExceptionByResult(int result) {
        if (result > 1) {
            throw new AccountException("More than one entity was updated!!!: " + result);
        }
    }
}
