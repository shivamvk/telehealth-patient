package com.developndesign.telehealthpatient.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.developndesign.telehealthpatient.R;
import com.developndesign.telehealthpatient.utils.MongoDB;

import org.w3c.dom.Text;

public class DoctorProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_baseline_arrow_back_24));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView textDoctorName=findViewById(R.id.doc_name);
        TextView textDoctorEdu=findViewById(R.id.doc_education);
        TextView textDoctorCollege=findViewById(R.id.doc_college);
        TextView textProfessionalStatement=findViewById(R.id.doc_professional_statement);
        ImageView docimageView=findViewById(R.id.doc_img);

        String name=getIntent().getStringExtra("name");
        String edu=getIntent().getStringExtra("education");
        String college=getIntent().getStringExtra("college");
        String professional=getIntent().getStringExtra("professional");
        String image=getIntent().getStringExtra("image");

        textDoctorName.setText(name);
        textDoctorEdu.setText(edu);
        textDoctorCollege.setText(college);
        textProfessionalStatement.setText(professional);
        if(image!=null&&!image.isEmpty())
        Glide.with(DoctorProfileActivity.this).load(MongoDB.AMAZON_BUCKET_URL+image).into(docimageView);

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }
}