package com.base.BaseDependencies.Models;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="Transactions")
public class Transaction implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="trans_id",updatable = false, nullable = false, unique = true)
    private int transId;

    private String transType;
    
    private double transAmount;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_number")
    @JsonManagedReference
    private Account fromAccount;
    
    private long toAcct;


}
