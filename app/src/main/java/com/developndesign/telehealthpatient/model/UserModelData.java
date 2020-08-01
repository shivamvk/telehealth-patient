package com.developndesign.telehealthpatient.model;

import java.util.ArrayList;

public class UserModelData {
    private CheckBoxModelData checkbox1;
    private CheckBoxModelData checkbox2;
    private CheckBoxModelData checkbox3;
    private CheckBoxModelData checkbox4;
    private CheckBoxModelData checkbox5;
    private ArrayList<String> location;
    private ArrayList<LanguageSymptomModelData> languages;
    private ArrayList<LanguageSymptomModelData> symptoms;
    private String _id;
    private boolean active;
    private String userType;
    private String full_name;
    private String profile_picture;
    private String email;
    private boolean verified;
    private String dob = "";
    private String education = "";
    private String college = "";
    private String gender = "";
    private String professionalStatement = "";
    private String stateLicense = "";
    private String licenseExpiration = "";
    private String boardExpiration = "";
    private String yearsOfExperience = "";
    private boolean malpracticeLawsuites;
    private String mobile_number;
    private boolean medicalBoardDeciplinaryAction;
    private boolean favourite;
    private AddressModelData address;

    public AddressModelData getAddress() {
        return address;
    }

    public void setAddress(AddressModelData address) {
        this.address = address;
    }

    public  boolean isFavourite(){
        return favourite;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(String profile_picture) {
        this.profile_picture = profile_picture;
    }

    public boolean isMalpracticeLawsuites() {
        return malpracticeLawsuites;
    }

    public void setMalpracticeLawsuites(boolean malpracticeLawsuites) {
        this.malpracticeLawsuites = malpracticeLawsuites;
    }

    public boolean isMedicalBoardDeciplinaryAction() {
        return medicalBoardDeciplinaryAction;
    }

    public void setMedicalBoardDeciplinaryAction(boolean medicalBoardDeciplinaryAction) {
        this.medicalBoardDeciplinaryAction = medicalBoardDeciplinaryAction;
    }

    public String getExperience() {
        return yearsOfExperience;
    }

    public void setExperience(String experience) {
        this.yearsOfExperience = experience;
    }

    public String getBoardExpiration() {
        return boardExpiration;
    }

    public void setBoardExpiration(String boardExpiration) {
        this.boardExpiration = boardExpiration;
    }

    public String getLicenseExpiration() {
        return licenseExpiration;
    }

    public void setLicenseExpiration(String licenseExpiration) {
        this.licenseExpiration = licenseExpiration;
    }

    public String getStateLicense() {
        return stateLicense;
    }

    public void setStateLicense(String stateLicense) {
        this.stateLicense = stateLicense;
    }

    public String getProfessionalStatement() {
        return professionalStatement;
    }

    public void setProfessionalStatement(String professionalStatement) {
        this.professionalStatement = professionalStatement;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCollege() {
        return college;
    }

    public void setCollege(String college) {
        this.college = college;
    }

    public String getDob() {
        return dob;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public CheckBoxModelData getCheckbox1() {
        return checkbox1;
    }

    public void setCheckbox1(CheckBoxModelData checkbox1) {
        this.checkbox1 = checkbox1;
    }

    public CheckBoxModelData getCheckbox2() {
        return checkbox2;
    }

    public void setCheckbox2(CheckBoxModelData checkbox2) {
        this.checkbox2 = checkbox2;
    }

    public CheckBoxModelData getCheckbox3() {
        return checkbox3;
    }

    public void setCheckbox3(CheckBoxModelData checkbox3) {
        this.checkbox3 = checkbox3;
    }

    public CheckBoxModelData getCheckbox4() {
        return checkbox4;
    }

    public void setCheckbox4(CheckBoxModelData checkbox4) {
        this.checkbox4 = checkbox4;
    }

    public CheckBoxModelData getCheckbox5() {
        return checkbox5;
    }

    public void setCheckbox5(CheckBoxModelData checkbox5) {
        this.checkbox5 = checkbox5;
    }

    public ArrayList<String> getLocation() {
        return location;
    }

    public void setLocation(ArrayList<String> location) {
        this.location = location;
    }

    public ArrayList<LanguageSymptomModelData> getLanguages() {
        return languages;
    }

    public void setLanguages(ArrayList<LanguageSymptomModelData> languages) {
        this.languages = languages;
    }

    public ArrayList<LanguageSymptomModelData> getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(ArrayList<LanguageSymptomModelData> symptoms) {
        this.symptoms = symptoms;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }
}
