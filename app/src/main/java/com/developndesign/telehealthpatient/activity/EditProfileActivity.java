package com.developndesign.telehealthpatient.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.developndesign.telehealthpatient.R;
import com.developndesign.telehealthpatient.model.RegistrationModelBase;
import com.developndesign.telehealthpatient.model.RegistrationModelData;
import com.developndesign.telehealthpatient.utils.LocalData;
import com.developndesign.telehealthpatient.utils.MongoDB;
import com.google.gson.Gson;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EditProfileActivity extends AppCompatActivity {
    private LocalData localData;
    private Activity activity;
    private Response response;
    private EditText name, email, phoneNumber, dob;
    private String gender = "male";
    private String strname = "", stremail = "", strPhoneNumber = "";
    private TextView male, female;
    private CircleImageView circleImageView;
    private ProgressDialog progressDialog;
    private Uri uri;
    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    private JSONObject jsonObject;
    String jsonData = "";
    private Calendar myCalendar;
    private int myear;
    private int mmonth;
    private int mday;
    private String isodob = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Toolbar toolbar = findViewById(R.id.toolbar);
        activity = EditProfileActivity.this;
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        dob = findViewById(R.id.dob);
        phoneNumber = findViewById(R.id.phone_number);
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);
        circleImageView = findViewById(R.id.image);
        Button updateProfile = findViewById(R.id.updateProfile);
        progressDialog = new ProgressDialog(EditProfileActivity.this);
        localData = new LocalData(activity);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_baseline_arrow_back_24));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        new GetProfile().execute(MongoDB.PROFILE_PATIENT_URL);
        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gender = "male";
                male.setBackground(getResources().getDrawable(R.drawable.button_background2));
                female.setBackground(getResources().getDrawable(R.drawable.button_background3));
            }
        });
        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gender = "female";
                female.setBackground(getResources().getDrawable(R.drawable.button_background2));
                male.setBackground(getResources().getDrawable(R.drawable.button_background3));
            }
        });
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImageFromDevice();
            }
        });
        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkValidity()) {
                    strname = name.getText().toString();
                    stremail = email.getText().toString();
                    strPhoneNumber = phoneNumber.getText().toString();
                    progressDialog.setMessage("Please wait...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    new UpdateProfile().execute(MongoDB.UPDATE_PROFILE_URL);
                }
            }
        });
        setDob();
    }

    private void setDob() {
        myCalendar = Calendar.getInstance();
        String myFormat = "yyyy-MM-dd";
        final SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        final Calendar c = Calendar.getInstance();
        myear = c.get(Calendar.YEAR);
        mmonth = c.get(Calendar.MONTH);
        mday = c.get(Calendar.DAY_OF_MONTH);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myear = year;
                mmonth = month;
                mday = dayOfMonth;
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                TimeZone tz = TimeZone.getTimeZone("UTC");
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
                df.setTimeZone(tz);
                isodob = df.format(myCalendar.getTime());
                String strdob = sdf.format(myCalendar.getTime());
                dob.setText(strdob);
            }
        };
        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog _date = new DatePickerDialog(EditProfileActivity.this, date, myear, mmonth, mday) {
                    @Override
                    public void onDateChanged(@NonNull DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                    }
                };
                _date.show();
            }
        });
    }

    private boolean checkValidity() {
        String strName = name.getText().toString();
        String strEmail = email.getText().toString();
        String strPhoneNumber = phoneNumber.getText().toString();
        if (strName.replaceAll(" ", "").isEmpty()) {
            Toast.makeText(activity, "Name is required", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!validate(strEmail)) {
            Toast.makeText(activity, "Invalid email address", Toast.LENGTH_SHORT).show();
            return false;
        } else if (strPhoneNumber.length() != 10) {
            Toast.makeText(activity, "Invalid phone number", Toast.LENGTH_SHORT).show();
            return false;
        } else if (isodob.isEmpty()) {
            Toast.makeText(activity, "Date of birth is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public static boolean validate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    private void selectImageFromDevice() {
        CropImage.activity().start(EditProfileActivity.this);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                assert result != null;
                uri = result.getUri();
                circleImageView.setImageURI(uri);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("StaticFieldLeak")
    class UpdateProfile extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                OkHttpClient client = new OkHttpClient();
                Log.e("TAG", "doInBackground: " + isodob);
                MultipartBody.Builder req = new MultipartBody.Builder().setType(MultipartBody.FORM)
                        .addFormDataPart("full_name", strname)
                        .addFormDataPart("email", stremail)
                        .addFormDataPart("gender", gender)
                        .addFormDataPart("dob", "" + isodob)
                        .addFormDataPart("mobile_number", strPhoneNumber);

                if (uri != null) {
                    File imageFile = new File(getFilePath(EditProfileActivity.this, uri));
                    String imageName = imageFile.getName();
                    req.addFormDataPart("profile_picture", imageName, RequestBody.create(MediaType.parse("image/png"), imageFile));
                }
                MultipartBody body = req.build();
                Request request = new Request.Builder()
                        .url(strings[0])
                        .post(body)
                        .addHeader("Content-Type", "application/json;charset=utf-8")
                        .addHeader("token", localData.getToken())
                        .build();
                Response response = client.newCall(request).execute();
                assert response.body() != null;
                jsonData = response.body().string();
            } catch (Exception e) {
                progressDialog.cancel();
                e.printStackTrace();
            }
            return jsonData;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            progressDialog.cancel();
            boolean errors = false;
            try {
                jsonObject = new JSONObject(response);
                Log.e("TAG", "onPostExecute: " + response);
                errors = jsonObject.getBoolean("errors");
                if (!errors) {
                    localData.setProfilePicture(jsonObject.getJSONObject("data").getString("profile_picture"));
                    localData.setName(strname);
                    localData.setEmail(stremail);
                    progressDialog.cancel();
                    Toast.makeText(EditProfileActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(EditProfileActivity.this, " " + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Log.e("TAG", "onPostExecute: " + e);
                Toast.makeText(EditProfileActivity.this, "" + e, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public String getFilePath(Context context, Uri uri) {
        String filePath = "";
        try {
            if (DocumentsContract.isDocumentUri(context, uri)) {
                String wholeID = DocumentsContract.getDocumentId(uri);
                String[] splits = wholeID.split(":");
                if (splits.length == 2) {
                    String id = splits[1];
                    String[] column = {MediaStore.Images.Media.DATA};
                    String sel = MediaStore.Images.Media._ID + "=?";
                    Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            column, sel, new String[]{id}, null);
                    int columnIndex = cursor.getColumnIndex(column[0]);
                    if (cursor.moveToFirst()) {
                        filePath = cursor.getString(columnIndex);
                    }
                    cursor.close();
                }
            } else {
                filePath = uri.getPath();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filePath;
    }

    @SuppressLint("StaticFieldLeak")
    class GetProfile extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(strings[0])
                        .get()
                        .addHeader("token", localData.getToken())
                        .addHeader("Content-Type", "application/json;charset=utf-8")
                        .build();
                response = client.newCall(request).execute();
                jsonData = response.body().string();
            } catch (Exception e) {
                Log.e("TAG", "doInBackground: " + e);
                e.printStackTrace();
            }
            return jsonData;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            boolean errors = false;
            if (response != null) {
//                try {
                try {
                    Log.e("TAG1", "onPostExecute: " + response);
                    JSONObject jsonObject = new JSONObject(response);
                    errors = jsonObject.getBoolean("errors");
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("TAG", "onPostExecute: " + e);
                }
                if (!errors) {
                    RegistrationModelData data = new Gson().fromJson(response, RegistrationModelBase.class).getData();
                    if (data != null) {
                        isodob = data.getUser().getDob();
                        name.setText(data.getUser().getFull_name());
                        email.setText(data.getUser().getEmail());
                        phoneNumber.setText(data.getUser().getMobile_number());
                        if (!data.getUser().getDob().split("T")[0].isEmpty())
                            dob.setText(data.getUser().getDob().split("T")[0]);
                        localData.setProfilePicture(data.getUser().getProfile_picture());
                        localData.setName(data.getUser().getFull_name());
                        localData.setEmail(data.getUser().getEmail());
                        if (!data.getUser().getGender().isEmpty() && data.getUser().getGender().equals("male")) {
                            male.setBackground(getResources().getDrawable(R.drawable.button_background2));
                            female.setBackground(getResources().getDrawable(R.drawable.button_background3));
                        }
                        if (!data.getUser().getGender().isEmpty() && data.getUser().getGender().equals("female")) {
                            female.setBackground(getResources().getDrawable(R.drawable.button_background2));
                            male.setBackground(getResources().getDrawable(R.drawable.button_background3));
                        }

                        if (data.getUser().getProfile_picture() != null && !data.getUser().getProfile_picture().isEmpty())
                            Glide.with(EditProfileActivity.this).load(MongoDB.AMAZON_BUCKET_URL + data.getUser().getProfile_picture()).into(circleImageView);


                    }
                }
//                } catch (Exception e) {
//                    Log.e("TAG getProfile", "onPostExecute: " + e);
//                    Toast.makeText(activity, "" + e, Toast.LENGTH_SHORT).show();
//                }
            }
        }
    }
}