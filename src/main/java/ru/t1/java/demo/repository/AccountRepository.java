package ru.t1.java.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.t1.java.demo.model.Account;
import ru.t1.java.demo.model.AccountStatus;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    @Query("select id from Account a where a.accountId = ?1")
    Long findIdByAccountId(@NonNull String accountId);

    @Query("select accountId from Account a where a.id = ?1")
    String findAccountIdById(@NonNull Long id);

    @Query("select status from Account a where a.id = ?1")
    AccountStatus findStatusById(@NonNull Long id);

    @Query("select balance from Account a where a.id = ?1")
    BigDecimal findBalanceById(@NonNull Long id);

    @Transactional
    @Modifying
    @Query("update Account a set a.balance = ?2 where a.id = ?1")
    int updateBalanceById(@NonNull Long id, BigDecimal updatedBalance);

}