package com.gabriel.workshop_api.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "certificates")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Certificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "code_certificate", nullable = false, unique = true, length = 10)
    private String codeCertificate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "registration_id", nullable = false, unique = true)
    private Registration registration;

    @PrePersist
    public void prePersist() {

        if(this.codeCertificate == null && this.codeCertificate.isBlank()) {
            this.codeCertificate = "CERT" + UUID.randomUUID().toString()
                    .replace("-", "")
                    .substring(0,6)
                    .toUpperCase();
        }
    }
}
