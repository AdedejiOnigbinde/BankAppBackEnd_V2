package com.base.BaseDependencies.Dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {

    private String transType;
    
    private double transAmount;

    private long fromAcct;
    
    private long toAcct;
}
