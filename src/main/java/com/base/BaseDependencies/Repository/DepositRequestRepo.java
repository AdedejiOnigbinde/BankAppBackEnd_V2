package com.base.BaseDependencies.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.base.BaseDependencies.Models.DepositRequest;
import com.base.BaseDependencies.Models.Account;

public interface DepositRequestRepo extends JpaRepository<DepositRequest, Integer> {
    Optional<List<DepositRequest>> findByStatus(String status);

    Optional<List<DepositRequest>> findByDepositAccount(Account depositAccount);
}
