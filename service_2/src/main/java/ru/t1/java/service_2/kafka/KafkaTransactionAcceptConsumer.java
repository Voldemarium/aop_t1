package ru.t1.java.service_2.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import ru.t1.java.library.dto.TransactionAcceptDto;
import ru.t1.java.library.dto.TransactionResultDto;
import ru.t1.java.library.dto.TransactionStatus;
import ru.t1.java.service_2.model.ClientAndAccountId;
import ru.t1.java.service_2.model.ObjectFromRecord;
import ru.t1.java.service_2.model.RecordsParameters;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaTransactionAcceptConsumer {
    @Value("${t1.kafka.consumer.consumer1.period_ms}")
    private Long period;
    @Value("${t1.kafka.consumer.consumer1.max-number-of-transactions}")
    private Integer maxNumberOfTransactions;
    private final KafkaTransactionResultProducer transactionResultProducer;
    // временное хранилище записей для выявления превышения кол-ва транзакций в заданный период времени
    private final Map<ClientAndAccountId, RecordsParameters> recordsParametersMap = new HashMap<>();
    //последнее время сообщения
    private long latestMessageTimestamp = 0;
    //последнее время очистки временного хранилища записей
    private long lastCleanOldRecords = System.currentTimeMillis();

    @KafkaListener(id = "${t1.kafka.consumer.consumer1.group-id}",
            topics = "${t1.kafka.topic.client_transactions_accept}",
            containerFactory = "kafkaListenerContainerFactory")
    public void listener(@Payload List<TransactionAcceptDto> messageList,
                         Acknowledgment ack,
                         @Header(KafkaHeaders.RECEIVED_TIMESTAMP) List<Long> messageTimestampsList
    ) {
        log.info("TransactionAccept consumer: Обработка новых сообщений");
        try {
            for (int i = 0; i < messageList.size(); i++) {
                ///1 Формируем clientAndAccountId
                TransactionAcceptDto transactionAcceptDto = messageList.get(i);
                ClientAndAccountId clientAndAccountId = ClientAndAccountId.builder()
                        .clientId(transactionAcceptDto.getClientId())
                        .accountId(transactionAcceptDto.getAccountId())
                        .build();
                ///2. Формируем объект ObjectFromRecord
                long messageTimestamp = messageTimestampsList.get(i);
                if (messageTimestamp > latestMessageTimestamp) {
                    latestMessageTimestamp = messageTimestamp;
                }
                ObjectFromRecord currentRecord = ObjectFromRecord.builder()
                        .transactionId(transactionAcceptDto.getTransactionId())
                        .messageTimestamp(messageTimestamp)
                        .transactionTimestamp(transactionAcceptDto.getTimestamp().getTime())
                        .build();
                ///3. Формируем объект RecordsParameters
                RecordsParameters parameters;
                if (recordsParametersMap.containsKey(clientAndAccountId)) {
                    parameters = recordsParametersMap.get(clientAndAccountId);
                } else {
                    parameters = new RecordsParameters();
                }
                ///  Добавляем в объект RecordsParameters новую запись и получаем результат
                boolean result = parameters.addObjectAndGetResult(currentRecord, maxNumberOfTransactions, period);
                if (!result) {
                    log.info("превышение кол-ва транзакций в заданный период времени");
                    log.info("блокирование транзакций счета {}", transactionAcceptDto.getAccountId());
                    ///транзакциям из списка присваиваем статус BLOCKED и отправляем в топик t1_demo_transaction_result
                    for (ObjectFromRecord record : parameters.getRecordList()) {
                        TransactionResultDto resultDto = TransactionResultDto.builder()
                                .transactionId(record.getTransactionId())
                                .accountId(clientAndAccountId.getAccountId())
                                .transactionStatus(TransactionStatus.BLOCKED)
                                .build();
                        transactionResultProducer.send(resultDto);
                    }
                    /// очищаем записи, по которым заблокированы транзакции
                    parameters.clear();
                } else {
                    /// - Если сумма списания в транзакции больше, чем баланс счета - отправить сообщение со статусом REJECTED
                    TransactionResultDto resultDto;
                    if (transactionAcceptDto.getTransactionAmount().compareTo(transactionAcceptDto.getAccountBalance()) > 0) {
                        resultDto = TransactionResultDto.builder()
                                .transactionId(currentRecord.getTransactionId())
                                .accountId(clientAndAccountId.getAccountId())
                                .transactionStatus(TransactionStatus.REJECTED)
                                .build();
                    } else {
                        /// - Если всё ок, то статус ACCEPTED
                        resultDto = TransactionResultDto.builder()
                                .transactionId(currentRecord.getTransactionId())
                                .accountId(clientAndAccountId.getAccountId())
                                .transactionStatus(TransactionStatus.ACCEPTED)
                                .build();
                    }
                    transactionResultProducer.send(resultDto);
                }
                ///4. Добавляем RecordsParameters в recordsParametersMap
                recordsParametersMap.put(clientAndAccountId, parameters);
            }
            /// Очищаем все записи от устаревших объектов, а также удаляем пустые записи, если
            /// после последней очистки прошло времени более period (чтобы не нагружать приложение).
            /// При большой загрузке памяти приложения - для временного хранилища записей можно использовать Reddis
            if (System.currentTimeMillis() - lastCleanOldRecords > period) {
                cleanOldRecords();
            }
        } finally {
            ack.acknowledge();
        }
        log.debug("TransactionAccept consumer: записи обработаны");
    }

    private void cleanOldRecords() {
        Iterator<Map.Entry<ClientAndAccountId, RecordsParameters>> iterator = recordsParametersMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<ClientAndAccountId, RecordsParameters> entry = iterator.next();
            ClientAndAccountId key = entry.getKey();
            RecordsParameters recordsParameters = entry.getValue();
            // Очищаем записи от устаревших объектов относительно последнего времени сообщения
            if (recordsParameters.getOldestMessageTime() < latestMessageTimestamp - period) {
                recordsParameters.removeOldRecords(latestMessageTimestamp, period);
                if (recordsParameters.getSize() == 0) {
                    // удаляем корзину с пустыми записями
                    iterator.remove();
                } else {
                    // сохраняем очищенные обновленные записи
                    recordsParametersMap.put(key, recordsParameters);
                }
            }
        }
        this.lastCleanOldRecords = System.currentTimeMillis();
    }
}
