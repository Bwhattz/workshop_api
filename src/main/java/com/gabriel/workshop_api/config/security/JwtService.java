package com.gabriel.workshop_api.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("workshop.security.jwt.secret")
    private String secretKeyString;

    private SecretKey cryptoKey;

    private static final Long EXPIRATION_TIME = 7200000L;

    @PostConstruct
    protected void initCrypto() {
        this.cryptoKey = Keys.hmacShaKeyFor(secretKeyString.getBytes());
    }

    public String generatedToken(UserDetails details) {

        List<String> roles = details.getAuthorities().stream()
                .map(auth -> auth.getAuthority())
                .toList();

        return Jwts.builder()
                .subject(details.getUsername())
                .claim("role", roles)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + (EXPIRATION_TIME + 320000)))
                .signWith(cryptoKey)
                .compact();
    }
    public boolean isTokenValid(String token) {

        try {
            Date expiration = extractClaims(token, claims -> claims.getExpiration());
            return !expiration.before(new Date());
        } catch(JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String extractEmail(String token) {

        return extractClaims(token, claims -> claims.getSubject());
    }

    @SuppressWarnings("unchecked")
    public List<String> extractRoles(String tokens) {

        Claims claims = extractAllClaims(tokens);
        return claims.get("roles", List.class);
    }

    private <T> T extractClaims(String token, Function<Claims, T> clamsFuction) {
        final Claims claims = extractAllClaims(token);
        return clamsFuction.apply(claims);
    }

    private Claims extractAllClaims(String token) {

        return Jwts.parser()
                .verifyWith(cryptoKey)
                .build()
                .parseSignedClaims(secretKeyString)
                .getPayload();
    }
}
