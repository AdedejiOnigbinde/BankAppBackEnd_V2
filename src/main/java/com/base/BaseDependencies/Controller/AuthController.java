package com.base.BaseDependencies.Controller;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
@CrossOrigin("http://localhost:4200")
public class AuthController {

    private ClientService clientService;
    

    @PostMapping("register")
    public ResponseEntity<ArrayList<String>> registerClient(@RequestBody RegClientDto regClientDto) {
        ArrayList<String> successMessage = new ArrayList<String>();
        String hasRegistered = clientService.createClient(regClientDto);
        successMessage.add(hasRegistered);
        return new ResponseEntity<>(successMessage, HttpStatus.CREATED);

    }

    @PostMapping("registerAdmin")
    public ResponseEntity<ArrayList<String>> registerAdmin(@RequestBody RegClientDto regClientDto) {
        ArrayList<String> successMessage = new ArrayList<String>();
        String hasRegistered = clientService.createAdmin(regClientDto);
        successMessage.add(hasRegistered);
        return new ResponseEntity<>(successMessage, HttpStatus.CREATED);

    }

    @PostMapping("login")
    public ResponseEntity<Map<String,String>> loginClient(@RequestBody LoginClientDto loginClientDto) {
        Map<String,String> verifiedClient = clientService.verifyClient(loginClientDto);
        return new ResponseEntity<>(verifiedClient, HttpStatus.OK);

    }
}
