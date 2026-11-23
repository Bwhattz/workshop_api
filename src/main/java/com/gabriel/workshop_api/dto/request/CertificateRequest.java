package com.gabriel.workshop_api.request;

import com.gabriel.workshop_api.model.Certificate;
import com.gabriel.workshop_api.model.Registration;
import jakarta.validation.constraints.NotNull;

public record CertificateRequest(

        @NotNull(message = "O registro do certificado precisa ser obrigátorio")
        Long idRegistrationCertificate
) {
    public Certificate toEntity() {

        return Certificate.builder()
                .registration(Registration.builder()
                        .id(this.idRegistrationCertificate())
                        .build())
                .build();
    }
}
