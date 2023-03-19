package com.base.BaseDependencies.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.base.BaseDependencies.Dtos.LoginClientDto;
import com.base.BaseDependencies.Dtos.RegClientDto;
import com.base.BaseDependencies.Models.Client;
import com.base.BaseDependencies.Models.Role;
import com.base.BaseDependencies.Repository.ClientRepo;
import com.base.BaseDependencies.Repository.RoleRepo;
import com.base.BaseDependencies.Utils.JwtManager;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ClientService {

    private ClientRepo clientRepo;
    private JwtManager tokenManager;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private RoleRepo roleRepo;

    public boolean createClient(RegClientDto regClient) throws Exception {
        Optional<Client> existingClient = clientRepo.findBySsn(regClient.getSsn());
        if (existingClient.isEmpty()) {
            Client client = new Client();
            client.setUserName(regClient.getUserName());
            client.setFirstName(regClient.getFirstName());
            client.setLastName(regClient.getLastName());
            client.setAddress(regClient.getAddress());
            client.setSsn(regClient.getSsn());
            client.setPassword(passwordEncoder.encode(regClient.getPassword()));
            client.setPinNumber(regClient.getPinNumber());
            Role roles = roleRepo.findByRoleName("USER").get();
            client.setRoles(Collections.singletonList(roles));
            clientRepo.save(client);
            return true;
        }
        throw new Exception("Client Already Exists");
    }

    public String verifyClient(LoginClientDto logClient) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(logClient.getUserName(), logClient.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = tokenManager.createToken(authentication);
        return token;

    }

    public List<Client> getAllClients() {
        return clientRepo.findAll();
    }

    public String deleteClient(String token) {
        // Integer getClientId = tokenManager.parseToken(token);
        // clientRepo.deleteById(getClientId);
        return "Completed";
    }
}
