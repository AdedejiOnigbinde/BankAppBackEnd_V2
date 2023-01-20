package com.base.BaseDependencies.Models;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Accounts")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Account implements Serializable{
    @Id
    @Column(updatable = false, nullable = false, unique = true)
    private long accountNumber;

    @JoinColumn(name = "clientId")
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private Client ownerId;

    @Column(columnDefinition = "double default 0")
    private double balance;

    @OneToMany(mappedBy = "fromAcct", orphanRemoval = true)
    @JsonBackReference
    private List<Transaction> transaction;

    private String accountType;
}
