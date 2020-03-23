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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    EditText email_phone,pass;
    Button signin;
    TextView signup;
    FirebaseAuth mAuth  ;
    FirebaseUser currentUser;
    FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i("AntivetyLife","onCreat:start");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        email_phone=(EditText)findViewById(R.id.em_ph_et);
        pass=(EditText)findViewById(R.id.pass_et);
        signin=(Button)findViewById(R.id.button_sign_in);
        signup=(TextView)findViewById(R.id.sign_up_tv);
        mAuth=FirebaseAuth.getInstance();
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

        /*signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email_phone.getText().toString().isEmpty()){
                    //email vide
                }else{
                    if(!email_phone.getText().toString().trim().matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")){
                        //email invalide
                    }else{
                        String email= email_phone.getText().toString();
                        String psw= pass.getText().toString();
                        mAuth.signInWithEmailAndPassword(email,psw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
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
