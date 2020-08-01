package com.developndesign.telehealthpatient.fragment;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.developndesign.telehealthpatient.R;
import com.developndesign.telehealthpatient.adapter.AllPatientAdapter;
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


public class AllPatientFragment extends Fragment {


    private String jsonData;
    private LocalData localData;
    private ArrayList<FamilyMemberModelData> familyMemberModelData;
    private RecyclerView recyclerView;

    public AllPatientFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_patient, container, false);
        localData=new LocalData(getActivity());
        recyclerView=view.findViewById(R.id.all_patient_recyclerview);
        LinearLayoutManager manager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(manager);
        new GetFamily().execute(MongoDB.FAMILY_URL);
        return view;
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
                    AllPatientAdapter patientAdapter=new AllPatientAdapter(getActivity(),familyMemberModelData);
                    recyclerView.setAdapter(patientAdapter);
                    patientAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    Log.e("TAG", "onPostExecute: " + e);
                    Toast.makeText(getActivity(), "" + e, Toast.LENGTH_SHORT).show();
                }
            }
        }

    }
}