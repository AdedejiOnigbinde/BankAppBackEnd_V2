package com.base.BaseDependencies.Dtos;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionDto {

    private String transType;
    
    private double transAmount;

    private long fromAcct;
    
    private long toAcct;

    private LocalDateTime transactionDate;

    private double balance;

    private String status;
}
