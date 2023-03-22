package com.base.BaseDependencies.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.base.BaseDependencies.Dtos.LoginClientDto;
import com.base.BaseDependencies.Dtos.RegClientDto;
import com.base.BaseDependencies.Service.ClientService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("auth/")
@AllArgsConstructor
public class AuthController {

    private ClientService clientService;

    @PostMapping("register")
    public ResponseEntity<?> registerClient(@RequestBody RegClientDto regClientDto) {

        boolean hasRegistered = clientService.createClient(regClientDto);
        return new ResponseEntity<>(hasRegistered, HttpStatus.CREATED);

    }

    @PostMapping("login")
    public ResponseEntity<?> loginClient(@RequestBody LoginClientDto loginClientDto) {
        String verifiedClient = clientService.verifyClient(loginClientDto);
        return new ResponseEntity<>(verifiedClient, HttpStatus.OK);

    }
}
