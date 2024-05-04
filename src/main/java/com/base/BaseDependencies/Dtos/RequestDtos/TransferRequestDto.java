package com.base.BaseDependencies.Dtos.RequestDtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransferRequestDto {
    private long toAcct;

    private long fromacct;

    private String bank;

    private double amount;

    private int pin;

    private boolean addBeneficary;
}
