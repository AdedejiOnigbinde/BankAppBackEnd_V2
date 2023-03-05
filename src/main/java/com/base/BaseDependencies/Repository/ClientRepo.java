package com.base.BaseDependencies.Repository;

import org.springframework.data.jpa.repository.JpaRepository;


import com.base.BaseDependencies.Models.Client;

public interface ClientRepo extends JpaRepository<Client, Integer>{

    Client findBySsn(int ssn);

    Client findByFirstNameAndLastName(String firstName,String lastName);

    Client findByPassword(String password);
    
}
