package ru.t1.java.demo.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.AbstractPersistable;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transaction")
public class Transaction extends AbstractEntity<Long> {
    @JoinColumn(name = "account_id")
    private Long account_id;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "transaction_time")
    private LocalDateTime transactionTime;

}