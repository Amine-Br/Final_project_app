package com.example.projet_final;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.util.Log;

public class app extends Application {
    private final String channelID="channel_ID";
    private NotificationManager maneger;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("Activity","app");
        Log.i("app","onCreate");

        creatNotification();
    }

    private void creatNotification() {
        Log.i("app","creatNotification");
        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.O){
            NotificationChannel notificationChannel= new NotificationChannel(channelID
                    , "channelName"
                    , NotificationManager.IMPORTANCE_HIGH);
            maneger=getSystemService(NotificationManager.class);
            maneger.createNotificationChannel(notificationChannel);
        }
    }













}
