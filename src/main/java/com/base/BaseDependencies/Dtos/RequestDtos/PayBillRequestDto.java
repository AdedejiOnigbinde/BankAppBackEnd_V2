package com.base.BaseDependencies.Dtos.RequestDtos;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PayBillRequestDto {
    @Positive
    private double amount;

    @NotBlank
    private String biller;

    private boolean saveBill;

    private String nickName;

    private String category;

    @Min(0)
    @Max(9999)
    private int pin;

}
