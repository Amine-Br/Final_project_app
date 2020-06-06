package com.example.projet_final;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;


import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private StorageReference storageReference;
    private FirebaseAuth mAuth;
    private Button Categories, humberger, lunch_service_button;
    private DrawerLayout drawer;
    private static boolean mPermissions = false;
    private static final String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    private FusedLocationProviderClient mFusedLocationClient;
    private NavigationView navigationView;
    private Intent saveL;
    private DatabaseReference mReference;
    private ArrayList<User> users;
    private Dialog dialog;
    private String flter = "no_filter";
    private PopupMenu menu;
    private SupportMapFragment supportMapFragment;
    private ArrayList<String> IDs;
    private User user;
    private File file;
    private Drawable drawable;
    private Bitmap bitmap;
    private String sendTo="global";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Log.i("Activity", "MapsActivity");
        Log.i("MapsActivity", "onCreate");
        init();
        if (checkServeur()) {
            checkPermissions();
        }
        categories_click();
        button_drawer_menu();
        Lunch_service_button_click();

        change_menu();
        menuRedy();
    }

    private void init() {
        Log.i("MapsActivity", "init");

        users = new ArrayList<>();
        IDs = new ArrayList<>();

        //view
        Categories = (Button) findViewById(R.id.Category_button);
        humberger = (Button) findViewById(R.id.humberger_button);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        lunch_service_button = findViewById(R.id.service_req_button);
        menu = new PopupMenu(MapsActivity.this, Categories);

        //firebase
        storageReference = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        mReference = FirebaseDatabase.getInstance().getReference("users");

    }

    private void menuRedy() {
        /*PopupMenu p=new PopupMenu(MapsActivity.this,Categories);
                p.getMenuInflater().inflate(R.menu.categories_menu,p.getMenu());
                p.show();*/

        menu.getMenuInflater().inflate(R.menu.categories_menu, menu.getMenu());
        menu.getMenu().findItem(R.id.no_filter).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                flter = "no_filter";
                addMarkers();
                return false;
            }
        });

        menu.getMenu().findItem(R.id.Plumber).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                flter = "plumber";
                addMarkers();
                return false;
            }
        });

        menu.getMenu().findItem(R.id.Electrician).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                flter = "electrician";
                addMarkers();
                return false;
            }
        });

        menu.getMenu().findItem(R.id.House_painter).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                flter = "House_painter";
                addMarkers();
                return false;
            }
        });

        menu.getMenu().findItem(R.id.Builder).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                flter = "Builder";
                addMarkers();
                return false;
            }
        });

        menu.getMenu().findItem(R.id.Air_conditioner).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                flter = "air_conditioner";
                addMarkers();
                return false;
            }
        });

        menu.getMenu().findItem(R.id.Gardening).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                flter = "gardening";
                addMarkers();
                return false;
            }
        });

        menu.getMenu().findItem(R.id.House_work).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                flter = "housework";
                addMarkers();
                return false;
            }
        });

        menu.getMenu().findItem(R.id.Moving).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                flter = "Moving";
                addMarkers();
                return false;
            }
        });


    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void initMap() {
        Log.i("MapsActivity", "initMap");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.i("MapsActivity", "mapReady");
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                users.clear();
                IDs.clear();

                for (DataSnapshot ID : dataSnapshot.getChildren()) {
                    IDs.add(ID.getKey());
                    user = ID.getValue(User.class);
                    users.add(user);
                }
                ;
                addMarkers();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
                    Location location = task.getResult();
                    LatLng user_location = new LatLng(location.getLatitude(), location.getLongitude());


                    mMap.moveCamera(CameraUpdateFactory.newLatLng(user_location));
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(user_location)      // Sets the center of the map to user_location
                            .zoom(10)                   // Sets the zoom to 15 (city zoom)
                            .build();                   // Creates a CameraPosition from the builder
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                    //mMap.setMyLocationEnabled(true);


                }
            }
        });*/

        serviceLocation();
    }

    private void addMarkers() {
        mMap.clear();
        Marker m;

        HashMap<String,Boolean> Hm;
        for(int i=0;i<users.size();i++){
            Hm=users.get(i).jabs();
            if(Hm.get(flter)) {
                    m = mMap.addMarker(new MarkerOptions()
                            .title(users.get(i).getUser_name())
                            .position(new LatLng(users.get(i).getLatitude(), users.get(i).getLongitude())));

                    m.setTag(i);

            }
        }

    }

    public void serviceLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if(LogStat()==3){
            saveLocation.setMmap(mMap);
            saveLocation.setUserID(mAuth.getCurrentUser().getUid());
            saveL=new Intent(this, com.example.projet_final.saveLocation.class);
            startService(saveL);
        }else{

        }



    }

    private void checkPermissions() {

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),permissions[0])== PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this.getApplicationContext(),permissions[1])== PackageManager.PERMISSION_GRANTED ){

            Log.i("MapsActivity","checked Permission");

            mPermissions= true;
            initMap();
        }else{

            Log.i("MapsActivity","not checked Permission");

            ActivityCompat.requestPermissions(this,permissions,99);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mPermissions=false;
        switch (requestCode){
            case 99:{
                for (int i=0;i<grantResults.length;i++){
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED){
                        mPermissions=false;
                        Log.i("MapsActivity","not checked Permission");
                        return;
                    }
                }
                mPermissions=true;
                Log.i("MapsActivity","checked Permission");
                initMap();

            }
        }
    }

    public boolean checkServeur(){
        int available= GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MapsActivity.this);
        if(available== ConnectionResult.SUCCESS){
            Log.i("MapsActivity","serveur case 1");
            return true;
        }else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            Log.i("MapsActivity","serveur case 2");
            Dialog dialog=GoogleApiAvailability.getInstance().getErrorDialog(MapsActivity.this,available,9001);
        }else{
            Log.i("MapsActivity","serveur case 3");
        }
        return false;
    }

    public void categories_click(){
        Categories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menu.show();
            }
        });
    }

    public void button_drawer_menu(){
        humberger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);

            }
        });

    }

    public void change_menu(){

        if(LogStat()<2){
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.drawer_menu);

            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                                                                 @Override
                 public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                             switch (item.getItemId()) {

                                     case R.id.change_lang_item:
                                          multi_activity.s="language_page";
                                          startActivity(new Intent(MapsActivity.this,multi_activity.class));
                                          break;

                                     case R.id.about_us_item:
                                          multi_activity.s="about_us_page";
                                          startActivity(new Intent(MapsActivity.this,multi_activity.class));
                                          break;

                                     case R.id.Rate_us_item:
                                          multi_activity.s="rate_us_page";
                                          startActivity(new Intent(MapsActivity.this,multi_activity.class));
                                          break;
                                                    }
                             drawer.closeDrawer(GravityCompat.START);
                             return true; }
            }


            );

            View header = navigationView.getHeaderView(0);

            Button signin=header.findViewById(R.id.drawer_h_signin);
            Button signup=header.findViewById(R.id.drawer_h_signup);
            ConstraintLayout user=header.findViewById(R.id.user_inteface);
            ConstraintLayout worker=header.findViewById(R.id.worker_inteface);

            user.setVisibility(View.VISIBLE);
            worker.setVisibility(View.GONE);

            signin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(".MainActivity"));
                }
            });
            signup.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(".sign_up"));
                }
            });

        }else{
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.drawer_menu_two);
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    switch (menuItem.getItemId()){
                        case R.id.Profile_item:
                            startActivity(new Intent(MapsActivity.this,Profile.class));
                            break;
                        case R.id.change_lang_item:
                            multi_activity.s="language_page";
                            startActivity(new Intent(MapsActivity.this,multi_activity.class));
                            break;
                        case R.id.sign_out_item:
                            Toast.makeText(MapsActivity.this,"clicked",Toast.LENGTH_SHORT);
                            Log.i("MapsActivity","sign_out");
                            //stopService(saveL);
                            mAuth.signOut();
                            recreate();
                            break;
                        case R.id.about_us_item:
                            multi_activity.s="about_us_page";
                            startActivity(new Intent(MapsActivity.this,multi_activity.class));
                            break;
                        case R.id.Rate_us_item:
                            multi_activity.s="rate_us_page";
                            startActivity(new Intent(MapsActivity.this,multi_activity.class));
                            break;

                    }
                    drawer.closeDrawer(GravityCompat.START);
                    return false;
                }
            });

            View header = navigationView.getHeaderView(0);


            ConstraintLayout user=header.findViewById(R.id.user_inteface);
            ConstraintLayout worker=header.findViewById(R.id.worker_inteface);

            user.setVisibility(View.GONE);
            worker.setVisibility(View.VISIBLE);

        }

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.i("MapsActivity","merker clicked");
        int tag=(Integer)marker.getTag();
        sendTo=IDs.get(tag);
        dialog=new Dialog(this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.activity_popup);
        TextView t=dialog.findViewById(R.id.username_popup);
        t.setText(users.get(tag).getUser_name());
        /*t=(TextView) dialog.findViewById(R.id.user_phone_popup);
        t.setText(users.get(tag).getPhone());*/
        t=dialog.findViewById(R.id.email_tv2);
        t.setText(users.get(tag).getEmail());
        t=dialog.findViewById(R.id.jobs_tv2);
        t.setText(users.get(tag).getJobsString());
        dialog.findViewById(R.id.cancel_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendTo="global";
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.prechedule_bitton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showReqDialog();
                dialog.dismiss();
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        return false;

    }

    public Bitmap getResizedBitmap(Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        return resizedBitmap;
    }

    private int LogStat(){
        /**
         * 1 null
         * 2 Anonymous (guest)
         * 3 worker
         */
        if(mAuth.getCurrentUser()!=null){
            if(!mAuth.getCurrentUser().isAnonymous()){
                return 3;
            }
            return 2;
        }
        return 1;
    }

    public void Lunch_service_button_click(){
        lunch_service_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showReqDialog();
            }
        });
    }


    private void showReqDialog(){
        final Dialog d=new Dialog(MapsActivity.this);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        d.setContentView(R.layout.request_popup);
        final Spinner spinner=d.findViewById(R.id.spinner);
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(MapsActivity.this,
                android.R.layout.simple_spinner_item,new String[]{"Plumber","Electrician"
                ,"House painter","Builder","Air conditioner","Gardening","House work","Moving"});

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        if (sendTo!="global") spinner.setEnabled(false);
        d.findViewById(R.id.cancel_button_req).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendTo="global";
                d.dismiss();
            }
        });
        d.findViewById(R.id.send_request_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sendTo.equals("global")){
                    TextView t;
                    DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("GlobalReq").push();
                    ref.child("Latitude").setValue(getCourrentLocation().latitude);
                    ref.child("Longitude").setValue(getCourrentLocation().longitude);
                    ref.child("job").setValue(spinner.getSelectedItem().toString());
                    t=d.findViewById(R.id.phone_Req);
                    ref.child("phone").setValue(t.getText().toString());
                    t=d.findViewById(R.id.date_Req);
                    ref.child("date").setValue(t.getText().toString());
                    t=d.findViewById(R.id.details_Req);
                    ref.child("details").setValue(t.getText().toString());
                    ref.child("taked").setValue("not yet");
                    ref.child("watched").setValue(false);
                }else{
                    TextView t;
                    DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("spesfReq").child(sendTo).push();
                    ref.child("Latitude").setValue(getCourrentLocation().latitude);
                    ref.child("Longitude").setValue(getCourrentLocation().longitude);
                    ref.child("job").setValue("no job");
                    t=d.findViewById(R.id.phone_Req);
                    ref.child("phone").setValue(t.getText().toString());
                    t=d.findViewById(R.id.date_Req);
                    ref.child("date").setValue(t.getText().toString());
                    t=d.findViewById(R.id.details_Req);
                    ref.child("details").setValue(t.getText().toString());
                    ref.child("taked").setValue("not yet");
                    ref.child("watched").setValue(false);
                }
            }
        });
        d.setCanceledOnTouchOutside(false);
        d.show();

    }


    public LatLng getCourrentLocation(){
        return new LatLng(36.672496,2.793588);
    }


    //Activity lifecycle

    @Override
    protected void onStart() {
        Log.i("MapsActivity","onStart");
        super.onStart();
        if(LogStat()==1){
            mAuth.signInAnonymously();
        }
    }

    @Override
    protected void onResume() {
        Log.i("MapsActivity","onResume");

        super.onResume();
        if(mMap==null){
            Log.i("MapsActivity","mMap=null");
            supportMapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    mMap=googleMap;
                }
            });
        }

    }

    @Override
    protected void onStop() {
        Log.i("MapsActivity","onStop");
        super.onStop();

    }

    @Override
    protected void onPause() {
        Log.i("MapsActivity","onPause");
        super.onPause();
    }

    @Override
    protected void onDestroy() {

        Log.i("MapsActivity","onDestroy");

        if(LogStat()==3){
            stopService(saveL);

        }
        super.onDestroy();
    }
}
