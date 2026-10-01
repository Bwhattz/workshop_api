package com.gabriel.workshop_api.exception;

public class UserNotFound extends RuntimeException {

    public UserNotFound(String message) {
        super(message);
    }
}
