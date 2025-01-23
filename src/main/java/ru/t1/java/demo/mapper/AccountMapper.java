package ru.t1.java.demo.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import ru.t1.java.demo.model.Account;
import ru.t1.java.demo.model.dto.AccountDto;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING)
public interface AccountMapper {
    Account toEntity(AccountDto accountDto);

    List<AccountDto> toAccountDto(List<Account> account);

    AccountDto toAccountDto(Account account);

    List<Account> toEntity(List<AccountDto> accountDto);
}