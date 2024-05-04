package com.base.BaseDependencies.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.base.BaseDependencies.Constants.ErrorMessageConstants;
import com.base.BaseDependencies.Constants.GeneralMessageConstants;
import com.base.BaseDependencies.Dtos.RequestDtos.LoginClientDto;
import com.base.BaseDependencies.Dtos.RequestDtos.RegClientDto;
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
    private JwtManager tokenManager;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private RoleRepo roleRepo;

    public String createClient(RegClientDto regClient) {
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
            return GeneralMessageConstants.SUCCESSFUL_REGISTRATION_MESSAGE;
        }
        throw new ClientAlreadyExists(ErrorMessageConstants.CLIENT_EXIST_EXCEPTION_MESSAGE);
    }

    public String createAdmin(RegClientDto regClient) {
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
            Role roles = roleRepo.findByRoleName("ADMIN").get();
            client.setRoles(Collections.singletonList(roles));
            clientRepo.save(client);
            return GeneralMessageConstants.SUCCESSFUL_REGISTRATION_MESSAGE;
        }
        throw new ClientAlreadyExists(ErrorMessageConstants.CLIENT_EXIST_EXCEPTION_MESSAGE);
    }

    public Map<String,String> verifyClient(LoginClientDto logClient) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(logClient.getUserName(), logClient.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Optional<Client> clientInfo = clientRepo.findByUserName(authentication.getName());
        String userRole = clientInfo.get().getRoles().get(0).getRoleName();
        String token = tokenManager.createToken(authentication);
        Map<String,String> response = new HashMap();
        response.put("accessToken", token);
        response.put("role", userRole);
        return response;
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
        throw new ClientNotFound(ErrorMessageConstants.CLIENT_NOT_FOUND_EXCEPTION_MESSAGE);

    }
}
