package com.developndesign.telehealthpatient.model;

import java.util.ArrayList;

public class QuestionModelBase {
    private Integer status;
    private ArrayList<QuestionModelData> data;
    private String message;
    private boolean errors;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public ArrayList<QuestionModelData> getData() {

        return data;
    }

    public void setData(ArrayList<QuestionModelData> data) {
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
