package com.base.BaseDependencies.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.base.BaseDependencies.Constants.ErrorMessageConstants;
import com.base.BaseDependencies.Constants.GeneralMessageConstants;
import com.base.BaseDependencies.Dtos.ClientDto;
import com.base.BaseDependencies.Dtos.RequestDtos.ChangePasswordRequestDto;
import com.base.BaseDependencies.Dtos.RequestDtos.LoginClientDto;
import com.base.BaseDependencies.Dtos.RequestDtos.RegClientDto;
import com.base.BaseDependencies.ExceptionHandler.SpecificExceptions.ClientAlreadyExists;
import com.base.BaseDependencies.ExceptionHandler.SpecificExceptions.ClientNotFound;
import com.base.BaseDependencies.ExceptionHandler.SpecificExceptions.InvalidPassword;
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
    private ModelMapper modelMapper;

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
            Role roles = roleRepo.findByRoleName("ADMIN").get();
            Client client = Client.builder()
                    .userName(regClient.getUserName().toLowerCase())
                    .firstName(regClient.getFirstName().toLowerCase())
                    .lastName(regClient.getLastName().toLowerCase())
                    .address(regClient.getAddress().toLowerCase())
                    .ssn(regClient.getSsn())
                    .password(passwordEncoder.encode(regClient.getPassword()))
                    .pinNumber(regClient.getPinNumber())
                    .roles(Collections.singletonList(roles))
                    .build();
            clientRepo.save(client);
            return GeneralMessageConstants.SUCCESSFUL_REGISTRATION_MESSAGE;
        }
        throw new ClientAlreadyExists(ErrorMessageConstants.CLIENT_EXIST_EXCEPTION_MESSAGE);
    }

    public Map<String, String> verifyClient(LoginClientDto logClient) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(logClient.getUserName().toLowerCase(),
                        logClient.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Client clientInfo = clientRepo.findByUserName(authentication.getName())
                .orElseThrow(() -> new ClientNotFound(ErrorMessageConstants.LOGIN_EXCEPTION_MESSAGE));
        String userRole = clientInfo.getRoles().get(0).getRoleName();
        String token = tokenManager.createToken(authentication);
        Map<String, String> response = new HashMap<>();
        response.put("accessToken", token);
        response.put("role", userRole);
        return response;
    }

    public List<ClientDto> getAllClients() {
        List<Client> clients = clientRepo.findAll();
        return clients.stream()
                .map(client -> modelMapper.map(client, ClientDto.class))
                .collect(Collectors.toList());
    }

    public boolean deleteClient(String token) {
        String getClientUserName = tokenManager.parseToken(token);
        Client existingClient = clientRepo.findByUserName(getClientUserName)
                .orElseThrow(() -> new ClientNotFound(ErrorMessageConstants.CLIENT_NOT_FOUND_EXCEPTION_MESSAGE));

        clientRepo.deleteById(existingClient.getClientId());
        return true;

    }

    public ClientDto updateProfile(ClientDto profile, String token) {
        String getClientUserName = tokenManager.parseToken(token);
        Client existingClient = clientRepo.findByUserName(getClientUserName)
                .orElseThrow(() -> new ClientNotFound(ErrorMessageConstants.CLIENT_NOT_FOUND_EXCEPTION_MESSAGE));
        existingClient.setPhotoUrl(profile.getPhotoUrl());
        existingClient.setFirstName(profile.getFirstName().toLowerCase());
        existingClient.setLastName(profile.getLastName().toLowerCase());
        existingClient.setAddress(profile.getAddress().toLowerCase());
        Client updatedClient = clientRepo.save(existingClient);
        ClientDto mappedClient = modelMapper.map(updatedClient, ClientDto.class);
        return mappedClient;

    }

    public String changePassword(ChangePasswordRequestDto passwordRequest, String token) {
        String getClientUserName = tokenManager.parseToken(token);
        Client existingClient = clientRepo.findByUserName(getClientUserName)
                .orElseThrow(() -> new ClientNotFound(ErrorMessageConstants.CLIENT_NOT_FOUND_EXCEPTION_MESSAGE));
        if (!passwordEncoder.matches(passwordRequest.getOldPassword(), existingClient.getPassword())) {
            throw new InvalidPassword(ErrorMessageConstants.INVALID_PASSWORD_CHANGE_EXCEPTION_MESSAGE);
        }
        existingClient.setPassword(passwordEncoder.encode(passwordRequest.getNewPassword()));
        clientRepo.save(existingClient);
        return GeneralMessageConstants.SUCCESSFUL_PASSWORD_RESET_MESSAGE;
    }

    public ClientDto getProfile(String token) {
        String getClientUserName = tokenManager.parseToken(token);
        Client existingClient = clientRepo.findByUserName(getClientUserName)
                .orElseThrow(() -> new ClientNotFound(ErrorMessageConstants.CLIENT_NOT_FOUND_EXCEPTION_MESSAGE));
        ClientDto mappedClient = modelMapper.map(existingClient, ClientDto.class);

        return mappedClient;
    }
}
