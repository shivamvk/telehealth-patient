package com.developndesign.telehealthpatient.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.developndesign.telehealthpatient.R;
import com.developndesign.telehealthpatient.adapter.MedicalProblemAdapter;
import com.developndesign.telehealthpatient.adapter.PillAdapter;
import com.developndesign.telehealthpatient.utils.LocalData;
import com.developndesign.telehealthpatient.utils.MongoDB;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AddPatientActivity extends AppCompatActivity {
    private LocalData localData;
    private ProgressDialog progressDialog;
    private Activity activity;
    private EditText name, age, relation;
    private String strname, strage, strrelation;
    private String strgender;
    private boolean boolSmooking;
    private Spinner gender, smoking;
    private AlertDialog alertDialog1;
    private ArrayList<String> arrayListMed, arrayListCheckMed, arrayListSur, arrayListCM;
    private boolean boolSwitchSug;
    private boolean boolSwitchCM;
    private String jsonData;
    private Button addSurgery, addMedication, addAllergy;
    private RecyclerView surgeries, medications, allergies;
    private ArrayList<String> surgeriesList, medicationsList, allergiesList;
    private PillAdapter surgeriesAdapter, medicationAdapter, allergiesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_patient);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_baseline_arrow_back_24));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        activity = AddPatientActivity.this;
        localData = new LocalData(activity);
        progressDialog = new ProgressDialog(activity);
        name = findViewById(R.id.name);
        age = findViewById(R.id.age);
        gender = findViewById(R.id.gender);
        relation = findViewById(R.id.relation);
        smoking = findViewById(R.id.smoking);
        addSurgery = findViewById(R.id.add_surgery);
        surgeries = findViewById(R.id.surgeries);
        surgeriesList = new ArrayList<>();
        addMedication = findViewById(R.id.add_medication);
        medications = findViewById(R.id.medications);
        medicationsList = new ArrayList<>();
        addAllergy = findViewById(R.id.add_alergies);
        allergies = findViewById(R.id.allergies);
        allergiesList = new ArrayList<>();
        RecyclerView rv_medicalProblem = findViewById(R.id.rv_mp);
//        surgeries = findViewById(R.id.sg);
//        currentMedication = findViewById(R.id.cm);
//        switchMed = findViewById(R.id.switch_cm);
//        switchSug = findViewById(R.id.switch_sg);
        rv_medicalProblem = findViewById(R.id.rv_mp);
        arrayListMed = new ArrayList<>();
        arrayListCheckMed = new ArrayList<>();
        arrayListSur = new ArrayList<>();
        arrayListCM = new ArrayList<>();

        String[] medicalsPb = getResources().getStringArray(R.array.mp);
        arrayListMed.addAll(Arrays.asList(medicalsPb));

        GridLayoutManager gridLayoutManager = new GridLayoutManager(AddPatientActivity.this, 2);
        rv_medicalProblem.setLayoutManager(gridLayoutManager);
        MedicalProblemAdapter medicalProblemAdapter = new MedicalProblemAdapter(AddPatientActivity.this, arrayListMed, arrayListCheckMed);
        rv_medicalProblem.setAdapter(medicalProblemAdapter);
        medicalProblemAdapter.notifyDataSetChanged();

        TextView done = findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validate()) {
                    strgender = gender.getSelectedItem().toString().toLowerCase();
                    boolSmooking = smoking.getSelectedItem().toString().toLowerCase().equals("yes");
//                    boolSwitchSug = switchSug.isChecked();
//                    boolSwitchCM = switchMed.isChecked();
                    progressDialog.setMessage("Please wait...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    new AddFamily().execute(MongoDB.FAMILY_URL);
                }
            }
        });

        surgeries.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        surgeriesAdapter = new PillAdapter(this, surgeriesList);
        surgeries.setAdapter(surgeriesAdapter);
        addSurgery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog("Surgeries");
            }
        });

        medications.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        medicationAdapter = new PillAdapter(this, medicationsList);
        medications.setAdapter(medicationAdapter);
        addMedication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog("Medication");
            }
        });

        allergies.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        allergiesAdapter = new PillAdapter(this, allergiesList);
        allergies.setAdapter(allergiesAdapter);
        addAllergy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog("Allergy");
            }
        });

//        switchSug.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                surgeries.setEnabled(isChecked);
//            }
//        });
//        switchMed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                currentMedication.setEnabled(isChecked);
//            }
//        });
//        surgeries.setOnTouchListener(new View.OnTouchListener() {
//            @SuppressLint("ClickableViewAccessibility")
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if(event.getAction() == MotionEvent.ACTION_UP) {
//                    if (switchSug.isChecked())
//                        showAlertDialog(surgeries, "Surgeries", arrayListSur);
//                    return true;
//                }
//                return false;
//            }
//        });

//        currentMedication.setOnTouchListener(new View.OnTouchListener() {
//            @SuppressLint("ClickableViewAccessibility")
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_UP) {
//                    if (switchMed.isChecked())
//                        showAlertDialog(currentMedication, "Current Medication", arrayListCM);
//                    return true;
//                }
//                return false;
//            }
//        });


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
            }
        }
        return true;
    }

    private void showAlertDialog(final String enter) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        View view = LayoutInflater.from(activity).inflate(R.layout.add_member_view, null);
        alertDialog.setView(view);
        alertDialog1 = alertDialog.create();
        alertDialog1.show();
        final TextView textViewName = view.findViewById(R.id.name);
        TextView textViewEnter = view.findViewById(R.id.enter);
        TextView cancel = view.findViewById(R.id.cancel);
        TextView done = view.findViewById(R.id.done);
        textViewEnter.setText("Enter " + enter);
        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                alertDialog1.cancel();
            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = textViewName.getText().toString();
                if (!str.replaceAll(" ", "").isEmpty()) {
                    if (enter.equals("Surgeries")){
                        surgeriesList.add(str);
                        surgeriesAdapter.notifyDataSetChanged();
                    } else if (enter.equals("Medication")){
                        medicationsList.add(str);
                        medicationAdapter.notifyDataSetChanged();
                    } else if (enter.equals("Allergy")){
                        allergiesList.add(str);
                        allergiesAdapter.notifyDataSetChanged();
                    }
                    alertDialog1.cancel();

                } else
                    Toast.makeText(activity, "Please enter " + enter, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validate() {
        strname = name.getText().toString();
        strage = age.getText().toString();
        strrelation = relation.getText().toString();
        if (strname.replaceAll(" ", "").isEmpty()) {
            Toast.makeText(activity, "Name is required", Toast.LENGTH_SHORT).show();
            return false;
        } else if (strage.replaceAll(" ", "").isEmpty()) {
            Toast.makeText(activity, "Age is required", Toast.LENGTH_SHORT).show();
            return false;
        } else if (strrelation.replaceAll(" ", "").isEmpty()) {
            Toast.makeText(activity, "Relation is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @SuppressLint("StaticFieldLeak")
    class AddFamily extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {

                OkHttpClient client = new OkHttpClient();
                MediaType mediaType = MediaType.parse("application/json;charset=utf-8");
                JSONObject params = new JSONObject();
                params.put("name", strname);
                params.put("age", strage);
                params.put("gender", "" + strgender);
                params.put("relationWithAccountHolder", strrelation);
                JSONArray jsonArrayMed = new JSONArray();

                for (String str : arrayListCheckMed)
                    jsonArrayMed.put(str);

                JSONArray jsonArraySur = new JSONArray();
                    for (String str : surgeriesAdapter.getData())
                        jsonArraySur.put(str);

                JSONArray jsonArrayCM = new JSONArray();
                for (String str : medicationAdapter.getData())
                        jsonArrayCM.put(str);

                JsonArray jsonArrayAll = new JsonArray();
                for (String str: allergiesAdapter.getData())
                    jsonArrayAll.add(str);

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("medicalProblems", jsonArrayMed);
                jsonObject.put("surgeries", jsonArraySur);
                jsonObject.put("currentMedication", jsonArrayCM);
                jsonObject.put("allergies", jsonArrayAll);
                jsonObject.put("smoking", boolSmooking);
                params.put("history", jsonObject);

                RequestBody body = RequestBody.create(mediaType, params.toString());
                Request request = new Request.Builder()
                        .url(strings[0])
                        .post(body)
                        .addHeader("Content-Type", "application/json;charset=utf-8")
                        .addHeader("token", localData.getToken())
                        .build();
                Response response = client.newCall(request).execute();
                jsonData = response.body().string();
                Log.e("TAG", "doInBackground: " + jsonData);
            } catch (Exception e) {
                Log.e("TAG", "doInBackground: " + e);
                e.printStackTrace();
            }
            return jsonData;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            progressDialog.cancel();
            if (response != null) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Toast.makeText(activity, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    if (!jsonObject.getBoolean("errors"))
                        finish();
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

}