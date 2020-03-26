package com.example.projet_final;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    EditText email_phone,pass;
    Button signin;
    TextView signup;
    FirebaseAuth mAuth  ;
    FirebaseUser currentUser;
    FirebaseAuth.AuthStateListener mAuthListener;
    DatabaseReference mReference;
    String password;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i("AntivetyLife","onCreat:start");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        email_phone=(EditText)findViewById(R.id.em_et);
        pass=(EditText)findViewById(R.id.pass_et);
        signin=(Button)findViewById(R.id.button_sign_in);
        signup=(TextView)findViewById(R.id.sign_up_tv);
        mAuth=FirebaseAuth.getInstance();
        mReference= FirebaseDatabase.getInstance().getReference();
        mAuthListener=new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                currentUser= mAuth.getCurrentUser();
                if(currentUser != null){
                    startActivity(new Intent(MainActivity.this,MapsActivity.class));
                    finish();
                }
            }
        };

        clicksignup();
        clicksignin();
        Log.i("AntivetyLife","onCreat:end");
    }
    public void clicksignup(){
       Log.i("SignUp","start");
       signup.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent i = new Intent(".sign_up");
               startActivity(i);
           }
       });
        Log.i("SignUp","end");
    }



    public void clicksignin(){

       /* signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email_phone.getText().toString().isEmpty()){
                    email_phone.setError("plz enter you phone or email");

                }else{
                    if(!email_phone.getText().toString().trim().matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")){
                        log_in_with_phone(email_phone.getText().toString());
                    }else{
                        mReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                try{
                                    String phone=dataSnapshot.child(email_phone.getText().toString()).getValue(String.class);
                                    log_in_with_phone(phone);
                                }catch (Exception f){
                                        email_phone.setError("email or phone are incorect");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        /*mAuth.signInWithEmailAndPassword(email,psw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    startActivity(new Intent(MainActivity.this,MapsActivity.class));
                                    finish();
                                }else{
                                    Toast.makeText(MainActivity.this,"Email or password is invalid",Toast.LENGTH_SHORT);
                                }
                            }
                        });
                    }
                }


            }
        });*/
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,MapsActivity.class));
            }
        });



    }
    private void log_in_with_phone(String s){
        if(isNumber(s)){
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential("12345", "12345");
            mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()){

                            mReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    password=dataSnapshot.child(mAuth.getCurrentUser().getUid()).child("password").getValue(String.class);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            if(password.equals(pass.getText().toString())){
                                startActivity(new Intent(MainActivity.this,MapsActivity.class));
                                finish();
                            }else{
                                mAuth.signOut();
                                pass.setError("password incorect");
                            }

                    }else{
                        email_phone.setError("email or phone is incorect");
                    }
                }
            });
        }
    }

    private boolean isNumber(String toString) {
            try{
                Integer.parseInt(toString);
                return true;
            }catch (Exception e){
                return false;
            }
    }

    @Override
    protected void onStart() {
        Log.i("AntivetyLife","onStart:start");
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        Log.i("AntivetyLife","onStart:end");
    }

    @Override
    protected void onStop() {
        Log.i("AntivetyLife","onStop:start");
        super.onStop();
        currentUser= mAuth.getCurrentUser();
        if(currentUser != null){
            startActivity(new Intent(MainActivity.this,MapsActivity.class));
            finish();
        }
        mAuth.removeAuthStateListener(mAuthListener);

        Log.i("AntivetyLife","onStop:start");
    }

    @Override
    protected void onResume() {
        Log.i("AntivetyLife","onResume:start");
        super.onResume();
        Log.i("AntivetyLife","onResume:end");
    }

    @Override
    protected void onPause() {
        Log.i("AntivetyLife","onPause:start");
        super.onPause();
        Log.i("AntivetyLife","onPause:end");
    }

    @Override
    protected void onDestroy() {
        Log.i("AntivetyLife","onDestroy:start");
        super.onDestroy();
        Log.i("AntivetyLife","onDestroy:end");
    }
}
