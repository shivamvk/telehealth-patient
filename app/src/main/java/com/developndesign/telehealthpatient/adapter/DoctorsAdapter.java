package com.developndesign.telehealthpatient.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.developndesign.telehealthpatient.R;
import com.developndesign.telehealthpatient.activity.DoctorProfileActivity;
import com.developndesign.telehealthpatient.activity.SelectDoctorActivity;
import com.developndesign.telehealthpatient.activity.VideoChatViewActivity;
import com.developndesign.telehealthpatient.model.UserModelData;
import com.developndesign.telehealthpatient.utils.CommonMethods;
import com.developndesign.telehealthpatient.utils.LocalData;
import com.developndesign.telehealthpatient.utils.MongoDB;

import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DoctorsAdapter extends RecyclerView.Adapter<DoctorsAdapter.DoctorViewHolder> {
    public Context context;
    public ArrayList<UserModelData> userModelData;
    private String jsonData;
    private LocalData localData;
    private ArrayList<String> favourites;

    public DoctorsAdapter(Context context, ArrayList<UserModelData> data) {
        this.context = context;
        this.userModelData = data;
        localData = new LocalData((Activity) context);
        favourites = new ArrayList<>();
    }

    @NonNull
    @Override
    public DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.select_doctor_view, parent, false);
        return new DoctorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final DoctorViewHolder holder, final int position) {
        holder.docName.setText("Dr. " + CommonMethods.capitalizeWord(userModelData.get(position).getFull_name()));
        if (userModelData.get(position).getProfile_picture() != null && !userModelData.get(position).getProfile_picture().isEmpty())
            Glide.with(context).load(MongoDB.AMAZON_BUCKET_URL + userModelData.get(position).getProfile_picture()).into(holder.docImage);
        if (userModelData.get(position).isFavourite()) {
            holder.docFav.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_favorite_24));
            favourites.add(userModelData.get(position).get_id());
        } else {
            holder.docFav.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_favorite_border_24));
            favourites.remove(userModelData.get(position).get_id());
        }
        holder.docFav.setVisibility(View.GONE);
        holder.docFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!favourites.contains(userModelData.get(position).get_id())) {
                    holder.docFav.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_favorite_24));
                    favourites.add(userModelData.get(position).get_id());
                    new AddFavDoctor().execute(MongoDB.FAVOURITE_DOC_URL, userModelData.get(position).get_id());
                } else {
                    holder.docFav.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_favorite_border_24));
                    favourites.remove(userModelData.get(position).get_id());
                    new DeleteFavDoctor().execute(MongoDB.FAVOURITE_DOC_URL + userModelData.get(position).get_id());
                }
            }
        });
        holder.viewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DoctorProfileActivity.class);
                intent.putExtra("name", userModelData.get(position).getFull_name());
                intent.putExtra("image", userModelData.get(position).getProfile_picture());
                if (favourites.contains(userModelData.get(position).get_id()))
                    intent.putExtra("fav", true);
                else
                    intent.putExtra("fav", false);
                intent.putExtra("id", userModelData.get(position).get_id());
                intent.putExtra("college", userModelData.get(position).getCollege());
                intent.putExtra("education", userModelData.get(position).getEducation());
                intent.putExtra("professional", userModelData.get(position).getProfessionalStatement());
                context.startActivity(intent);
            }
        });
        holder.connectNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //new SendNotificationToDoctor().execute(MongoDB.SEND_NOTIFICATION_URL+userModelData.get(position).get_id());
                Intent intent = new Intent(context, VideoChatViewActivity.class);
                intent.putExtra("id", userModelData.get(position).get_id());
                ((SelectDoctorActivity) context).bookAppointmentModelData.setDoctor(userModelData.get(position).get_id());
                intent.putExtra(LocalData.BOOK_APPOINTMENT, ((SelectDoctorActivity) context).bookAppointmentModelData);
                context.startActivity(intent);
            }
        });
    }

    @SuppressLint("StaticFieldLeak")
    class AddFavDoctor extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                OkHttpClient client = new OkHttpClient();
                MediaType mediaType = MediaType.parse("application/json;charset=utf-8");
                JSONObject params = new JSONObject();
                params.put("doctor", strings[1]);
                RequestBody body = RequestBody.create(mediaType, params.toString());
                Request request = new Request.Builder()
                        .url(strings[0])
                        .post(body)
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
                    JSONObject jsonObject = new JSONObject(jsonData);
                    String message = jsonObject.getString("message");

                    Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Log.e("TAG", "onPostExecute: " + e);
                    Toast.makeText(context, "" + e, Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    @SuppressLint("StaticFieldLeak")
    class SendNotificationToDoctor extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                OkHttpClient client = new OkHttpClient();
                MediaType mediaType = MediaType.parse("application/json;charset=utf-8");
                JSONObject params = new JSONObject();
                RequestBody body = RequestBody.create(mediaType, params.toString());
                Request request = new Request.Builder()
                        .url(strings[0])
                        .post(body)
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
                    JSONObject jsonObject = new JSONObject(jsonData);
                    String message = jsonObject.getString("message");
                    Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Log.e("TAG", "onPostExecute: " + e);
                    Toast.makeText(context, "" + e, Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    @SuppressLint("StaticFieldLeak")
    class DeleteFavDoctor extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(strings[0])
                        .delete()
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
                    JSONObject jsonObject = new JSONObject(jsonData);
                    String message = jsonObject.getString("message");
                    Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Log.e("TAG", "onPostExecute: " + e);
                    Toast.makeText(context, "" + e, Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    @Override
    public int getItemCount() {
        return userModelData.size();
    }

    static class DoctorViewHolder extends RecyclerView.ViewHolder {
        private TextView docName;
        private CircleImageView docImage;
        private TextView connectNow;
        private TextView viewProfile;
        private ImageView docFav;

        public DoctorViewHolder(@NonNull View itemView) {
            super(itemView);
            docName = itemView.findViewById(R.id.doc_name);
            docImage = itemView.findViewById(R.id.doc_circleImageView);
            docFav = itemView.findViewById(R.id.doc_fav);
            connectNow = itemView.findViewById(R.id.doc_connect_doctor);
            viewProfile = itemView.findViewById(R.id.doc_view_profile);
        }
    }
}
