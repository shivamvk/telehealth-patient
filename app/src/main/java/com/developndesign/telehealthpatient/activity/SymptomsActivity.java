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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.developndesign.telehealthpatient.R;
import com.developndesign.telehealthpatient.adapter.SymptomsAdapter;
import com.developndesign.telehealthpatient.model.BookAppointmentModelData;
import com.developndesign.telehealthpatient.model.LanguageSymptomModelBase;
import com.developndesign.telehealthpatient.model.LanguageSymptomModelData;
import com.developndesign.telehealthpatient.utils.LocalData;
import com.developndesign.telehealthpatient.utils.MongoDB;
import com.google.gson.Gson;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SymptomsActivity extends AppCompatActivity {
    Activity activity;
    LocalData localData;
    private RecyclerView recyclerViewSymptoms;
    private Response response;
    String jsonData;
    public BookAppointmentModelData bookAppointmentModelData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_symptoms);
        activity = SymptomsActivity.this;
        localData = new LocalData(activity);
        recyclerViewSymptoms = findViewById(R.id.symptom_recyclerview);
        bookAppointmentModelData=getIntent().getParcelableExtra(LocalData.BOOK_APPOINTMENT);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_baseline_arrow_back_24));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        new GetSymptoms().execute(MongoDB.SYMPTOMS_PATIENT_URL);
    }

    @SuppressLint("StaticFieldLeak")
    class GetSymptoms extends AsyncTask<String, Void, String> {

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
                    ArrayList<LanguageSymptomModelData> data = new Gson().fromJson(jsonData, LanguageSymptomModelBase.class).getData();
                    if (data != null) {
                        SymptomsAdapter symptomsAdapter = new SymptomsAdapter(activity, data);
                        GridLayoutManager gridLayoutManager=new GridLayoutManager(activity,2);
                        recyclerViewSymptoms.setLayoutManager(gridLayoutManager);
                        recyclerViewSymptoms.setAdapter(symptomsAdapter);
                        symptomsAdapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    Log.e("TAG", "onPostExecute: " + e);
                    Toast.makeText(activity, "" + e, Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.e("TAG", "onPostExecute: "+response.message());
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