package ru.t1.java.demo.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@Table(name = "account")
@NoArgsConstructor
@AllArgsConstructor
public class Account extends AbstractEntity<Long> {
    @JoinColumn(name = "client_id")
    private Long client_id;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_type")
    private AccountType accountType;

    @Column(name = "balance")
    Double balance;
}