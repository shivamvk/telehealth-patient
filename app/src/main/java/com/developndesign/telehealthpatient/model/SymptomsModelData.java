package com.developndesign.telehealthpatient.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class SymptomsModelData implements Parcelable {
    private String symptom;
    private ArrayList<QuestionAnswerModelData> questions;
    public SymptomsModelData(){

    }

    protected SymptomsModelData(Parcel in) {
        symptom = in.readString();
        questions = in.createTypedArrayList(QuestionAnswerModelData.CREATOR);
    }

    public static final Creator<SymptomsModelData> CREATOR = new Creator<SymptomsModelData>() {
        @Override
        public SymptomsModelData createFromParcel(Parcel in) {
            return new SymptomsModelData(in);
        }

        @Override
        public SymptomsModelData[] newArray(int size) {
            return new SymptomsModelData[size];
        }
    };

    public String getSymptom() {
        return symptom;
    }

    public void setSymptom(String symptom) {
        this.symptom = symptom;
    }

    public ArrayList<QuestionAnswerModelData> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<QuestionAnswerModelData> questions) {
        this.questions = questions;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(symptom);
        dest.writeTypedList(questions);
    }
}
