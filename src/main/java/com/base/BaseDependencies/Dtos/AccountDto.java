package com.base.BaseDependencies.Dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
@Data
public class AccountDto {

    private long accountNumber;

    private double balance;

    private String accountType;
}
