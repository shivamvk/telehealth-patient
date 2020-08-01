package com.developndesign.telehealthpatient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.developndesign.telehealthpatient.R;

import java.util.ArrayList;

public class MedicalProblemAdapter extends RecyclerView.Adapter<MedicalProblemAdapter.MedProbHolder> {
    private Context context;
    private ArrayList<String> medicalsProblem;
    private ArrayList<String> checkMedicalProb;

    public MedicalProblemAdapter(Context context, ArrayList<String> medicalsProblem, ArrayList<String> checkMedicalProb) {
        this.context = context;
        this.medicalsProblem = medicalsProblem;
        this.checkMedicalProb = checkMedicalProb;
    }

    @NonNull
    @Override
    public MedProbHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.med_prob_view, parent, false);
        return new MedProbHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedProbHolder holder, final int position) {
        holder.checkBox.setText(medicalsProblem.get(position));
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkMedicalProb.add(medicalsProblem.get(position));
                } else if(checkMedicalProb.contains(medicalsProblem.get(position))){
                    checkMedicalProb.remove(medicalsProblem.get(position));
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return medicalsProblem.size();
    }

    class MedProbHolder extends RecyclerView.ViewHolder {
        private CheckBox checkBox;

        public MedProbHolder(@NonNull View itemView) {
            super(itemView);
            checkBox=itemView.findViewById(R.id.checkbox);
        }
    }
}
