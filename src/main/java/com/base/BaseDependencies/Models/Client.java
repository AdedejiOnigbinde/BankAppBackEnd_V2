package com.base.BaseDependencies.Models;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Entity
@Table(name = "Clients")
public class Client implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="clint_id",updatable = false, nullable = false, unique = true)
    private int clientId;

    private String firstName;

    private String lastName;

    private String password;

    private String address;

    private int pinNumber;

    @Column(updatable = false, nullable = false, unique = true)
    private int ssn;

    @OneToMany(mappedBy ="accountNumber" ,cascade=CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private List<Account> accounts;

}
