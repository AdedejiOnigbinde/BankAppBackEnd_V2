package com.base.BaseDependencies.Repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.base.BaseDependencies.Models.Account;
import com.base.BaseDependencies.Models.Client;
@Repository
public interface AccountRepo extends JpaRepository<Account, Long>{

    void deleteByAccountNumberAndOwnerId(Long accountNumber,Integer ownerId);

    Optional<Account> findByAccountNumberAndOwnerId(long accountNumber, Client client);

    Optional<List<Account>> findByOwnerId(Client client);
    
}
