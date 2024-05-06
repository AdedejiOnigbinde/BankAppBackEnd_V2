package com.base.BaseDependencies.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.base.BaseDependencies.Models.Client;
import com.base.BaseDependencies.Models.Loan;

public interface LoansRepo extends JpaRepository<Loan, Integer> {
        Optional<List<Loan>> findByLoanOwner(Client client);

        @Query("SELECT SUM(l.amount) FROM Loan l WHERE l.loanOwner= :clientId AND l.status = 'approved'")
        Optional<Double> getSumOfLoanByLoanOwner(@Param("clientId") Client clientId);

}
