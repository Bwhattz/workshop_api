package com.gabriel.workshop_api.error;

public class RegistrationNotFoundException extends RuntimeException{

    public RegistrationNotFoundException(String message) {
        super(message);
    }
}
