package com.gabriel.workshop_api.service;

import com.gabriel.workshop_api.model.Event;
import com.gabriel.workshop_api.repository.EventRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Transactional
    public Event save(Event event) {


        return eventRepository.save(event);
    }

    public List<Event> findAll() {
        return eventRepository.findAll();
    }

    @Transactional
    public Event update(Event event) {

        Event updateEvent = eventRepository.findById(event.getId())
                .orElseThrow(() -> new EntityNotFoundException("Este evento não existe"));

        eventRepository.findByConflictEvent(event.getOrganize().getId(), event.getStartTime(), event.getEndTime()).ifPresent(conflictEvent -> {
            if(!updateEvent.getOrganize().getId().equals(event.getOrganize().getId())) {

            }
        })
    }
}
