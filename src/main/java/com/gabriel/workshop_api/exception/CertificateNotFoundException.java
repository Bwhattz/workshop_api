package com.gabriel.workshop_api.exception;

public class CertificateNotFoundException extends RuntimeException{
    public CertificateNotFoundException(String message) {
        super(message);
    }
}
