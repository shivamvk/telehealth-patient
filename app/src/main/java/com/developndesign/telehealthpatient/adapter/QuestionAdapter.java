package com.developndesign.telehealthpatient.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.developndesign.telehealthpatient.R;
import com.developndesign.telehealthpatient.activity.QuestionsActivity;
import com.developndesign.telehealthpatient.model.QuestionAnswerModelData;
import com.developndesign.telehealthpatient.model.QuestionModelData;
import com.developndesign.telehealthpatient.model.SymptomsModelData;

import java.util.ArrayList;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.SymptomViewHolder> {
    public Context context;
    public ArrayList<QuestionModelData> questionModelData;
    private ArrayList<String> checkQuesions;
    private ArrayList<QuestionAnswerModelData> questionAnswerModelDataArrayList;

    public QuestionAdapter(Context context, ArrayList<QuestionModelData> data, ArrayList<String> checkQuesions) {
        this.context = context;
        this.questionModelData = data;
        this.checkQuesions = checkQuesions;
        questionAnswerModelDataArrayList = new ArrayList<>();
    }

    @NonNull
    @Override
    public SymptomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.question_view, parent, false);
        return new SymptomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SymptomViewHolder holder, final int position) {
        holder.textQuestion.setText(questionModelData.get(position).getTitle());
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    QuestionAnswerModelData questionAnswerModelData = new QuestionAnswerModelData();
                    questionAnswerModelData.setQuestion(questionModelData.get(position).getTitle());
                    ArrayList<String> answers = new ArrayList<>();
                    answers.add("Yes");
                    questionAnswerModelData.setAnswers(answers);
                    questionAnswerModelDataArrayList.add(questionAnswerModelData);
                    checkQuesions.add(questionModelData.get(position).getTitle());
                } else {
                    try {
                        ArrayList<QuestionAnswerModelData> questionAnswerModelDataRemove = new ArrayList<>();
                        for (QuestionAnswerModelData questionAnswerModelData : questionAnswerModelDataArrayList) {
                            if (checkQuesions.contains(questionAnswerModelData.getQuestion()))
                                questionAnswerModelDataRemove.add(questionAnswerModelData);
                        }
                        questionAnswerModelDataArrayList.removeAll(questionAnswerModelDataRemove);
                        checkQuesions.remove(questionModelData.get(position).getTitle());
                    } catch (Exception e) {
                        Log.e("TAG", "onCheckedChanged: " + e);
                        Toast.makeText(context, "" + e, Toast.LENGTH_LONG).show();
                    }
                }
                SymptomsModelData symptomsModelData = ((QuestionsActivity) context).bookAppointmentModelData.getSymptoms().get(0);
                symptomsModelData.setQuestions(questionAnswerModelDataArrayList);
            }
        });

    }

    @Override
    public int getItemCount() {
        return questionModelData.size();
    }

    static class SymptomViewHolder extends RecyclerView.ViewHolder {
        private TextView textQuestion;
        private CheckBox checkBox;

        public SymptomViewHolder(@NonNull View itemView) {
            super(itemView);
            textQuestion = itemView.findViewById(R.id.questions);
            checkBox = itemView.findViewById(R.id.qustion_checkbox);
        }
    }
}
