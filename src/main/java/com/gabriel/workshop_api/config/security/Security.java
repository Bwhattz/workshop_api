package com.gabriel.workshop_api.config.security;

import com.gabriel.workshop_api.config.security.JwtAuthenticationFilter;
import com.gabriel.workshop_api.config.security.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.Customizer;
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
public class Security {

    private final JwtService jwtService;

    public Security(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth.
                        requestMatchers("/auth/**", "/v3/api-docs", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        .requestMatchers(HttpMethod.POST, "/users/login").hasAnyRole("USER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/users/**").hasAnyRole("ADMIN", "ORGANIZE")
                        .requestMatchers(HttpMethod.PUT, "/users/**").hasAnyRole("ADMIN" ,"PARTICIPANT")
                        .requestMatchers(HttpMethod.DELETE, "/users/delete/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "users/find_user/{name}/{email}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "users/role/{rolesName}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/roles/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/roles/role/{roleName}").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/roles/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/roles/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/registrations/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/registrations/**").hasAnyRole("ADMIN", "ORGANIZE")


                        .anyRequest().authenticated()
                ).addFilterBefore(new JwtAuthenticationFilter(jwtService), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exception -> exception.accessDeniedHandler((request, response, accessDeniedException) -> {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN, "Acesso negado");
                }))
                .exceptionHandling(exception -> exception.authenticationEntryPoint((request, response, authException) -> {
                    if(authException == null) {
                        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    } else {
                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                    }
                }));

        return httpSecurity.build();
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        return RoleHierarchyImpl.fromHierarchy("ROLE_ADMIN > ROLE_ORGANIZE > ROLE_PARTICIPANT");
    }

    @Bean
    public CorsConfigurationSource corsConfiguration() {

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept"));

        UrlBasedCorsConfigurationSource based = new UrlBasedCorsConfigurationSource();

        based.registerCorsConfiguration("/*", configuration);

        return based;
    }
}
