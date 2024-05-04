package com.base.BaseDependencies.Dtos.RequestDtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PayBillRequestDto {
    private double amount;

    private String biller;

    private boolean saveBill;

    private String nickName;

    private String category;

    private int pin;

}
