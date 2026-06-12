package com.base.BaseDependencies.Dtos.RequestDtos;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoanRequestDto {
    private int loanRequestId;

    @Positive
    private double amount;

    @Positive
    private double installment;

    @NotBlank
    private String duration;

    @Positive
    private double interestRate;
}
