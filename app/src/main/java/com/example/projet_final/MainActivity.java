package com.example.projet_final;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private AlertDialog alertDialog;
    EditText email,phone,pass;
    Button signin;
    TextView signup;
    FirebaseAuth mAuth  ;
    FirebaseUser currentUser;
    FirebaseAuth.AuthStateListener mAuthListener;
    DatabaseReference mReference;
    String codeSent,code;
    private PhoneAuthCredential credential;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i("AntivetyLife","onCreat:start");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        email=(EditText)findViewById(R.id.em_et);
        phone=(EditText)findViewById(R.id.ph_et);
        pass=(EditText)findViewById(R.id.pass_et);
        signin=(Button)findViewById(R.id.button_sign_in);
        signup=(TextView)findViewById(R.id.sign_up_tv);
        mAuth=FirebaseAuth.getInstance();
        mReference= FirebaseDatabase.getInstance().getReference();
        mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                Log.i("code","onVerificationCompleted:star; ");
                signInWithPhoneAuthCredential(phoneAuthCredential);
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
                showConfrmDialog();
                Log.i("code","onCodeSend:end;   codeSent="+codeSent+"    s="+s);
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
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(confData()){
                    if (!phone.getText().toString().isEmpty()){
                        logInWithPhone(phone.getText().toString());
                    }else {
                        logInWithEmailAndPassword(email.getText().toString(),pass.getText().toString());
                    }
                }


                /*Intent map=new Intent(".MapsActivity");
                startActivity(map);*/

            }
        });



    }
    private void logInWithPhone(String phone){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks          // OnVerificationStateChangedCallbacks
        );


    }

    private void logInWithEmailAndPassword(String email,String password){
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    startActivity(new Intent(MainActivity.this,MapsActivity.class));
                    finish();
                }else{

                }
            }
        });

    }


    protected void showConfrmDialog(){
        Log.i("dialog","start");
        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        final View mView=getLayoutInflater().inflate(R.layout.confirm_phone_dialog,null);
        builder.setView(mView);
        alertDialog=builder.create();
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
    }


    private boolean confData(){
        return true;
    }

    private void signInWithPhoneAuthCredential(final PhoneAuthCredential credential) {
        Log.i("code","signIn  start; ");
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.i("code","signIn  succ; ");
                            //saveData(mAuth.getCurrentUser());
                            Log.i("code","signIn  credential; "+credential);

                            Intent map=new Intent(MainActivity.this,MapsActivity.class);
                            startActivity(map);

                        } else {
                            Log.i("code","signIn not succ; ");
                        }
                    }
                });
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
        //mAuth.addAuthStateListener(mAuthListener);
        Log.i("AntivetyLife","onStart:end");
    }

    @Override
    protected void onStop() {
        Log.i("AntivetyLife","onStop:start");
        super.onStop();
        //mAuth.removeAuthStateListener(mAuthListener);
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
