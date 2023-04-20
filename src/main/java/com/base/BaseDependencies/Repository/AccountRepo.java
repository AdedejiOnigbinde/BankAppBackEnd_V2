package com.base.BaseDependencies.Repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.base.BaseDependencies.Models.Account;
import com.base.BaseDependencies.Models.Client;
@Repository
public interface AccountRepo extends JpaRepository<Account, Long>{

    @Transactional
    void deleteByAccountNumberAndOwnerId(Long accountNumber,Client client);

    Optional<Account> findByAccountNumberAndOwnerId(long accountNumber, Client client);

    Optional<List<Account>> findByOwnerId(Client client);
    
}
