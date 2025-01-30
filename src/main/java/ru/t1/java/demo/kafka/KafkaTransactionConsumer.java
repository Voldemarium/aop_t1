package ru.t1.java.demo.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.t1.java.demo.model.Account;
import ru.t1.java.demo.model.AccountStatus;
import ru.t1.java.demo.model.Transaction;
import ru.t1.java.demo.model.TransactionStatus;
import ru.t1.java.demo.model.dto.AccountDto;
import ru.t1.java.demo.model.dto.TransactionAcceptDto;
import ru.t1.java.demo.model.dto.TransactionDto;
import ru.t1.java.demo.service.AccountService;
import ru.t1.java.demo.service.ClientService;
import ru.t1.java.demo.service.TransactionService;
import ru.t1.java.demo.util.TransactionMapper;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaTransactionConsumer {
    private final TransactionService transactionService;
    private final AccountService accountService;
    private final ClientService clientService;
    private final TransactionMapper mapper;
    private final KafkaTransactionAcceptProducer transactionAcceptProducer;

    @KafkaListener(id = "${t1.kafka.consumer.consumer3.group-id}",
            topics = "${t1.kafka.topic.client_transactions}",
            containerFactory = "kafkaListenerContainerFactory3")
    public void listener(@Payload List<TransactionDto> messageList,
                         Acknowledgment ack,
                         @Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
                         @Header(KafkaHeaders.RECEIVED_KEY) String key) {
        log.debug("Transaction consumer: Обработка новых сообщений");

        try {
            messageList.stream()
                    .map(mapper::toEntity)
                    .forEach(transaction -> {
                        Account account = accountService.getAccountById(transaction.getAccountId());
                        if (account.getStatus().equals(AccountStatus.OPEN)) {
                            // если статус счета OPEN, то cохраняем транзакцию в БД со статусом REQUESTED
                            transaction.setTransactionStatus(TransactionStatus.REQUESTED);
                            transaction.setTimestamp(new Timestamp(System.currentTimeMillis()));
                            transactionService.saveTransaction(transaction);
                            // изменяем счет клиента на сумму транзакции,
                            BigDecimal balance = account.getBalance();
                            BigDecimal transactionAmount = transaction.getAmount();
                            BigDecimal updatedBalance = balance.subtract(transactionAmount);
                            int res = accountService.updateBalanceById(account.getId(), updatedBalance);
                            if (res == 1) {
                                // отправляет сообщение в топик t1_demo_transaction_accept с информацией
                                TransactionAcceptDto transactionAcceptDto = TransactionAcceptDto.builder()
                                        .clientId(clientService.getClientIdById(account.getClientId()))
                                        .accountId(account.getAccountId())
                                        .transactionId(transaction.getTransactionId())
                                        .timestamp(transaction.getTimestamp())
                                        .transactionAmount(transaction.getAmount())
                                        .accountBalance(updatedBalance)
                                        .build();
                                transactionAcceptProducer.send(transactionAcceptDto);
                            }
                        }
                    });
        } finally {
            ack.acknowledge();
        }
        log.debug("Transaction consumer: записи обработаны");
    }
}
