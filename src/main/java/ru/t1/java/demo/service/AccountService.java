package ru.t1.java.demo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.t1.java.demo.aop.annotations.LogDataSourceError;
import ru.t1.java.demo.exception.AccountException;
import ru.t1.java.demo.model.Account;
import ru.t1.java.demo.model.dto.AccountDto;
import ru.t1.java.demo.repository.AccountRepository;
import ru.t1.java.demo.util.AccountMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
@Slf4j
public class AccountService {
    private final AccountRepository repository;
    private final AccountMapper mapper;
    private final Map<Long, Account> cache;

    @LogDataSourceError
    public AccountDto getAccountDtoById(Long id) {
        if (cache.containsKey(id)) {
            return mapper.toDto(cache.get(id));
        }
        Account entity = repository.findById(id).orElseThrow();
        cache.put(id, entity);
        return mapper.toDto(entity);
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
        if (result > 1) {
            throw new AccountException("More than one entity was updated!!!: " + result);
        }
        return result;
    }
}
