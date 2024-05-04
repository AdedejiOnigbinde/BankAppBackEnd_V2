package com.base.BaseDependencies.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.base.BaseDependencies.Models.Account;
import com.base.BaseDependencies.Models.Transaction;


public interface TransactionRepo extends JpaRepository<Transaction, Integer> {

    Optional<List<Transaction>> findByFromAccount(Account fromAccount);

    Optional<List<Transaction>> findByFromAccountOrderByTransactionDateDesc(Account fromAccount , Pageable pageable);

}
