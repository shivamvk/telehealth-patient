package com.developndesign.telehealthpatient.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class QuestionAnswerModelData implements Parcelable {
    private String question;
    private ArrayList<String> answers;

    public QuestionAnswerModelData() {

    }

    protected QuestionAnswerModelData(Parcel in) {
        question = in.readString();
        answers = in.createStringArrayList();
    }

    public static final Creator<QuestionAnswerModelData> CREATOR = new Creator<QuestionAnswerModelData>() {
        @Override
        public QuestionAnswerModelData createFromParcel(Parcel in) {
            return new QuestionAnswerModelData(in);
        }

        @Override
        public QuestionAnswerModelData[] newArray(int size) {
            return new QuestionAnswerModelData[size];
        }
    };

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public ArrayList<String> getAnswers() {
        return answers;
    }

    public void setAnswers(ArrayList<String> answers) {
        this.answers = answers;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(question);
        dest.writeStringList(answers);
    }
}
