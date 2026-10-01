package com.gabriel.workshop_api.request;

import com.gabriel.workshop_api.model.Role;
import com.gabriel.workshop_api.model.User;
import jakarta.validation.constraints.*;

import java.util.List;

public record UserRequest(

        @NotBlank(message = "O nome precisa ser obrigátorio")
        @Size(max = 150, message = "O limite é apenas 150 caracteres")
        String name,

        @NotBlank(message = "Email precisa ser obrigátorio")
        @Email(message = "Email inválido")
        String email,

        @NotBlank(message = "A senha precisa ser obrigátoria")
        @Size(min = 6, message = "É necessário pelo menos 6 caracteres")
        @Pattern(regexp =
                "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,}$")
        String password,

        @NotNull(message = "O cargo precisa ser obrigátorio")
        Role role
) {
    public User toEntityUser() {

        return User.builder()
                .name(this.name())
                .email(this.email())
                .password(this.password())
                .roles(List.of(role))
                .build();
    }
}
