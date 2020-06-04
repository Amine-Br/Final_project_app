package com.example.projet_final;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class update_profile extends AppCompatActivity {
    private ImageView user_img_update_profile;
    private EditText change_username,change_birthday;
    private CheckBox update_profile_CB_plumber,update_profile_CB_electrician,update_profile_CB_House_painter
            ,update_profile_CB_Builder,update_profile_CB_air_conditioner,update_profile_CB_gardening,update_profile_CB_housework
            ,update_profile_CB_Moving;
    private RadioButton Male,Female;
    private Button Update_profile_Cancel_button,Update_profile_Confirm_button;
    //firebase
    private DatabaseReference mReference;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        init();
        init_edit();
        button_click();
    }



    private void button_click() {
        Update_profile_Cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(update_profile.this,Profile.class);
                startActivity(intent);
                finish();

            }
        });
        Update_profile_Confirm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mReference.child("user_name").setValue(change_username.getText().toString());
                mReference.child("birthday").setValue(change_birthday.getText().toString());
                if(Male.isChecked()){
                    mReference.child("sex").setValue("Male");
                }else{
                    mReference.child("sex").setValue("Female");
                }
                mReference.child("Builder").setValue(update_profile_CB_Builder.isChecked());
                mReference.child("House_painter").setValue(update_profile_CB_House_painter.isChecked());
                mReference.child("Moving").setValue(update_profile_CB_Moving.isChecked());
                mReference.child("air_conditioner").setValue(update_profile_CB_air_conditioner.isChecked());
                mReference.child("electrician").setValue(update_profile_CB_electrician.isChecked());
                mReference.child("gardening").setValue(update_profile_CB_gardening.isChecked());
                mReference.child("housework").setValue(update_profile_CB_housework.isChecked());
                mReference.child("plumber").setValue(update_profile_CB_plumber.isChecked());
                Toast.makeText(update_profile.this,"your profile is updated",Toast.LENGTH_LONG).show();
                Intent intent=new Intent(update_profile.this,Profile.class);
                startActivity(intent);
                finish();


            }
        });
    }

    private void init() {
        user_img_update_profile=findViewById(R.id.user_img_update_profile);
        change_username=findViewById(R.id.change_username);
        change_birthday=findViewById(R.id.change_birthday);
        Male=findViewById(R.id.Male);
        Female=findViewById(R.id.Female);
        update_profile_CB_plumber=findViewById(R.id.update_profile_CB_plumber);
        update_profile_CB_electrician=findViewById(R.id.update_profile_CB_electrician);
        update_profile_CB_House_painter=findViewById(R.id.update_profile_CB_House_painter);
        update_profile_CB_Builder=findViewById(R.id.update_profile_CB_Builder);
        update_profile_CB_air_conditioner=findViewById(R.id.update_profile_CB_air_conditioner);
        update_profile_CB_gardening=findViewById(R.id.update_profile_CB_gardening);
        update_profile_CB_housework=findViewById(R.id.update_profile_CB_housework);
        update_profile_CB_Moving=findViewById(R.id.update_profile_CB_Moving);
        Update_profile_Cancel_button=findViewById(R.id.Update_profile_Cancel_button);
        Update_profile_Confirm_button=findViewById(R.id.Update_profile_Confirm_button);

        //firebase
        mAuth=FirebaseAuth.getInstance();
        mReference = FirebaseDatabase.getInstance().getReference("users").child(mAuth.getCurrentUser().getUid());

    }

    private void init_edit() {
        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user=dataSnapshot.getValue(User.class);
                change_username.setText(user.getUser_name());
                change_birthday.setText(user.getBirthday());
                if(user.getSex().equals("Male")){
                    Male.setChecked(true);
                }else{
                    Female.setChecked(true);
                }
                update_profile_CB_plumber.setChecked(user.isPlumber());
                update_profile_CB_electrician.setChecked(user.isElectrician());
                update_profile_CB_House_painter.setChecked(user.isHouse_painter());
                update_profile_CB_Builder.setChecked(user.isBuilder());
                update_profile_CB_air_conditioner.setChecked(user.isAir_conditioner());
                update_profile_CB_gardening.setChecked(user.isGardening());
                update_profile_CB_housework.setChecked(user.isHousework());
                update_profile_CB_Moving.setChecked(user.isMoving());
                final Uri[] s = new Uri[1];
                StorageReference storageReference= FirebaseStorage.getInstance().getReference()
                        .child("default").child("default_men_img.png");
                storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        s[0] =task.getResult();
                        Picasso.get().load(s[0]).into(user_img_update_profile);
                    }
                });
                /*if(user.getUri()==null){
                    Toast.makeText(update_profile.this," uri is null",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(update_profile.this," uri is not null",Toast.LENGTH_LONG).show();
                }*/

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
