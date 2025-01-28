package ru.t1.java.demo.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Builder
@Table(name = "account")
@NoArgsConstructor
@AllArgsConstructor
public class Account extends AbstractEntity<Long> {
    @Column(name = "account_id")
    private String accountId;

    @JoinColumn(name = "client_id")
    private Long client_id;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type")
    private AccountType accountType;

    @Column(name = "balance", precision = 10, scale = 2)
    BigDecimal balance;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private AccountStatus status;

    @Column(name = "frozen_amount", precision = 19, scale = 2)
    private BigDecimal frozenAmount;

}