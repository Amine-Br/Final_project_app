package com.example.projet_final;

import android.Manifest;
import android.app.PendingIntent;
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
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import static android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP;

public class UserService extends Service {

    private User user;
    private Thread thread;
    private boolean whileCon=true;
    private String userID;
    private boolean haschild=false;
    private DatabaseReference databaseReferenceSpes,databaseReferenceGlobal;
    private ArrayList<resulttReq> arrayList;
    private ArrayList<String> arrayListID;
    private static int IdForNot=0;


    public UserService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        userID=intent.getStringExtra("userID");
        arrayList=new ArrayList<>();
        arrayListID=new ArrayList<>();
        FirebaseDatabase.getInstance().getReference().child("resulttReq").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(userID)){
                    haschild=true;
                }else{
                    haschild=false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                haschild=false;
            }
        });
        /*databaseReferenceSpes= FirebaseDatabase.getInstance().getReference().child("spesfReq").child(userID);
        databaseReferenceGlobal= FirebaseDatabase.getInstance().getReference().child("GlobalReq");
        databaseReferenceSpes.addChildEventListener(new )*/
        thread=new Thread(new Runnable() {
            @Override
            public void run() {
                while (whileCon){
                    Log.i("UserService","working");
                    final CountDownLatch done=new CountDownLatch(1);
                    if(haschild){
                        FirebaseDatabase.getInstance().getReference().child("resulttReq").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                resulttReq resulttReq;
                                for (DataSnapshot resault : dataSnapshot.getChildren()) {
                                    resulttReq=resault.getValue(com.example.projet_final.resulttReq.class);
                                    arrayList.add(resulttReq);
                                    arrayListID.add(resault.getKey());
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
                        arrayList.clear();
                        arrayListID.clear();

                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
        return START_STICKY;
    }

    private void notify_spec() {
        for (int i=0;i<arrayList.size();i++){
            if(!arrayList.get(i).iswatched()){
                Log.i("UserService",arrayList.get(i).getAccID());
                Intent intent = new Intent(UserService.this, notif_click.class);
                PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(),
                                0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                NotificationCompat.Builder builder=new NotificationCompat.Builder(this,"channel_ID")
                        .setSmallIcon(R.drawable.ic_work_black_24dp)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setAutoCancel(true);
                final CountDownLatch done=new CountDownLatch(1);
                FirebaseDatabase.getInstance().getReference().child("users").child(arrayList.get(i).getAccID()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.i("UserService","arrayList.get(i).getAccID()");
                        user=dataSnapshot.getValue(User.class);
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
                if(arrayList.get(i).isAccpet()){
                    intent.putExtra("user_name", user.getUser_name());
                    intent.putExtra("sex", user.getSex());
                    intent.putExtra("Birthday", user.getBirthday());
                    intent.putExtra("email", user.getEmail());
                    intent.putExtra("jobs", user.getJobsString());
                    builder.setContentText("accepted").setContentTitle("your request is accepted").setContentIntent(contentIntent);
                }
                else{
                    builder.setContentText("rejected").setContentTitle("your request is rejected");
                }
                NotificationManagerCompat.from(this).notify(IdForNot,builder.build());
                IdForNot++;
                FirebaseDatabase.getInstance().getReference().child("resulttReq").child(userID).child(arrayListID.get(i)).child("watched").setValue(true);
            }
        }
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
