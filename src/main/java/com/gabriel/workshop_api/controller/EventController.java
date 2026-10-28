package com.gabriel.workshop_api.controller;

import com.gabriel.workshop_api.model.Event;
import com.gabriel.workshop_api.request.EventRequest;
import com.gabriel.workshop_api.response.EventResponse;
import com.gabriel.workshop_api.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @PostMapping
    public ResponseEntity<EventResponse> createEvent(@RequestBody @Valid EventRequest eventRequest) {
        Event event = eventRequest.toEntity();

        Event savedEvent = eventService.save(event);

        EventResponse eventResponse = EventResponse.fromEvent(savedEvent);

        return ResponseEntity.status(HttpStatus.CREATED).body(eventResponse);
    }

    @GetMapping
    public ResponseEntity<List<EventResponse>> getAllEvents() {

        List<EventResponse> listEvents = eventService.findAll().stream()
                .map(EventResponse::fromEvent)
                .toList();

        return ResponseEntity.ok(listEvents);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EventResponse> updateEvent(@PathVariable Long id, @RequestBody @Valid EventRequest eventRequest) {
        Event event = eventRequest.toEntity();

        Event updatedEvent = eventService.update(event);

        EventResponse eventResponse = EventResponse.fromEvent(updatedEvent);

        return ResponseEntity.ok(eventResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {

        eventService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<EventResponse>> getEventsByCategory(@PathVariable Long categoryId) {

        List<EventResponse> listEvents = eventService.findByCategoryId(categoryId).stream()
                .map(EventResponse::fromEvent)
                .toList();

        return ResponseEntity.ok(listEvents);
    }

    @GetMapping("/{id}/registrations/count")
    public ResponseEntity<Long> getRegistrationCount(@PathVariable Long id) {

        long count = eventService.getConfirmedRegistrationsCount(id);

        return ResponseEntity.ok(count);
    }
}
