package com.example.projet_final;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class WorkerService extends Service {
    private static int IdForNot=0;
    private Thread thread1,thread2;
    private boolean whileCon=true;
    private String workerID;
    private DatabaseReference databaseReferenceSpes,databaseReferenceGlobal,databaseReferenceSpesWorker;
    private boolean haveChild=false;
    private ArrayList<Notification> allNotification,allNotificationGlobal;
    private ArrayList<String> notificationID,notificationIDGlobal;
    private ArrayList<String> needNotis,needNotisGlobal;
    private ValueEventListener valueEventListener,valueEventListenerGlobal;
    private double distance;
    public WorkerService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        workerID=intent.getStringExtra("workerID");
        Log.i("workerService","started with id ="+workerID);
        init();
        thread1=new Thread(new Runnable() {
            @Override
            public void run() {
                while (whileCon){
                    Log.i("workerService","thread working");
                    if(haveChild){
                        Log.i("workerService","have child");
                        databaseReferenceSpesWorker.addListenerForSingleValueEvent(valueEventListener);
                        for(int i=0;i<allNotification.size();i++){
                            if(!allNotification.get(i).isWatched()){
                                Log.i("workerService","ther is not");
                                needNotis.add(notificationID.get(i));
                            }
                        }
                        Log.i("workerService","eedNot size="+needNotis.size());
                        notis(needNotis);
                        try {
                            Thread.sleep(4000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        thread2=new Thread(new Runnable() {
            @Override
            public void run() {
                LatLng correntLoc;
                while (whileCon){
                    databaseReferenceGlobal.addListenerForSingleValueEvent(valueEventListenerGlobal);
                    for(int i=0;i<allNotificationGlobal.size();i++){
                        correntLoc=getCourrentLocation();
                        distance=distance(correntLoc.latitude,correntLoc.longitude,allNotificationGlobal.get(i).getLatitude(),allNotification.get(i).getLongitude());
                        if(!allNotificationGlobal.get(i).getTaked().equals("not yet") && distance<10){
                            needNotisGlobal.add(notificationIDGlobal.get(i));
                        }
                        notisGlobal(needNotisGlobal);
                    }
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        thread2.start();
        thread1.start();
        return START_REDELIVER_INTENT;
    }

    private void notis(ArrayList<String> needNotis) {
        Intent intent=new Intent(this,tasks.class);
        PendingIntent pendingIntent=PendingIntent.getActivities(this,0, new Intent[]{intent},0);
        for (int i=0;i<needNotis.size();i++){
            NotificationCompat.Builder builder=new NotificationCompat.Builder(this,"channel_ID")
                    .setSmallIcon(R.drawable.ic_work_black_24dp)
                    .setContentTitle("You have request for job")
                    .setContentText("hellow")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);

            NotificationManagerCompat.from(this).notify(IdForNot,builder.build());
            IdForNot++;
            databaseReferenceSpesWorker.child(needNotis.get(i)).child("watched").setValue(true);
        }
        allNotification.clear();
        notificationID.clear();
        this.needNotis.clear();
    }

    private void notisGlobal(ArrayList<String> needNotisGlobal){
        for (int i=0;i<needNotisGlobal.size();i++){
            IdForNot++;
            int index=notificationIDGlobal.indexOf(needNotisGlobal.get(i));
            databaseReferenceSpesWorker.child(needNotisGlobal.get(i)).setValue(allNotificationGlobal.get(index));
        }
        allNotificationGlobal.clear();
        notificationIDGlobal.clear();
        this.needNotisGlobal.clear();
    }

    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void onDestroy() {
        Log.i("workerService","stop");
        whileCon=false;
        thread1.interrupt();
        super.onDestroy();

    }

    private void init(){
        needNotis=new ArrayList<>();
        needNotisGlobal=new ArrayList<>();
        allNotification=new ArrayList<>();
        notificationID=new ArrayList<>();
        notificationIDGlobal=new ArrayList<>();
        allNotificationGlobal=new ArrayList<>();
        valueEventListenerGlobal=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Notification notification;
                for (DataSnapshot notifications : dataSnapshot.getChildren()) {
                    notificationIDGlobal.add(notifications.getKey());
                    notification = notifications.getValue(Notification.class);
                    allNotificationGlobal.add(notification);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        valueEventListener=new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Notification notification;
                for (DataSnapshot notifications : dataSnapshot.getChildren()) {
                    notificationID.add(notifications.getKey());
                    notification = notifications.getValue(Notification.class);
                    allNotification.add(notification);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        databaseReferenceSpes= FirebaseDatabase.getInstance().getReference().child("spesfReq");
        databaseReferenceGlobal= FirebaseDatabase.getInstance().getReference().child("GlobalReq");
        databaseReferenceSpes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(workerID)) {
                    databaseReferenceSpesWorker=FirebaseDatabase.getInstance().getReference().child("spesfReq").child(workerID);
                    haveChild = true;
                }else{
                    allNotification.clear();
                    notificationID.clear();
                    haveChild=false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public LatLng getCourrentLocation(){
        return new LatLng(36.672496,2.793588);
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
}
