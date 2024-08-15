package com.flightsearch.utils.firebase;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.flightsearch.R;
import com.flightsearch.constants.ApplicationConstants;
import com.flightsearch.ui.splash.activity.SplashActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Inject
    SharedPreferences sharedPreferences;

    public static String getDeviceId(SharedPreferences sharedPreferences) {
        return sharedPreferences.getString(ApplicationConstants.FIREBASE_TOKEN, "");
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        sharedPreferences
                .edit()
                .putString(ApplicationConstants.FIREBASE_TOKEN, token)
                .apply();
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Bundle bundle = new Bundle();
        for (String key : remoteMessage.getData().keySet()) {
            bundle.putString(key, remoteMessage.getData().get(key));
        }

        Intent intent = new Intent(this, SplashActivity.class);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        String channelId = "Default";
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
        Notification notification = new NotificationCompat.Builder(this, channelId)
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody())
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.logo)
                .build();
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }
        manager.notify(0, notification);
    }

}
