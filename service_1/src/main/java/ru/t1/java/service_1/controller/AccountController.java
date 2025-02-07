package ru.t1.java.service_1.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.t1.java.service_1.aop.annotations.Metric;
import ru.t1.java.service_1.model.dto.AccountDto;
import ru.t1.java.service_1.service.AccountService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountController {
    private final AccountService accountService;

    @GetMapping(value = "/get/{id}")
    public AccountDto getAccountById(@PathVariable("id") long id) {
        return accountService.getAccountDtoById(id);
    }

    @Metric(maxExecutionTime = 3000)
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

