package ru.t1.java.service_1.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.t1.java.service_1.exception.TransactionException;
import ru.t1.java.service_1.model.AccountStatus;
import ru.t1.java.service_1.model.dto.AccountDto;
import ru.t1.java.service_1.model.dto.TransactionDto;
import ru.t1.java.service_1.service.AccountService;
import ru.t1.java.service_1.service.ClientService;
import ru.t1.java.service_1.service.TransactionService;
import ru.t1.java.service_1.util.TransactionMapper;
import ru.t1.java.library.dto.TransactionAcceptDto;
import ru.t1.java.library.dto.TransactionStatus;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaTransactionConsumer {
    private final TransactionService transactionService;
    private final AccountService accountService;
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
                        AccountDto accountDto = accountService.getAccountDtoById(transaction.getAccountId());
                        String accountId = accountDto.getAccountId();
                        if (accountDto.getStatus().equals(AccountStatus.OPEN)) {
                            // если статус счета OPEN, то cохраняем транзакцию в БД со статусом REQUESTED
                            transaction.setTransactionStatus(TransactionStatus.REQUESTED);
                            transaction.setTimestamp(new Timestamp(System.currentTimeMillis()));
                            transactionService.saveTransaction(transaction);
                            // изменяем счет клиента на сумму транзакции,
                            BigDecimal balance = accountDto.getBalance();
                            BigDecimal transactionAmount = transaction.getAmount();
                            int res = accountService.updateBalanceById(accountId, balance.subtract(transactionAmount));
                            if (res == 1) {
                                // отправляет сообщение в топик t1_demo_transaction_accept с информацией
                                TransactionAcceptDto transactionAcceptDto = TransactionAcceptDto.builder()
                                        .clientId(accountDto.getClientId())
                                        .accountId(accountId)
                                        .transactionId(transaction.getTransactionId())
                                        .timestamp(transaction.getTimestamp())
                                        .transactionAmount(transaction.getAmount())
                                        .accountBalance(balance)
                                        .build();
                                transactionAcceptProducer.send(transactionAcceptDto);
                            }
                        } else {
                            throw new TransactionException("счет" + accountId +
                                    "не открыт! Статус:" + accountDto.getStatus() + "!!!, " +
                                    "транзакция: " + transaction.getTransactionId());
                        }
                    });
        } finally {
            ack.acknowledge();
        }
        log.debug("Transaction consumer: записи обработаны");
    }
}
