package com.example.projet_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    TextView singup;
    Button signin;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        singup=(TextView)findViewById(R.id.sign_up_tv);
        signin=(Button)findViewById(R.id.button_sign_in);
        clicksignup();
        clicksignin();
    }
    public void clicksignup(){

       singup.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent i = new Intent(".sign_up");
               startActivity(i);
           }
       });



    }



    public void clicksignin(){

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapi = new Intent(".MapsActivity");
                startActivity(mapi);

            }
        });



    }

}
