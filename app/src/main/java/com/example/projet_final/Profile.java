package com.example.projet_final;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
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

public class Profile extends AppCompatActivity {

    private String userID;
    private User user;

    //firebase
    private DatabaseReference mReference;
    private FirebaseAuth mAuth;
    private StorageReference storageReference;

    //view
    private TextView user_name,user_phone,user_jobs,user_email;
    private ImageView user_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        init();
    }
    public void init(){

        //view
        user_name=(TextView) findViewById(R.id.user_name_profile);
        user_email=(TextView) findViewById(R.id.user_mail_profile);
        user_jobs=(TextView) findViewById(R.id.user_jobs_profile);
        user_phone=(TextView) findViewById(R.id.user_phone_profile);
        user_image=(ImageView) findViewById(R.id.user_img_profile);

        //firebase
        mAuth=FirebaseAuth.getInstance();
        userID=mAuth.getCurrentUser().getUid();
        storageReference=FirebaseStorage.getInstance().getReference();
        mReference= FirebaseDatabase.getInstance().getReference().child("users").child(userID);
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user=dataSnapshot.getValue(User.class);
                user_email.setText(user.getEmail());
                user_jobs.setText("jobs .....");
                user_name.setText(user.getUser_name());
                user_phone.setText(user.getPhone());
                Bitmap bitmap=user.getIconeBitmap();
                if(bitmap==null){
                    Toast.makeText(Profile.this,"bitmap ==null", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(Profile.this,"bitmap !=null", Toast.LENGTH_SHORT).show();
                }
                user_image.setImageBitmap(bitmap);
                }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
