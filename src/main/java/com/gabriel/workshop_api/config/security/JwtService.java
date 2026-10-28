package com.gabriel.workshop_api.config.security;

import com.gabriel.workshop_api.emus.RoleType;
import com.gabriel.workshop_api.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtService {

    @Value("${api.security.token.secret}")
    private String scriptSecret;

    private SecretKey cryptoKey;

    private static final Long EXPIRATION_TIME = Duration.ofHours(12).toMillis();

    public JwtService(){}

    @PostConstruct
    protected void init() {
        this.cryptoKey = Keys.hmacShaKeyFor(scriptSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(User userToken) {

        List<String> roles = userToken.getRoles().stream()
                .map(role -> role.getName())
                .toList();


        return Jwts.builder()
                .subject(userToken.getEmail())
                .claim("userId", userToken.getId())
                .claim("userName", userToken.getName())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(cryptoKey)
                .compact();
    }

    public boolean isTokenValid(String token) {

        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public Claims parseClaims(String tokenEmail) {

        return Jwts.parser()
                .verifyWith(cryptoKey)
                .build()
                .parseSignedClaims(tokenEmail)
                .getPayload();
    }

    public String extractEmail(String tokenEmail) {
        return extractClaims(tokenEmail, Claims::getSubject);
    }

    private <T> T extractClaims(String tokenEmail, Function<Claims, T> claimsFunction) {
        final Claims claim = parseClaims(tokenEmail);
        return claimsFunction.apply(claim);
    }
}