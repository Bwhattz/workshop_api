package com.gabriel.workshop_api.emus;

public enum EventStatus {

    ACTIVE("ACTIVE"),
    CONFIRMED("CONFIRM"),
    CANCELLED("CANCELLED");

    private String status;

    EventStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }
}
