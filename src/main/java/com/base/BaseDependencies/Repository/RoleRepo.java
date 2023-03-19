package com.base.BaseDependencies.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.base.BaseDependencies.Models.Role;

public interface RoleRepo extends JpaRepository<Role,Integer>{
    Optional<Role> findByRoleName(String roleName);
}
