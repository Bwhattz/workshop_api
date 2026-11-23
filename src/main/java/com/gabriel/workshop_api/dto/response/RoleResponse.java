package com.gabriel.workshop_api.dto.response;

import com.gabriel.workshop_api.model.Role;

public record RoleResponse(

        String status

) {
    public static RoleResponse fromRole(Role role) {

        return new RoleResponse(
                role.getStatus().name()
        );
    }
}
