package com.gabriel.workshop_api.response;

import com.gabriel.workshop_api.emus.EventStatus;
import com.gabriel.workshop_api.model.Event;

import java.time.LocalDateTime;

public record EventResponse(
        Long id,
        String title,
        String description,
        LocalDateTime eventDate,
        LocalDateTime startTime,
        LocalDateTime endTime,
        Integer capacity,
        EventStatus status
) {
    public static EventResponse fromEvent(Event event) {

        return new EventResponse(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getEventDate(),
                event.getStartTime(),
                event.getEndTime(),
                event.getCapacity(),
                event.getStatus()

        );
    }
}
