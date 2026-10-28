package com.gabriel.workshop_api.emus;

public enum UserStatus {

    ACTIVED("ACTIVE"),
    DESACTIVED("DESACTIVE");

    private String status;

    UserStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return this.status;
    }
}
