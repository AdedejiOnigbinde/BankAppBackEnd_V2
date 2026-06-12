package com.base.BaseDependencies.Dtos.RequestDtos;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransferRequestDto {
    @Positive
    private long toAcct;

    @Positive
    private long fromacct;

    @NotBlank
    private String bank;

    @Positive
    private double amount;

    @Min(0)
    @Max(9999)
    private int pin;

    private boolean addBeneficary;
}
