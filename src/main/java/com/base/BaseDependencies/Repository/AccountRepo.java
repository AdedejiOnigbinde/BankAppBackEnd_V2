package com.base.BaseDependencies.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.base.BaseDependencies.Models.Account;

public interface AccountRepo extends JpaRepository<Account, Long>{

    @Query(value="delete from Accounts where (accountNumber like :accountId) and (ownerId like :ownerId)"
    ,nativeQuery = true)
    void deleteAccountById(@Param(value="accountId")Long accountId, @Param(value="ownerId")Integer ownerId);

    @Query(value="select * from Accounts where (ownerId like :ownerId)"
    ,nativeQuery = true)
    List<Account> findByClientId(@Param(value="ownerId")Integer ownerId);
    
}
