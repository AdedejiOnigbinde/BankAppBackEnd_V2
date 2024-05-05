package com.base.BaseDependencies.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Entity
@Table(name = "Clients")
@Builder
public class Client implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "client_id", updatable = false, nullable = false, unique = true)
    private int clientId;

    private String photoUrl;

    private String firstName;

    private String userName;

    private String lastName;

    private String password;

    private String address;

    private int pinNumber;

    @Column(updatable = false, nullable = false, unique = true)
    private int ssn;

    @OneToMany(mappedBy = "ownerId", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private List<Account> accounts;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "client_roles", joinColumns = @JoinColumn(name = "client_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles = new ArrayList<>();

    @OneToMany(mappedBy = "loanOwner", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private List<Loan> loans = new ArrayList<>();

    @OneToMany(mappedBy = "beneficiaryOwner", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore 
    @JsonBackReference
    private List<Beneficiary> beneficiaries = new ArrayList<>();

    @OneToMany(mappedBy = "billOwner", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private List<Bill> bills = new ArrayList<>();

    @OneToMany(mappedBy = "payee", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private List<PaidBills> paidBills = new ArrayList<>();

    @OneToMany(mappedBy = "loanRequestOwner", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private List<LoanRequest> loanRequests = new ArrayList<>();
}
