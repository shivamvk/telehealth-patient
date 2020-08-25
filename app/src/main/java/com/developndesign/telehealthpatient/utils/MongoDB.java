package com.developndesign.telehealthpatient.utils;

public class MongoDB {
    public static final String WEB_URL = "http://13.126.155.120";
    public static final String AMAZON_BUCKET_URL = "https://mindful-telehealth.s3.ap-south-1.amazonaws.com/";
    public static final String REGISTER_PATIENT_URL = WEB_URL + "/api/auth/register/patient";
    public static final String LOGIN_PATIENT_URL = WEB_URL + "/api/auth/login";
    public static final String SYMPTOMS_PATIENT_URL = WEB_URL + "/api/symptom";
    public static final String LANGUAGE_PATIENT_URL = WEB_URL + "/api/language";
    public static final String PROFILE_PATIENT_URL = WEB_URL + "/api/auth/me";
    public static final String UPDATE_PROFILE_URL = WEB_URL + "/api/user/update/me";
    public static final String FAMILY_URL = WEB_URL + "/api/family";
    public static final String QUESTIONS_BYSYMPTOM_URL = WEB_URL + "/api/question/bysymptom/";
    public static final String GET_DOCTORS_URL = WEB_URL + "/api/user/doctor";
    public static final String FAVOURITE_DOC_URL=WEB_URL+"/api/favourite/";
    public static final String BOOK_APPOINTMENT_URL=WEB_URL+"/api/appointment";
    public static final String SEND_TOKEN_URL = WEB_URL + "/api/firebase/notification/add";
    public static final String SEND_NOTIFICATION_URL = WEB_URL + "/api/call/id/";
    public static final String TERMS_URL = WEB_URL + "/api/tac";
}
