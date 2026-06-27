package com.base.BaseDependencies.Repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.base.BaseDependencies.Models.Client;

public interface ClientRepo extends JpaRepository<Client, Integer>{

    Optional<Client> findBySsn(String ssn);

    @Query(value = "SELECT DISTINCT c FROM Client c JOIN c.roles r WHERE r.roleName = :roleName",
           countQuery = "SELECT COUNT(DISTINCT c) FROM Client c JOIN c.roles r WHERE r.roleName = :roleName")
    Page<Client> findClientsByRoleName(@Param("roleName") String roleName, Pageable pageable);

    Optional<Client> findByFirstNameAndLastNameAndPassword(String firstName,String lastName,String password);

    Optional<Client> findByUserName(String username);

    void deleteByUserName(String username);
    
}
