package com.gabriel.workshop_api.dto.response;

import com.gabriel.workshop_api.model.User;

import java.util.Collections;
import java.util.List;

public record UserResponse(
        String codeUser,
        String name,
        String email,
        List<String> roles
) {
    public static UserResponse fromUser(User user) {

        final List<String> roles = user.getRoles() != null ? user.getRoles().stream()
                .map(role -> role.getStatus().name()).toList()
                : Collections.emptyList();

        return new UserResponse(
                user.getCodeUser(),
                user.getName(),
                user.getEmail(),
                roles
        );
    }
}
