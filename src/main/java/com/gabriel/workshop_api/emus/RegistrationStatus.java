package com.gabriel.workshop_api.emus;

public enum RegistrationStatus {

    CONFIRMED("CONFIRM"),
    PENDING("PENDING"),
    CANCELLED("CANCELLED");

    private String status;

    RegistrationStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }

}
