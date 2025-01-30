package ru.t1.java.demo.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.t1.java.demo.model.Account;
import ru.t1.java.demo.model.AccountStatus;
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
    private final Map<Long, Account> cache;
    private final AccountMapper mapper;

    @PostConstruct
    void init() {
        getAccountDtoById(1L);
    }

    public AccountDto getAccountDtoById(Long id) {
        log.debug("Call method getAccount with id {}", id);
        AccountDto accountDto = null;

        if (cache.containsKey(id)) {
            return mapper.toDto(cache.get(id));
        }
        Account entity = repository.findById(id).get();
        accountDto = mapper.toDto(entity);
        cache.put(id, entity);
        return accountDto;
    }

    public Account getAccountById(Long id) {
        return repository.findById(id).orElseThrow();
    }

    //    @LogDataSourceError
    public void deleteAccountById(Long id) {
        repository.deleteById(id);
    }

    public void saveAccounts(List<Account> accounts) {
        repository.saveAll(accounts);
    }

    public Long getIdByAccountId(String accountId) {
        return repository.findIdByAccountId(accountId);
    }

    public String getAccountIdById(Long id) {
        return repository.findAccountIdById(id);
    }

//    public AccountStatus getStatusById(Long id) {
//        return repository.findStatusById(id);
//    }

//    public BigDecimal getBalanceById(Long id) { return repository.findBalanceById(id); }

    public int updateBalanceById(Long id, BigDecimal updatedBalance) {
       return repository.updateBalanceById(id, updatedBalance);
    }
}
