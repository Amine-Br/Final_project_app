package com.example.projet_final;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
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

import java.util.ArrayList;

public class WorkerService extends Service {
    private static int IdForNot=0;
    private Thread thread;
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
    public WorkerService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this,"service is started",Toast.LENGTH_LONG).show();
        workerID=intent.getStringExtra("workerID");
        Log.i("workerService","started with id ="+workerID);
        init();
        thread=new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i("workerService","thread start");
                //start
                /*needNotis=new ArrayList<>();
                needNotisGlobal=new ArrayList<>();
                allNotification=new ArrayList<>();
                notificationID=new ArrayList<>();
                notificationIDGlobal=new ArrayList<>();
                allNotificationGlobal=new ArrayList<>();
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
                };*/
                //stop
                int cpt=0;
                while (whileCon){
                    Log.i("workerService","while cpt="+cpt);
                    if(haveChild){
                        Log.i("workerService","have child cheked");
                        databaseReferenceSpesWorker.addListenerForSingleValueEvent(valueEventListener);
                        //test1
                    }
                    databaseReferenceGlobal.addListenerForSingleValueEvent(valueEventListenerGlobal);
                        //test2
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    cpt++;
                }
            }
        });
        thread.start();
        return START_STICKY;
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

    private void notisGlobal(final ArrayList<String> needNotisGlobal){
        for (int i=0;i<needNotisGlobal.size();i++){
            Log.i("workerService","for i="+i);
            IdForNot++;
            int index=notificationIDGlobal.indexOf(needNotisGlobal.get(i));
            final int finalI = i;
            if(haveChild) {
                databaseReferenceSpesWorker.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(needNotisGlobal.get(finalI))) {
                            haveChilsGlobal = true;
                        } else {
                            haveChilsGlobal = false;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
            if(!haveChilsGlobal){
                databaseReferenceSpesWorker.child(needNotisGlobal.get(i)).setValue(allNotificationGlobal.get(index));
            }
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
        thread.interrupt();
        super.onDestroy();

    }

    private void init(){
        Log.i("workerService","init called");
        needNotis=new ArrayList<>();
        needNotisGlobal=new ArrayList<>();
        allNotification=new ArrayList<>();
        notificationID=new ArrayList<>();
        notificationIDGlobal=new ArrayList<>();
        allNotificationGlobal=new ArrayList<>();
        databaseReferenceSpes= FirebaseDatabase.getInstance().getReference().child("spesfReq");
        databaseReferenceGlobal= FirebaseDatabase.getInstance().getReference().child("GlobalReq");
        databaseReferenceSpes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.i("workerService","databaseReferenceSpes used");
                if(dataSnapshot.hasChild(workerID)) {
                    Log.i("workerService","databaseReferenceSpes result= true (havechild)");
                    databaseReferenceSpesWorker=FirebaseDatabase.getInstance().getReference().child("spesfReq").child(workerID);
                    haveChild = true;
                }else{
                    Log.i("workerService","databaseReferenceSpes result= false (don't havechild)");
                    allNotification.clear();
                    notificationID.clear();
                    haveChild=false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        valueEventListenerGlobal=new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.i("workerService","valueEventListenerGlobal used");
                Notification notification;
                for (DataSnapshot notifications : dataSnapshot.getChildren()) {
                    notificationIDGlobal.add(notifications.getKey());
                    notification = notifications.getValue(Notification.class);
                    allNotificationGlobal.add(notification);
                }
                Log.i("workerService","valueEventListenerGlobal end res= allNotificationGlobal "+allNotificationGlobal.size()+" notificationIDGlobal "+notificationIDGlobal.size() );
                Log.i("workerService","valueEventListenerGlobal end res 1= allNotificationGlobal "+allNotificationGlobal.get(0).isWatched()+" notificationIDGlobal "+notificationIDGlobal.get(0));
                test2();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        valueEventListener=new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.i("workerService","init called");
                Notification notification;
                for (DataSnapshot notifications : dataSnapshot.getChildren()) {
                    notificationID.add(notifications.getKey());
                    notification = notifications.getValue(Notification.class);
                    allNotification.add(notification);
                }
                Log.i("workerService","valueEventListener end res= allNotificationl "+allNotification.size()+" notificationID "+notificationID.size() );
                Log.i("workerService","valueEventListenerl end res 1= allNotificationl "+allNotification.get(0).isWatched()+" notificationID "+notificationID.get(0));
                test1();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
    }

    public LatLng getCourrentLocation(){

        return new LatLng(36.672496,2.793588);
    }

    private void test1(){
        Log.i("workerService","there is "+allNotificationGlobal.size()+" not for this worker");
        for(int i=0;i<allNotification.size();i++){
            if(!allNotification.get(i).isWatched()){
                needNotis.add(notificationID.get(i));
            }
        }
        Log.i("workerService","needNotis size="+needNotis.size());
        notis(needNotis);

    }
    private void test2(){
        Log.i("workerService","there is "+allNotificationGlobal.size()+" not global");
        for(int i=0;i<allNotificationGlobal.size();i++){
            Log.i("workerService","notis global work");
            correntLoc=new Location("courent location");
            correntLoc.setLatitude(getCourrentLocation().latitude);
            correntLoc.setLongitude(getCourrentLocation().longitude);
            notLoc=new Location("nptification location");
            notLoc.setLatitude(allNotificationGlobal.get(i).getLatitude());
            notLoc.setLongitude(allNotificationGlobal.get(i).getLongitude());
            distance=correntLoc.distanceTo(notLoc);
            //distance=distance/(float)1000;
            if(allNotificationGlobal.get(i).getTaked().equals("not yet") && distance<=10){
                needNotisGlobal.add(notificationIDGlobal.get(i));
            }
            Log.i("workerService","neesNotisGlobal size="+needNotisGlobal.size());
        }
        Log.i("workerService","neesNotisGlobal size="+needNotisGlobal.size());
        notisGlobal(needNotisGlobal);
    }

}
