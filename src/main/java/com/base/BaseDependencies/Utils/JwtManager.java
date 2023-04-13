package com.base.BaseDependencies.Utils;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.base.BaseDependencies.ExceptionHandler.SpecificExceptions.InvalidToken;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class JwtManager {
    private final Key tokenKey;
    @Autowired
    private Environment env;

    public JwtManager() {
        this.tokenKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

    public String createToken(Authentication authentication) {
        return Jwts.builder()
                .setSubject(authentication.getName())
                .setIssuer("BankerApi")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() * 1000 * 60 * 24))
                .signWith(tokenKey)
                .compact();
    }

    public String parseToken(String token) {

        String validToken = validateTokenFormat(token);

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(tokenKey)
                .build()
                .parseClaimsJws(validToken)
                .getBody();

        return claims.getSubject();

    }

    public Date getTokenExpiration(String token) {

        String validToken = validateTokenFormat(token);

        return Jwts.parserBuilder()
                .setSigningKey(tokenKey)
                .build()
                .parseClaimsJws(validToken)
                .getBody()
                .getExpiration();

    }

    public boolean isTokenExpired(String token) {
        return getTokenExpiration(token).before(new Date());
    }

    public String validateTokenFormat(String token) {
        if (StringUtils.hasText(token) && token.startsWith("bearer ")) {
            String bearerToken = token.substring(7, token.length());
            return bearerToken;
        }
        throw new InvalidToken(env.getProperty("INVALID.TOKEN.EXCEPTION.MESSAGE"));
    }
}
