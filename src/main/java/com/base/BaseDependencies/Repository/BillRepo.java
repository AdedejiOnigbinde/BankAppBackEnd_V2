package com.base.BaseDependencies.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.base.BaseDependencies.Models.Bill;
import com.base.BaseDependencies.Models.Client;

public interface BillRepo extends JpaRepository<Bill, Integer> {
    Optional<List<Bill>> findByBillOwner(Client beneficiaryOwner);
}
