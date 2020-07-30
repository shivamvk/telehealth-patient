package com.developndesign.telehealthpatient.model;

import android.os.Parcel;
import android.os.Parcelable;

public class PatientModelData implements Parcelable {
    private String name;
    private Integer age;
    private String gender;
    private String relationWithAccountHolder;

    public PatientModelData(){

    }
    protected PatientModelData(Parcel in) {
        name = in.readString();
        if (in.readByte() == 0) {
            age = null;
        } else {
            age = in.readInt();
        }
        gender = in.readString();
        relationWithAccountHolder = in.readString();
    }

    public static final Creator<PatientModelData> CREATOR = new Creator<PatientModelData>() {
        @Override
        public PatientModelData createFromParcel(Parcel in) {
            return new PatientModelData(in);
        }

        @Override
        public PatientModelData[] newArray(int size) {
            return new PatientModelData[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRelationWithAccountHolder() {
        return relationWithAccountHolder;
    }

    public void setRelationWithAccountHolder(String relationWithAccountHolder) {
        this.relationWithAccountHolder = relationWithAccountHolder;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        if (age == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(age);
        }
        dest.writeString(gender);
        dest.writeString(relationWithAccountHolder);
    }
}
