package com.base.BaseDependencies.Dtos.RequestDtos;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountStatementRequestDto {
    private LocalDate startDate;

    private LocalDate endDate;
}
