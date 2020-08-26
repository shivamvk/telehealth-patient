package com.developndesign.telehealthpatient.utils;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.developndesign.telehealthpatient.R;
import com.developndesign.telehealthpatient.activity.MainActivity;
import com.developndesign.telehealthpatient.activity.VideoChatViewActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class FCMService extends FirebaseMessagingService {
    public static String tag = null;
    String type;
    String Id = "fcm_default_channel";
    Intent intent;
    Bitmap bitmap;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().get("channel").toString().equals("general")){
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, Id);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            builder.setSmallIcon(R.mipmap.ic_launcher);
            builder.setContentTitle(remoteMessage.getData().get("title"));
            builder.setContentTitle(remoteMessage.getData().get("body"));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(Id, "Default channel", NotificationManager.IMPORTANCE_HIGH);
                manager.createNotificationChannel(channel);
            }
            manager.notify(0, builder.build());
        } else if (!new LocalData(this).getToken().isEmpty()) {
            String imageUri = remoteMessage.getData().get("image");
            if (imageUri != null)
                bitmap = getBitmapfromUri(imageUri);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, createInCallIntent(), PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, Id);
            if (bitmap != null) {
                builder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap).setBigContentTitle(remoteMessage.getData().get("title")).setSummaryText(remoteMessage.getData().get("body")));
            } else
                builder.setStyle(new NotificationCompat.BigTextStyle().bigText(remoteMessage.getData().get("body")).setBigContentTitle(remoteMessage.getData().get("title")));

            builder.setFullScreenIntent(pendingIntent, true);
            builder.setCategory(Notification.CATEGORY_CALL);
            builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
            builder.setPriority(NotificationCompat.PRIORITY_MAX);
            builder.setVibrate(new long[]{500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500});
            builder.setAutoCancel(true);
            builder.setDefaults(Notification.DEFAULT_VIBRATE);
            builder.setLights(Color.RED, 10000, 10000);
            builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
            builder.setContentIntent(pendingIntent);
            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            builder.setSmallIcon(R.mipmap.ic_launcher);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(Id, "Default channel", NotificationManager.IMPORTANCE_HIGH);
                manager.createNotificationChannel(channel);
            }
            manager.notify(0, builder.build());
        }
    }

    public static Intent createInCallIntent() {
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
                | Intent.FLAG_ACTIVITY_NO_USER_ACTION);
        intent.setClassName("com.developndesign.telehealthpatient.activity", VideoChatViewActivity.class.getName());
        return intent;
    }

    private Bitmap getBitmapfromUri(String imageUri) {
        URL url;
        HttpURLConnection connection;
        InputStream input;
        Bitmap bitmap = null;
        try {
            url = new URL(imageUri);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            input = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(input);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("TAG", "getBitmapfromUri: " + e);
        }
        return bitmap;
    }
}