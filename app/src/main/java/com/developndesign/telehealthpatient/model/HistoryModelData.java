package com.developndesign.telehealthpatient.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class HistoryModelData implements Parcelable {
    private ArrayList<String> medicalProblems;
    private ArrayList<String> surgeries;
    private ArrayList<String> currentMedication;
    private boolean smoking;

    protected HistoryModelData(Parcel in) {
        medicalProblems = in.createStringArrayList();
        surgeries = in.createStringArrayList();
        currentMedication = in.createStringArrayList();
        smoking = in.readByte() != 0;
    }

    public static final Creator<HistoryModelData> CREATOR = new Creator<HistoryModelData>() {
        @Override
        public HistoryModelData createFromParcel(Parcel in) {
            return new HistoryModelData(in);
        }

        @Override
        public HistoryModelData[] newArray(int size) {
            return new HistoryModelData[size];
        }
    };

    public ArrayList<String> getMedicalProblems() {
        return medicalProblems;
    }

    public void setMedicalProblems(ArrayList<String> medicalProblems) {
        this.medicalProblems = medicalProblems;
    }

    public ArrayList<String> getSurgeries() {
        return surgeries;
    }

    public void setSurgeries(ArrayList<String> surgeries) {
        this.surgeries = surgeries;
    }

    public ArrayList<String> getCurrentMedication() {
        return currentMedication;
    }

    public void setCurrentMedication(ArrayList<String> currentMedication) {
        this.currentMedication = currentMedication;
    }

    public boolean isSmoking() {
        return smoking;
    }

    public void setSmoking(boolean smoking) {
        this.smoking = smoking;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(medicalProblems);
        dest.writeStringList(surgeries);
        dest.writeStringList(currentMedication);
        dest.writeByte((byte) (smoking ? 1 : 0));
    }
}
