package com.developndesign.telehealthpatient.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class BookAppointmentModelData implements Parcelable {
    private String doctor;
    private String language;
    private long callDuration;
    private String callType;
    private PatientModelData patient;
    private ArrayList<SymptomsModelData> symptoms;

    public BookAppointmentModelData() {

    }

    protected BookAppointmentModelData(Parcel in) {
        doctor = in.readString();
        language = in.readString();
        callDuration = in.readLong();
        callType = in.readString();
        patient = in.readParcelable(PatientModelData.class.getClassLoader());
        symptoms = in.createTypedArrayList(SymptomsModelData.CREATOR);
    }

    public static final Creator<BookAppointmentModelData> CREATOR = new Creator<BookAppointmentModelData>() {
        @Override
        public BookAppointmentModelData createFromParcel(Parcel in) {
            return new BookAppointmentModelData(in);
        }

        @Override
        public BookAppointmentModelData[] newArray(int size) {
            return new BookAppointmentModelData[size];
        }
    };

    public PatientModelData getPatient() {
        return patient;
    }

    public void setPatient(PatientModelData patient) {
        this.patient = patient;
    }

    public String getDoctor() {
        return doctor;
    }

    public void setDoctor(String doctor) {
        this.doctor = doctor;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

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

    public ArrayList<SymptomsModelData> getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(ArrayList<SymptomsModelData> symptoms) {
        this.symptoms = symptoms;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(doctor);
        dest.writeString(language);
        dest.writeLong(callDuration);
        dest.writeString(callType);
        dest.writeParcelable(patient, flags);
        dest.writeTypedList(symptoms);
    }
}
