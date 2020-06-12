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
import android.app.ActivityManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private StorageReference storageReference;
    private FirebaseAuth mAuth;
    private Button Categories, humberger, lunch_service_button,signin,signup;
    private DrawerLayout drawer;
    private static boolean mPermissions = false;
    private static final String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    private FusedLocationProviderClient mFusedLocationClient;
    private NavigationView navigationView;
    private Intent saveL;
    private DatabaseReference mReference;
    private ArrayList<User> users;
    private Dialog dialog,d;
    private String flter = "no_filter";
    private PopupMenu menu;
    private SupportMapFragment supportMapFragment;
    private ArrayList<String> IDs;
    private User user;
    private File file;
    private Drawable drawable;
    private Bitmap bitmap;
    private String sendTo="global";
    private ImageView nvimg;
    private int tag;
    private View header;
    private Menu nav_menu ;
    static String lang="en";
    private TextView t1,t;
    private Spinner spinner;
    private LatLng user_location;
    private User corentUser;

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
        loadlang();
        change_language();

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
        navigationView.setItemIconTintList(null);
        nav_menu=navigationView.getMenu();
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        lunch_service_button = (Button) findViewById(R.id.service_req_button);
        menu = new PopupMenu(MapsActivity.this, Categories);
        nvimg=findViewById(R.id.nav_view_img);


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

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if(location != null){

                         user_location = new LatLng(location.getLatitude(), location.getLongitude());


                        mMap.moveCamera(CameraUpdateFactory.newLatLng(user_location));
                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(user_location)      // Sets the center of the map to user_location
                                .zoom(10)                   // Sets the zoom to 15 (city zoom)
                                .build();                   // Creates a CameraPosition from the builder
                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


                    }
                }
            });
            mMap.setMyLocationEnabled(true);
        }


        service();
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

                final File file[] = new File[1];
                StorageReference storageReference= FirebaseStorage.getInstance().getReference()
                        .child("users_photo").child(users.get(i).getIcone());

                try {
                    file[0]=File.createTempFile("image","png");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                final Marker finalM = m;
                storageReference.getFile(file[0]).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Log.i("bitmap","secc");
                        bitmap= BitmapFactory.decodeFile(file[0].getAbsolutePath());
                        Bitmap newbitmap=getResizedBitmap(bitmap,25,25);
                        finalM.setIcon(BitmapDescriptorFactory.fromBitmap(newbitmap));
                    }
                });

                m.setTag(i);

            }
        }

    }

    public void service() {
        saveL=new Intent(this, com.example.projet_final.saveLocation.class);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if(LogStat()==2){
            Intent intent=new Intent(MapsActivity.this,UserService.class);
            intent.putExtra("userID",mAuth.getCurrentUser().getUid());
            startService(intent);
        }
        if(LogStat()==3){
            Intent intent=new Intent(MapsActivity.this,UserService.class);
            intent.putExtra("userID",mAuth.getCurrentUser().getUid());
            startService(intent);
            Intent intent2=new Intent(MapsActivity.this,WorkerService.class);
            intent2.putExtra("workerID",mAuth.getCurrentUser().getUid());
            startService(intent2);
        }
        if(LogStat()==3){
            saveLocation.setMmap(mMap);
            saveLocation.setUserID(mAuth.getCurrentUser().getUid());
           // startService(saveL);
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
                change_menu_lang();
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

        if(LogStat()<=2){
            nvimg.setVisibility(View.VISIBLE);
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

            header = navigationView.getHeaderView(0);

            signin=header.findViewById(R.id.drawer_h_signin);
            signup=header.findViewById(R.id.drawer_h_signup);
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
            nvimg.setVisibility(View.GONE);

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
                        case R.id.task_item:
                            startActivity(new Intent(MapsActivity.this,tasks.class));
                            break;


                    }
                    drawer.closeDrawer(GravityCompat.START);
                    return false;
                }
            });
            header = navigationView.getHeaderView(0);
            mReference.child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    corentUser=dataSnapshot.getValue(User.class);
                    final Uri[] s = new Uri[1];
                    StorageReference storageReference= FirebaseStorage.getInstance().getReferenceFromUrl("gs://finalprojectapp-153c6.appspot.com/")
                            .child("users_photo").child(corentUser.getIcone());

                    storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            s[0] =task.getResult();
                            Picasso.get().load(s[0]).into((ImageView) header.findViewById(R.id.user_img_update_profile));
                            TextView t=header.findViewById(R.id.drawer_h_tv2);
                            t.setText(corentUser.getUser_name());
                        }
                    });
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            ConstraintLayout user=header.findViewById(R.id.user_inteface);
            ConstraintLayout worker=header.findViewById(R.id.worker_inteface);
            user.setVisibility(View.GONE);
            worker.setVisibility(View.VISIBLE);

        }

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Log.i("MapsActivity","merker clicked");
        tag=(Integer)marker.getTag();
        sendTo=IDs.get(tag);
        dialog=new Dialog(this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.activity_popup);
        t=dialog.findViewById(R.id.username_popup);
        t.setText(users.get(tag).getUser_name());
        /*t=(TextView) dialog.findViewById(R.id.user_phone_popup);
        t.setText(users.get(tag).getPhone());*/
        t=dialog.findViewById(R.id.email_tv2);
        t.setText(users.get(tag).getEmail());
        t=dialog.findViewById(R.id.jobs_tv2);
        t.setText(users.get(tag).getJobsString());
        t=(TextView)dialog.findViewById(R.id.sex_tv2);
        t.setText(users.get(tag).getSex());
        t=(TextView)dialog.findViewById(R.id.birthday_tv2);
        t.setText(users.get(tag).getBirthday());
        TextView Call,SMS;
        final Uri[] s = new Uri[1];
        StorageReference storageReference= FirebaseStorage.getInstance().getReferenceFromUrl("gs://finalprojectapp-153c6.appspot.com/")
                .child("users_photo").child(user.getIcone());

        storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                s[0] =task.getResult();
                Picasso.get().load(s[0]).into((ImageView) dialog.findViewById(R.id.user_image));
            }
        });
        Call=dialog.findViewById(R.id.task_tv);
        Call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Intent.ACTION_DIAL);
                String s="tel:"+users.get(tag).getPhone();
                i.setData(Uri.parse(s));
                startActivity(i);
            }
        });
        SMS=dialog.findViewById(R.id.SMS_button);
        SMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i=new Intent(Intent.ACTION_VIEW,Uri.fromParts("sms",users.get(tag).getPhone(),null));
                startActivity(i);
            }
        });



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
        change_dialog_lang();
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
        d=new Dialog(MapsActivity.this);
        d.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        d.setContentView(R.layout.request_popup);
        spinner=d.findViewById(R.id.spinner);
        if(MapsActivity.lang.equals("en")) {
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MapsActivity.this,
                    android.R.layout.simple_spinner_item, new String[]{"plumber", "electrician"
                    , "House_painter", "Builder", "air_conditioner", "gardening", "housework", "Moving"});
            spinner.setAdapter(adapter);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        }else{
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MapsActivity.this,
                    android.R.layout.simple_spinner_item, new String[]{"plombier", "électricien"
                    , "Peintre en bâtiment", "constructeur", "climatisation", "jardinage", "travaux ménagers", "Déplacement"});
            spinner.setAdapter(adapter);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        }

        if (!sendTo.equals("global")) spinner.setEnabled(false);
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

                    DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("GlobalReq").push();
                    ref.child("Latitude").setValue(user_location.latitude);
                    ref.child("Longitude").setValue(user_location.longitude);
                    ref.child("job").setValue(spinner.getSelectedItem().toString());
                    t=d.findViewById(R.id.phone_Req);
                    ref.child("phone").setValue(t.getText().toString());
                    t=d.findViewById(R.id.date_Req);
                    ref.child("date").setValue(t.getText().toString());
                    t=d.findViewById(R.id.details_Req);
                    ref.child("details").setValue(t.getText().toString());
                    ref.child("taked").setValue("not yet");
                    ref.child("watched").setValue(false);
                    ref.child("senderID").setValue(mAuth.getCurrentUser().getUid());
                    ref.child("accepterID").setValue("no one");
                }else{

                    DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("spesfReq").child(sendTo).push();
                    ref.child("Latitude").setValue(user_location.latitude);
                    ref.child("Longitude").setValue(user_location.longitude);
                    ref.child("job").setValue("no job");
                    t=d.findViewById(R.id.phone_Req);
                    ref.child("phone").setValue(t.getText().toString());
                    t=d.findViewById(R.id.date_Req);
                    String date=t.getText().toString();
                    ref.child("date").setValue(date);
                    t=d.findViewById(R.id.details_Req);
                    ref.child("details").setValue(t.getText().toString());
                    ref.child("taked").setValue("not yet");
                    ref.child("watched").setValue(false);
                    ref.child("senderID").setValue(mAuth.getCurrentUser().getUid());
                    ref.child("accepterID").setValue("no one");

                }
            }
        });
        change_d_lang();
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
            mAuth.signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(MapsActivity.this, UserService.class);
                        intent.putExtra("userID", mAuth.getCurrentUser().getUid());
                        startService(intent);
                    }
                }
            });


        }


    }

    @Override
    protected void onResume() {
        Log.i("MapsActivity","onResume");

        super.onResume();

        change_language();

    }

    @Override
    protected void onStop() {
        savelang();
        Log.i("MapsActivity","onStop");
        super.onStop();

    }

    @Override
    protected void onPause() {
        savelang();
        Log.i("MapsActivity","onPause");
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        savelang();

        Log.i("MapsActivity","onDestroy");

        if(isMyServiceRunning(saveLocation.class)){
            Intent intent= new Intent(MapsActivity.this,saveLocation.class);
            stopService(intent);
        }
        if(isMyServiceRunning(WorkerService.class)){
            Intent intent= new Intent(MapsActivity.this,WorkerService.class);
            stopService(intent);
        }
        if(isMyServiceRunning(UserService.class)){
            Intent intent= new Intent(MapsActivity.this,UserService.class);
            stopService(intent);
        }
        super.onDestroy();
    }
    public void change_menu_lang(){
        switch (MapsActivity.lang){


            case "en":


                menu.getMenu().findItem(R.id.no_filter).setTitle("cancel filter");

                menu.getMenu().findItem(R.id.Plumber).setTitle("plumber");

                menu.getMenu().findItem(R.id.Electrician).setTitle("electrician");

                menu.getMenu().findItem(R.id.House_painter).setTitle("House_painter");

                menu.getMenu().findItem(R.id.Builder).setTitle("Builder");

                menu.getMenu().findItem(R.id.Air_conditioner).setTitle("air_conditioner");

                menu.getMenu().findItem(R.id.Gardening).setTitle("gardening");

                menu.getMenu().findItem(R.id.House_work).setTitle("housework");

                menu.getMenu().findItem(R.id.Moving).setTitle("Moving");


                break;
            case "fr":
                menu.getMenu().findItem(R.id.no_filter).setTitle("annuler le filtre");

                menu.getMenu().findItem(R.id.Plumber).setTitle("plombier");

                menu.getMenu().findItem(R.id.Electrician).setTitle("électricien");

                menu.getMenu().findItem(R.id.House_painter).setTitle("Peintre en bâtiment");

                menu.getMenu().findItem(R.id.Builder).setTitle("constructeur");

                menu.getMenu().findItem(R.id.Air_conditioner).setTitle("climatisation");

                menu.getMenu().findItem(R.id.Gardening).setTitle("jardinage");

                menu.getMenu().findItem(R.id.House_work).setTitle("travaux ménagers");

                menu.getMenu().findItem(R.id.Moving).setTitle("Déplacement");
                break;
        }


    }
    public void change_d_lang(){
        switch (MapsActivity.lang){


            case "en":

                t=d.findViewById(R.id.phone_num);
                t.setText("Phone number:");
                t=d.findViewById(R.id.service_type);
                t.setText("Type of service::");
                t=d.findViewById(R.id.date_service);
                t.setText("Date:");
                t=d.findViewById(R.id.cancel_button_req);
                t.setText("Cancel");
                t=d.findViewById(R.id.send_request_button);
                t.setText("Send");


                break;
            case "fr":

                t = d.findViewById (R.id.phone_num);
                t.setText ("Numéro de téléphone:");
                t = d.findViewById (R.id.service_type);
                t.setText ("Type de service :");
                t = d.findViewById (R.id.date_service);
                t.setText ("Date:");
                t = d.findViewById (R.id.cancel_button_req);
                t.setText ("Annuler");
                t = d.findViewById (R.id.send_request_button);
                t.setText ("Envoyer");
                break;
        }

    }
    public  void change_dialog_lang(){
        switch (MapsActivity.lang){


            case "en":

                t=dialog.findViewById(R.id.sex_tv);
                t.setText("SEX:");
                t=dialog.findViewById(R.id.birthday_tv);
                t.setText("Birthday:");
                t=dialog.findViewById(R.id.jobs_tv);
                t.setText("Jobs:");
                t=dialog.findViewById(R.id.cancel_button);
                t.setText("cancel");
                t=dialog.findViewById(R.id.prechedule_bitton);
                t.setText("request");
                t=dialog.findViewById(R.id.task_tv);
                t.setText("Call");

                break;
            case "fr":

                t1 = dialog.findViewById(R.id.sex_tv);
                t1.setText ("SEXE:");
                t1 = dialog.findViewById(R.id.birthday_tv);
                t1.setText ("date de naissance:");
                t1 = dialog.findViewById(R.id.jobs_tv);
                t1.setText ("Travaux:");
                t1 = dialog.findViewById (R.id.cancel_button);
                t1.setText ("annuler");
                t1 = dialog.findViewById (R.id.prechedule_bitton);
                t1.setText ("demande");
                t1 = dialog.findViewById (R.id.task_tv);
                t1.setText ("Appel");
                break;
        }
    }
    public  void change_language(){
        switch (MapsActivity.lang){


            case "en":

                lunch_service_button.setText("SERVICE REQUEST");
                if(LogStat()<=2) {
                    signin.setText("SIGN IN");
                    signup.setText("SIGN UP");
                    nav_menu.findItem(R.id.change_lang_item).setTitle("Change Language");
                    nav_menu.findItem(R.id.about_us_item).setTitle("About Us");
                    nav_menu.findItem(R.id.Rate_us_item).setTitle("Rate Us");
                }else {
                    nav_menu.findItem(R.id.change_lang_item).setTitle("Changer de langue");
                    nav_menu.findItem(R.id.about_us_item).setTitle("À propos de nous");
                    nav_menu.findItem(R.id.Rate_us_item).setTitle("Évaluez nous");
                    nav_menu.findItem(R.id.Profile_item).setTitle("Profile");
                    nav_menu.findItem(R.id.sign_out_item).setTitle("Sign out");
                    nav_menu.findItem(R.id.task_item).setTitle("Tasks");
                }



                break;
            case "fr":

                lunch_service_button.setText ("DEMANDE DE SERVICE");
                if(LogStat()<=2) {
                    signin.setText ("SE CONNECTER");
                    signup.setText ("INSCRIPTION");
                    nav_menu.findItem(R.id.change_lang_item).setTitle("Changer de langue");
                    nav_menu.findItem(R.id.about_us_item).setTitle("À propos de nous");
                    nav_menu.findItem(R.id.Rate_us_item).setTitle("Évaluez nous");
                }else{
                    nav_menu.findItem(R.id.change_lang_item).setTitle("Changer de langue");
                    nav_menu.findItem(R.id.about_us_item).setTitle("À propos de nous");
                    nav_menu.findItem(R.id.Rate_us_item).setTitle("Évaluez nous");
                    nav_menu.findItem(R.id.Profile_item).setTitle("Profil");
                    nav_menu.findItem(R.id.sign_out_item).setTitle("Déconnexion");
                    nav_menu.findItem(R.id.task_item).setTitle("Tâches");
                }

                break;
        }
    }
    public void loadlang(){
        try{
            SharedPreferences sharedPref = getSharedPreferences("My_Languages", Activity.MODE_PRIVATE);
            lang = sharedPref.getString("lang",lang);
        }
        catch (Exception e){
            lang="en";
        }
    }
    void savelang(){
        SharedPreferences sharedPref = getSharedPreferences("My_Languages", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("lang",lang);
        editor.apply();
        //
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }




}
