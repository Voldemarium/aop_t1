package ru.t1.java.demo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.t1.java.demo.aop.annotations.LogDataSourceError;
import ru.t1.java.demo.model.dto.AccountDto;
import ru.t1.java.demo.model.Account;
import ru.t1.java.demo.repository.AccountRepository;
import ru.t1.java.demo.util.AccountMapper;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
@Slf4j
public class AccountService {
    private final AccountRepository repository;
    private final Map<Long, Account> cache;

    @LogDataSourceError
    public AccountDto getAccountById(Long id) {
        if (cache.containsKey(id)) {
            return AccountMapper.toDto(cache.get(id));
        }
        Account entity = repository.findById(id).orElseThrow();
        cache.put(id, entity);
        return AccountMapper.toDto(entity);
    }

    @LogDataSourceError
    public void deleteAccountById(Long id) {
        repository.deleteById(id);
    }

    @LogDataSourceError
    public void saveAccounts(List<Account> accounts) {
        repository.saveAll(accounts);
    }
}
