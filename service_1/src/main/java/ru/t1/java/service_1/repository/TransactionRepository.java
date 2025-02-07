package ru.t1.java.service_1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.t1.java.service_1.model.Transaction;
import ru.t1.java.library.dto.TransactionStatus;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Transactional
    @Modifying
    @Query("update Transaction t set t.transactionStatus = ?2 where t.transactionId = ?1")
    int updateStatusByTransactionId(String transactionId, TransactionStatus transactionStatus);

    @Query("select t.amount from Transaction t where t.transactionId = ?1")
    Optional<BigDecimal> findAmountByTransactionId(@NonNull String transactionId);

}