package com.example.projet_final;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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
    final class theTheread implements Runnable{
        private FusedLocationProviderClient mFusedLocationClient;
        private GoogleMap mMap;
        //MarkerOptions marker;
        private FirebaseDatabase mFirebaseDatabase;
        private String userID;
        private Location location;
        public theTheread(GoogleMap map,String ID){
            mMap=map;
            userID=ID;
            mFirebaseDatabase=FirebaseDatabase.getInstance();
            mReference=mFirebaseDatabase.getReference();
            mFusedLocationClient= LocationServices.getFusedLocationProviderClient(saveLocation.this);
        }

        @Override
        public void run() {
            Log.i("seveLocation","theredRun");
            int i=0;
            mReference.child("users").child(userID).child("stat").setValue("online");
            while (true){
                i++;
                Log.i("seveLocation","while i="+i);
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
                            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));*/

                            //Toast.makeText(saveLocation.this,"Latitude:"+location.getLatitude()+"   Longitude:"+location.getLongitude(),Toast.LENGTH_SHORT).show();
                            Log.i("seveLocation","Latitude:"+location.getLatitude()+"   Longitude:"+location.getLongitude());
                            mReference.child("users").child(userID).child("Latitude").setValue(location.getLatitude());
                            mReference.child("users").child(userID).child("Longitude").setValue(location.getLongitude());
                            Log.i("seveLocation","saved");

                        }else{
                            Log.i("seveLocation","not succ");
                        }
                    }
                });

            }
        }

    }


    private static GoogleMap Mmap;
    private static String userID;
    private Thread saveL;
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("seveLocation","serviceCreat");
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this,"thereadStart",Toast.LENGTH_SHORT).show();
        Log.i("seveLocation","serviceStart");
        saveL=new Thread(new theTheread(Mmap,userID));
        saveL.start();
        return START_STICKY;
        //return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {

        saveL.interrupt();
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
}
