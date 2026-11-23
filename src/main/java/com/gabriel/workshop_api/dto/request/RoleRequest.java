package com.gabriel.workshop_api.dto.request;

import com.gabriel.workshop_api.emus.StatusProfile;
import com.gabriel.workshop_api.model.Role;
import jakarta.validation.constraints.NotBlank;

public record RoleRequest(

        @NotBlank(message = "O tipo de perfil é obrigátorio")
        StatusProfile status
) {
    public Role toEntity() {

        return Role.builder()
                .status(this.status())
                .build();
    }
}
