package com.base.BaseDependencies.Dtos.RequestDtos;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class DepositRequestDto {
    private int depositRequestId;

    @NotBlank
    private String checkNumber;

    @NotBlank
    private String checkBank;

    @Positive
    private double checkAmount;

    private String description;

    private String status;

    private LocalDateTime requestDate;

    @PositiveOrZero
    private double splitCheckingAmount;

    @PositiveOrZero
    private double splitSavingsAmount;
}
