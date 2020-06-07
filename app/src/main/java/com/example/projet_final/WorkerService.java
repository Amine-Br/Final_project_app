package com.example.projet_final;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;

public class WorkerService extends Service {
    private static int IdForNot=0;
    private Thread thread1,thread2;
    private boolean whileCon=true;
    private String workerID;
    private DatabaseReference databaseReferenceSpes,databaseReferenceGlobal,databaseReferenceSpesWorker;
    private boolean haveChild=false,haveChilsGlobal;
    private ArrayList<Notification> allNotification,allNotificationGlobal;
    private ArrayList<String> notificationID,notificationIDGlobal;
    private ArrayList<String> needNotis,needNotisGlobal;
    private ValueEventListener valueEventListener,valueEventListenerGlobal;
    private double distance;
    private Location correntLoc;
    private Location notLoc;
    private boolean readDataComplet=false;
    private int ondata=0,whileI=0,notI=0;
    private User user;
    public WorkerService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        workerID=intent.getStringExtra("workerID");
        init();
        thread1=new Thread(new Runnable() {
            @Override
            public void run() {
                while (whileCon){
                    final CountDownLatch done=new CountDownLatch(1);
                    Log.i("workerService","while i="+whileI);
                    whileI++;
                    databaseReferenceSpes.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Log.i("workerService","onDatachang i="+ondata);
                            ondata++;
                            Notification notification;
                            for (DataSnapshot notifications : dataSnapshot.getChildren()) {
                                notificationID.add(notifications.getKey());
                                notification = notifications.getValue(Notification.class);
                                allNotification.add(notification);
                            }
                            done.countDown();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    try {
                        done.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    notify_spec();
                    allNotification.clear();
                    notificationID.clear();
                }
            }
        });
        thread2=new Thread(new Runnable() {
            @Override
            public void run() {
                while (whileCon){
                    final CountDownLatch done=new CountDownLatch(1);
                    databaseReferenceGlobal.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Notification notification;
                            for (DataSnapshot notifications : dataSnapshot.getChildren()) {
                                notificationIDGlobal.add(notifications.getKey());
                                notification = notifications.getValue(Notification.class);
                                allNotificationGlobal.add(notification);
                            }
                            done.countDown();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                    try {
                        done.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    notify_global();
                    allNotificationGlobal.clear();
                    notificationIDGlobal.clear();
                }
            }
        });
        thread1.start();
        thread2.start();
        return START_REDELIVER_INTENT;
    }

    private void notify_global() {
        for(int i=0;i<allNotificationGlobal.size();i++){
            correntLoc=new Location("courent location");
            correntLoc.setLatitude(getCourrentLocation().latitude);
            correntLoc.setLongitude(getCourrentLocation().longitude);
            notLoc=new Location("nptification location");
            notLoc.setLatitude(allNotificationGlobal.get(i).getLatitude());
            notLoc.setLongitude(allNotificationGlobal.get(i).getLongitude());
            distance=correntLoc.distanceTo(notLoc);
            final CountDownLatch done=new CountDownLatch(1);

            //final boolean haveChilde=false;
            final int finalI = i;
            databaseReferenceSpes.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.hasChild(notificationIDGlobal.get(finalI))){
                        haveChild=true;
                        done.countDown();
                    }else{
                        done.countDown();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            try {
                done.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            final CountDownLatch done2=new CountDownLatch(1);
            DatabaseReference userRef=FirebaseDatabase.getInstance().getReference().child("users").child(workerID);
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    user =dataSnapshot.getValue(User.class);
                    done2.countDown();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });

            try {
                done2.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if(distance<=10 && allNotificationGlobal.get(i).getTaked().equals("not yet") && !haveChild /*&& user.jabs().get(allNotificationGlobal.get(i).getJob())*/){
                databaseReferenceSpes.child(notificationIDGlobal.get(i)).setValue(allNotificationGlobal.get(i));
            }
            haveChild=false;
        }

    }

    private void notify_spec() {
        Log.i("workerService","not i="+notI);
        notI++;
        for(int i=0;i<allNotification.size();i++){
            if(!allNotification.get(i).isWatched()){
                Intent intent=new Intent(this,tasks.class);
                PendingIntent pendingIntent=PendingIntent.getActivities(this,0, new Intent[]{intent},0);
                    NotificationCompat.Builder builder=new NotificationCompat.Builder(this,"channel_ID")
                            .setSmallIcon(R.drawable.ic_work_black_24dp)
                            .setContentTitle("You have request for job")
                            .setContentText("hellow")
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setContentIntent(pendingIntent)
                            .setAutoCancel(true);

                    NotificationManagerCompat.from(this).notify(IdForNot,builder.build());
                    IdForNot++;
                    databaseReferenceSpes.child(notificationID.get(i)).child("watched").setValue(true);

                DatabaseReference tasckRef=FirebaseDatabase.getInstance().getReference().child("task").child(workerID).child(notificationID.get(i));
                Geocoder geocoder=new Geocoder(WorkerService.this, Locale.getDefault());
                List<Address> addresses = null;
                try {
                    addresses= geocoder.getFromLocation(getCourrentLocation().latitude,getCourrentLocation().longitude,1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(addresses!=null) {
                    String city = addresses.get(0).getAddressLine(0);
                    String state = addresses.get(0).getAddressLine(1);
                    String country = addresses.get(0).getAddressLine(2);
                    tasckRef.setValue(country + "," + state + "," + country + "at:" + allNotification.get(i).getDate());
                }else{
                    String city = addresses.get(0).getAddressLine(0);
                    String state = addresses.get(0).getAddressLine(1);
                    String country = addresses.get(0).getAddressLine(2);
                    tasckRef.setValue(country + "," + state + "," + country + "at:" + allNotification.get(i).getDate());
                }
            }
        }
    }

    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    public void onDestroy() {
        Log.i("workerService","stop");
        whileCon=false;
        thread1.interrupt();
        thread2.interrupt();
        super.onDestroy();

    }

    private void init(){
        databaseReferenceGlobal=FirebaseDatabase.getInstance().getReference().child("GlobalReq");
        databaseReferenceSpes= FirebaseDatabase.getInstance().getReference().child("spesfReq").child(workerID);
        valueEventListener=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.i("workerService","onDatachang i="+ondata);
                ondata++;
                Notification notification;
                for (DataSnapshot notifications : dataSnapshot.getChildren()) {
                    notificationID.add(notifications.getKey());
                    notification = notifications.getValue(Notification.class);
                    allNotification.add(notification);
                }
                //notify_spec();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        notificationID=new ArrayList<>();
        allNotification=new ArrayList<>();
        allNotificationGlobal=new ArrayList<>();
        notificationIDGlobal=new ArrayList<>();
    }

    public LatLng getCourrentLocation(){
        return new LatLng(36.672496,2.793588);
    }



}
