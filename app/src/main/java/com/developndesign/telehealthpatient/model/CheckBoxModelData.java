package com.developndesign.telehealthpatient.model;

public class CheckBoxModelData {
    private String title;
    private boolean accepted;

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
