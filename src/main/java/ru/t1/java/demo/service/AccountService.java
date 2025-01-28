package ru.t1.java.demo.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.t1.java.demo.aop.annotations.LogDataSourceError;
import ru.t1.java.demo.dto.AccountDto;
import ru.t1.java.demo.model.Account;
import ru.t1.java.demo.repository.AccountRepository;
import ru.t1.java.demo.util.AccountMapper;

import java.util.Map;

@RequiredArgsConstructor
@Service
@Slf4j
public class AccountService {
    private final AccountRepository repository;
    private final Map<Long, Account> cache;

    @PostConstruct
    void init() {
        getAccountById(1L);
    }

    public AccountDto getAccountById(Long id) {
        log.debug("Call method getAccount with id {}", id);
        AccountDto accountDto;

        if (cache.containsKey(id)) {
            return AccountMapper.toDto(cache.get(id));
        }
        Account entity = repository.findById(id).orElseThrow();
        accountDto = AccountMapper.toDto(entity);
        cache.put(id, entity);
        return accountDto;
    }

    @LogDataSourceError
    public void deleteAccountById(Long id) {
        repository.deleteById(id);
    }
}
