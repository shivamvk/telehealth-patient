package com.developndesign.telehealthpatient.model;

public class RegistrationModelBase {
    private int status;
    private RegistrationModelData data;
    private String message;
    private boolean errors;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public RegistrationModelData getData() {
        return data;
    }

    public void setData(RegistrationModelData data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isErrors() {
        return errors;
    }

    public void setErrors(boolean errors) {
        this.errors = errors;
    }
}
