package com.developndesign.telehealthpatient.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.developndesign.telehealthpatient.R;
import com.developndesign.telehealthpatient.model.RegistrationModelBase;
import com.developndesign.telehealthpatient.model.RegistrationModelData;
import com.developndesign.telehealthpatient.utils.LocalData;
import com.developndesign.telehealthpatient.utils.MongoDB;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {
    private EditText email, password;
    private String strEmail, strPassword;
    private ProgressDialog progressDialog;
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    private Activity activity;
    private LocalData localData;
    private String jsonData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button login = findViewById(R.id.login);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        activity = LoginActivity.this;
        progressDialog = new ProgressDialog(activity);
        localData = new LocalData(activity);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkValidity()) {
                    progressDialog.setMessage("Please wait...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    new LoginUser().execute(MongoDB.LOGIN_PATIENT_URL);
                }
            }
        });
    }

    public void finish(View view) {
        finish();
    }

    @SuppressLint("StaticFieldLeak")
    class LoginUser extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                OkHttpClient client = new OkHttpClient();
                MediaType mediaType = MediaType.parse("application/json;charset=utf-8");
                JSONObject params = new JSONObject();
                params.put("email", strEmail);
                params.put("password", strPassword);
                RequestBody body = RequestBody.create(mediaType, params.toString());
                Request request = new Request.Builder()
                        .url(strings[0])
                        .post(body)
                        .addHeader("Content-Type", "application/json;charset=utf-8")
                        .build();

                Response response = client.newCall(request).execute();
                jsonData = response.body().string();
            } catch (Exception e) {
                Toast.makeText(activity, "" + e, Toast.LENGTH_SHORT).show();
                progressDialog.cancel();
                e.printStackTrace();
            }
            return jsonData;
        }

        @Override
        protected void onPostExecute(String jsonData) {
            super.onPostExecute(jsonData);
            if (jsonData != null) {
                try {
                    progressDialog.cancel();
                    JSONObject jsonObject = new JSONObject(jsonData);
                    boolean errors = jsonObject.getBoolean("errors");
                    Toast.makeText(activity, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    if (!errors) {
                        RegistrationModelData data = new Gson().fromJson(jsonData, RegistrationModelBase.class).getData();
                        localData.setToken(data.getToken());
                        localData.setName(data.getUser().getFull_name());
                        localData.setEmail(data.getUser().getEmail());
                        localData.setVerified(data.getUser().isVerified());
                        localData.setProfilePicture(data.getUser().getProfile_picture());
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        finishAffinity();
                    }
                } catch (Exception e) {
                    progressDialog.cancel();
                    Log.e("TAG", "onPostExecute: " + e);
                    Toast.makeText(activity, "" + e, Toast.LENGTH_SHORT).show();
                }
            } else {
                progressDialog.cancel();
            }
        }

    }


    private boolean checkValidity() {
        strEmail = email.getText().toString();
        strPassword = password.getText().toString();
        if (!validate(strEmail)) {
            Toast.makeText(activity, "Invalid email address", Toast.LENGTH_SHORT).show();
            return false;
        } else if (strPassword.replaceAll(" ", "").isEmpty()) {
            Toast.makeText(activity, "Password is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

}