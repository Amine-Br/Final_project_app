package com.example.projet_final;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;


import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
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
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private FirebaseAuth mAuth;
    private Button Categories,humberger;
    private DrawerLayout drawer;
    private static boolean mPermissions=false;
    private static final String[]  permissions={Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION};
    private FusedLocationProviderClient mFusedLocationClient;
    private NavigationView navigationView;
    private Intent saveL;
    private DatabaseReference mReference;
    private ArrayList<User> users;
    private ArrayList<MarkerOptions> markers;
    private Dialog dialog;
    private String flter="no_filter";
    private PopupMenu menu;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        //init all vars
        init_objects();
        mAuth = FirebaseAuth.getInstance();
        //categories button menus
        categories_click();
        //button of navigation drawer
        button_drawer_menu();
        //navigation drawer items
        nav_view_items_actions();

        //ArratList
        users= new ArrayList<>();

        //change menu if user login/logout
        change_menu();

        mFusedLocationClient= LocationServices.getFusedLocationProviderClient(this);

        //map
        if(checkServeur()){
            checkPermissions();
        }

        mReference=FirebaseDatabase.getInstance().getReference("users");
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                users.clear();
                ArrayList <String> IDs=new ArrayList<>();
                for (DataSnapshot ID:dataSnapshot.getChildren()){
                    IDs.add(ID.getKey());
                    User user=ID.getValue(User.class);
                    users.add(user);
                };
                addMarkers();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        menuRedy();


        //d=new Dialog(this);

    }

    private void menuRedy() {
        menu=new PopupMenu(MapsActivity.this,Categories);
        menu.getMenuInflater().inflate(R.menu.categories_menu,menu.getMenu());
        menu.getMenu().findItem(R.id.no_filter).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                flter="no_filter";
                addMarkers();
                return false;
            }
        });

        menu.getMenu().findItem(R.id.Plumber).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                flter="plumber";
                addMarkers();
                return false;
            }
        });

        menu.getMenu().findItem(R.id.Electrician).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                flter="electrician";
                addMarkers();
                return false;
            }
        });

        menu.getMenu().findItem(R.id.House_painter).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                flter="House_painter";
                addMarkers();
                return false;
            }
        });

        menu.getMenu().findItem(R.id.Builder).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                flter="Builder";
                addMarkers();
                return false;
            }
        });

        menu.getMenu().findItem(R.id.Air_conditioner).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                flter="air_conditioner";
                addMarkers();
                return false;
            }
        });

        menu.getMenu().findItem(R.id.Gardening).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                flter="gardening";
                addMarkers();
                return false;
            }
        });

        menu.getMenu().findItem(R.id.House_work).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                flter="housework";
                addMarkers();
                return false;
            }
        });

        menu.getMenu().findItem(R.id.Moving).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                flter="Moving";
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

    public void initMap(){
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near User Location
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
        Toast.makeText(this,"mapReady",Toast.LENGTH_SHORT).show();
        getLastKnownLocation();
    }


    private void addMarkers() {
        mMap.clear();
        mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
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
                    mMap.setMyLocationEnabled(true);


                }
            }
        });
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


    public void getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if(mAuth.getCurrentUser()!=null){
            Toast.makeText(this,"have User",Toast.LENGTH_SHORT).show();
            Log.i("seveLocation","have user");
            saveLocation.setMmap(mMap);
            saveLocation.setUserID(mAuth.getCurrentUser().getUid());
            saveL=new Intent(this, com.example.projet_final.saveLocation.class);
            startService(saveL);
        }else{
            Log.i("seveLocation","no user");
            Toast.makeText(this,"no User",Toast.LENGTH_SHORT).show();

        }



    }


    private void checkPermissions() {

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),permissions[0])== PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this.getApplicationContext(),permissions[1])== PackageManager.PERMISSION_GRANTED ){
            mPermissions= true;
            initMap();
        }else{
            ActivityCompat.requestPermissions(this,permissions,99);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.i("Permission","result:start");
        mPermissions=false;
        switch (requestCode){
            case 99:{
                for (int i=0;i<grantResults.length;i++){
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED){
                        mPermissions=false;
                        Log.i("Permission","result:not Accept");
                        return;
                    }
                }
                Log.i("Permission","result:Accept");
                mPermissions=true;
                initMap();

            }
        }
    }

    public boolean checkServeur(){
        int available= GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MapsActivity.this);
        if(available== ConnectionResult.SUCCESS){
            Log.i("serverOk","case1");
            return true;
        }else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            Log.i("serverOk","case2");
            Dialog dialog=GoogleApiAvailability.getInstance().getErrorDialog(MapsActivity.this,available,9001);
        }else{
            Log.i("serverOk","case3");
        }
        return false;
    }
    public  void init_objects(){

        Categories=(Button)findViewById(R.id.Category_button);
        humberger = (Button) findViewById(R.id.humberger_button);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);


    }
    public void categories_click(){
        Categories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*PopupMenu p=new PopupMenu(MapsActivity.this,Categories);
                p.getMenuInflater().inflate(R.menu.categories_menu,p.getMenu());
                p.show();*/
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
    public void nav_view_items_actions(){

       /* navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                        switch (item.getItemId()) {
                    case R.id.login_item:
                       startActivity(new Intent(".MainActivity"));
                        break;

                    case R.id.signup_item:

                        startActivity(new Intent(".sign_up"));
                        break;

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
                return true;
            }
        }


        );

        */
    }
    public void change_menu(){

        if(mAuth.getCurrentUser() == null){
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.drawer_menu);

            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                                                                 @Override
                 public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                             switch (item.getItemId()) {
                                     case R.id.login_item:
                                          startActivity(new Intent(".MainActivity"));
                                          break;

                                     case R.id.signup_item:

                                          startActivity(new Intent(".sign_up"));
                                          break;

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
                            stopService(saveL);
                            mAuth.signOut();
                            //recreate();
                            Intent i=getIntent();
                            finish();
                            startActivity(i);
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

        }

    }

    @Override
    protected void onDestroy() {
        if(mAuth.getCurrentUser()!=null){
            stopService(saveL);
        }
        super.onDestroy();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        /*AlertDialog.Builder builder=new AlertDialog.Builder(MapsActivity.this);
        final View mView=getLayoutInflater().inflate(R.layout.activity_popup,null);
        builder.setView(mView);
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
        return false; */
        int tag=(Integer)marker.getTag();
        dialog=new Dialog(this);
        //View mView=getLayoutInflater().inflate(R.layout.activity_popup,null);
        dialog.setContentView(R.layout.activity_popup);
        TextView t=dialog.findViewById(R.id.username_popup);
        t.setText(users.get(tag).getUser_name());
        t=(TextView) dialog.findViewById(R.id.user_phone_popup);
        t.setText(users.get(tag).getPhone());
        t=(TextView)dialog.findViewById(R.id.user_mail_popup);
        t.setText(users.get(tag).getEmail());
        t=(TextView)dialog.findViewById(R.id.user_jobs_popup);
        t.setText(users.get(tag).getPhone());
        dialog.show();
        return false;

    }
}
