package com.base.BaseDependencies.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


import com.base.BaseDependencies.Models.Client;

public interface ClientRepo extends JpaRepository<Client, Integer>{

    Optional<Client> findBySsn(int ssn);

    Optional<Client> findByFirstNameAndLastNameAndPassword(String firstName,String lastName,String password);

    Optional<Client> findByUserName(String username);
    
}
