package com.base.BaseDependencies.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;



import com.base.BaseDependencies.Models.Account;

public interface AccountRepo extends JpaRepository<Account, Long>{

    void deleteByAccountNumberAndOwnerId(Long accountNumber,Integer ownerId);

    List<Account> findByOwnerId(Integer ownerId);
    
}
