package com.example.projet_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class notif_click extends AppCompatActivity {
    private String extra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notif_click);
        getInfoFromNot();
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        getInfoFromNot();
    }

    public void getInfoFromNot(){
        String s=getIntent().getStringExtra("key");
        Toast.makeText(this,s,Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}