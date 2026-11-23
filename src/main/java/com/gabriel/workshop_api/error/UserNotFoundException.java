package com.gabriel.workshop_api.error;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message)  {
        super(message);
    }

}