package ru.t1.java.service_1.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.t1.java.service_1.model.AccountStatus;
import ru.t1.java.service_1.service.AccountService;
import ru.t1.java.service_1.service.TransactionService;
import ru.t1.java.library.dto.TransactionResultDto;
import ru.t1.java.library.dto.TransactionStatus;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaTransactionResultConsumer {
    private final TransactionService transactionService;
    private final AccountService accountService;
    AtomicBoolean prevStatusIsBlocked = new AtomicBoolean(false);
    Map<String, BigDecimal> blockedSumMap = new HashMap<>();


    @KafkaListener(id = "${t1.kafka.consumer.consumer4.group-id}",
            topics = "${t1.kafka.topic.client_transactions_result}",
            containerFactory = "kafkaListenerContainerFactory4")
    public void listener(@Payload List<TransactionResultDto> messageList, Acknowledgment ack) {
        log.info("TransactionResult consumer: Обработка новых сообщений");

        try {
            messageList.forEach(transactionResult -> {
                TransactionStatus status = transactionResult.getTransactionStatus();
                String transactionId = transactionResult.getTransactionId();
                String accountId = transactionResult.getAccountId();
                //достаем из БД сумму транзакции
                BigDecimal amount = transactionService.getAmountByTransactionId(transactionId);

                /// ==============TransactionStatus.BLOCKED==========================
                if (status.equals(TransactionStatus.BLOCKED)) {
                    if (!prevStatusIsBlocked.get()) {
                        prevStatusIsBlocked.set(true);
                    }
                    transactionService.updateStatusByTransactionId(transactionId, TransactionStatus.BLOCKED);

                    if (!blockedSumMap.containsKey(accountId)) {
                        blockedSumMap.put(accountId, amount);
                    } else {
                        BigDecimal sumAmount = blockedSumMap.get(accountId);
                        sumAmount = sumAmount.add(amount);
                        blockedSumMap.put(accountId, sumAmount);
                    }
                } else if (prevStatusIsBlocked.get()) {
                    // Если статус не BLOCKED, но предыдущий был BLOCKED - проверяем, есть ли в blockedSumMap объекты
                    if (!blockedSumMap.isEmpty()) {
                        blockedSumMap.forEach((blockedAccountId, blockedAmount) -> {
                            //  Dыставляем счёту статус BLOCKED.
                            // Баланс счёта меняется следующим образом: счет корректируется на сумму
                            //заблокированных транзакций, сумма записывается в поле frozenAmount
                            accountService.updateBlockedBalanceByAccountId(blockedAccountId, AccountStatus.BLOCKED,
                                    blockedAmount);
                        });
                        //  очищаем blockedSumMap и скидываем prevStatusIsBlocked на false
                        blockedSumMap.clear();
                        prevStatusIsBlocked.set(false);
                    }
                }

                /// ==============TransactionStatus.ACCEPTED==========================
                if (status.equals(TransactionStatus.ACCEPTED)) {
                    transactionService.updateStatusByTransactionId(transactionId, TransactionStatus.ACCEPTED);

                    /// ==============TransactionStatus.REJECTED ==========================
                } else if (status.equals(TransactionStatus.REJECTED)) {
                    transactionService.updateStatusByTransactionId(transactionId, TransactionStatus.REJECTED);
                    accountService.updateRejectedBalanceByAccountId(accountId, amount);
                }
            });
        } finally {
            ack.acknowledge();
        }
        log.debug("TransactionResult consumer: записи обработаны");
    }


}
