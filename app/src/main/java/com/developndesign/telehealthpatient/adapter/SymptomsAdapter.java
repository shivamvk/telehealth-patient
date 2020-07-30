package com.developndesign.telehealthpatient.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.developndesign.telehealthpatient.R;
import com.developndesign.telehealthpatient.activity.QuestionsActivity;
import com.developndesign.telehealthpatient.activity.SymptomsActivity;
import com.developndesign.telehealthpatient.model.LanguageSymptomModelData;
import com.developndesign.telehealthpatient.model.SymptomsModelData;
import com.developndesign.telehealthpatient.utils.LocalData;
import com.developndesign.telehealthpatient.utils.MongoDB;

import java.util.ArrayList;

public class SymptomsAdapter extends RecyclerView.Adapter<SymptomsAdapter.SymptomViewHolder> {
    public Context context;
    public ArrayList<LanguageSymptomModelData> languageSymptomModelData;

    public SymptomsAdapter(Context context, ArrayList<LanguageSymptomModelData> data) {
        this.context = context;
        this.languageSymptomModelData = data;
    }

    @NonNull
    @Override
    public SymptomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.symptom_view, parent, false);
        return new SymptomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SymptomViewHolder holder, final int position) {
        holder.symptomName.setText(languageSymptomModelData.get(position).getName());
        Glide.with(context).load(MongoDB.AMAZON_BUCKET_URL + languageSymptomModelData.get(position).getIcon()).into(holder.symptomImg);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<SymptomsModelData> symptomsModelDatalist = new ArrayList<>();
                SymptomsModelData symptomsModelData1 = new SymptomsModelData();
                symptomsModelData1.setSymptom(languageSymptomModelData.get(position).getName());
                symptomsModelDatalist.add(symptomsModelData1);
                ((SymptomsActivity) context).bookAppointmentModelData.setSymptoms(symptomsModelDatalist);
                ((Activity) context).startActivity(new Intent(context, QuestionsActivity.class)
                        .putExtra("id", languageSymptomModelData.get(position).get_id())
                        .putExtra(LocalData.BOOK_APPOINTMENT,((SymptomsActivity) context).bookAppointmentModelData)
                );
            }
        });
    }

    @Override
    public int getItemCount() {
        return languageSymptomModelData.size();
    }

    static class SymptomViewHolder extends RecyclerView.ViewHolder {
        private TextView symptomName;
        private ImageView symptomImg;

        public SymptomViewHolder(@NonNull View itemView) {
            super(itemView);
            symptomName = itemView.findViewById(R.id.symptom_name);
            symptomImg = itemView.findViewById(R.id.symptom_img);
        }
    }
}
