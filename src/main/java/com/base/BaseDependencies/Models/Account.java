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
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Accounts")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Account implements Serializable {
    @Id
    @Column(name = "account_number", updatable = false, nullable = false, unique = true)
    private long accountNumber;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "client_id")
    @JsonManagedReference
    private Client ownerId;

    @Column(columnDefinition = "double precision default 0")
    private double balance;

    @Column(columnDefinition = "double precision default 1000000")
    private double dailyTransferLimit;

    @Column(columnDefinition = "double precision default 0")
    private double calcLimit;

    @OneToMany(mappedBy = "fromAccount", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private List<Transaction> transaction;

    @OneToMany(mappedBy = "requestAccount", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private List<LimitModificationRequest> limitModificationRequests;

    @OneToMany(mappedBy = "depositAccount", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private List<DepositRequest> depositRequests;

    private String accountType;

    private String accountStatus;
}
