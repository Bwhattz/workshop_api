package com.gabriel.workshop_api.response;

import com.gabriel.workshop_api.model.Certificate;

public record CertificateResponse(

        Long id,
        String codeCertificate,
        Long registrationId,
        String studentName,
        String eventTitle
) {
    public static CertificateResponse fromCertificate(Certificate certificate) {
        return new CertificateResponse(
                certificate.getId(),
                certificate.getCodeCertificate(),
                certificate.getRegistration() != null ? certificate.getRegistration().getId() : null,
                certificate.getRegistration() != null && certificate.getRegistration().getUser() != null ? certificate.getRegistration().getUser().getName() : null,
                certificate.getRegistration() != null && certificate.getRegistration().getEvent() != null ? certificate.getRegistration().getEvent().getTitle() : null
        );
    }
}
