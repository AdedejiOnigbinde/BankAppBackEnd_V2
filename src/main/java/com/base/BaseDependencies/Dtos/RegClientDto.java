package com.base.BaseDependencies.Dtos;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
@Data
public class RegClientDto {

    private String firstName;

    private String lastName;

    private String password;

    private String address;

    private int pinNumber;

    private int ssn;

}
