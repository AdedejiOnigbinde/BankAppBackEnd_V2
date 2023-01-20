package com.base.BaseDependencies.Repository;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.base.BaseDependencies.Models.Transaction;

public interface TransactionRepo extends JpaRepository<Transaction, Integer> {

    @Query(value = "select * from Transactions where accountNumber = :fromAccount returning ", nativeQuery = true)
    List<Transaction> findByAccountNumber(@Param(value = "accountNumber") long accountNumber);

}
