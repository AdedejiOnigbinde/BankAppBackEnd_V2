package com.base.BaseDependencies.Utils;

import java.security.Key;
import java.time.LocalDate;
import java.util.Date;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class JwtManager {
    private final Key tokenKey;

    public JwtManager() {
        this.tokenKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

    public String createToken(Authentication authentication) {
        return Jwts.builder()
                .setSubject(authentication.getName())
                .setIssuer("BankerApi")
                .setIssuedAt(new Date())
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusWeeks(7)))
                .signWith(tokenKey)
                .compact();
    }

    public String parseToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(tokenKey)
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();

    }

    public boolean validateToken(String token) throws Exception{
        try{
            Jwts.parser().setSigningKey(tokenKey).parseClaimsJws(token);
            return true;
        }catch(Exception exception){
            throw new Exception("JWT Incorrect Or Expired");
        }
       
    }
}
