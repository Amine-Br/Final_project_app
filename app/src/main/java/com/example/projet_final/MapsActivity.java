package com.example.projet_final;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;


import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    FirebaseAuth mAuth;
    Button Categories;
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private static boolean mPermissions=false;
    private static final String[]  permissions={Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION};
    private FusedLocationProviderClient mFusedLocationClient;
    private NavigationView navigationView;




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
        build_button_drawer_mneu();
        //navigation drawer items
        nav_view_items_actions();

        //change menu if user login/logout
        change_menu();

        mFusedLocationClient= LocationServices.getFusedLocationProviderClient(this);

        //map
        if(checkServeur()){
            checkPermissions();
        }





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
        getLastKnownLocation();
    }
    public void getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful()) {
                    Location location = task.getResult();
                    LatLng user_location = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(user_location).title("Marker in your location"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(user_location));
                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(user_location)      // Sets the center of the map to user_location
                            .zoom(15)                   // Sets the zoom to 15 (city zoom)
                            .build();                   // Creates a CameraPosition from the builder
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


                }
            }
        });

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
        toolbar = findViewById(R.id.toolbar);
        drawer = findViewById(R.id.drawer_layout);
         navigationView = findViewById(R.id.nav_view);
    }
    public void categories_click(){
        Categories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu p=new PopupMenu(MapsActivity.this,Categories);
                p.getMenuInflater().inflate(R.menu.categories_menu,p.getMenu());
                p.show();
            }
        });
    }
    public void build_button_drawer_mneu(){

            setSupportActionBar(toolbar);


            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                    R.string.nav_draw_open, R.string.nav_draw_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

    }
    public void nav_view_items_actions(){

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
                    case R.id.lang_item:
                        Toast.makeText(getApplicationContext(),"...", Toast.LENGTH_SHORT)
                                .show();
                        break;
                }

                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

    }
    public void change_menu(){

        if(mAuth.getCurrentUser() == null){
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.drawer_menu);
        }else{
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.categories_menu);
        }

    }


}
