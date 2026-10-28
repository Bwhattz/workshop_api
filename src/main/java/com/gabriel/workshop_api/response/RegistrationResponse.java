package com.gabriel.workshop_api.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gabriel.workshop_api.emus.RegistrationStatus;
import com.gabriel.workshop_api.model.Event;
import com.gabriel.workshop_api.model.Registration;

import java.time.LocalDateTime;

public record RegistrationResponse(
        LocalDateTime registrationDate,
        RegistrationStatus status,
        Long eventId,
        Long userId
) {
        public static RegistrationResponse fromRegistration(Registration registration) {
                return new RegistrationResponse(
                        registration.getRegistrationDate(),
                        registration.getStatus(),
                        registration.getEvent() != null ? registration.getEvent().getId() : null,
                        registration.getUser() != null ? registration.getUser().getId() : null
                );
        }
}
