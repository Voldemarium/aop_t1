package ru.t1.java.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Builder
@Table(schema = "t1_demo", name = "account")
@NoArgsConstructor
@AllArgsConstructor
public class Account extends AbstractEntity<Long> {
    @Column(name = "account_id", unique = true)
    @NotNull
    private String accountId;

    @JoinColumn(name = "client_id")
    @NotNull
    private Long clientId;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type")
    @NotNull
    private AccountType accountType;

    @Column(name = "balance", precision = 19, scale = 2)
    BigDecimal balance;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private AccountStatus status;

    @Column(name = "frozen_amount", precision = 19, scale = 2)
    private BigDecimal frozenAmount;

}