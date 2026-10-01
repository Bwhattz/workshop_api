package com.gabriel.workshop_api.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("workshop.security.jwt.secret")
    private String secretString;

    private SecretKey cryptoKey;

    private final static Long EXPIRATION_TIME = 720000L;

    public JwtService(){}

    @PostConstruct
    public void init() {
        this.cryptoKey = Keys.hmacShaKeyFor(secretString.getBytes());
    }

    public String generateToken(UserDetails details) {

        List<String> roles = details.getAuthorities().stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .toList();

        return Jwts.builder()
                .subject(details.getUsername())
                .claim("roles", roles)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(cryptoKey)
                .compact();
    }

    public boolean isTokenValid(String token) {

        try {
            Date expiration = extractClaims(token, claims -> claims.getExpiration());
            return !expiration.before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
    
    public String extractEmail(String token) {
        return extractClaims(token, claims -> claims.getSubject());
    }

    private <T> T extractClaims(String token, Function<Claims, T> fuction) {
        final Claims claims = extractAllClaims(token);
        return fuction.apply(claims);
    }

    private Claims extractAllClaims(String token) {

        return Jwts.parser()
                .verifyWith(cryptoKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    @SuppressWarnings("unchecked")
    public List<String> extractRoles(String token) {
        final Claims claims = extractAllClaims(token);
        return claims.get("roles", List.class);
    }
}
