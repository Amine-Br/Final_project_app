package com.example.projet_final;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

public class notif_click extends AppCompatActivity {
    private User user;
    private HashMap<String,User> users;
    private Dialog dialog;
    private TextView t1,t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notif_click);
        Log.i("notifa","oncreate");
        getInfoFromNot();
    }

    @Override
    public void onNewIntent(Intent intent) {
        Log.i("notifa","onNewIntent");
        super.onNewIntent(intent);
        setIntent(intent);
        getInfoFromNot();
    }

    public void getInfoFromNot(){
        Bundle bundle=getIntent().getExtras();
        String user_name=bundle.getString("user_name");
        String sex=bundle.getString("sex");
        String Birthday=bundle.getString("Birthday");
        String email=bundle.getString("email");
        String jobs=bundle.getString("jobs");
        final String phone=bundle.getString("phone");
        Log.i("notifa",user_name);
         dialog=new Dialog(this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        Toast.makeText(this,"pass",Toast.LENGTH_LONG).show();
        dialog.setContentView(R.layout.activity_popup);
        TextView t=dialog.findViewById(R.id.username_popup);
        t.setText(user_name);
        t=dialog.findViewById(R.id.sex_tv2);
        t.setText(sex);
        t=dialog.findViewById(R.id.birthday_tv2);
        t.setText(Birthday);
        t=dialog.findViewById(R.id.email_tv2);
        t.setText(email);
        t=dialog.findViewById(R.id.jobs_tv2);
        t.setText(jobs);
        TextView b=dialog.findViewById(R.id.prechedule_bitton);
        b.setVisibility(View.GONE);
        dialog.setCanceledOnTouchOutside(false);
        b=dialog.findViewById(R.id.cancel_button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextView Call,SMS;

        Call=dialog.findViewById(R.id.task_tv);
        Call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Intent.ACTION_DIAL);
                String s="tel:"+phone;
                i.setData(Uri.parse(s));
                startActivity(i);
            }
        });
        SMS=dialog.findViewById(R.id.SMS_button);
        SMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Intent.ACTION_VIEW,Uri.fromParts("sms",phone,null));
                startActivity(i);
            }
        });

        change_dialog_lang();
        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
    public  void change_dialog_lang(){
        switch (MapsActivity.lang){


            case "en":

                t=dialog.findViewById(R.id.sex_tv);
                t.setText("SEX:");
                t=dialog.findViewById(R.id.birthday_tv);
                t.setText("Birthday:");
                t=dialog.findViewById(R.id.jobs_tv);
                t.setText("Jobs:");
                t=dialog.findViewById(R.id.cancel_button);
                t.setText("cancel");
                t=dialog.findViewById(R.id.task_tv);
                t.setText("Call");

                break;
            case "fr":

                t1 = dialog.findViewById(R.id.sex_tv);
                t1.setText ("SEXE:");
                t1 = dialog.findViewById(R.id.birthday_tv);
                t1.setText ("date de naissance:");
                t1 = dialog.findViewById(R.id.jobs_tv);
                t1.setText ("Travaux:");
                t1 = dialog.findViewById (R.id.cancel_button);
                t1.setText ("annuler");
                t1 = dialog.findViewById (R.id.task_tv);
                t1.setText ("Appel");
                break;
        }
    }
}