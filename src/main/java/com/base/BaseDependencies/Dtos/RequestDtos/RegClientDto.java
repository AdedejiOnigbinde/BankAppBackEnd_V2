package com.base.BaseDependencies.Dtos.RequestDtos;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegClientDto {

    private String firstName;

    private String lastName;

    private String userName;

    private String password;

    private String address;

    private int pinNumber;

    private int ssn;

}
