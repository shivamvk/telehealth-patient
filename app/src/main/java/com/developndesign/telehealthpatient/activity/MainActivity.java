package com.developndesign.telehealthpatient.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.developndesign.telehealthpatient.R;
import com.developndesign.telehealthpatient.fragment.AllPatientFragment;
import com.developndesign.telehealthpatient.fragment.BlogFragment;
import com.developndesign.telehealthpatient.fragment.CallHistoryFragment;
import com.developndesign.telehealthpatient.fragment.HomeFragment;
import com.developndesign.telehealthpatient.fragment.AddNewBookingFragment;
import com.developndesign.telehealthpatient.fragment.NotificationFragment;
import com.developndesign.telehealthpatient.fragment.PrivacyPolicyFragment;
import com.developndesign.telehealthpatient.utils.LocalData;
import com.developndesign.telehealthpatient.utils.MongoDB;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {
    private TextView name, email;
    private CircleImageView image;
    private LocalData localData;
    private DrawerLayout mDrawerLayout;
    private BlogFragment blogFragment;
    private CallHistoryFragment callHistoryFragment;
    private AddNewBookingFragment addNewBookingFragment;
    private NotificationFragment notificationFragment;
    private HomeFragment homeFragment;
    private PrivacyPolicyFragment privacyPolicyFragment;
    private AllPatientFragment allPatientFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);
        localData = new LocalData(MainActivity.this);


        addNewBookingFragment =new AddNewBookingFragment();
        callHistoryFragment=new CallHistoryFragment() ;
        blogFragment=new BlogFragment();
        notificationFragment=new NotificationFragment();
        privacyPolicyFragment=new PrivacyPolicyFragment();
        homeFragment=new HomeFragment();
        allPatientFragment=new AllPatientFragment();
        goToFragment(homeFragment);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, null, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        name = navigationView.getHeaderView(0).findViewById(R.id.uname);
        email = navigationView.getHeaderView(0).findViewById(R.id.uphone);
        image = navigationView.getHeaderView(0).findViewById(R.id.userthumbimage);
        navigationView.getHeaderView(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,EditProfileActivity.class));
            }
        });
        navigationView.setNavigationItemSelectedListener(this);
        FloatingActionButton imageView = findViewById(R.id.menu_main);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDrawer();
            }
        });
        Log.e("TAG", "onCreate: "+localData.getToken() );

    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        if(menuItem.getItemId()==R.id.nav_home){
            goToFragment(homeFragment);
        } else if(menuItem.getItemId()==R.id.nav_new_booking){
            goToFragment(addNewBookingFragment);
        }else if(menuItem.getItemId()==R.id.nav_patient){
            goToFragment(allPatientFragment);
        } else if(menuItem.getItemId()==R.id.nav_call_history){
            goToFragment(callHistoryFragment);
        } else if (menuItem.getItemId() == R.id.nav_privacy) {
            goToFragment(privacyPolicyFragment);
        } else if(menuItem.getItemId()==R.id.nav_notification){
            goToFragment(notificationFragment);
        } else if(menuItem.getItemId()==R.id.nav_support){
            shareToGMail(new String[]{"support@telehealth.com"},"","");
        }else if(menuItem.getItemId()==R.id.nav_blog){
            goToFragment(blogFragment);
        }else if (menuItem.getItemId() == R.id.nav_logout) {
            localData.setToken("");
            startActivity(new Intent(MainActivity.this, OnBoardingActivity.class));
        }

        mDrawerLayout.closeDrawer(Gravity.LEFT);
        return true;
    }

    public void openDrawer() {
        mDrawerLayout.openDrawer(Gravity.LEFT);
    }
    private void goToFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment).commit();
    }



    public void shareToGMail(String[] email, String subject, String content) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, email);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, content);
        final PackageManager pm = getPackageManager();
        final List<ResolveInfo> matches = pm.queryIntentActivities(emailIntent, 0);
        ResolveInfo best = null;
        for (final ResolveInfo info : matches)
            if (info.activityInfo.packageName.endsWith(".gm") || info.activityInfo.name.toLowerCase().contains("gmail"))
                best = info;
        if (best != null)
            emailIntent.setClassName(best.activityInfo.packageName, best.activityInfo.name);
        startActivity(emailIntent);
    }


    @Override
    protected void onResume() {
        super.onResume();
        email.setText(localData.getEmail());
        name.setText(localData.getName());
        Log.e("TAG", "onResume: "+localData.getProfilePicture() );
        if (!localData.getProfilePicture().isEmpty())
            Glide.with(MainActivity.this).load(MongoDB.AMAZON_BUCKET_URL + localData.getProfilePicture()).into(image);

    }


}