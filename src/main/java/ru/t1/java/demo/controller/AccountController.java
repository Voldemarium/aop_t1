package ru.t1.java.demo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.t1.java.demo.aop.annotations.Metric;
import ru.t1.java.demo.model.dto.AccountDto;
import ru.t1.java.demo.service.AccountService;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountController {
    private final AccountService accountService;

    @GetMapping(value = "/get/{id}")
    public AccountDto getAccountById(@PathVariable("id") long id) {
        log.info("account: {}", accountService.getAccountById(id));
        return accountService.getAccountById(id);
    }

    @Metric(maxExecutionTime = 100)
    @DeleteMapping(value = "/deleteById/{id}")
    public void deleteAccountById(@PathVariable("id") long id){
        accountService.deleteAccountById(id);
    }
}

