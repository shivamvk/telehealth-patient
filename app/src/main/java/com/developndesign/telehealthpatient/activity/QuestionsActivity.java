package com.developndesign.telehealthpatient.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.developndesign.telehealthpatient.R;
import com.developndesign.telehealthpatient.adapter.QuestionAdapter;
import com.developndesign.telehealthpatient.model.BookAppointmentModelData;
import com.developndesign.telehealthpatient.model.QuestionModelBase;
import com.developndesign.telehealthpatient.model.QuestionModelData;
import com.developndesign.telehealthpatient.utils.LocalData;
import com.developndesign.telehealthpatient.utils.MongoDB;
import com.google.gson.Gson;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class QuestionsActivity extends AppCompatActivity {
    Activity activity;
    LocalData localData;
    private RecyclerView recyclerViewSymptoms;
    private Response response;
    private ArrayList<String> checkQuestions;
    String jsonData;
    public BookAppointmentModelData bookAppointmentModelData;
    private Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);
        activity = QuestionsActivity.this;
        localData = new LocalData(activity);
        recyclerViewSymptoms = findViewById(R.id.question_recyclerview);
        checkQuestions = new ArrayList<>();
        bookAppointmentModelData = getIntent().getParcelableExtra(LocalData.BOOK_APPOINTMENT);
        Toolbar toolbar = findViewById(R.id.toolbar);
        next = findViewById(R.id.next);
        next.setVisibility(View.GONE);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bookAppointmentModelData.getSymptoms().get(0).getQuestions().size() > 0)
                    startActivity(new Intent(QuestionsActivity.this, SelectDoctorActivity.class)
                    .putExtra(LocalData.BOOK_APPOINTMENT,bookAppointmentModelData));
                else
                    Toast.makeText(activity, "Please select at least one question", Toast.LENGTH_SHORT).show();
            }
        });
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_baseline_arrow_back_24));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String symptomId = getIntent().getStringExtra("id");
        new GetQuestion().execute(MongoDB.QUESTIONS_BYSYMPTOM_URL + symptomId);
    }

    @SuppressLint("StaticFieldLeak")
    class GetQuestion extends AsyncTask<String, Void, String> {

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
                    ArrayList<QuestionModelData> data = new Gson().fromJson(jsonData, QuestionModelBase.class).getData();
                    if (data != null && data.size() > 0) {
                        QuestionAdapter questionAdapter = new QuestionAdapter(activity, data, checkQuestions);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity);
                        recyclerViewSymptoms.setLayoutManager(linearLayoutManager);
                        recyclerViewSymptoms.setAdapter(questionAdapter);
                        questionAdapter.notifyDataSetChanged();
                        next.setVisibility(View.VISIBLE);
                    } else
                        next.setVisibility(View.GONE);

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