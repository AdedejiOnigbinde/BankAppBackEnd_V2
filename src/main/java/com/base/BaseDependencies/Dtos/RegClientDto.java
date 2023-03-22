package com.base.BaseDependencies.Dtos;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
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
