package com.gabriel.workshop_api.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gabriel.workshop_api.model.Role;

import java.util.List;

public record RolesResponse(

        String roles,

        @JsonIgnore
        List<UserResponse> userResponse

) {
    public static RolesResponse fromRoles(Role role) {

        return new RoleResponse(
                role.getName(),
                role.getUsers() ?
        )
    }
}
