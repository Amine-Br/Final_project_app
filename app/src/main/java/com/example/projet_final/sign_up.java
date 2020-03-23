package com.example.projet_final;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class sign_up extends AppCompatActivity {
    //View
    private Button signup;
    private EditText name,phone,email,pass,confpass;
    private RadioGroup groupsex;
    private String log_email,log_pass;

    //FireBase
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mReference;
    private FirebaseDatabase mFirebaseDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Log.i("sign_up_create","start");

        //View
        signup=(Button)findViewById(R.id.SU_Button_sign_up);
        name=(EditText)findViewById(R.id.SU_User_Name);
        phone=(EditText)findViewById(R.id.SU_Phone);
        email=(EditText)findViewById(R.id.SU_Email);
        pass=(EditText)findViewById(R.id.SU_Password);
        confpass=(EditText)findViewById(R.id.SU_ConfirmPassword);
        groupsex=(RadioGroup)findViewById(R.id.SU_SexGroup);

        //Firebase
        mAuth= FirebaseAuth.getInstance();
        mFirebaseDatabase=FirebaseDatabase.getInstance();
        mReference= mFirebaseDatabase.getReference();
        /*mAuthListener=new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                //currentUser= mAuth.getCurrentUser();
                if(currentUser != null){
                    String userID=currentUser.getUid();
                    mReference.child(userID).child("user_name").setValue(name.getText().toString());
                    mReference.child(userID).child("phone").setValue(phone.getText().toString());
                    mReference.child(userID).child("birthday").setValue(name.getText().toString());
                    RadioButton sex=(RadioButton) findViewById(groupsex.getCheckedRadioButtonId());
                    mReference.child(userID).child("sex").setValue(sex.getText().toString());
                }
            }
        };*/

        //
        Log.i("sign_up_create","end");
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addData();
            }
        });


    }

    private void addData() {
        Log.i("addData","start");
        if(confirmData()){
            Log.i("addData","confData");
            //Toast.makeText(sign_up.this,"confirmed",Toast.LENGTH_SHORT);
            log_email=email.getText().toString();
            log_pass=pass.getText().toString();
            Log.i("addData","log: email: "+log_email+" \n pass:"+log_pass);
            mAuth.createUserWithEmailAndPassword(log_email,log_pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(!task.isSuccessful()){
                        Log.i("addData","not sucs");
                        //Toast.makeText(sign_up.this,"Sign_Up in Unsuccessful",Toast.LENGTH_SHORT);
                    }else{
                        //Toast.makeText(sign_up.this,"Sign_Up in Successful",Toast.LENGTH_SHORT);!
                        Log.i("addData","sucs");
                        Intent map=new Intent(sign_up.this,MapsActivity.class);
                        mAuth.signInWithEmailAndPassword(log_email,log_pass);
                        currentUser= mAuth.getCurrentUser();
                        saveData();
                        startActivity(map);
                        finish();
                    }
                }
            }
            );


        }else{
            ///Toast.makeText(sign_up.this,"not confirmed",Toast.LENGTH_SHORT);
            Log.i("addData","not confData");
        }
        Log.i("addData","end");
    }


    public boolean confirmData() {
        Log.i("confirmData","confirmData_start");
        if (name.getText().toString().isEmpty()){
            name.setError("Please Enter email");
            //Toast.makeText(sign_up.this,"Please Enter email",Toast.LENGTH_SHORT);
            Log.i("confirmData","name invalid");
            return false;
        }
        Log.i("confirmData","name valid");
        if (phone.getText().toString().isEmpty()){
            //phone.setError("Please Enter phone");
            //Toast.makeText(sign_up.this,"Please Enter phone",Toast.LENGTH_SHORT);
            Log.i("confirmData","phone invalid");
            return false;
        }
        Log.i("confirmData","phone valid");
        if(!email.getText().toString().isEmpty()){
            Log.i("confirmData","mail m3amar");
            if (!email.getText().toString().trim().matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")){
                //email.setError("Please Enter correct email");
                //Toast.makeText(sign_up.this,"Please Enter correct email",Toast.LENGTH_SHORT);
                Log.i("confirmData","mail invalid");
                return false;
            }
        }
        Log.i("confirmData","mail valid");
        if (pass.getText().toString().isEmpty()){
            //pass.setError("Please Enter password");
            //Toast.makeText(sign_up.this,"Please Enter password",Toast.LENGTH_SHORT);
            Log.i("confirmData","pass invalid");
            return false;
        }
        Log.i("confirmData","pass valid");
        if (confpass.getText().toString().isEmpty()){
            if (!pass.getText().toString().isEmpty()){
                //confpass.setError("Please confirm your password");
                //Toast.makeText(sign_up.this,"Please confirm your password",Toast.LENGTH_SHORT);
                Log.i("confirmData","confpass inconf");
                return false;
            }
        }else{
            if (!confpass.getText().toString().equals(pass.getText().toString())){
                //confpass.setError("password and confirm password are not the same");
                //Toast.makeText(sign_up.this,"password and confirm password are not the same",Toast.LENGTH_SHORT);
                Log.i("confirmData","confpass note same");
                return  false;
            }
        }
        Log.i("confirmData","confpass same");

        Log.i("confirmData","sex valid");

        //Toast.makeText(sign_up.this,"data confirmed",Toast.LENGTH_SHORT);
        Log.i("confirmData","confirmData_end");
        return true;
    }


    private void saveData(){
        Log.i("saveData","start");
        String userID=currentUser.getUid();
        Log.i("saveData","UID:"+userID);
        mReference.child(userID).child("user_name").setValue(name.getText().toString());
        mReference.child(userID).child("phone").setValue(phone.getText().toString());
        mReference.child(userID).child("birthday").setValue(name.getText().toString());
        RadioButton sex=(RadioButton) findViewById(groupsex.getCheckedRadioButtonId());
        mReference.child(userID).child("sex").setValue(sex.getText().toString());
        Log.i("saveData","end");
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);;
    }

}
