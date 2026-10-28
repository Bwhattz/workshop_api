package com.gabriel.workshop_api.response;

import com.gabriel.workshop_api.model.Role;

import java.util.List;

public record RoleResponse(

        String roles

) {
    public static RoleResponse fromRole(Role role) {

        return new RoleResponse(
                role.getName()
        );
    }
}
