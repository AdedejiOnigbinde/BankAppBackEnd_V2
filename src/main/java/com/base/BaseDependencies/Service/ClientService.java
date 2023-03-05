package com.base.BaseDependencies.Service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.base.BaseDependencies.Dtos.LoginClientDto;
import com.base.BaseDependencies.Dtos.RegClientDto;
import com.base.BaseDependencies.Models.Client;
import com.base.BaseDependencies.Repository.ClientRepo;
import com.base.BaseDependencies.Utils.JwtManager;

import lombok.AllArgsConstructor;


@AllArgsConstructor
@Service
public class ClientService {

    private  ClientRepo clientRepo;
    private  JwtManager tokenManager;
    private ModelMapper modelMapper;

    public String createClient(RegClientDto regClient) {
        Client client = modelMapper.map(regClient, Client.class);
        Client existingClient = clientRepo.findBySsn(client.getSsn());
        if (existingClient != null) {
            return null;
        } else {
            clientRepo.save(client);
            return tokenManager.createToken(client);
        }
    }

    public String verifyClient(LoginClientDto logClient) {
        Client client = modelMapper.map(logClient, Client.class);
        Client getClientByName = clientRepo.findByFirstNameAndLastName(client.getFirstName(), client.getLastName());
        Client getClientByPassword = clientRepo.findByPassword(client.getPassword());

        if (getClientByName != null && getClientByPassword != null) {
            return null;
        } else {
            return tokenManager.createToken(client);
        }
    }

    public List<Client> getAllClients(){
        return clientRepo.findAll();
    }

    public String deleteClient(String token){
        Integer getClientId = tokenManager.parseToken(token);
        clientRepo.deleteById(getClientId);
        return "Completed";
    }
}
