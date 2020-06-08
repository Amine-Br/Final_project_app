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

    private String codeSent,code;
    private AlertDialog alertDialog;

    //view
    private EditText phone;
    private Button signin;
    private TextView signup,phone_tv,signin_tv,welcome,info;

    //firebase
    private FirebaseAuth mAuth  ;
    private DatabaseReference mReference;
    private PhoneAuthCredential credential;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("Activity","MainActivity");
        Log.i("MainActivity","onCreate");

        init();
        clicksignup();
        clicksignin();
        change_language();
    }

    private void  init(){

        Log.i("MainActivity","init");
        //view
        phone=(EditText)findViewById(R.id.ph_et);
        phone_tv=findViewById(R.id.ph_tv);
        signin=(Button)findViewById(R.id.button_sign_in);
        signup=(TextView)findViewById(R.id.sign_up_tv);
        signin_tv=findViewById(R.id.sign_in_tv);
        welcome=findViewById(R.id.welcome_tv);
        info=findViewById(R.id.info_tv);
        //firebase
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

    }

    public void clicksignup(){
       signup.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               Log.i("MainActivity","sign_up clicked");

               Intent i = new Intent(".sign_up");
               startActivity(i);
           }
       });
    }

    public void clicksignin(){
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i("MainActivity","log_in clicked");

                if(confData()){
                    if (!phone.getText().toString().isEmpty()){

                        Log.i("MainActivity","log_in with phone");

                        logInWithPhone(phone.getText().toString());
                    }else {

                        Log.i("MainActivity","log_in with email and password");


                    }
                }


            }
        });



    }

    private void logInWithPhone(String phone){

        Log.i("MainActivity","send code");

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

        Log.i("MainActivity","dialog show");

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
                code=((EditText) mView.findViewById(R.id.code)).getText().toString();
                credential = PhoneAuthProvider.getCredential(codeSent, code);
                signInWithPhoneAuthCredential(credential);

            }
        });
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    private boolean confData(){

        Log.i("MainActivity","data confermed");

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
                            Intent servic=new Intent(MainActivity.this,WorkerService.class);
                            servic.putExtra("workerID",mAuth.getCurrentUser().getUid());
                            startService(servic);
                            finish();

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










    //Activity lifecycle

    @Override
    protected void onStart() {
        Log.i("MainActivity","onStart");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.i("MainActivity","onResume");
        super.onResume();
    }

    @Override
    protected void onStop() {
        Log.i("MainActivity","onStop");
        super.onStop();
    }

    @Override
    protected void onPause() {
        Log.i("MainActivity","onPause");
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Log.i("MainActivity","onDestroy");
        super.onDestroy();
    }
    public  void change_language(){
        switch (MapsActivity.lang){

            case "en":
                //signup,phone_tv,signin_tv,welcome,info,signin
                signin_tv.setText("Sign in");
                welcome.setText("WELCOME");
                phone_tv.setText("Phone number");
                signin.setText("SIGN IN");
                info.setText("Don’t have an account?");
                signup.setText("Sign up");


                break;
            case "fr":
                signin_tv.setText("Se Connecter");
                welcome.setText("BIENVENU");
                phone_tv.setText("Numéro de téléphone");
                signin.setText("SE CONNECTER");
                info.setText("Vous n'avez pas de compte?");
                signup.setText("s'inscrire");

                break;
        }
    }

}
