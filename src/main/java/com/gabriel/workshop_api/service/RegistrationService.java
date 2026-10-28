package com.gabriel.workshop_api.service;

import com.gabriel.workshop_api.exception.RegistrationNotFoundException;
import com.gabriel.workshop_api.model.Event;
import com.gabriel.workshop_api.model.Registration;
import com.gabriel.workshop_api.repository.RegistrationRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RegistrationService {

    private final RegistrationRepository registrationRepository;

    public RegistrationService(RegistrationRepository registrationRepository) {
        this.registrationRepository = registrationRepository;
    }

    @Transactional
    public Registration save(Registration registration) {

        registrationRepository.findByUserAndEvent(registration.getUser().getId(), registration.getEvent().getId())
                .ifPresent(existsRegis -> {
                    throw new IllegalArgumentException("Este participante já possui uma inscrição ativa para este evento.");
                });


        conflictRegistration(registration);

        return registrationRepository.save(registration);
    }

    @Transactional(readOnly = true)
    public List<Registration> findAll() {
        return registrationRepository.findAll();
    }

    public void conflictRegistration(Registration registration) {
        if(registrationRepository != null && registrationRepository.existsById(registration.getId())) {
            throw new IllegalArgumentException("O registro escolhido colide com o registro existente");
        }
    }

    @Transactional(readOnly = true)
    public List<Registration> findByFutureRegistration() {
        return registrationRepository.findByFutureRegistration();
    }

    @Transactional(readOnly = true)
    public Optional<Registration> findByEvent(Long eventId) {
        return registrationRepository.findByEvent(eventId);
    }

    @Transactional
    public Registration update(Registration registration) {

        Registration foundRegistration = registrationRepository.findById(registration.getId())
                .orElseThrow(() -> new RegistrationNotFoundException("Este registro não existe."));

        registrationRepository.findByUserAndEvent(registration.getUser().getId(), registration.getEvent().getId()).ifPresent(existsRegis -> {
            if(!existsRegis.getId().equals(foundRegistration.getId())) {
                throw new IllegalArgumentException("Este registro já existe pois colide com outro registro.");
            }
        });

        return registrationRepository.save(foundRegistration);
    }

    @Transactional
    public void deleteId(Long id) {

        if(!registrationRepository.existsById(id)) {
            throw new IllegalArgumentException("Este registro não pôde ser encontrado para exclusão.");
        }

        registrationRepository.deleteById(id);
    }
}
