package com.gabriel.workshop_api.service;

import com.gabriel.workshop_api.exception.CertificateNotFoundException;
import com.gabriel.workshop_api.exception.RegistrationNotFoundException;
import com.gabriel.workshop_api.model.Certificate;
import com.gabriel.workshop_api.model.Registration;
import com.gabriel.workshop_api.repository.CertificateRepository;
import com.gabriel.workshop_api.repository.RegistrationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CertificateService {

    private RegistrationRepository registrationRepository;
    private CertificateRepository certificateRepository;

    @Transactional
    public Certificate save(Certificate certificate) {
        return certificateRepository.save(certificate);
    }

    @Transactional(readOnly = true)
    public List<Certificate> findAll() {
        return certificateRepository.findAll();
    }

    @Transactional
    public void delete(Long id) {

        if(!certificateRepository.existsById(id)) {
            throw new IllegalArgumentException("Este certificado não pôde ser encontrado para exclusão.");
        }

        certificateRepository.deleteById(id);
    }

    @Transactional
    public Certificate generateCertificate(Long registrationId) {

        Registration registration = registrationRepository.findById(registrationId)
                .orElseThrow(() -> new RegistrationNotFoundException("Inscrição não encontrada."));

        if (!"CONFIRMED".equals(registration.getStatus().name())) {
            throw new IllegalArgumentException("Inscrição precisa estar confirmada.");
        }

        certificateRepository.findByRegistrationId(registrationId).ifPresent(exists -> {
            throw new IllegalArgumentException("Certificado já emitido.");
        });

        Certificate certificate = Certificate.builder()
                .registration(registration)
                .build();

        return certificateRepository.save(certificate);
    }

    @Transactional(readOnly = true)
    public Optional<Certificate> findByCode(String code) {
        return certificateRepository.findByCodeCertificate(code);
    }
}
