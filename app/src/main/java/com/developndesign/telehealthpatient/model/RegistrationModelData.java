package com.developndesign.telehealthpatient.model;

public class RegistrationModelData {
    private String token;
    private UserModelData user;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserModelData getUser() {
        return user;
    }

    public void setUser(UserModelData user) {
        this.user = user;
    }
}