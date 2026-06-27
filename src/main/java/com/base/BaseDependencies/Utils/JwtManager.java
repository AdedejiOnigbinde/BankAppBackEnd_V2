package com.base.BaseDependencies.Utils;

import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.base.BaseDependencies.Constants.ErrorMessageConstants;
import com.base.BaseDependencies.ExceptionHandler.SpecificExceptions.InvalidToken;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtManager {

    private static final long TOKEN_VALIDITY_MILLISECONDS = 1000L * 60 * 60 * 24;

    private final Key tokenKey;

    public JwtManager(@Value("${jwt.secret}") String secret) {
        this.tokenKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    public String createToken(Authentication authentication) {
        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        return Jwts.builder()
                .setSubject(authentication.getName())
                .setIssuer("BankerApi")
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY_MILLISECONDS))
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

    @SuppressWarnings("unchecked")
    public Collection<GrantedAuthority> getAuthorities(String token) {

        String validToken = validateTokenFormat(token);

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(tokenKey)
                .build()
                .parseClaimsJws(validToken)
                .getBody();

        List<String> roles = claims.get("roles", List.class);
        return roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
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
        if (!StringUtils.hasText(token)) {
            throw new InvalidToken(ErrorMessageConstants.INVALID_TOKEN_EXCEPTION_MESSAGE);
        }
        if (token.startsWith("bearer ")) {
            return token.substring(7);
        }
        return token;
    }
}
