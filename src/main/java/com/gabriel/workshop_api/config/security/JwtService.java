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
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtService {


    @Value("${api.security.token.secret:7f4c9b1a8e2d6c5b4a3f2e1d0c9b8a7f6e5d4c3b2a1f0e9d8c7b6a5f4e3d2c1b}")
    private String secretString;

    private SecretKey cryptoKey;

    private final static Long EXPIRATION_TIME = Duration.ofHours(12).toMillis();

    public JwtService(){}

    @PostConstruct
    public void init() {
        this.cryptoKey = Keys.hmacShaKeyFor(secretString.getBytes());
    }

    public String generateToken(User user) {

        List<String> roles = user.getRoles().stream()
                .map(RoleType::getAuthorize)
                .collect(Collectors.toList());

        Date now = new Date();

        return Jwts.builder()
                .subject(user.getEmail())
                .claim("roles", roles)
                .issuedAt(now)
                .expiration(new Date(now.getTime() + EXPIRATION_TIME))
                .signWith(cryptoKey)
                .compact();
    }

    public boolean isTokenValid(String token) {

        try {
            Date expiration = extractClaims(token, Claims::getExpiration);
            return !expiration.before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String extractEmail(String token) {
        return extractClaims(token, Claims::getSubject);
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
