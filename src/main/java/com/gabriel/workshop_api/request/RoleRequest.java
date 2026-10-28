package com.gabriel.workshop_api.request;

import com.gabriel.workshop_api.model.Role;
import jakarta.validation.constraints.NotBlank;

public record RoleRequest(

        @NotBlank(message = "O tipo de perfil é obrigátorio")
        String name
) {
    public Role toEntity() {

        return Role.builder()
                .name(this.name())
                .build();
    }
}
