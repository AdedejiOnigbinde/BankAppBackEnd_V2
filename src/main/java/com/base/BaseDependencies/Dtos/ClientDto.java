package com.base.BaseDependencies.Dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@AllArgsConstructor
@Data
public class ClientDto {

    private String photoUrl;
    
    private String firstName;

    private String userName;

    private String lastName;

    private String address;
}
