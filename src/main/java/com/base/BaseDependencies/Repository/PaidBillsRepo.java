package com.base.BaseDependencies.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.base.BaseDependencies.Models.Client;
import com.base.BaseDependencies.Models.PaidBills;
import java.util.List;
import java.util.Optional;


public interface PaidBillsRepo extends JpaRepository<PaidBills, Integer> {
        Optional<List<PaidBills>> findByPayee(Client payee);
}
