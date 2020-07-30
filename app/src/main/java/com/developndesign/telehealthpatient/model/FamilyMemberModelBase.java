package com.developndesign.telehealthpatient.model;

import java.util.ArrayList;

public class FamilyMemberModelBase {
    private Integer status;
    private Boolean errors;
    private ArrayList<FamilyMemberModelData> data;
    private String message;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Boolean getErrors() {
        return errors;
    }

    public void setErrors(Boolean errors) {
        this.errors = errors;
    }

    public ArrayList<FamilyMemberModelData> getData() {
        return data;
    }

    public void setData(ArrayList<FamilyMemberModelData> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
