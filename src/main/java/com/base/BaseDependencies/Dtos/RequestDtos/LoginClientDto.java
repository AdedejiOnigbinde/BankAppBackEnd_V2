package com.base.BaseDependencies.Dtos.RequestDtos;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
@Data
public class LoginClientDto {
    @NotBlank
    private String userName;

    @NotBlank
    private String password;
}
