package com.developndesign.telehealthpatient.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.developndesign.telehealthpatient.R;
import com.developndesign.telehealthpatient.adapter.DoctorsAdapter;
import com.developndesign.telehealthpatient.model.BookAppointmentModelData;
import com.developndesign.telehealthpatient.model.DoctorModelBase;
import com.developndesign.telehealthpatient.model.UserModelData;
import com.developndesign.telehealthpatient.utils.LocalData;
import com.developndesign.telehealthpatient.utils.MongoDB;
import com.google.gson.Gson;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SelectDoctorActivity extends AppCompatActivity {
    Activity activity;
    LocalData localData;
    private RecyclerView recyclerViewDoctor;
    private Response response;
    private String jsonData;
    public BookAppointmentModelData bookAppointmentModelData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_doctor);
        activity = SelectDoctorActivity.this;
        localData = new LocalData(activity);
        recyclerViewDoctor = findViewById(R.id.doctor_recyclerview);
        bookAppointmentModelData=getIntent().getParcelableExtra(LocalData.BOOK_APPOINTMENT);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_baseline_arrow_back_24));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        new GetDoctors().execute(MongoDB.GET_DOCTORS_URL);

    }

    @SuppressLint("StaticFieldLeak")
    class GetDoctors extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(strings[0])
                        .get()
                        .addHeader("Content-Type", "application/json;charset=utf-8")
                        .addHeader("token", localData.getToken())
                        .build();
                response = client.newCall(request).execute();
                jsonData = response.body().string();
            } catch (Exception e) {
                Log.e("TAG", "doInBackground: " + e);
                e.printStackTrace();
            }
            return jsonData;
        }

        @Override
        protected void onPostExecute(String jsonData) {
            super.onPostExecute(jsonData);
            if (jsonData != null) {
                try {
                    ArrayList<UserModelData> data = new Gson().fromJson(jsonData, DoctorModelBase.class).getData();
                    if (data != null) {
                        DoctorsAdapter questionAdapter = new DoctorsAdapter(activity, data);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
                        recyclerViewDoctor.setLayoutManager(linearLayoutManager);
                        recyclerViewDoctor.setAdapter(questionAdapter);
                        questionAdapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    Log.e("TAG", "onPostExecute: " + e);
                    Toast.makeText(activity, "" + e, Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.e("TAG", "onPostExecute: " + response.message());
                Toast.makeText(activity, "" + response.message(), Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}