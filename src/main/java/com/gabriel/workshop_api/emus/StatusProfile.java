package com.gabriel.workshop_api.emus;

public enum RoleType {

    ADMIN("ROLE_ADMIN"),
    ORGANIZE("ROLE_ORGANIZE"),
    PARTICIPANT("ROLE_PARTCIPANT");

    private String authorize;

    RoleType(String authorize) {
        this.authorize = authorize;
    }

    public String getAuthorize() {
        return this.authorize;
    }
}
