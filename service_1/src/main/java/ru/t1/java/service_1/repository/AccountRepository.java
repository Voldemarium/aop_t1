package ru.t1.java.service_1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.t1.java.service_1.model.Account;
import ru.t1.java.service_1.model.AccountStatus;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    @Query("from Account a where a.accountId = ?1")
    Optional<Account> findAccountByAccountId(@NonNull String accountId);

    @Query("select id from Account a where a.accountId = ?1")
    Optional<Long> findIdByAccountId(@NonNull String accountId);

    @Query("select accountId from Account a where a.id = ?1")
    Optional<String> findAccountIdById(@NonNull Long id);

    @Transactional
    @Modifying
    @Query("update Account a set a.balance = ?2 where a.accountId = ?1")
    int updateBalanceById(@NonNull String id, BigDecimal updatedBalance);

    @Transactional
    @Modifying
    @Query("update Account a set a.balance = a.balance + ?2 where a.accountId = ?1")
    int updateRejectedBalanceByAccountId(String accountId, BigDecimal transactionAmount);

    @Transactional
    @Modifying
    @Query("update Account a set  a.status = ?2, a.balance = a.balance - ?3, a.frozenAmount = a.frozenAmount + ?3 " +
            "where a.accountId = ?1")
    int updateBlockedBalanceByAccountId(String accountId, AccountStatus status, BigDecimal blockedAmount);

}