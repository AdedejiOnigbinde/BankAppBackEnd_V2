package com.base.BaseDependencies.Controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.base.BaseDependencies.Dtos.RequestDtos.LoginClientDto;
import com.base.BaseDependencies.Dtos.RequestDtos.RegClientDto;
import com.base.BaseDependencies.Service.ClientService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("auth/")
@AllArgsConstructor
public class AuthController {

    private ClientService clientService;

    @PostMapping("register")
    public ResponseEntity<String> registerClient(@RequestBody RegClientDto request) {
        String response = clientService.createClient(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }

    @PostMapping("register-admin")
    public ResponseEntity<String> registerAdmin(@RequestBody RegClientDto request) {
        String response = clientService.createAdmin(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }

    @PostMapping("login")
    public ResponseEntity<Map<String, String>> loginClient(@RequestBody LoginClientDto request) {
        Map<String, String> response = clientService.verifyClient(request);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }
}
