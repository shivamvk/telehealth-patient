package com.developndesign.telehealthpatient.model;

import java.util.ArrayList;

public class LanguageSymptomModelBase {
    private int status;
    private String message;
    private boolean errors;
    private ArrayList<LanguageSymptomModelData> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public ArrayList<LanguageSymptomModelData> getData() {
        return data;
    }

    public void setData(ArrayList<LanguageSymptomModelData> data) {
        this.data = data;
    }
}
