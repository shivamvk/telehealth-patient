package com.developndesign.telehealthpatient.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.developndesign.telehealthpatient.R;
import com.developndesign.telehealthpatient.activity.ShowHistoryActivity;
import com.developndesign.telehealthpatient.adapter.GetAppointmentAdapter;
import com.developndesign.telehealthpatient.model.GetBookAppointmentModelBase;
import com.developndesign.telehealthpatient.model.GetBookAppointmentModelData;
import com.developndesign.telehealthpatient.utils.LocalData;
import com.developndesign.telehealthpatient.utils.MongoDB;
import com.google.gson.Gson;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CallHistoryFragment extends Fragment {


    public CallHistoryFragment() {
        // Required empty public constructor
    }

    Activity activity;
    LocalData localData;
    private RecyclerView recyclerViewCallHistory;
    String jsonData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_call_history, container, false);
        activity = getActivity();
        localData = new LocalData(activity);
        recyclerViewCallHistory = view.findViewById(R.id.call_history_recyclerview);
        new GetCallHistory().execute(MongoDB.BOOK_APPOINTMENT_URL);
        return view;

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
}