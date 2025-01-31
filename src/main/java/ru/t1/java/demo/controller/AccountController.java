package ru.t1.java.demo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.t1.java.demo.aop.annotations.Metric;
import ru.t1.java.demo.model.dto.AccountDto;
import ru.t1.java.demo.repository.AccountRepository;
import ru.t1.java.demo.service.AccountService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountController {
    private final AccountService accountService;

    @GetMapping(value = "/getById/{id}")
    public AccountDto getAccountById(@PathVariable("id") long id) {
        return accountService.getAccountDtoById(id);
    }

    @Metric(maxExecutionTime = 100)
    @DeleteMapping(value = "/deleteById/{id}")
    public void deleteAccountById(@PathVariable("id") long id){
        accountService.deleteAccountById(id);
    }

    @Metric(maxExecutionTime = 100)
    @GetMapping(value = "/getByAccountId/{account_id}")
    public AccountDto getAccountDtoByAccountId(@PathVariable("account_id") String accountId) {
         return accountService.getAccountDtoByAccountId(accountId);

    }
}

