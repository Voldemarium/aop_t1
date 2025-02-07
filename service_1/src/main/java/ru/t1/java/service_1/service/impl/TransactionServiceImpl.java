package ru.t1.java.service_1.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import ru.t1.java.service_1.aop.annotations.HandlingResult;
import ru.t1.java.service_1.aop.annotations.LogExecution;
import ru.t1.java.service_1.aop.annotations.Track;
import ru.t1.java.service_1.kafka.KafkaTransactionProducer;
import ru.t1.java.service_1.model.Transaction;
import ru.t1.java.service_1.model.dto.TransactionDto;
import ru.t1.java.service_1.repository.TransactionRepository;
import ru.t1.java.service_1.util.TransactionMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionServiceImpl implements ImplService<Transaction, TransactionDto> {
    private final TransactionRepository repository;
    private final KafkaTransactionProducer kafkaTransactionProducer;
    private final TransactionMapper transactionMapper;
    @Value("${t1.mock_transaction}")
    private String mock;

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
        ObjectMapper mapper = new ObjectMapper();

        TransactionDto[] transactionDtos;

        StringBuilder stringBuilder = new StringBuilder();
        try {
            InputStream resource = new ClassPathResource(mock).getInputStream();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource))) {
                String line;
                do {
                    line = reader.readLine();
                    stringBuilder.append(line);
                }
                while (line != null);
                String stringJson = stringBuilder.toString();
                transactionDtos = mapper.readValue(stringJson, TransactionDto[].class);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return Arrays.asList(transactionDtos);
    }
}
