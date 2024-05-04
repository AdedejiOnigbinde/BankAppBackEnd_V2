package com.base.BaseDependencies.Dtos;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PaidBillsDto {
    private LocalDateTime paymentDate;

    private String status;

    private double amount;

    private String biller;

}
