package ru.t1.java.demo.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.security.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "transaction")
public class Transaction extends AbstractEntity<Long> {
    @Column(name = "transaction_id")
    private String transactionId;

    @JoinColumn(name = "account_id")
    private Long account_id;

    @Column(name = "amount", precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(name = "transaction_time")
    private LocalDateTime transactionTime;

    @Column(name = "timestamp")
    @CreationTimestamp
    private Timestamp timestamp;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_status")
    private TransactionStatus transactionStatus;


}