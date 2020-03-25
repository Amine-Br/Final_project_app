package com.example.projet_final;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthSettings;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;

public class sign_up extends AppCompatActivity {
    //View
    private Button signup;
    private EditText name,phone,email,pass,confpass,birthday;
    private RadioGroup groupsex;
    private String log_email,log_pass;
    private static CheckBox CB_Builder,CB_air_conditioner,CB_electrician,CB_gardening,CB_House_painter,CB_housework,CB_Moving,CB_plumber;
    private String code,codeSent;
    //FireBase
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    //private FirebaseAuth.AuthStateListener mAuthListener;
    private DatabaseReference mReference;
    private FirebaseDatabase mFirebaseDatabase;
    private boolean emailUsed,phoneUsed;
    PhoneAuthCredential credential;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Log.i("sign_up_create","start");

        ///View
        signup=(Button)findViewById(R.id.SU_Button_sign_up);
        name=(EditText)findViewById(R.id.SU_User_Name);
        phone=(EditText)findViewById(R.id.SU_Phone);
        email=(EditText)findViewById(R.id.SU_Email);
        pass=(EditText)findViewById(R.id.SU_Password);
        confpass=(EditText)findViewById(R.id.SU_ConfirmPassword);
        groupsex=(RadioGroup)findViewById(R.id.SU_SexGroup);
        birthday=(EditText)findViewById(R.id.SU_Birthday);
        CB_Builder=(CheckBox)findViewById(R.id.CB_Builder);
        CB_air_conditioner=(CheckBox)findViewById(R.id.CB_air_conditioner);
        CB_electrician=(CheckBox)findViewById(R.id.CB_electrician);
        CB_gardening=(CheckBox)findViewById(R.id.CB_gardening);
        CB_House_painter=(CheckBox)findViewById(R.id.CB_House_painter);
        CB_housework=(CheckBox)findViewById(R.id.CB_housework);
        CB_Moving=(CheckBox)findViewById(R.id.CB_Moving);
        CB_plumber=(CheckBox)findViewById(R.id.CB_plumber);

        //Firebase
        mAuth= FirebaseAuth.getInstance();
        mFirebaseDatabase=FirebaseDatabase.getInstance();
        mReference= mFirebaseDatabase.getReference();


        mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                Log.i("code","onVerificationCompleted:star; ");
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.i("code","onVerificationFailed:star; ");
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                Log.i("code","onCodeSend:start; s=  "+s);
                super.onCodeSent(s, forceResendingToken);
                codeSent=s;
                Log.i("code","onCodeSend:end;   codeSent="+codeSent+"    s="+s);
            }

            @Override
            public void onCodeAutoRetrievalTimeOut(String s) {
                super.onCodeAutoRetrievalTimeOut(s);
                Log.i("code","onCodeAutoRetrievalTimeOut:start ");
            }
        };

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

                /*AlertDialog.Builder builder=new AlertDialog.Builder(sign_up.this);
                View mView=getLayoutInflater().inflate(R.layout.confirm_phone_dialog,null);
                builder.setView(mView);
                AlertDialog alertDialog=builder.create();
                alertDialog.show();*/


            }
        });


    }

    private void addData() {
        Log.i("addData","start");
        if(confirmData()){
            /*Log.i("addData","confData");
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
            );*/
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    phone.getText().toString(),        // Phone number to verify
                    60,                 // Timeout duration
                    TimeUnit.SECONDS,   // Unit of timeout
                    this,               // Activity (for callback binding)
                    mCallbacks);        // OnVerificationStateChangedCallbacks
            showConfrmDialog();
            Log.i("code","code have :"+code);





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
            phone.setError("Please Enter phone");
            //Toast.makeText(sign_up.this,"Please Enter phone",Toast.LENGTH_SHORT);
            Log.i("confirmData","phone invalid");
            return false;
        }/*else if (!(phone.getText().toString().charAt(0)=='0' && (phone.getText().toString().charAt(1)=='5' || phone.getText().toString().charAt(1)=='6' || phone.getText().toString().charAt(1)=='7') &&  phone.getText().toString().length()==10)){
            phone.setError("conferm your phone");
            return false;
        }*//*else{
            mReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child("Users").hasChild(phone.getText().toString())){
                        phone.setError("this phone has used");
                        phoneUsed=true;
                    }else{
                        phoneUsed=true;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            if(phoneUsed) return false;
        }*/
        Log.i("confirmData","phone valid");
        if(!email.getText().toString().isEmpty()){
            Log.i("confirmData","mail m3amar");
            if (!email.getText().toString().trim().matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")){
                email.setError("Please Enter correct email");
                //Toast.makeText(sign_up.this,"Please Enter correct email",Toast.LENGTH_SHORT);
                Log.i("confirmData","mail invalid");
                return false;
            }/*else{
                mReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child("Users").hasChild(email.getText().toString())){
                            email.setError("this email has used");
                            emailUsed=true;
                        }else{
                            emailUsed=true;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                if(emailUsed) return false;

            }*/
        }
        Log.i("confirmData","mail valid");
        if (pass.getText().toString().isEmpty()){
            pass.setError("Please Enter password");
            //Toast.makeText(sign_up.this,"Please Enter password",Toast.LENGTH_SHORT);
            Log.i("confirmData","pass invalid");
            return false;
        }
        Log.i("confirmData","pass valid");
        if (confpass.getText().toString().isEmpty()){
            if (!pass.getText().toString().isEmpty()){
                confpass.setError("Please confirm your password");
                //Toast.makeText(sign_up.this,"Please confirm your password",Toast.LENGTH_SHORT);
                Log.i("confirmData","confpass inconf");
                return false;
            }
        }else{
            if (!confpass.getText().toString().equals(pass.getText().toString())){
                confpass.setError("password and confirm password are not the same");
                //Toast.makeText(sign_up.this,"password and confirm password are not the same",Toast.LENGTH_SHORT);
                Log.i("confirmData","confpass note same");
                return  false;
            }
        }
        if(birthday.getText().toString().isEmpty()){
            birthday.setError("Please Enter your birthday");
            return false;
        }
        Log.i("confirmData","confpass same");

        Log.i("confirmData","sex valid");

        //Toast.makeText(sign_up.this,"data confirmed",Toast.LENGTH_SHORT);
        Log.i("confirmData","confirmData_end");
        return true;
    }


    private void saveData(){
        currentUser= mAuth.getCurrentUser();
        Log.i("saveData","start");
        String userID=currentUser.getUid();
        Log.i("saveData","UID:"+userID);
        if(!email.getText().toString().isEmpty()) mReference.child(email.getText().toString()).setValue(phone.getText().toString());
        mReference.child(userID).child("password").setValue(pass.getText().toString());
        mReference.child(userID).child("user_name").setValue(name.getText().toString());
        mReference.child(userID).child("phone").setValue(phone.getText().toString());
        mReference.child(userID).child("birthday").setValue(birthday.getText().toString());
        RadioButton sex=(RadioButton) findViewById(groupsex.getCheckedRadioButtonId());
        mReference.child(userID).child("sex").setValue(sex.getText().toString());
        mReference.child(userID).child("credential").setValue(credential);
        if(CB_Builder.isChecked()){
            mReference.child(userID).child(CB_Builder.getText().toString()).setValue(true);
        }else{
            mReference.child(userID).child(CB_Builder.getText().toString()).setValue(false);
        }
        if(CB_air_conditioner.isChecked()){
            mReference.child(userID).child(CB_air_conditioner.getText().toString()).setValue(true);
        }else{
            mReference.child(userID).child(CB_air_conditioner.getText().toString()).setValue(false);
        }
        if(CB_electrician.isChecked()){
            mReference.child(userID).child(CB_electrician.getText().toString()).setValue(true);
        }else{
            mReference.child(userID).child(CB_electrician.getText().toString()).setValue(false);
        }
        if(CB_gardening.isChecked()){
            mReference.child(userID).child(CB_gardening.getText().toString()).setValue(true);
        }else{
            mReference.child(userID).child(CB_gardening.getText().toString()).setValue(false);
        }
        if(CB_House_painter.isChecked()){
            mReference.child(userID).child(CB_House_painter.getText().toString()).setValue(true);
        }else{
            mReference.child(userID).child(CB_House_painter.getText().toString()).setValue(false);
        }
        if(CB_housework.isChecked()){
            mReference.child(userID).child(CB_housework.getText().toString()).setValue(true);
        }else{
            mReference.child(userID).child(CB_housework.getText().toString()).setValue(false);
        }
        if(CB_Moving.isChecked()){
            mReference.child(userID).child(CB_Moving.getText().toString()).setValue(true);
        }else{
            mReference.child(userID).child(CB_Moving.getText().toString()).setValue(false);
        }
        if(CB_plumber.isChecked()){
            mReference.child(userID).child(CB_plumber.getText().toString()).setValue(true);
        }else{
            mReference.child(userID).child(CB_plumber.getText().toString()).setValue(false);
        }
        Log.i("saveData","end");
    }

    @Override
    protected void onStart() {
        super.onStart();
        //mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //mAuth.removeAuthStateListener(mAuthListener);;
    }

    protected void showConfrmDialog(){
        Log.i("dialog","start");
        AlertDialog.Builder builder=new AlertDialog.Builder(sign_up.this);
        final View mView=getLayoutInflater().inflate(R.layout.confirm_phone_dialog,null);
        builder.setView(mView);
        final AlertDialog alertDialog=builder.create();
        mView.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        mView.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("code","credential:star; ");
                code=((EditText) mView.findViewById(R.id.code)).getText().toString();
                credential = PhoneAuthProvider.getCredential(codeSent, code);
                Log.i("code","credential:end;   credential= "+credential +"  credentialSmsCode= "+credential.getSmsCode()+"  credentialSignInMethod= "+credential.getSignInMethod()+"  credentialProvider= "+credential.getProvider());
                signInWithPhoneAuthCredential(credential);


            }
        });
        Log.i("dialog","show");
        alertDialog.show();
        Log.i("dialog","end");
        //return code;
    }


    /*PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

        }

        @Override
        public void onVerificationFailed(FirebaseException e) {

        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            codeSent=s;
            Log.i("code","code send:"+codeSent);
        }
    };*/


    private void signInWithPhoneAuthCredential(final PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.i("code","signIn  credential; "+credential);
                            Intent map=new Intent(sign_up.this,MapsActivity.class);
                            saveData();
                            startActivity(map);

                        } else {

                        }
                    }
                });
    }
}
