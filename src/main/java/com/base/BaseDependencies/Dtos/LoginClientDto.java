package com.base.BaseDependencies.Dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
@Data
public class LoginClientDto {
    private String userName;
    private String password;
}
