package com.developndesign.telehealthpatient.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.developndesign.telehealthpatient.R;
import com.developndesign.telehealthpatient.activity.AddPatientActivity;
import com.developndesign.telehealthpatient.activity.SymptomsActivity;
import com.developndesign.telehealthpatient.model.BookAppointmentModelData;
import com.developndesign.telehealthpatient.model.FamilyMemberModelBase;
import com.developndesign.telehealthpatient.model.FamilyMemberModelData;
import com.developndesign.telehealthpatient.model.PatientModelData;
import com.developndesign.telehealthpatient.utils.LocalData;
import com.developndesign.telehealthpatient.utils.MongoDB;
import com.google.gson.Gson;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AddNewBookingFragment extends Fragment {

    private LocalData localData;
    private Activity activity;
    private ArrayList<FamilyMemberModelData> languagesData;
    private Spinner spinnerLangauges;
    private ArrayList<FamilyMemberModelData> familyMemberModelData;
    private Spinner spinnerFamily;
    private BookAppointmentModelData bookAppointmentModelData;
    private String jsonData;

    public AddNewBookingFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_booking, container, false);
        spinnerFamily = view.findViewById(R.id.spinner);
        spinnerLangauges = view.findViewById(R.id.spinner2);
        activity = getActivity();

        localData = new LocalData(activity);
        bookAppointmentModelData = new BookAppointmentModelData();
        bookAppointmentModelData.setCallType("video");
        Button next = view.findViewById(R.id.next);
        CardView cardView = view.findViewById(R.id.add_patient);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddPatientActivity.class));
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (familyMemberModelData != null && familyMemberModelData.size() > 0)
                    startActivity(new Intent(activity, SymptomsActivity.class).putExtra(LocalData.BOOK_APPOINTMENT, bookAppointmentModelData));
                else
                    Toast.makeText(activity, "Please select a patient", Toast.LENGTH_SHORT).show();
            }
        });
        new GetLanguages().execute(MongoDB.LANGUAGE_PATIENT_URL);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        new GetFamily().execute(MongoDB.FAMILY_URL);
    }

    @SuppressLint("StaticFieldLeak")
    class GetFamily extends AsyncTask<String, Void, String> {

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
            }
        }

    }

    @SuppressLint("StaticFieldLeak")
    class GetLanguages extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                Log.e("TAG", "doInBackground: " + localData.getToken());
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
            }
        }

    }
}