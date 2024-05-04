package com.base.BaseDependencies.Dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
@Data
public class AccountDto {

    private long accountNumber;

    private int ownerId;

    private double balance;

    private String accountType;

    private double calcLimit;

    private double dailyTransferLimit;

    private String accountStatus;
}
