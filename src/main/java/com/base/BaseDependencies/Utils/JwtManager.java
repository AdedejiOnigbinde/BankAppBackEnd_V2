package com.base.BaseDependencies.Utils;

import java.security.Key;
import java.time.LocalDate;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.base.BaseDependencies.Models.Client;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtManager {
    private final Key tokenKey;

    public JwtManager() {
        this.tokenKey =  Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

    public String createToken(Client client){
        return Jwts.builder()
                .setId(String.valueOf(client.getClientId()))
                // .setSubject(String.valueOf(client.getSsn()))
                .setIssuer("BankerApi")
                .setIssuedAt(new Date())
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusWeeks(7)))
                .signWith(tokenKey)
                .compact();
    }

    public Integer parseToken(String token){
        try{
            return Integer.parseInt(Jwts.parserBuilder()
            .setSigningKey(tokenKey)
            .build()
            .parseClaimsJws(token)
            .getBody()
            .getId());
        }catch(Exception e){
            return null;
        }
    }
}
