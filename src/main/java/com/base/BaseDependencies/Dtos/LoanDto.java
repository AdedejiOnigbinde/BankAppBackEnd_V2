package com.base.BaseDependencies.Dtos;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
@Data
public class LoanDto {
    private LocalDateTime startDate;

    private double amount;

    private double paidAmount;

    private double installment;

    private String duration;

    private String status;
}
