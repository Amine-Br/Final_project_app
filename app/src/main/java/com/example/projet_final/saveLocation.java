package com.example.projet_final;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class saveLocation extends Service {

    private DatabaseReference mReference;
    private static GoogleMap Mmap;
    private static String userID;
    private FusedLocationProviderClient mFusedLocationClient;
    private FirebaseDatabase mFirebaseDatabase;
    private Location location;
    private Notification notification;
    boolean mStatus = true;
    /*private NotificationManager maneger;
    private final String channelID="channel_ID";*/

    /*final class theTheread implements Runnable{


        public theTheread(){

        }

        @Override
        public void run() {
            Log.i("seveLocation","theredRun");
            int i=0;
            mReference.child("users").child(userID).child("stat").setValue("online");
            //notication();
            final long[] time = {System.currentTimeMillis() + 1000};
            while (true){
                i++;
                //Log.i("seveLocation","while i="+i);
                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            location = task.getResult();
                            Log.i("seveLocation","secc");
                            LatLng user_location = new LatLng(location.getLatitude(), location.getLongitude());
                            /*marker=new MarkerOptions().position(user_location).title("Marker in your location");
                            mMap.addMarker(marker);
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(user_location));
                            CameraPosition cameraPosition = new CameraPosition.Builder()
                                    .target(user_location)      // Sets the center of the map to user_location
                                    .zoom(15)                   // Sets the zoom to 15 (city zoom)
                                    .build();                   // Creates a CameraPosition from the builder
                            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                            Toast.makeText(saveLocation.this,"Latitude:"+location.getLatitude()+"   Longitude:"+location.getLongitude(),Toast.LENGTH_SHORT).show();*/
                            /*Log.i("seveLocation","Latitude:"+location.getLatitude()+"   Longitude:"+location.getLongitude());
                            mReference.child("users").child(userID).child("Latitude").setValue(location.getLatitude());
                            mReference.child("users").child(userID).child("Longitude").setValue(location.getLongitude());
                            Log.i("seveLocation","saved");
                            //SystemClock.sleep(1000);
                            /*while (System.currentTimeMillis()< time[0]){

                            }
                            time[0] +=1000;

                        }else{
                            Log.i("seveLocation","not succ");
                        }
                    }
                });

            }
        }



    }*/


    private Thread saveL;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("Activity", "saveLocation");
        Log.i("saveLocation", "onCreate");
        init();
    }


    public void init() {
        Log.i("saveLocation", "init");
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mReference = mFirebaseDatabase.getReference();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(saveLocation.this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("saveLocation", "onStartCommand");
        //saveL=new Thread(new theTheread());
        saveL = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.i("saveLocation", "thread run");
                int i = 0;
                mReference.child("users").child(userID).child("stat").setValue("online");
                while (mStatus) {
                    i++;
                    Log.i("saveLocation", "while i=" + i);

                    //save Location
                    if (ActivityCompat.checkSelfPermission(saveLocation.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(saveLocation.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    /*mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            if (task.isSuccessful()) {
                                location = task.getResult();
                                //LatLng user_location = new LatLng(location.getLatitude(), location.getLongitude());
                                mReference.child("users").child(userID).child("Latitude").setValue(location.getLatitude());
                                mReference.child("users").child(userID).child("Longitude").setValue(location.getLongitude());
                            } else {

                            }
                        }
                    });*/

                    //SystemClock.sleep(1000);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        saveL.start();
        notification();
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.i("saveLocation","onDestroy");
        mStatus = false;
        saveL.interrupt();
        mReference.child("users").child(userID).child("stat").setValue("offline");
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static void setMmap(GoogleMap mmap) {
        Mmap = mmap;
    }

    public static void setUserID(String userID) {
        saveLocation.userID = userID;
    }

    private void notification() {
        Log.i("saveLocation","notification");
        Intent NIntent=new Intent(saveLocation.this,MapsActivity.class);
        PendingIntent pendingIntent=PendingIntent.getActivities(saveLocation.this,0, new Intent[]{NIntent},0);
        final String channelID="channel_ID";
        notification=new NotificationCompat.Builder(this,channelID)
                .setContentTitle("working")
                .setContentText("you are working now")
                .setSmallIcon(R.drawable.ic_work_black_24dp)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(123,notification);
    }


}
