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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
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

    private String code,codeSent;

    //View
    private AlertDialog alertDialog;
    private Button signup;
    private EditText name,phone,email,birthday;
    private TextView signup_tv,username_tv,birthday_tv,sex_tv,phone_tv,email_tv,jobs_tv;
    private RadioButton male,female;
    private RadioGroup groupsex;
    private CheckBox CB_Builder,CB_air_conditioner,CB_electrician,CB_gardening,CB_House_painter,CB_housework,CB_Moving,CB_plumber;

    //FireBase
    private FirebaseAuth mAuth;
    private DatabaseReference mReference;
    private FirebaseDatabase mFirebaseDatabase;
    private PhoneAuthCredential credential;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Log.i("Activity","sign_up");
        Log.i("sign_up","onCreate");
        init();
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (confirmData())  signUp();
            }
        });
        change_language();



    }

    private void init(){

        Log.i("sign_up","init");

        ///View
        signup_tv=findViewById(R.id.sign_up_tv);
        username_tv=findViewById(R.id.SU_Text_User_Name);
        birthday_tv=findViewById(R.id.SU_Text_Birthday);
        sex_tv=findViewById(R.id.textView_sexe2);
        phone_tv =findViewById(R.id.SU_Text_Phone);
        email_tv=findViewById(R.id.SU_Text_Email);
        jobs_tv=findViewById(R.id.SU_Text_Jobs);
        male=findViewById(R.id.SU_Male);
        female=findViewById(R.id.SU_Female);
        signup=(Button)findViewById(R.id.SU_Button_sign_up);
        name=(EditText)findViewById(R.id.SU_User_Name);
        phone=(EditText)findViewById(R.id.SU_Phone);
        email=(EditText)findViewById(R.id.SU_Email);
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

        //mCallbacks
        mCallbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
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

            @Override
            public void onCodeAutoRetrievalTimeOut(String s) {
                super.onCodeAutoRetrievalTimeOut(s);
                Log.i("code","onCodeAutoRetrievalTimeOut:start ");
            }
        };


    }

    private void signUp() {
        Log.i("sign_up","send Config code");
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                    phone.getText().toString(),        // Phone number to verify
                    60,                 // Timeout duration
                    TimeUnit.SECONDS,   // Unit of timeout
                    this,               // Activity (for callback binding)
                    mCallbacks          // OnVerificationStateChangedCallbacks
        );
    }

    public boolean confirmData() {
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
        /*if (pass.getText().toString().isEmpty()){
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
        }*/
        if(birthday.getText().toString().isEmpty()){
            birthday.setError("Please Enter your birthday");
            return false;
        }
        Log.i("confirmData","confpass same");
        Log.i("confirmData","sex valid");
        Log.i("sign_up","data confermed");
        return true;
    }

    private void saveData(FirebaseUser currentUser){
        String userID=currentUser.getUid();
        mReference.child("usedPhone").child(phone.getText().toString()).setValue(true);
        RadioButton sex=(RadioButton) findViewById(groupsex.getCheckedRadioButtonId());

        //set Values
        mReference.child("users").child(userID).child("user_name").setValue(name.getText().toString());
        mReference.child("users").child(userID).child("phone").setValue(phone.getText().toString());
        mReference.child("users").child(userID).child("birthday").setValue(birthday.getText().toString());
        if(groupsex.getCheckedRadioButtonId()==R.id.SU_Male){
            mReference.child("users").child(userID).child("sex").setValue("Male");
            mReference.child("users").child(userID).child("icone").setValue("gs://finalprojectapp-153c6.appspot.com/default/default_men_img.png");
        }else {
            mReference.child("users").child(userID).child("sex").setValue("Female");
            mReference.child("users").child(userID).child("icone").setValue("gs://finalprojectapp-153c6.appspot.com/default/default_women_img.png");
        }


        //set email
        if (!email.getText().toString().isEmpty()){
            mReference.child("users").child(userID).child("email").setValue(email.getText().toString());
        }

        //set jobs
        if(CB_Builder.isChecked()){
            mReference.child("users").child(userID).child("Builder").setValue(true);
        }else{
            mReference.child("users").child(userID).child("Builder").setValue(false);
        }
        if(CB_air_conditioner.isChecked()){
            mReference.child("users").child(userID).child("air_conditioner").setValue(true);
        }else{
            mReference.child("users").child(userID).child("air_conditioner").setValue(false);
        }
        if(CB_electrician.isChecked()){
            mReference.child("users").child(userID).child("electrician").setValue(true);
        }else{
            mReference.child("users").child(userID).child("electrician").setValue(false);
        }
        if(CB_gardening.isChecked()){
            mReference.child("users").child(userID).child("gardening").setValue(true);
        }else{
            mReference.child("users").child(userID).child("gardening").setValue(false);
        }
        if(CB_House_painter.isChecked()){
            mReference.child("users").child(userID).child("House_painter").setValue(true);
        }else{
            mReference.child("users").child(userID).child("House_painter").setValue(false);
        }
        if(CB_housework.isChecked()){
            mReference.child("users").child(userID).child("housework").setValue(true);
        }else{
            mReference.child("users").child(userID).child("housework").setValue(false);
        }
        if(CB_Moving.isChecked()){
            mReference.child("users").child(userID).child("Moving").setValue(true);
        }else{
            mReference.child("users").child(userID).child("Moving").setValue(false);
        }
        if(CB_plumber.isChecked()){
            mReference.child("users").child(userID).child("plumber").setValue(true);
        }else{
            mReference.child("users").child(userID).child("plumber").setValue(false);
        }

        Log.i("sign_up","data saved on firebase");
    }

    protected void showConfrmDialog(){

        Log.i("sign_up","dialog show");

        AlertDialog.Builder builder=new AlertDialog.Builder(sign_up.this);
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
        alertDialog.show();
    }

    private void signInWithPhoneAuthCredential(final PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Log.i("sign_up","sign_up secsseful");

                            saveData(mAuth.getCurrentUser());
                            /*if (!email.getText().toString().isEmpty()){
                               // signInWithEmailAndPassword(email.getText().toString(),pass.getText().toString());
                                link(EmailAuthProvider.getCredential(email.getText().toString(),pass.getText().toString()));
                                //mAuth.signOut();
                            }*/
                            Intent map=new Intent(sign_up.this,MapsActivity.class);
                            startActivity(map);

                        } else {

                            Log.i("sign_up"," sign_up not secsseful");

                        }
                    }
                });
    }

    public void link(AuthCredential credential){
        mAuth.getCurrentUser().linkWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = task.getResult().getUser();

                            Log.i("sign_up","link secsseful");

                        } else {

                            Log.i("sign_up","link not secsseful");

                        }
                    }
                });
    }












    //Activity lifecycle

    @Override
    protected void onStart() {
        Log.i("sign_up","onStart");
        super.onStart();
    }

    @Override
    protected void onResume() {
        Log.i("sign_up","onResume");
        super.onResume();
    }

    @Override
    protected void onStop() {
        Log.i("sign_up","onStop");
        super.onStop();
    }

    @Override
    protected void onPause() {
        Log.i("sign_up","onPause");
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Log.i("sign_up","onDestroy");
        super.onDestroy();
    }
    public  void change_language(){
        switch (multi_activity.lang){

            case "en":
                signup_tv.setText("Sign Up");
                username_tv.setText("User_Name");
                birthday_tv.setText("Birthday");
                sex_tv.setText("Sex");
                phone_tv.setText("Phone");
                email_tv.setText("Email");
                jobs_tv.setText("Jobs");
                male.setText("Male");
                female.setText("Female");
                CB_plumber.setText("plumber");
                CB_electrician.setText("electrician");
                CB_House_painter.setText("House_painter");
                CB_Builder.setText("Builder");
                CB_air_conditioner.setText("air_conditioner");
                CB_gardening.setText("gardening");
                CB_housework.setText("housework");
                CB_Moving.setText("Moving");
                signup.setText("SIGN UP");




                break;
            case "fr":
                signup_tv.setText ("Inscription");
                username_tv.setText ("Nom d'utilisateur");
                birthday_tv.setText ("Anniversaire");
                sex_tv.setText ("Sexe");
                phone_tv.setText ("Téléphone");
                email_tv.setText ("Email");
                jobs_tv.setText ("Travaux");
                male.setText ("Homme");
                female.setText ("Femme");
                CB_plumber.setText ("plombier");
                CB_electrician.setText ("électricien");
                CB_House_painter.setText ("Peintre en bâtiment");
                CB_Builder.setText ("constructeur");
                CB_air_conditioner.setText ("climatisation");
                CB_gardening.setText ("jardinage");
                CB_housework.setText ("travaux ménagers");
                CB_Moving.setText ("Déplacement");
                signup.setText ("INSCRIPTION");


                break;
        }
    }
}
