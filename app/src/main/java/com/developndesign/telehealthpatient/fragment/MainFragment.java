package com.developndesign.telehealthpatient.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.developndesign.telehealthpatient.R;
import com.developndesign.telehealthpatient.activity.MainActivity;
import com.developndesign.telehealthpatient.activity.SymptomsActivity;
import com.developndesign.telehealthpatient.model.BookAppointmentModelData;
import com.developndesign.telehealthpatient.model.FamilyMemberModelBase;
import com.developndesign.telehealthpatient.model.FamilyMemberModelData;
import com.developndesign.telehealthpatient.model.PatientModelData;
import com.developndesign.telehealthpatient.utils.LocalData;
import com.developndesign.telehealthpatient.utils.MongoDB;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainFragment extends Fragment {

    private LocalData localData;
    private ProgressDialog progressDialog;
    private Response response;
    private Activity activity;
    private EditText name, age,  relation;
    private String strname,strage, strrelation;
    private AlertDialog alertDialog1;
    private Spinner gender;
    private ArrayList<FamilyMemberModelData> languagesData;
    private Spinner spinnerLangauges;
    private ArrayList<FamilyMemberModelData> familyMemberModelData;
    private Spinner spinnerFamily;
    private BookAppointmentModelData bookAppointmentModelData;
    private String strgender;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_main, container, false);
        FloatingActionButton addMember = view.findViewById(R.id.add_member);
        spinnerFamily = view.findViewById(R.id.spinner);
        spinnerLangauges = view.findViewById(R.id.spinner2);
        activity = getActivity();
        localData = new LocalData(activity);
        progressDialog = new ProgressDialog(activity);
        bookAppointmentModelData = new BookAppointmentModelData();
        bookAppointmentModelData.setCallType("video");
        Button next = view.findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (familyMemberModelData != null && familyMemberModelData.size() > 0)
                    startActivity(new Intent(activity, SymptomsActivity.class).putExtra(LocalData.BOOK_APPOINTMENT, bookAppointmentModelData));
                else
                    Toast.makeText(activity, "Please select a patient", Toast.LENGTH_SHORT).show();
            }
        });
        new GetFamily().execute(MongoDB.FAMILY_URL);
        new GetLanguages().execute(MongoDB.LANGUAGE_PATIENT_URL);
        addMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog();
            }
        });
        return view;
    }
    private boolean validate() {
        strname = name.getText().toString();
        strage = age.getText().toString();
        strrelation = relation.getText().toString();
        if (strname.replaceAll(" ", "").isEmpty()) {
            Toast.makeText(activity, "Name is required", Toast.LENGTH_SHORT).show();
            return false;
        }else if (strage.replaceAll(" ", "").isEmpty()) {
            Toast.makeText(activity, "Age is required", Toast.LENGTH_SHORT).show();
            return false;
        } else if (strrelation.replaceAll(" ", "").isEmpty()) {
            Toast.makeText(activity, "Relation is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    private void showAlertDialog() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        View view = LayoutInflater.from(activity).inflate(R.layout.add_member_view, null);
        alertDialog.setView(view);
        alertDialog1 = alertDialog.create();
        alertDialog1.show();
        name = view.findViewById(R.id.name);
        age = view.findViewById(R.id.age);
        gender = view.findViewById(R.id.gender);
        relation = view.findViewById(R.id.relation);
        TextView cancel = view.findViewById(R.id.cancel);
        TextView done = view.findViewById(R.id.done);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog1.cancel();
            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (validate()) {
                    strgender = gender.getSelectedItem().toString().toLowerCase();

                    progressDialog.setMessage("Please wait...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    new AddFamily().execute(MongoDB.FAMILY_URL);
                }
            }
        });
    }
    @SuppressLint("StaticFieldLeak")
    class AddFamily extends AsyncTask<String, Void, Response> {

        @Override
        protected Response doInBackground(String... strings) {
            try {

                OkHttpClient client = new OkHttpClient();
                MediaType mediaType = MediaType.parse("application/json;charset=utf-8");
                JSONObject params = new JSONObject();
                params.put("name", strname);
                params.put("age", strage);
                params.put("gender", ""+strgender);
                params.put("relationWithAccountHolder", strrelation);
                RequestBody body = RequestBody.create(mediaType, params.toString());
                Request request = new Request.Builder()
                        .url(strings[0])
                        .post(body)
                        .addHeader("Content-Type", "application/json;charset=utf-8")
                        .addHeader("token", localData.getToken())
                        .build();
                response = client.newCall(request).execute();
            } catch (Exception e) {
                Log.e("TAG", "doInBackground: " + e);
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);
            progressDialog.cancel();
            alertDialog1.cancel();
            String jsonData;
            if (response.body() != null) {
                try {
                    jsonData = response.body().string();
                    JSONObject jsonObject = new JSONObject(jsonData);
                    Toast.makeText(activity, "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    new GetFamily().execute(MongoDB.FAMILY_URL);
                } catch (Exception e) {
                    progressDialog.cancel();
                    Log.e("TAG", "onPostExecute: " + e);
                    Toast.makeText(activity, "" + e, Toast.LENGTH_SHORT).show();
                }
            } else {
                progressDialog.cancel();
                Toast.makeText(activity, "" + response.message(), Toast.LENGTH_SHORT).show();
            }
        }
    }
    @SuppressLint("StaticFieldLeak")
    class GetFamily extends AsyncTask<String, Void, Response> {

        @Override
        protected Response doInBackground(String... strings) {
            try {

                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(strings[0])
                        .get()
                        .addHeader("Content-Type", "application/json;charset=utf-8")
                        .addHeader("token", localData.getToken())
                        .build();
                response = client.newCall(request).execute();
            } catch (Exception e) {
                Log.e("TAG", "doInBackground: " + e);
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);
            String jsonData;
            if (response.body() != null) {
                try {
                    jsonData = response.body().string();
                    familyMemberModelData = new Gson().fromJson(jsonData, FamilyMemberModelBase.class).getData();
                    if (familyMemberModelData != null) {
                        ArrayList<String> names = new ArrayList<>();
                        for (int i = 0; i < familyMemberModelData.size(); i++)
                            names.add(familyMemberModelData.get(i).getName());

                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, names);
                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerFamily.setAdapter(arrayAdapter);
                        spinnerFamily.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                Log.e("TAG", "onItemSelected: " + familyMemberModelData.get(position).get_id());
                                PatientModelData patientModelData = new PatientModelData();
                                patientModelData.setAge(familyMemberModelData.get(position).getAge());
                                patientModelData.setGender(familyMemberModelData.get(position).getGender());
                                patientModelData.setName(familyMemberModelData.get(position).getName());
                                patientModelData.setRelationWithAccountHolder(familyMemberModelData.get(position).getRelationWithAccountHolder());
                                bookAppointmentModelData.setPatient(patientModelData);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }
                } catch (Exception e) {
                    Log.e("TAG", "onPostExecute: " + e);
                    Toast.makeText(activity, "" + e, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(activity, "" + response.message(), Toast.LENGTH_SHORT).show();
            }
        }

    }

    @SuppressLint("StaticFieldLeak")
    class GetLanguages extends AsyncTask<String, Void, Response> {

        @Override
        protected Response doInBackground(String... strings) {
            try {
                Log.e("TAG", "doInBackground: "+localData.getToken() );
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(strings[0])
                        .get()
                        .addHeader("Content-Type", "application/json;charset=utf-8")
                        .addHeader("token", localData.getToken())
                        .build();
                response = client.newCall(request).execute();
            } catch (Exception e) {
                Log.e("TAG", "doInBackground: " + e);
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(Response response) {
            super.onPostExecute(response);
            String jsonData;
            if (response.body() != null) {
                try {
                    jsonData = response.body().string();
                    languagesData = new Gson().fromJson(jsonData, FamilyMemberModelBase.class).getData();
                    if (languagesData != null) {
                        ArrayList<String> names = new ArrayList<>();
                        for (int i = 0; i < languagesData.size(); i++)
                            names.add(languagesData.get(i).getName());
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, names);
                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerLangauges.setAdapter(arrayAdapter);
                        spinnerLangauges.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                Log.e("TAG", "onItemSelected: " + languagesData.get(position).get_id());
                                bookAppointmentModelData.setLanguage(languagesData.get(position).get_id());
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }
                } catch (Exception e) {
                    Log.e("TAG", "onPostExecute: " + e);
                    Toast.makeText(activity, "" + e, Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(activity, "" + response.message(), Toast.LENGTH_SHORT).show();
            }
        }

    }
}