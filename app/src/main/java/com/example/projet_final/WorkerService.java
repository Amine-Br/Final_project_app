package com.example.projet_final;

import android.Manifest;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;

public class WorkerService extends Service {
    private static int IdForNot=0;
    private Thread thread1,thread2;
    private boolean whileCon=true;
    private static String workerID;
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
    private int finalI2;
    LatLng user_location;
    private FusedLocationProviderClient mFusedLocationClient;
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
                            done.countDown();
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
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
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
                            done.countDown();
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
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread1.start();
        thread2.start();
        return START_STICKY;
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
            finalI2 = i;
            databaseReferenceSpes.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.hasChild(notificationIDGlobal.get(finalI2))){
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
                    done2.countDown();
                }
            });
            try {
                done2.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(user==null){
                Log.i("workerGlobal","null");
            }else{
                Log.i("workerGlobal","userIDJob="+user.getJobsString());
            }
            Log.i("workerGlobal","");
            HashMap<String,Boolean> Hm=user.jabs();
            boolean sameJob=Hm.get(allNotificationGlobal.get(i).getJob());
            if(distance<=10000 && allNotificationGlobal.get(i).getTaked().equals("not yet") && !haveChild && sameJob ){
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
                    addresses= geocoder.getFromLocation(allNotification.get(i).getLatitude(),allNotification.get(i).getLongitude(),1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(addresses!=null) {
                    String city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    tasckRef.setValue(country + "," + state + "," + city + " at:" + allNotification.get(i).getDate());
                }else{
                    tasckRef.setValue("null addres");
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
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
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
        final CountDownLatch done=new CountDownLatch(1);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if(location != null){
                        user_location = new LatLng(location.getLatitude(), location.getLongitude());
                        done.countDown();

                    }
                }
            });
            try {
                done.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return new LatLng(user_location.latitude,user_location.longitude);
    }



}
