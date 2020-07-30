package com.developndesign.telehealthpatient.model;

import android.os.Parcel;
import android.os.Parcelable;

public class FamilyMemberModelData implements Parcelable {
    private String _id;
    private String name;
    private Integer age;
    private String gender;
    private String relationWithAccountHolder;
    private String user;

    public FamilyMemberModelData(){

    }
    protected FamilyMemberModelData(Parcel in) {
        _id = in.readString();
        name = in.readString();
        if (in.readByte() == 0) {
            age = null;
        } else {
            age = in.readInt();
        }
        gender = in.readString();
        relationWithAccountHolder = in.readString();
        user = in.readString();
    }

    public static final Creator<FamilyMemberModelData> CREATOR = new Creator<FamilyMemberModelData>() {
        @Override
        public FamilyMemberModelData createFromParcel(Parcel in) {
            return new FamilyMemberModelData(in);
        }

        @Override
        public FamilyMemberModelData[] newArray(int size) {
            return new FamilyMemberModelData[size];
        }
    };

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

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

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_id);
        dest.writeString(name);
        if (age == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(age);
        }
        dest.writeString(gender);
        dest.writeString(relationWithAccountHolder);
        dest.writeString(user);
    }
}
