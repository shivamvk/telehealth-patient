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
import com.developndesign.telehealthpatient.adapter.GetAppointmentAdapter;
import com.developndesign.telehealthpatient.model.BookAppointmentModelData;
import com.developndesign.telehealthpatient.model.GetBookAppointmentModelBase;
import com.developndesign.telehealthpatient.model.GetBookAppointmentModelData;
import com.developndesign.telehealthpatient.utils.LocalData;
import com.developndesign.telehealthpatient.utils.MongoDB;
import com.google.gson.Gson;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ShowHistoryActivity extends AppCompatActivity {
    Activity activity;
    LocalData localData;
    private RecyclerView recyclerViewCallHistory;
    String jsonData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_history);
        activity = ShowHistoryActivity.this;
        localData = new LocalData(activity);
        recyclerViewCallHistory = findViewById(R.id.call_history_recyclerview);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_baseline_arrow_back_24));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        new GetCallHistory().execute(MongoDB.BOOK_APPOINTMENT_URL);
    }

    @SuppressLint("StaticFieldLeak")
    class GetCallHistory extends AsyncTask<String, Void, String> {

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
                Response response = client.newCall(request).execute();
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
                    ArrayList<GetBookAppointmentModelData> data = new Gson().fromJson(jsonData, GetBookAppointmentModelBase.class).getData();
                    if (data != null && data.size() > 0) {
                        GetAppointmentAdapter appointmentAdapter = new GetAppointmentAdapter(activity, data);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
                        recyclerViewCallHistory.setLayoutManager(linearLayoutManager);
                        recyclerViewCallHistory.setAdapter(appointmentAdapter);
                        appointmentAdapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    Log.e("TAG", "onPostExecute: " + e);
                    Toast.makeText(activity, "" + e, Toast.LENGTH_SHORT).show();
                }
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