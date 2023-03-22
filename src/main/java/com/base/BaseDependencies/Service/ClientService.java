package com.base.BaseDependencies.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.base.BaseDependencies.Dtos.LoginClientDto;
import com.base.BaseDependencies.Dtos.RegClientDto;
import com.base.BaseDependencies.ExceptionHandler.SpecificExceptions.ClientAlreadyExists;
import com.base.BaseDependencies.ExceptionHandler.SpecificExceptions.ClientNotFound;
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
    private Environment env;
    private JwtManager tokenManager;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private RoleRepo roleRepo;

    public boolean createClient(RegClientDto regClient) {
        Optional<Client> existingSsn = clientRepo.findBySsn(regClient.getSsn());
        Optional<Client> existingUserName = clientRepo.findByUserName(regClient.getUserName());
        if (existingSsn.isEmpty() && existingUserName.isEmpty()) {
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
        throw new ClientAlreadyExists(env.getProperty("CLIENT_EXISTS_EXCEPTION_MESSAGE"));
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

    public boolean deleteClient(String token) {
        String getClientUserName = tokenManager.parseToken(token);
        Optional<Client> existingClient = clientRepo.findByUserName(getClientUserName);
        if (existingClient.isPresent()) {
            clientRepo.deleteByUserName(getClientUserName);
            return true;
        }
        throw new ClientNotFound(env.getProperty("CLIENT_NOT_FOUND_EXCEPTION_MESSAGE"));

    }
}
