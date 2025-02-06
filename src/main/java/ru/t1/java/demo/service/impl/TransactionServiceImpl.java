package ru.t1.java.demo.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.t1.java.demo.aop.annotations.HandlingResult;
import ru.t1.java.demo.aop.annotations.LogExecution;
import ru.t1.java.demo.aop.annotations.Track;
import ru.t1.java.demo.kafka.KafkaTransactionProducer;
import ru.t1.java.demo.model.Transaction;
import ru.t1.java.demo.model.dto.TransactionDto;
import ru.t1.java.demo.repository.TransactionRepository;
import ru.t1.java.demo.util.TransactionMapper;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionServiceImpl implements ImplService<Transaction, TransactionDto> {
    private final TransactionRepository repository;
    private final KafkaTransactionProducer kafkaTransactionProducer;
    private final TransactionMapper transactionMapper;
    private final ObjectMapper mapper;

    @Override
    public void registerEvents(List<Transaction> transactions) {
        transactions = transactions.stream().filter(transaction -> transaction.getTransactionId() != null &&
                transaction.getAccountId() != null).toList();
        if (!transactions.isEmpty()) {
            repository.saveAll(transactions)             // сохраняем список в БД
                    .stream()
                    .map(transactionMapper::toDto)
                    .forEach(kafkaTransactionProducer::send); // отправляем сообщения в Kafka с сохраненным Id клиента
        }
    }

    @Override
    @LogExecution
    @Track
    @HandlingResult
    public List<TransactionDto> parseJson() {
        TransactionDto[] transactionDtos = new TransactionDto[0];
        try {
            transactionDtos = mapper.readValue(new File("src/main/resources/MOCK_TRANSACTION.json"),
                    TransactionDto[].class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Arrays.asList(transactionDtos);
    }
}
