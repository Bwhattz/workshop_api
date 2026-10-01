package com.gabriel.workshop_api.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class Security {

    private final JwtService jwtService;

    @Bean
    public SecurityFilterChain security(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfiguration()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth.
                        requestMatchers("/auth/**").permitAll()
                        .requestMatchers("/v3/api-docs", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        .requestMatchers("/users/**").hasRole("ADMIN")
                        .requestMatchers("/events/**").hasAnyRole("ADMIN", "ORGANIZE")
                        .requestMatchers("/registration/**").hasRole("PARTICIPANTE")
                        .anyRequest().authenticated()).addFilterBefore(new JwtAuthenticationFilter(jwtService), UsernamePasswordAuthenticationFilter.class
                );

        return httpSecurity.build();
    }

    @Bean
    public CorsConfigurationSource corsConfiguration() {

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedMethods(Arrays.asList("Authorization", "Content-Type", "Accept"));

        UrlBasedCorsConfigurationSource based = new UrlBasedCorsConfigurationSource();

        based.registerCorsConfiguration("/*", configuration);

        return based;
    }
}
