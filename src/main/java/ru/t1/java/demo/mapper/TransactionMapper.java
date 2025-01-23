package ru.t1.java.demo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.t1.java.demo.model.Transaction;
import ru.t1.java.demo.model.dto.TransactionDto;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface TransactionMapper {
    Transaction toEntity(TransactionDto transactionDto);

    List<TransactionDto> toTransactionDto(List<Transaction> transaction);

    TransactionDto toTransactionDto(Transaction transaction);

    List<Transaction> toEntity(List<TransactionDto> transactionDto);
}