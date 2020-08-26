package com.developndesign.telehealthpatient.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.developndesign.telehealthpatient.R;
import com.developndesign.telehealthpatient.model.BookAppointmentModelData;
import com.developndesign.telehealthpatient.network.SendNotificationToDoctor;
import com.developndesign.telehealthpatient.utils.LocalData;
import com.developndesign.telehealthpatient.utils.MongoDB;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import cdflynn.android.library.checkview.CheckView;
import io.agora.rtc.IRtcEngineEventHandler;
import io.agora.rtc.RtcEngine;
import io.agora.rtc.video.VideoCanvas;
import io.agora.rtc.video.VideoEncoderConfiguration;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class VideoChatViewActivity extends AppCompatActivity {
    private static final String TAG = VideoChatViewActivity.class.getSimpleName();

    private static final int PERMISSION_REQ_ID = 22;
    private static final String[] REQUESTED_PERMISSIONS = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private RtcEngine mRtcEngine;
    private boolean mCallEnd;
    private boolean mMuted;

    private FrameLayout mLocalContainer;
    private RelativeLayout mRemoteContainer;
    private SurfaceView mLocalView;
    private SurfaceView mRemoteView;
    private static final String FORMAT = "%02d:%02d:%02d";
    private ImageView mCallBtn;
    private ImageView mMuteBtn;
    private ImageView mSwitchCameraBtn;
    private TextView timerTextView;
    private String jsonData;
    private BookAppointmentModelData bookAppointmentModelData;
    private LocalData localData;
    private CountDownTimer countDownTimer;

    private final IRtcEngineEventHandler mRtcEventHandler = new IRtcEngineEventHandler() {

        @Override
        public void onJoinChannelSuccess(String channel, final int uid, int elapsed) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i("agora","Join channel success, uid: " + (uid & 0xFFFFFFFFL));
                }
            });
        }

        @Override
        public void onFirstRemoteVideoDecoded(final int uid, int width, int height, int elapsed) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setupRemoteVideo(uid);
                }
            });
        }

        @Override
        public void onUserOffline(final int uid, int reason) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    onRemoteUserLeft();
                    checkBookAppointment();

                }
            });
        }
    };

    private void checkBookAppointment() {
        if (timeRemaining != -1) {
            bookAppointmentModelData.setCallDuration(timeRemaining);
            new BookAppointment().execute(MongoDB.BOOK_APPOINTMENT_URL);
        } else
            finish();
    }

    private String id;
    private long timeRemaining = -1;

    private void setupRemoteVideo(int uid) {
        int count = mRemoteContainer.getChildCount();
        View view = null;
        for (int i = 0; i < count; i++) {
            View v = mRemoteContainer.getChildAt(i);
            if (v.getTag() instanceof Integer && ((int) v.getTag()) == uid) {
                view = v;
            }
        }

        if (view != null) {
            return;
        }

        mRemoteView = RtcEngine.CreateRendererView(getBaseContext());
        mRemoteContainer.addView(mRemoteView);
        mRtcEngine.setupRemoteVideo(new VideoCanvas(mRemoteView, VideoCanvas.RENDER_MODE_HIDDEN, uid));
        mRemoteView.setTag(uid);
        startTimer();
    }

    @SuppressLint("StaticFieldLeak")
    class BookAppointment extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
                OkHttpClient client = new OkHttpClient();
                MediaType mediaType = MediaType.parse("application/json;charset=utf-8");
                Gson gson = new Gson();
                String json = gson.toJson(bookAppointmentModelData);
                Log.e("TAG", "doInBackground: " + json);
                RequestBody body = RequestBody.create(mediaType, json);
                Request request = new Request.Builder()
                        .url(strings[0])
                        .post(body)
                        .addHeader("token", localData.getToken())
                        .addHeader("Content-Type", "application/json;charset=utf-8")
                        .build();
                Response response = client.newCall(request).execute();
                jsonData = response.body().string();
            } catch (Exception e) {
                Log.e("TAG", "doInBackground: " + e);
                Toast.makeText(VideoChatViewActivity.this, "" + e, Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            return jsonData;
        }


        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            if (response != null) {
                try {
                    countDownTimer.cancel();
                    AlertDialog.Builder builder = new AlertDialog.Builder(VideoChatViewActivity.this);
                    View view = LayoutInflater.from(VideoChatViewActivity.this)
                            .inflate(R.layout.doctor_review, null);
                    builder.setView(view);
                    final AlertDialog dialog = builder.create();
                    dialog.show();
                    final TextInputLayout text = view.findViewById(R.id.text);
                    final ImageView s1 = view.findViewById(R.id.star_1);
                    final ImageView s2 = view.findViewById(R.id.star_2);
                    final ImageView s3 = view.findViewById(R.id.star_3);
                    final ImageView s4 = view.findViewById(R.id.star_4);
                    final ImageView s5 = view.findViewById(R.id.star_5);
                    final int[] rating = {-1};
                    final TextView notNow = view.findViewById(R.id.cancel);
                    final boolean[] bool = {false};
                    s1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            rating[0] = 1;
                            bool[0] = true;
                            s1.setImageDrawable(getResources().getDrawable(R.drawable.ic_star));
                            s2.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_outline));
                            s3.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_outline));
                            s4.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_outline));
                            s5.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_outline));
                        }
                    });
                    s2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            rating[0] = 2;
                            bool[0] = true;
                            s1.setImageDrawable(getResources().getDrawable(R.drawable.ic_star));
                            s2.setImageDrawable(getResources().getDrawable(R.drawable.ic_star));
                            s3.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_outline));
                            s4.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_outline));
                            s5.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_outline));
                        }
                    });
                    s3.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            rating[0] = 3;
                            bool[0] = true;
                            s1.setImageDrawable(getResources().getDrawable(R.drawable.ic_star));
                            s2.setImageDrawable(getResources().getDrawable(R.drawable.ic_star));
                            s3.setImageDrawable(getResources().getDrawable(R.drawable.ic_star));
                            s4.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_outline));
                            s5.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_outline));
                        }
                    });
                    s4.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            bool[0] = true;
                            rating[0] = 4;
                            s1.setImageDrawable(getResources().getDrawable(R.drawable.ic_star));
                            s2.setImageDrawable(getResources().getDrawable(R.drawable.ic_star));
                            s3.setImageDrawable(getResources().getDrawable(R.drawable.ic_star));
                            s4.setImageDrawable(getResources().getDrawable(R.drawable.ic_star));
                            s5.setImageDrawable(getResources().getDrawable(R.drawable.ic_star_outline));
                        }
                    });
                    s5.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            bool[0] = true;
                            rating[0] = 5;
                            s1.setImageDrawable(getResources().getDrawable(R.drawable.ic_star));
                            s2.setImageDrawable(getResources().getDrawable(R.drawable.ic_star));
                            s3.setImageDrawable(getResources().getDrawable(R.drawable.ic_star));
                            s4.setImageDrawable(getResources().getDrawable(R.drawable.ic_star));
                            s5.setImageDrawable(getResources().getDrawable(R.drawable.ic_star));
                        }
                    });
                    text.setEndIconOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //sendReview
                            if (bool[0]) {
                                dialog.cancel();
                                JsonObject jsonObject = new JsonObject();
                                jsonObject.addProperty("doctor", id);
                                jsonObject.addProperty("rating", rating[0]);
                                jsonObject.addProperty("review", text.getEditText().getText().toString());
                                new SendReview().execute(
                                        MongoDB.ADD_DOCTOR_REVIEW,
                                        jsonObject.toString(),
                                        localData.getToken()
                                );
                                Intent i = new Intent(VideoChatViewActivity.this, MainActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                            } else {
                                Toast.makeText(VideoChatViewActivity.this, "Please select a rating star", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    notNow.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.cancel();
                            Intent i = new Intent(VideoChatViewActivity.this, MainActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                        }
                    });
                } catch (Exception e) {
                    Log.e("TAG", "onPostExecute: " + e);
                    Toast.makeText(VideoChatViewActivity.this, "" + e, Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    class SendReview extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(strings[0])
                    .post(RequestBody.create(MediaType.parse("application/json;charset=utf-8"), strings[1]))
                            .addHeader("Content-Type", "application/json;charset=utf-8")
                                    .addHeader("token", strings[2])
                                    .build();
            try {
                return client.newCall(request).execute().toString();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
        }
    }

    private void onRemoteUserLeft() {
        removeRemoteVideo();
    }

    private void removeRemoteVideo() {
        if (mRemoteView != null) {
            mRemoteContainer.removeView(mRemoteView);
        }
        // Destroys remote view
        mRemoteView = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_chat_view);
        initUI();
        id = getIntent().getStringExtra("id");
        try {
            sendNotificationToDoctor();
            bookAppointmentModelData = getIntent().getParcelableExtra(LocalData.BOOK_APPOINTMENT);
            Log.e("TAG", "onCreate: " + id);
            if (checkSelfPermission(REQUESTED_PERMISSIONS[0], PERMISSION_REQ_ID) &&
                    checkSelfPermission(REQUESTED_PERMISSIONS[1], PERMISSION_REQ_ID) &&
                    checkSelfPermission(REQUESTED_PERMISSIONS[2], PERMISSION_REQ_ID)) {
                initEngineAndJoinChannel();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendNotificationToDoctor() throws Exception {
        if (id == null){
            throw new Exception("Please provide a valid id of doctor");
        }
        new SendNotificationToDoctor().execute(
                new String[]
                        {MongoDB.SEND_NOTIFICATION_URL + "/" + id,
                                localData.getToken()});
    }

    private void initUI() {
        localData = new LocalData(VideoChatViewActivity.this);
        mLocalContainer = findViewById(R.id.local_video_view_container);
        mRemoteContainer = findViewById(R.id.remote_video_view_container);
        mCallBtn = findViewById(R.id.btn_call);
        mMuteBtn = findViewById(R.id.btn_mute);
        mSwitchCameraBtn = findViewById(R.id.btn_switch_camera);
        timerTextView = findViewById(R.id.timerText);
    }

    private void startTimer() {
         countDownTimer =
                new CountDownTimer(1000 * 60 * 7, 1000) {

                    public void onTick(long millisUntilFinished) {
                        timeRemaining = 1000 * 60 * 7 - millisUntilFinished;
                        timerTextView.setText("Time remaining " + String.format(FORMAT,
                                TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                    }

                    public void onFinish() {
                        timeRemaining = 1000 * 60 * 7;
                        timerTextView.setText("done!");
                        checkBookAppointment();

                    }

                }.start();
    }


    private boolean checkSelfPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, REQUESTED_PERMISSIONS, requestCode);
            return false;
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == PERMISSION_REQ_ID) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED ||
                    grantResults[1] != PackageManager.PERMISSION_GRANTED ||
                    grantResults[2] != PackageManager.PERMISSION_GRANTED) {
                showLongToast("Need permissions " + Manifest.permission.RECORD_AUDIO +
                        "/" + Manifest.permission.CAMERA + "/" + Manifest.permission.WRITE_EXTERNAL_STORAGE);
                finish();
                return;
            }
            initEngineAndJoinChannel();
        }
    }


    private void showLongToast(final String msg) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initEngineAndJoinChannel() {
        initializeEngine();
        setupVideoConfig();
        setupLocalVideo();
        joinChannel();
    }

    private void initializeEngine() {
        try {
            mRtcEngine = RtcEngine.create(getBaseContext(), getString(R.string.agora_app_id), mRtcEventHandler);
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
            throw new RuntimeException("NEED TO check rtc sdk init fatal error\n" + Log.getStackTraceString(e));
        }
    }

    private void setupVideoConfig() {
        mRtcEngine.enableVideo();
        mRtcEngine.setVideoEncoderConfiguration(new VideoEncoderConfiguration(
                VideoEncoderConfiguration.VD_640x360,
                VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_15,
                VideoEncoderConfiguration.STANDARD_BITRATE,
                VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT));
    }

    private void setupLocalVideo() {
        mLocalView = RtcEngine.CreateRendererView(getBaseContext());
        mLocalView.setZOrderMediaOverlay(true);
        mLocalContainer.addView(mLocalView);
        mRtcEngine.setupLocalVideo(new VideoCanvas(mLocalView, VideoCanvas.RENDER_MODE_HIDDEN, 0));
    }

    private void joinChannel() {

        String token = "";
        if (TextUtils.isEmpty(token) || TextUtils.equals(token, "#YOUR ACCESS TOKEN#")) {
            token = null; // default, no token
        }
        mRtcEngine.joinChannel(token, id, "Extra Optional Data", 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!mCallEnd) {
            leaveChannel();
        }
        RtcEngine.destroy();
    }

    private void leaveChannel() {
        if (mRtcEngine == null){
            return;
        }
        mRtcEngine.leaveChannel();
    }

    public void onLocalAudioMuteClicked(View view) {
        mMuted = !mMuted;
        mRtcEngine.muteLocalAudioStream(mMuted);
        int res = mMuted ? R.drawable.btn_mute : R.drawable.btn_unmute;
        mMuteBtn.setImageResource(res);
    }

    public void onSwitchCameraClicked(View view) {
        mRtcEngine.switchCamera();
    }

    public void onCallClicked(View view) {
        if (mCallEnd) {
            startCall();
            mCallEnd = false;
            mCallBtn.setImageResource(R.drawable.btn_endcall);
        } else {
            endCall();
            mCallEnd = true;
            mCallBtn.setImageResource(R.drawable.btn_startcall);
            checkBookAppointment();
        }

        showButtons(!mCallEnd);
    }

    private void startCall() {
        setupLocalVideo();
        joinChannel();
    }

    private void endCall() {
        removeLocalVideo();
        removeRemoteVideo();
        leaveChannel();
    }

    private void removeLocalVideo() {
        if (mLocalView != null) {
            mLocalContainer.removeView(mLocalView);
        }
        mLocalView = null;
    }

    private void showButtons(boolean show) {
        int visibility = show ? View.VISIBLE : View.GONE;
        mMuteBtn.setVisibility(visibility);
        mSwitchCameraBtn.setVisibility(visibility);
    }
}
