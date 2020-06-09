package com.example.projet_final;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
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
        String user_name=getIntent().getStringExtra("user_name");
        String sex=getIntent().getStringExtra("sex");
        String Birthday=getIntent().getStringExtra("Birthday");
        String email=getIntent().getStringExtra("email");
        String jobs=getIntent().getStringExtra("jobs");
        Log.i("notifa",user_name);
        Dialog dialog=new Dialog(this);
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
        Button b=dialog.findViewById(R.id.prechedule_bitton);
        b.setVisibility(View.GONE);
        dialog.setCanceledOnTouchOutside(false);
        b=dialog.findViewById(R.id.cancel_button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}