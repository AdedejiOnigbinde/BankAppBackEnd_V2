package com.base.BaseDependencies.Controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.base.BaseDependencies.Models.Client;
import com.base.BaseDependencies.Service.ClientService;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/client")
@CrossOrigin("http://localhost:4200")
public class ClientController {

    private final ClientService clientService;

    @GetMapping("/allclients")
    public ResponseEntity<List<Client>> getAllClient() {
        List<Client> clientList = clientService.getAllClients();
        return new ResponseEntity<>(clientList, HttpStatus.OK);
    }

    @DeleteMapping("/removeclient")
    public ResponseEntity<HttpStatus> deleteClient(@RequestHeader("Authorization") String userToken) {
        clientService.deleteClient(userToken);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
