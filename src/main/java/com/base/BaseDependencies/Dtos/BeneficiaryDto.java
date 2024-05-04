package com.base.BaseDependencies.Dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BeneficiaryDto {
    private int beneficiaryId;

    private long bankAccountNumber;

    private String bank;
}
