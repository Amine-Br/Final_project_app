package com.example.projet_final;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

public class Profile extends AppCompatActivity {
    private Button update;
    private String userID;
    private User user;
    private Uri s;



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
        update=findViewById(R.id.Update_account);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Profile.this,update_profile.class);
                startActivity(intent);
            }
        });
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
                user_jobs.setText(user.getJobsString());
                user_name.setText(user.getUser_name());
                user_phone.setText(user.getPhone());
                Bitmap bitmap=user.getIconeBitmap();
                //user_image.setImageBitmap(bitmap);
                final Uri[] s = new Uri[1];
                StorageReference storageReference= FirebaseStorage.getInstance().getReference()
                        .child("default").child("default_men_img.png");
                storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        s[0] =task.getResult();
                        Picasso.get().load(s[0]).into(user_image);
                    }
                });
                //Picasso.get().load(user.getUri()).into(user_image);
                }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }




    @Override
    protected void onStart() {

        super.onStart();
    }
}
