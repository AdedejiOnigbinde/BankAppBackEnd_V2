package com.base.BaseDependencies.Dtos.RequestDtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanRequestDto {
    private double amount;
    
    private double installment;

    private String duration;

    private double interestRate;
}
