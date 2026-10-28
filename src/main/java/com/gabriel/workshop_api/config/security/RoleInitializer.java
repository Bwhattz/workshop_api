package com.gabriel.workshop_api.config.security;

import com.gabriel.workshop_api.model.Role;
import com.gabriel.workshop_api.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RoleInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;


    @Override
    public void run(String... args) {
        createRole("ADMIN");
        createRole("ORGANIZE");
        createRole("PARTICIPANT");
    }

    public void createRole(String roleName) {

        if(roleRepository.findByRole(roleName).isEmpty()) {
            roleRepository.save(Role.builder()
                    .name(roleName)
                    .build());
        }
    }
}