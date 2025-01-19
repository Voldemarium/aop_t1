package ru.t1.java.demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.t1.java.demo.dto.TransactionDto;
import ru.t1.java.demo.service.TransactionService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transaction")
public class TransactionController {
    private final TransactionService transactionService;

    @GetMapping(value = "/get/{id}")
    public TransactionDto getTransactionById(@PathVariable("id") long id) {
        return transactionService.getTransactionById(id);
    }

    @DeleteMapping(value = "/deleteById/{id}")
    public void deleteTransactionById(@PathVariable("id") long id){
        transactionService.deleteTransactionById(id);
    }
}

