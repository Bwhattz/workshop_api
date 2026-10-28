package com.gabriel.workshop_api.controller;

import com.gabriel.workshop_api.model.Certificate;
import com.gabriel.workshop_api.request.CertificateRequest;
import com.gabriel.workshop_api.response.CertificateResponse;
import com.gabriel.workshop_api.service.CertificateService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/certificates")
@RequiredArgsConstructor
public class CertificateController {

    private CertificateService certificateService;

    @PostMapping
    public ResponseEntity<CertificateResponse> createCertificate(@RequestBody @Valid CertificateRequest certificateRequest) {

        Certificate certificate = certificateRequest.toEntity();

        Certificate serviceCertificate = certificateService.save(certificate);

        CertificateResponse certificateResponse = CertificateResponse.fromCertificate(serviceCertificate);

        return ResponseEntity.status(HttpStatus.CREATED).body(certificateResponse);
    }

    @GetMapping
    public ResponseEntity<List<CertificateResponse>> getCertificate() {

        List<CertificateResponse> responseList = certificateService.findAll().stream()
                .map(CertificateResponse::fromCertificate)
                .toList();

        return ResponseEntity.ok().body(responseList);
    }

    @GetMapping(value = "/registration/{registrationId}")
    public ResponseEntity<CertificateResponse> getRegistrationIdEvent(@PathVariable Long registrationId) {

        Certificate certificate = certificateService.generateCertificate(registrationId);

        CertificateResponse certificateResponse = CertificateResponse.fromCertificate(certificate);

        return ResponseEntity.ok().body(certificateResponse);
    }

    @GetMapping(value = "validate/{code}")
    public ResponseEntity<CertificateResponse> validateCertificate(@PathVariable String code) {

        return certificateService.findByCode(code)
                .map(CertificateResponse::fromCertificate)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());

    }

}
