package com.developndesign.telehealthpatient.fragment;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.developndesign.telehealthpatient.R;
import com.developndesign.telehealthpatient.activity.TermsConditionActivity;
import com.developndesign.telehealthpatient.utils.MongoDB;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class PrivacyPolicyFragment extends Fragment {

    public PrivacyPolicyFragment() {
        // Required empty public constructor
    }

    private TextView tv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_privacy_policy, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tv = view.findViewById(R.id.text);
        new GetTermsAndConditions().execute(MongoDB.TERMS_URL);
    }

    class GetTermsAndConditions extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            String jsonObject = "";
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(strings[0])
                    .build();
            try {
                Response response = client.newCall(request).execute();
                jsonObject = response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
                return e.getLocalizedMessage();
            }
            return jsonObject;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                String data = jsonObject.getString("data");
                if (data!=null) {
                    tv.setText(data);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}