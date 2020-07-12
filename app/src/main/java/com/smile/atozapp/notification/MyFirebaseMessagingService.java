package com.smile.atozapp.notification;


import android.app.NotificationManager;
import android.content.Context;
import android.media.RingtoneManager;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.smile.atozapp.R;
import com.smile.atozapp.controller.TempData;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    String TAG = "Firebase message service class";
    TempData t ;

    @Override
    public void onCreate() {
        super.onCreate();
        t = new TempData(getApplicationContext());
    }

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);
        t.addtoken(token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Log.d(TAG ,remoteMessage.getNotification().getBody());
    }


}
