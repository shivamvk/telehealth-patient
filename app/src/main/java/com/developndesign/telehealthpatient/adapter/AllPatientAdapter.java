package com.developndesign.telehealthpatient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.developndesign.telehealthpatient.R;
import com.developndesign.telehealthpatient.model.FamilyMemberModelData;

import java.util.ArrayList;

public class AllPatientAdapter extends RecyclerView.Adapter<AllPatientAdapter.PatientViewHolder> {


    private ArrayList<FamilyMemberModelData> familyMemberModelData;
    private Context context;

    public AllPatientAdapter(Context context, ArrayList<FamilyMemberModelData> familyMemberModelData) {
        this.familyMemberModelData = familyMemberModelData;
        this.context = context;
    }

    @NonNull
    @Override
    public PatientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.patient_view, parent, false);
        return new PatientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientViewHolder holder, int position) {
        holder.patName.setText(familyMemberModelData.get(position).getName());
        if (familyMemberModelData.get(position).getAge() != null)
            holder.patInfo.append(familyMemberModelData.get(position).getAge()+"yr" + " - " + familyMemberModelData.get(position).getGender() + "\n");
        else
            holder.patInfo.append(familyMemberModelData.get(position).getGender() + "\n");

        holder.patInfo.append("Relation- " + familyMemberModelData.get(position).getRelationWithAccountHolder());


    }

    @Override
    public int getItemCount() {
        return familyMemberModelData.size();
    }

    static class PatientViewHolder extends RecyclerView.ViewHolder {
        private TextView patName;
        private TextView patInfo;

        public PatientViewHolder(@NonNull View itemView) {
            super(itemView);
            patName = itemView.findViewById(R.id.pat_name);
            patInfo = itemView.findViewById(R.id.pat_detail);

        }
    }
}
