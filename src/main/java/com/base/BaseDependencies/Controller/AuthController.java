package com.base.BaseDependencies.Controller;

import java.time.Duration;
import java.util.Arrays;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
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

    private static final String ACCESS_TOKEN_COOKIE = "accessToken";

    private ClientService clientService;
    private Environment environment;

    @PostMapping("register")
    public ResponseEntity<String> registerClient(@Valid @RequestBody RegClientDto request) {
        String response = clientService.createClient(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("register-admin")
    public ResponseEntity<String> registerAdmin(@Valid @RequestBody RegClientDto request) {
        String response = clientService.createAdmin(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("login")
    public ResponseEntity<Map<String, String>> loginClient(@Valid @RequestBody LoginClientDto request,
            HttpServletResponse response) {
        Map<String, String> result = clientService.verifyClient(request);
        boolean secure = Arrays.asList(environment.getActiveProfiles()).contains("prod");
        ResponseCookie cookie = ResponseCookie.from(ACCESS_TOKEN_COOKIE, result.get(ACCESS_TOKEN_COOKIE))
                .httpOnly(true)
                .secure(secure)
                .sameSite("Strict")
                .path("/")
                .maxAge(Duration.ofHours(24))
                .build();
        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return new ResponseEntity<>(Map.of("role", result.get("role")), HttpStatus.OK);
    }

    @PostMapping("logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from(ACCESS_TOKEN_COOKIE, "")
                .httpOnly(true)
                .path("/")
                .maxAge(0)
                .build();
        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
