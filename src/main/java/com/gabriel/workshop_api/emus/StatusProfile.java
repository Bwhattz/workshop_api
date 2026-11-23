package com.gabriel.workshop_api.emus;

public enum StatusProfile {

    ADMIN("ROLE_ADMIN"),
    ORGANIZE("ROLE_ORGANIZE"),
    PARTICIPANT("ROLE_PARTCIPANT");

    private String authorize;

    StatusProfile(String authorize) {
        this.authorize = authorize;
    }

    public String getAuthorize() {
        return this.authorize;
    }
}
