package ru.t1.java.demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.t1.java.demo.aop.annotations.HandlingResult;
import ru.t1.java.demo.aop.annotations.LogException;
import ru.t1.java.demo.aop.annotations.Track;
import ru.t1.java.demo.kafka.KafkaClientProducer;
import ru.t1.java.demo.kafka.KafkaTransactionProducer;
import ru.t1.java.demo.model.dto.ClientDto;
import ru.t1.java.demo.model.dto.TransactionDto;
import ru.t1.java.demo.service.TransactionService;
import ru.t1.java.demo.service.impl.ClientServiceImpl;
import ru.t1.java.demo.service.impl.TransactionServiceImpl;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transaction")
public class TransactionController {
    private final TransactionService transactionService;
    private final TransactionServiceImpl transactionServiceImpl;
    private final KafkaTransactionProducer kafkaTransactionProducer;

    @GetMapping(value = "/get/{id}")
    public TransactionDto getTransactionById(@PathVariable("id") long id) {
        return transactionService.getTransactionById(id);
    }

    @DeleteMapping(value = "/deleteById/{id}")
    public void deleteTransactionById(@PathVariable("id") long id){
        transactionService.deleteTransactionById(id);
    }

    @LogException
    @Track
    @GetMapping(value = "/sendAllTransactions")
    @HandlingResult
    public void sendAllTransactions() {
        List<TransactionDto> transactionDtos = transactionServiceImpl.parseJson();
        transactionDtos.forEach(kafkaTransactionProducer::send);
    }
}

