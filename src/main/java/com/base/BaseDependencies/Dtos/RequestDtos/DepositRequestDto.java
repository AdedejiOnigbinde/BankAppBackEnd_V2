package com.base.BaseDependencies.Dtos.RequestDtos;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class DepositRequestDto {
    private int depositRequestId;

    private long checkNumber;

    private String checkBank;

    private double checkAmount;

    private String description;

    private String status;

    private LocalDateTime requestDate;

    private long accountNumber;

    private double spitCheckingAmount;

    private double spitSavingsAmount;
}
