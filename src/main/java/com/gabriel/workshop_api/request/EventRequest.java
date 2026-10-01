package com.gabriel.workshop_api.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gabriel.workshop_api.model.Event;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record EventRequest(

        @NotBlank(message = "O título do evento é obrigátorio")
        String title,

        @NotBlank(message = "A descrição do evento é obrigátorio")
        String description,

        @NotNull(message = "A data do evento é obrigátorio")
        @JsonFormat(pattern = "dd-mm-YYYY")
        LocalDateTime eventDate,

        @NotNull(message = "A capacidade de evento é obrigátorio")
        @Positive(message = "A capacidade do evento precisa ser maior que zero")
        Integer capacity,

        @NotNull(message = "O id da categoria é obrigátorio")



) {

    public Event toEntity() {

        return Event.builder()
                .title(this.title())
                .description(this.description())
                .eventDate(this.eventDate())
                .capacity(this.capacity())
                .
    }
}
