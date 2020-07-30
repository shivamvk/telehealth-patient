package com.developndesign.telehealthpatient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.developndesign.telehealthpatient.R;
import com.developndesign.telehealthpatient.model.GetBookAppointmentModelData;
import com.developndesign.telehealthpatient.utils.CommonMethods;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class GetAppointmentAdapter extends RecyclerView.Adapter<GetAppointmentAdapter.AppointmentViewHolder> {

    private ArrayList<GetBookAppointmentModelData> bookAppointmentModelDataArrayList;
    private Context context;

    private static final String FORMAT = "%02d:%02d";

    public GetAppointmentAdapter(Context context, ArrayList<GetBookAppointmentModelData> data) {
        this.bookAppointmentModelDataArrayList = data;
        this.context = context;
    }

    @NonNull
    @Override
    public AppointmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.booking_item_view, parent, false);
        return new AppointmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentViewHolder holder, int position) {
        holder.patientName.setText(bookAppointmentModelDataArrayList.get(position).getPatient().getName());
        holder.doctorName.setText("To Dr. "+ CommonMethods.capitalizeWord(bookAppointmentModelDataArrayList.get(position).getDoctor().getFull_name()));
        holder.createdDate.setText(bookAppointmentModelDataArrayList.get(position).getCreatedAt().split("T")[0]);
        long millisUntilFinished = bookAppointmentModelDataArrayList.get(position).getCallDuration();
        holder.callDuration.setText(String.format(FORMAT,
                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
        holder.symptom.setText(bookAppointmentModelDataArrayList.get(position).getSymptoms().get(0).getSymptom());

    }

    @Override
    public int getItemCount() {
        return bookAppointmentModelDataArrayList.size();
    }

    class AppointmentViewHolder extends RecyclerView.ViewHolder {
        private TextView patientName;
        private TextView doctorName;
        private TextView createdDate;
        private TextView symptom;
        private TextView callDuration;


        public AppointmentViewHolder(@NonNull View itemView) {
            super(itemView);
            patientName = itemView.findViewById(R.id.patientName);
            doctorName = itemView.findViewById(R.id.doctorName);
            createdDate = itemView.findViewById(R.id.callDate);
            symptom = itemView.findViewById(R.id.callSymptom);
            callDuration = itemView.findViewById(R.id.callDuration);
        }
    }
}
