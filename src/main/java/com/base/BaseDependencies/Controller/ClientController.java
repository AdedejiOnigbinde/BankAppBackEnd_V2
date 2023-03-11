package com.base.BaseDependencies.Controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.base.BaseDependencies.Dtos.LoginClientDto;
import com.base.BaseDependencies.Dtos.RegClientDto;
import com.base.BaseDependencies.Models.Client;
import com.base.BaseDependencies.Service.ClientService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/client")
public class ClientController {

    private final ClientService clientService;

    @PostMapping("/register")
    public ResponseEntity<?> registerClient(@RequestBody RegClientDto regClientDto) {
        try {
            String hasRegistered = clientService.createClient(regClientDto);
            return new ResponseEntity<>(hasRegistered, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }

    }

    @PostMapping("/login")
    public ResponseEntity<?> loginClient(@RequestBody LoginClientDto loginClientDto){
        try {
            String verifiedClient = clientService.verifyClient(loginClientDto);
            return new ResponseEntity<>(verifiedClient, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/allclients")
    public ResponseEntity<?> getAllClient(){
        try {
            List<Client> clientList = clientService.getAllClients();
            return new ResponseEntity<>(clientList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/removeclient")
    public ResponseEntity<?> deleteClient(@RequestHeader("userToken") String userToken){
        try {
            clientService.deleteClient(userToken);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }
}
