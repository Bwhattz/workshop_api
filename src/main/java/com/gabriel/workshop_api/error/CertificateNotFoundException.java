package com.gabriel.workshop_api.error;

public class CertificateNotFoundException extends RuntimeException{
    public CertificateNotFoundException(String message) {
        super(message);
    }
}
