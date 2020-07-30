package com.developndesign.telehealthpatient.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.developndesign.telehealthpatient.R;
import com.developndesign.telehealthpatient.adapter.MenuAdapter;
import com.developndesign.telehealthpatient.fragment.BlogFragment;
import com.developndesign.telehealthpatient.fragment.CallHistoryFragment;
import com.developndesign.telehealthpatient.fragment.MainFragment;
import com.developndesign.telehealthpatient.fragment.NotificationFragment;
import com.developndesign.telehealthpatient.fragment.PrivacyPolicyFragment;
import com.developndesign.telehealthpatient.utils.LocalData;
import com.developndesign.telehealthpatient.utils.MongoDB;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import nl.psdcompany.duonavigationdrawer.views.DuoDrawerLayout;
import nl.psdcompany.duonavigationdrawer.views.DuoMenuView;
import nl.psdcompany.duonavigationdrawer.widgets.DuoDrawerToggle;

public class MainActivity extends AppCompatActivity implements DuoMenuView.OnMenuClickListener {
    private TextView textName, textEmail;
    private CircleImageView circleImageView;
    private MenuAdapter mMenuAdapter;
    private ViewHolder mViewHolder;
    private ArrayList<String> mTitles = new ArrayList<>();
    private LocalData localData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        localData = new LocalData(MainActivity.this);
        mTitles = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.menuOptions)));

        // Initialize the views
        mViewHolder = new ViewHolder();

        // Handle toolbar actions
        handleToolbar();

        // Handle menu actions
        handleMenu();

        // Handle drawer actions
        handleDrawer();

        // Show main fragment in container
        goToFragment(new MainFragment(), false);
        mMenuAdapter.setViewSelected(0, true);
        setTitle(mTitles.get(0));
        Log.e("TAG", "onCreate: " + localData.getToken());


        textName = mViewHolder.mDuoMenuView.getHeaderView().findViewById(R.id.uname);
        textEmail = mViewHolder.mDuoMenuView.getHeaderView().findViewById(R.id.uphone);
        circleImageView =(CircleImageView) mViewHolder.mDuoMenuView.getHeaderView().findViewById(R.id.userthumbimage);
        mViewHolder.mDuoMenuView.getHeaderView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, EditProfileActivity.class));
            }
        });
        mViewHolder.mDuoMenuView.getFooterView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                localData.setToken("");
                startActivity(new Intent(MainActivity.this,OnBoardingActivity.class));
                finish();
            }
        });

//        support.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String[] email={"info@mindfulmachine.in"};
//                shareToGMail(email,"Need help!","");
//            }
//        });
//        notification.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this, NotificationActivity.class));
//            }
//        });
//        blog.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this, BlogActivity.class));
//            }
//        });
//        callHistory.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this, ShowHistoryActivity.class));
//            }
//        });
//        termsCondition.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this, TermsConditionActivity.class));
//            }
//        });
//        TextView editProfile = guillotineMenu.findViewById(R.id.edit_profile);
//        Log.e("TAG", "onCreate: " + localData.getToken());
//        new GetFamily().execute(MongoDB.FAMILY_URL);
//        new GetLanguages().execute(MongoDB.LANGUAGE_PATIENT_URL);
//        addMember.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showAlertDialog();
//            }
//        });
//        editProfile.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(activity, EditProfileActivity.class));
//            }
//        });
//        logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                localData.setToken("");
//                startActivity(new Intent(activity, RegistrationActivity.class));
//                finish();
//            }
//        });
//
    }

    private void goToFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (addToBackStack) {
            transaction.addToBackStack(null);
        }

        transaction.replace(R.id.container, fragment).commit();
    }

    private void handleToolbar() {
        setSupportActionBar(mViewHolder.mToolbar);
    }

    private void handleDrawer() {
        DuoDrawerToggle duoDrawerToggle = new DuoDrawerToggle(this,
                mViewHolder.mDuoDrawerLayout,
                mViewHolder.mToolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        mViewHolder.mDuoDrawerLayout.setDrawerListener(duoDrawerToggle);
        duoDrawerToggle.syncState();


    }

    private void handleMenu() {
        mMenuAdapter = new MenuAdapter(mTitles);
        mViewHolder.mDuoMenuView.setOnMenuClickListener(this);
        mViewHolder.mDuoMenuView.setAdapter(mMenuAdapter);
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
    public void onFooterClicked() {

    }

    @Override
    public void onHeaderClicked() {

    }

    @Override
    public void onOptionClicked(int position, Object objectClicked) {
        // Set the toolbar title
        setTitle(mTitles.get(position));

        mMenuAdapter.setViewSelected(position, true);

        switch (position) {
            case 0:
                goToFragment(new MainFragment(), false);
                break;
            case 1:
                goToFragment(new CallHistoryFragment(), false);
                break;
            case 2:
                goToFragment(new NotificationFragment(), false);
                break;
            case 3:
                String[] email = {"support@telehealth.com"};
                shareToGMail(email, "Need help!", "");
                break;
            case 4:
                goToFragment(new BlogFragment(), false);
                break;
            case 5:
                goToFragment(new PrivacyPolicyFragment(), false);
                break;

        }

        mViewHolder.mDuoDrawerLayout.closeDrawer();
    }

    @Override
    protected void onResume() {
        super.onResume();

        textEmail.setText(localData.getEmail());
        textName.setText(localData.getName());
        Log.e("TAG", "onResume: "+localData.getProfilePicture() );
        if (!localData.getProfilePicture().isEmpty())
            Glide.with(MainActivity.this).load(MongoDB.AMAZON_BUCKET_URL + localData.getProfilePicture()).into(circleImageView);

    }

    private class ViewHolder {
        private DuoDrawerLayout mDuoDrawerLayout;
        private DuoMenuView mDuoMenuView;
        private Toolbar mToolbar;


        ViewHolder() {
            mDuoDrawerLayout = (DuoDrawerLayout) findViewById(R.id.drawer);
            mDuoMenuView = (DuoMenuView) mDuoDrawerLayout.getMenuView();
            mToolbar = (Toolbar) findViewById(R.id.toolbar);

        }
    }
}