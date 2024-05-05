package com.base.BaseDependencies.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.base.BaseDependencies.Models.LoanRequest;

public interface LoansRequestRepo extends JpaRepository<LoanRequest, Integer> {
    Optional<List<LoanRequest>> findByStatus(String status);
}
