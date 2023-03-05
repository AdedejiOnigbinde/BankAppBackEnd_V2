package com.base.BaseDependencies.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;



import com.base.BaseDependencies.Models.Account;
import com.base.BaseDependencies.Models.Transaction;


public interface TransactionRepo extends JpaRepository<Transaction, Integer> {

    List<Transaction> findByFromAccount(Account fromAccount);

}
