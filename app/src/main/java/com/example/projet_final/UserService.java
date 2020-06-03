package com.example.projet_final;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserService extends Service {

    private Thread thread;
    private boolean whileCon=true;
    private String userID;
    private DatabaseReference databaseReferenceSpes,databaseReferenceGlobal;


    public UserService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        /*userID=intent.getStringExtra("userID");
        databaseReferenceSpes= FirebaseDatabase.getInstance().getReference().child("spesfReq").child(userID);
        databaseReferenceGlobal= FirebaseDatabase.getInstance().getReference().child("GlobalReq");
        databaseReferenceSpes.addChildEventListener(new )*/
        thread=new Thread(new Runnable() {
            @Override
            public void run() {
                while (whileCon){
                    Log.i("UserService","working");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
        return START_REDELIVER_INTENT;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        whileCon=false;
        thread.interrupt();
        super.onDestroy();

    }
}
