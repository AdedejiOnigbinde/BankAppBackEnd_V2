package com.base.BaseDependencies.Controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.base.BaseDependencies.Dtos.LoginClientDto;
import com.base.BaseDependencies.Dtos.RegClientDto;
import com.base.BaseDependencies.Repository.ClientRepo;
import com.base.BaseDependencies.Repository.RoleRepo;
import com.base.BaseDependencies.Service.ClientService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("auth/")
@AllArgsConstructor
public class AuthController {

    private ClientService clientService;

    @PostMapping("register")
    public ResponseEntity<?> registerClient(@RequestBody RegClientDto regClientDto) {
        try {
            boolean hasRegistered = clientService.createClient(regClientDto);
            return new ResponseEntity<>(hasRegistered, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("login")
    public ResponseEntity<?> loginClient(@RequestBody LoginClientDto loginClientDto) {
        try {
            String verifiedClient = clientService.verifyClient(loginClientDto);
            return new ResponseEntity<>(verifiedClient, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
}
