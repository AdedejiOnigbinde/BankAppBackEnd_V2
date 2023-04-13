package com.base.BaseDependencies.Security;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.env.Environment;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.base.BaseDependencies.Models.Client;
import com.base.BaseDependencies.Models.Role;
import com.base.BaseDependencies.Repository.ClientRepo;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private ClientRepo clientRepo;
    private Environment env;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Client client = clientRepo.findByUserName(username).orElseThrow(()-> new UsernameNotFoundException(env.getProperty("CLIENT.NOT.FOUND_EXCEPTION.MESSAGE")));
        return new User(client.getUserName(), client.getPassword(), mapRolesToAuthorities(client.getRoles()));
    }

    private Collection<GrantedAuthority> mapRolesToAuthorities(List<Role> roles){
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getRoleName())).collect(Collectors.toList());
    }

}