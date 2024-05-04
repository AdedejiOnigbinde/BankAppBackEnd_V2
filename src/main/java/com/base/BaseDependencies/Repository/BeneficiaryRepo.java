package com.base.BaseDependencies.Repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.base.BaseDependencies.Models.Beneficiary;
import java.util.List;
import java.util.Optional;

import com.base.BaseDependencies.Models.Client;


public interface BeneficiaryRepo extends JpaRepository<Beneficiary, Integer> {
        Optional<List<Beneficiary>> findByBeneficiaryOwner(Client beneficiaryOwner);
}
