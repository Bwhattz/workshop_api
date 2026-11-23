package com.gabriel.workshop_api.dto.response;

import com.gabriel.workshop_api.emus.RegistrationStatus;
import com.gabriel.workshop_api.model.Registration;

import java.time.LocalDateTime;

public record RegistrationResponse(
        LocalDateTime registrationDate,
        RegistrationStatus status,
        Long eventId,
        Long userId
) {
        public static RegistrationResponse fromRegistration(Registration registration) {

                final Long eventId = registration.getId() != null ? registration.getEvent().getId() : null;
                final Long userId = registration.getUser() != null ? registration.getUser().getId() : null;

                return new RegistrationResponse(
                        registration.getRegistrationDate(),
                        registration.getStatus(),
                        eventId,
                        userId
                );
        }
}
