package com.gabriel.workshop_api.request;

import com.gabriel.workshop_api.emus.RegistrationStatus;
import com.gabriel.workshop_api.model.Event;
import com.gabriel.workshop_api.model.Registration;
import com.gabriel.workshop_api.model.User;
import jakarta.validation.constraints.NotNull;

public record RegistrationRequest(


        @NotNull(message = "O ID do usuário participante é obrigatório.")
        Long userId,

        @NotNull(message = "O ID do evento acadêmico é obrigatório.")
        Long eventId,

        RegistrationStatus status
) {

    public Registration toEntity() {

        return Registration.builder()
                .user(User.builder().id(this.userId).build())
                .event(Event.builder().id(this.eventId).build())
                .status(this.status != null ? this.status : RegistrationStatus.CONFIRMED)
                .build();
    }
}
