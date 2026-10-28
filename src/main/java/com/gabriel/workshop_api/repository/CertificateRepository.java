package com.gabriel.workshop_api.repository;

import com.gabriel.workshop_api.model.Certificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CertificateRepository extends JpaRepository<Certificate, Long> {


    @Query(
            value = "SELECT c.* FROM certificates AS c " +
                    "WHERE c.code_certificate = :codeCertificate",
            nativeQuery = true
    )
    Optional<Certificate> findByCodeCertificate(@Param("codeCertificate") String codeCertificate);

    @Query(
            value = "SELECT c.* FROM certificates AS c " +
                    "WHERE c.registration_id = :registrationId",
            nativeQuery = true
    )
    Optional<Certificate> findByRegistrationId(@Param("registrationId") Long registrationId);
}
