package com.developndesign.telehealthpatient.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

public class RegistrationActivity extends AppCompatActivity {
    private EditText name, email, password;
    private String strName, strEmail, strPassword;
    private ProgressDialog progressDialog;
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    private Activity activity;
    private LocalData localData;
    private String jsonData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        final Button signup = findViewById(R.id.register);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        activity = RegistrationActivity.this;
        progressDialog = new ProgressDialog(activity);
        localData = new LocalData(activity);
        CheckBox checkBox = findViewById(R.id.checkbox);
        checkBox.setText(Html.fromHtml(getResources().getString(R.string.text_terms_condition)));
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    signup.setEnabled(true);
                else
                    signup.setEnabled(false);

            }
        });
        signup.setEnabled(true);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (signup.isEnabled())
                    if (checkValidity()) {
                        progressDialog.setMessage("Please wait...");
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.show();
                        new RegisterUser().execute(MongoDB.REGISTER_PATIENT_URL);
                    } else
                        Toast.makeText(activity, "Please check privacy policy", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    class RegisterUser extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                OkHttpClient client = new OkHttpClient();
                MediaType mediaType = MediaType.parse("application/json;charset=utf-8");
                JSONObject params = new JSONObject();
                params.put("full_name", strName);
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
                Log.e("TAG", "doInBackground: " + e);
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
                        progressDialog.cancel();
                        startActivity(new Intent(activity, MainActivity.class));
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
        strName = name.getText().toString();
        strEmail = email.getText().toString();
        strPassword = password.getText().toString();
        if (strName.replaceAll(" ", "").isEmpty()) {
            Toast.makeText(activity, "Name is required", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!validate(strEmail)) {
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

    public void openSignInActivity(View view) {
        startActivity(new Intent(activity, LoginActivity.class));
    }
}