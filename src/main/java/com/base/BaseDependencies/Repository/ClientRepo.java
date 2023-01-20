package com.base.BaseDependencies.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.base.BaseDependencies.Models.Client;

public interface ClientRepo extends JpaRepository<Client, Integer>{

    Client findBySsn(int ssn);

    @Query(value="select * from Clients where (firstName like :firstName) and (lastName like :lastName)"
    ,nativeQuery = true)
    Client findByName(@Param(value="firstName")String firstName, @Param(value="lastName")String lastName);

    Client findByPassword(String password);
    
}
