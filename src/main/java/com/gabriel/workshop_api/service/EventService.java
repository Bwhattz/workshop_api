package com.gabriel.workshop_api.service;

import com.gabriel.workshop_api.exception.EventNotFoundException;
import com.gabriel.workshop_api.model.Event;
import com.gabriel.workshop_api.repository.EventRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Transactional
    public Event save(Event event) {

        eventRepository.findByConflictEvent(event.getOrganize().getId(), event.getStartTime(), event.getEndTime())
                .ifPresent(conflictEvent -> {
                    throw new IllegalArgumentException("Conflito de agenda: Este organizador já possui um evento marcado neste mesmo horário.");
                });

        return eventRepository.save(event);
    }

    @Transactional
    public List<Event> findAll() {
        return eventRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Event> findByCategoryId(Long categoryId) {
        return eventRepository.findByCategoryId(categoryId);
    }

    @Transactional(readOnly = true)
    public long getConfirmedRegistrationsCount(Long eventId) {
        if (!eventRepository.existsById(eventId)) {
            throw new EventNotFoundException("Evento não encontrado para contagem de inscrições.");
        }
        return eventRepository.countConfirmedRegistration(eventId);
    }

    @Transactional
    public Event update(Event event) {

        Event updateEvent = eventRepository.findById(event.getId())
                .orElseThrow(() -> new EventNotFoundException("Este evento não encontrado no sistema."));

        eventRepository.findByConflictEvent(event.getOrganize().getId(), event.getStartTime(), event.getEndTime()).ifPresent(conflictEvent -> {
            if(!conflictEvent.getId().equals(updateEvent.getId())) {
                throw new IllegalArgumentException("O horário escolhido colide com outro evento existente.");
            }
        });

        return eventRepository.save(updateEvent);
    }

    @Transactional
    public void delete(Long id) {

        if(!eventRepository.existsById(id)) {
            throw new EventNotFoundException("Este evento já foi apagado.");
        }

        eventRepository.deleteById(id);
    }
}
