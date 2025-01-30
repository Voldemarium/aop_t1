package ru.t1.java.demo.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.t1.java.demo.model.Account;
import ru.t1.java.demo.model.dto.AccountDto;
import ru.t1.java.demo.service.ClientService;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AccountMapper {
    private final ClientService clientService;

    public Account toEntity(AccountDto accountDto) {
        if ( accountDto == null ) {
            return null;
        }
        Long clientId = clientService.getIdByClientId(accountDto.getClient_id());
        return Account.builder()
                .accountId(accountDto.getAccountId())
                .clientId(clientId)
                .accountType(accountDto.getAccountType())
                .balance(accountDto.getBalance())
                .status(accountDto.getStatus())
                .frozenAmount(accountDto.getFrozenAmount())
                .build();
    }

    public AccountDto toDto(Account account) {
        if ( account == null ) {
            return null;
        }
        assert account.getId() != null;
        String clientId = clientService.getClientIdById(account.getClientId());
        return AccountDto.builder()
                .accountId(account.getAccountId())
                .client_id(clientId)
                .accountType(account.getAccountType())
                .balance(account.getBalance())
                .status(account.getStatus())
                .frozenAmount(account.getFrozenAmount())
                .build();
    }

    public List<AccountDto> toDto(List<Account> accounts) {
        if (accounts == null) {
            return null;
        }
        List<AccountDto> list = new ArrayList<>(accounts.size());
        for (Account account : accounts) {
            list.add(toDto(account));
        }
        return list;
    }

    public List<Account> toEntity(List<AccountDto> accountDtos) {
        if ( accountDtos == null ) {
            return null;
        }
        List<Account> list = new ArrayList<>( accountDtos.size() );
        for ( AccountDto accountDto : accountDtos ) {
            list.add( toEntity( accountDto ) );
        }
        return list;
    }
}