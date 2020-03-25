package com.example.projet_final;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;


import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    FirebaseAuth mAuth;
    Button Categories;
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private static boolean mPermissions=false;
    private static final String[]  permissions={Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //menu
        Categories=(Button)findViewById(R.id.Category_button);
        Categories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu p=new PopupMenu(MapsActivity.this,Categories);
                p.getMenuInflater().inflate(R.menu.categories_menu,p.getMenu());
                p.show();
            }
        });
        //button of navigation drawer
         toolbar = findViewById(R.id.toolbar);
         setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.nav_draw_open, R.string.nav_draw_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
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
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng Tipaza = new LatLng(36.592606, 2.442341);
        mMap.addMarker(new MarkerOptions().position(Tipaza).title("Marker in Tipaza"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Tipaza));
    }


    private void checkPermissions() {

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),permissions[0])== PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this.getApplicationContext(),permissions[1])== PackageManager.PERMISSION_GRANTED ){
            mPermissions= true;
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

}
