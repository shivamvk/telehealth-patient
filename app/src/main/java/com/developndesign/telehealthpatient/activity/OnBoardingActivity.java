package com.developndesign.telehealthpatient.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.developndesign.telehealthpatient.R;
import com.developndesign.telehealthpatient.adapter.SliderAdapter;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;

public class OnBoardingActivity extends AppCompatActivity {
    private ArrayList<String> titles;
    private ArrayList<String> descriptions;
    private ArrayList<Drawable> images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);
        titles = new ArrayList<>();
        descriptions = new ArrayList<>();
        images = new ArrayList<>();
        titles.add("Welcome to TeleHealth");
        titles.add("Ask a doctor online");
        titles.add("Meet Doctors on your need");
        descriptions.add("At TeleHealth, one of the leading online consultation providers, doctors are available for online consultation 24×7. You don’t have to wait for consultation days or hours. You can call them the moment you start feeling unwell.");
        descriptions.add("There are no location boundaries for online doctor consultation. You can consult the doctor of your choice irrespective of his or your location, or the day or time.");
        descriptions.add("With online consultation you are  settled in your own comfort zone sans the travel expenses. Moreover, with all the services provided under one roof including online doctor consultation, diagnostics, and prescription of medication, it would certainly prove economical.");

        images.add(getResources().getDrawable(R.drawable.ic_undraw1));
        images.add(getResources().getDrawable(R.drawable.ic_undraw2));
        images.add(getResources().getDrawable(R.drawable.ic_undraw_doctor_kw5l));
        SliderView sliderView = findViewById(R.id.imageSlider);
        final TextView title = findViewById(R.id.title);
        final TextView description = findViewById(R.id.description);
        title.setText(titles.get(0));
        description.setText(descriptions.get(0));
        SliderAdapter sliderAdapter = new SliderAdapter(this, images);
        sliderView.setSliderAdapter(sliderAdapter);
        sliderView.startAutoCycle();
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setCurrentPageListener(new SliderView.OnSliderPageListener() {
            @Override
            public void onSliderPageChanged(int position) {
                title.setText(titles.get(position));
                description.setText(descriptions.get(position));
            }
        });


    }

    public void signup(View view) {
        startActivity(new Intent(OnBoardingActivity.this,RegistrationActivity.class));

    }

    public void login(View view) {
        startActivity(new Intent(OnBoardingActivity.this,LoginActivity.class));

    }
}