package com.base.BaseDependencies.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.base.BaseDependencies.Models.Client;
import com.base.BaseDependencies.Models.Loan;

public interface LoansRepo extends JpaRepository<Loan,Integer>{
        Optional<List<Loan>> findByLoanOwner(Client client);
}
