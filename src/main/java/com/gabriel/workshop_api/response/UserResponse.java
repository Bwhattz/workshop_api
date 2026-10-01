package com.gabriel.workshop_api.response;

import com.gabriel.workshop_api.model.Role;
import com.gabriel.workshop_api.model.User;

import java.util.List;

public record UserResponse(
        Long id,
        String idUser,
        String name,
        String email,
        List<Role> roles
) {
    public static UserResponse fromUser(User user) {

        return new UserResponse(
                user.getId(),
                user.getIdUser(),
                user.getName(),
                user.getEmail(),
                user.getRoles()
        );
    }
}
