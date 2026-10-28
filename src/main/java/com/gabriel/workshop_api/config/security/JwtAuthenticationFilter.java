package com.gabriel.workshop_api.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        String auth = request.getHeader("Authorization");

        if(auth == null || auth.startsWith("Bearer ") || auth.length() <= 7) {
            chain.doFilter(request, response);
            return;
        }

        String token = auth.substring(7);

        if(SecurityContextHolder.getContext().getAuthentication() == null) {

            String email = jwtService.extractEmail(token);

            List<String> roles = jwtService.extractClaims(token, claims -> claims.get("roles", List.class));

            List<GrantedAuthority> autorities = (roles == null)
                    ? Collections.emptyList() :
                    roles.stream()
                            .<GrantedAuthority>map(SimpleGrantedAuthority::new)
                            .toList();

            UsernamePasswordAuthenticationToken tokenUser = new UsernamePasswordAuthenticationToken(email, null, autorities);

            tokenUser.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(tokenUser);
        }

        chain.doFilter(request, response);
    }
}
