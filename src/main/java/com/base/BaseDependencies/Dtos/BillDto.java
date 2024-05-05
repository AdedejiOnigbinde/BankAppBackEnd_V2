package com.base.BaseDependencies.Dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
@Data
public class BillDto {
    private int billId;
    
    private String category;

    private String biller;

    private String nickname;
}
