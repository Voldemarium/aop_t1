package ru.t1.java.demo.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.t1.java.demo.model.Transaction;
import ru.t1.java.demo.model.dto.TransactionDto;
import ru.t1.java.demo.service.AccountService;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TransactionMapper {
    private final AccountService accountService;

    public Transaction toEntity(TransactionDto transactionDto) {
        Long accountId = accountService.getIdByAccountId(transactionDto.getAccountId());
        return Transaction.builder()
                .transactionId(transactionDto.getTransactionId())
                .accountId(accountId)
                .amount(transactionDto.getAmount())
                .transactionTime(transactionDto.getTransactionTime())
                .timestamp(transactionDto.getTimestamp())
                .transactionStatus(transactionDto.getTransactionStatus())
                .build();
    }

    public TransactionDto toDto(Transaction transaction) {
        if (transaction == null) {
            return null;
        }
        assert transaction.getId() != null;
        String account_id = accountService.getAccountIdById(transaction.getAccountId());
        return TransactionDto.builder()
                .transactionId(transaction.getTransactionId())
                .accountId(account_id)
                .amount(transaction.getAmount())
                .transactionTime(transaction.getTransactionTime())
                .timestamp(transaction.getTimestamp())
                .transactionStatus(transaction.getTransactionStatus())
                .build();
    }

    public List<Transaction> toEntity(List<TransactionDto> transactionDto) {
        if (transactionDto == null) {
            return null;
        }
        List<Transaction> list = new ArrayList<>(transactionDto.size());
        for (TransactionDto transactionDto1 : transactionDto) {
            list.add(toEntity(transactionDto1));
        }
        return list;
    }

    public List<TransactionDto> toDto(List<Transaction> transaction) {
        if ( transaction == null ) {
            return null;
        }
        List<TransactionDto> list = new ArrayList<>( transaction.size() );
        for ( Transaction transaction1 : transaction ) {
            list.add( toDto( transaction1 ) );
        }
        return list;
    }

}