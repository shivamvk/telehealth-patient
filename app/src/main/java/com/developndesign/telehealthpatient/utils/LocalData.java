package com.developndesign.telehealthpatient.utils;

import android.app.Activity;
import android.content.Context;

public class LocalData {
    private Context context;
    private String baseSharedPrefernceKey = "user";
    public final static String BOOK_APPOINTMENT="book_appointment_data";

    public LocalData(Context context) {
        this.context = context;
    }

    public String getToken() {
        return context.getSharedPreferences(baseSharedPrefernceKey, Context.MODE_PRIVATE).getString("token", "");
    }

    public void setToken(String token) {
        context.getSharedPreferences(baseSharedPrefernceKey, Context.MODE_PRIVATE).edit().putString("token", token).apply();
    }

    public String getName() {
        return context.getSharedPreferences(baseSharedPrefernceKey, Context.MODE_PRIVATE).getString("name", "");
    }

    public void setName(String name) {
        context.getSharedPreferences(baseSharedPrefernceKey, Context.MODE_PRIVATE).edit().putString("name", name).apply();
    }

    public String getEmail() {
        return context.getSharedPreferences(baseSharedPrefernceKey, Context.MODE_PRIVATE).getString("email", "");
    }

    public void setEmail(String email) {
        context.getSharedPreferences(baseSharedPrefernceKey, Context.MODE_PRIVATE).edit().putString("email", email).apply();
    }

    public boolean isVerified() {
        return context.getSharedPreferences(baseSharedPrefernceKey, Context.MODE_PRIVATE).getBoolean("verified", false);
    }

    public void setVerified(boolean verified) {
        context.getSharedPreferences(baseSharedPrefernceKey, Context.MODE_PRIVATE).edit().putBoolean("verified", verified).apply();
    }

    public void setProfilePicture(String profilePicture) {
        context.getSharedPreferences(baseSharedPrefernceKey, Context.MODE_PRIVATE).edit().putString("profile_picture", profilePicture).apply();
    }

    public String getProfilePicture() {
        return context.getSharedPreferences(baseSharedPrefernceKey, Context.MODE_PRIVATE).getString("profile_picture","");
    }

}
