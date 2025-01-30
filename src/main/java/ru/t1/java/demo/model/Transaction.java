package ru.t1.java.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(schema = "t1_demo", name = "transaction")
public class Transaction extends AbstractEntity<Long> {
    @Column(name = "transaction_id")
    @NotNull
    private String transactionId;

    @JoinColumn(name = "account_id")
    @NotNull
    private Long accountId;

    @Column(name = "amount", precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(name = "transaction_time")
//    @CreationTimestamp // устанавливает дату и время создания при первом сохранении записи
    private LocalDateTime transactionTime;

    @Column(name = "time_stamp")
//    @UpdateTimestamp // обновляет время последнего изменения при каждом обновлении записи
    private Timestamp timestamp;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_status")
    private TransactionStatus transactionStatus;

}