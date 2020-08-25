package com.developndesign.telehealthpatient.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import com.developndesign.telehealthpatient.R;
import com.developndesign.telehealthpatient.utils.MongoDB;
import com.google.gson.JsonObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TermsConditionActivity extends AppCompatActivity {

    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_condition);
        tv = findViewById(R.id.text);
        new GetTermsAndConditions().execute(MongoDB.TERMS_URL);
    }

    class GetTermsAndConditions extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... strings) {
            String jsonObject = "";
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(strings[0])
                    .get()
                    .build();
            try {
                Response response = client.newCall(request).execute();
                jsonObject = response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return jsonObject;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            tv.setText(s);
        }
    }
}