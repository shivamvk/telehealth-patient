package com.developndesign.telehealthpatient.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class GetBookAppointmentModelData  {

    private long callDuration;
    private String callType;
    private PatientModelData patient;
    private String _id;
    private String createdAt;
    private DoctorModelData doctor;
    private ArrayList<SymptomsModelData> symptoms;

    public long getCallDuration() {
        return callDuration;
    }

    public void setCallDuration(long callDuration) {
        this.callDuration = callDuration;
    }

    public String getCallType() {
        return callType;
    }

    public void setCallType(String callType) {
        this.callType = callType;
    }

    public PatientModelData getPatient() {
        return patient;
    }

    public void setPatient(PatientModelData patient) {
        this.patient = patient;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public DoctorModelData getDoctor() {
        return doctor;
    }

    public void setDoctor(DoctorModelData doctor) {
        this.doctor = doctor;
    }

    public ArrayList<SymptomsModelData> getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(ArrayList<SymptomsModelData> symptoms) {
        this.symptoms = symptoms;
    }

    public GetBookAppointmentModelData() {

    }


}
