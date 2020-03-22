package com.example.projet_final;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class sign_up extends AppCompatActivity {

    private Button signup;
    private EditText name,phone,email,pass,confpass;
    private FirebaseAuth mAuth;
    private RadioGroup groupsex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Log.i("sign_up_create","start");
        signup=(Button)findViewById(R.id.SU_Button_sign_up);
        name=(EditText)findViewById(R.id.SU_User_Name);
        phone=(EditText)findViewById(R.id.SU_Phone);
        email=(EditText)findViewById(R.id.SU_Email);
        pass=(EditText)findViewById(R.id.SU_Password);
        confpass=(EditText)findViewById(R.id.SU_ConfirmPassword);
        groupsex=(RadioGroup)findViewById(R.id.SU_SexGroup);
        mAuth= FirebaseAuth.getInstance();
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
            String log_email=email.getText().toString();
            String log_pass=pass.getText().toString();
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
                        startActivity(map);
                    }
                }
            }
            );


        }else{
            //Toast.makeText(sign_up.this,"not confirmed",Toast.LENGTH_SHORT);
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


}
