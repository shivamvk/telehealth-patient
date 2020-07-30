package com.developndesign.telehealthpatient.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.developndesign.telehealthpatient.R;
import com.developndesign.telehealthpatient.utils.LocalData;


public class SplashActivity extends AppCompatActivity implements Animation.AnimationListener {
    LocalData localData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        localData = new LocalData(SplashActivity.this);
        try {
            Animation animFadeIn = AnimationUtils.loadAnimation(getApplicationContext(),
                    R.anim.animation_fade_in);
            animFadeIn.setAnimationListener(this);
            RelativeLayout linearLayout = findViewById(R.id.layout_linear);
            linearLayout.setVisibility(View.VISIBLE);
            linearLayout.startAnimation(animFadeIn);
        } catch (Exception e) {
            Log.e("TAG", "onCreate: " + e);
        }
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if (localData.getToken().isEmpty()) {
            Intent intent = new Intent(SplashActivity.this, OnBoardingActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}