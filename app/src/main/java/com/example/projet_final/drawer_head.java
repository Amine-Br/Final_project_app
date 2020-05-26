package com.example.projet_final;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class drawer_head extends AppCompatActivity {
    private Button signin,signup;
    private ImageView logo_img;
    private TextView logo_tv;
    private ConstraintLayout main,worker;
    static String visibility="nothing";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_head);
        init();
        signin_click();
        signup_click();
        change_visibility();
    }
    public void init(){
        signin=findViewById(R.id.drawer_h_signin);
        signup=findViewById(R.id.drawer_h_signup);
        logo_img=findViewById(R.id.drawer_h_image1);
        logo_tv=findViewById(R.id.drawer_h_tv1);
        main=findViewById(R.id.drawer_head_id);
        worker=findViewById(R.id.worker_inteface);

    }
    public void signin_click(){
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(".MainActivity"));
            }
        });
    }
    public void signup_click(){
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(".sign_up"));
            }
        });
    }

    public void  change_visibility(){
        if(visibility.equals("worker_mode")){
            signup.setVisibility(View.GONE);
            signin.setVisibility(View.GONE);
            logo_tv.setVisibility(View.GONE);
            logo_img.setVisibility(View.GONE);
            worker.setVisibility(View.VISIBLE);

            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) main.getLayoutParams();

            params.height = 200;

            main.setLayoutParams(params);

        }
        if(visibility.equals("user_mode")){

            signup.setVisibility(View.VISIBLE);
            signin.setVisibility(View.VISIBLE);
            logo_tv.setVisibility(View.VISIBLE);
            logo_img.setVisibility(View.VISIBLE);
            worker.setVisibility(View.GONE);
            ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) main.getLayoutParams();

            params.height = 400;

            main.setLayoutParams(params);


        }

    }


}
