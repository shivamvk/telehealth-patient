package com.developndesign.telehealthpatient.model;

import java.util.ArrayList;

public class DoctorModelBase {
    private Integer status;
    private ArrayList<UserModelData> data;
    private String message;
    private boolean errors;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public ArrayList<UserModelData> getData() {
        return data;
    }

    public void setData(ArrayList<UserModelData> data) {
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
