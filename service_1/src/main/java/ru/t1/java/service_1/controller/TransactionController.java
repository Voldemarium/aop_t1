package ru.t1.java.service_1.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.t1.java.service_1.aop.annotations.LogException;
import ru.t1.java.service_1.aop.annotations.Track;
import ru.t1.java.service_1.kafka.KafkaTransactionProducer;
import ru.t1.java.service_1.model.dto.TransactionDto;
import ru.t1.java.service_1.service.TransactionService;
import ru.t1.java.service_1.service.impl.TransactionServiceImpl;

import java.util.List;

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
//    @HandlingResult
    public void sendAllTransactions() {
        List<TransactionDto> transactionDtos = transactionServiceImpl.parseJson();
        transactionDtos.forEach(kafkaTransactionProducer::send);
    }
}

