package com.example.projet_final;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class multi_activity extends AppCompatActivity {
    private ListView lv;
    private ArrayAdapter ad;
    private TextView about_us,rate_us_tv;
    private ImageView star1,star2,star3,star4,star5;
    private Button rate_us_button;
    private ConstraintLayout rate_us_layout;
    static String s="nothing";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_activity);
        lv=(ListView)findViewById(R.id.change_language_list);
        about_us=findViewById(R.id.aboutus_tv);
        rate_us_tv=findViewById(R.id.rateus_tv);
        star1=findViewById(R.id.star1);
        star2=findViewById(R.id.star2);
        star3=findViewById(R.id.star3);
        star4=findViewById(R.id.star4);
        star5=findViewById(R.id.star5);
        rate_us_layout=findViewById(R.id.rateus_layout);
        rate_us_button=findViewById(R.id.rateus_button);

        change_language();
        rate_us();
        change_rating();
        visibility();

    }
    public void change_language(){
        lv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        ad=new ArrayAdapter(this,android.R.layout.simple_list_item_1,new String[]{"English","Français","العربية"});
        lv.setAdapter(ad);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(multi_activity.this,Integer.toString(position),Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void rate_us(){

    }
    public void change_rating(){
    star1.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            star1.setImageResource(R.drawable.rating_bar_fill);
            star2.setImageResource(R.drawable.rating_bar);
            star3.setImageResource(R.drawable.rating_bar);
            star4.setImageResource(R.drawable.rating_bar);
            star5.setImageResource(R.drawable.rating_bar);
        }
    });
        star2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star1.setImageResource(R.drawable.rating_bar_fill);
                star2.setImageResource(R.drawable.rating_bar_fill);
                star3.setImageResource(R.drawable.rating_bar);
                star4.setImageResource(R.drawable.rating_bar);
                star5.setImageResource(R.drawable.rating_bar);

            }
        });
        star3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                star1.setImageResource(R.drawable.rating_bar_fill);
                star2.setImageResource(R.drawable.rating_bar_fill);
                star3.setImageResource(R.drawable.rating_bar_fill);
                star4.setImageResource(R.drawable.rating_bar);
                star5.setImageResource(R.drawable.rating_bar);
            }
        });
        star4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star1.setImageResource(R.drawable.rating_bar_fill);
                star2.setImageResource(R.drawable.rating_bar_fill);
                star3.setImageResource(R.drawable.rating_bar_fill);
                star4.setImageResource(R.drawable.rating_bar_fill);
                star5.setImageResource(R.drawable.rating_bar);
            }
        });
        star5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                star1.setImageResource(R.drawable.rating_bar_fill);
                star2.setImageResource(R.drawable.rating_bar_fill);
                star3.setImageResource(R.drawable.rating_bar_fill);
                star4.setImageResource(R.drawable.rating_bar_fill);
                star5.setImageResource(R.drawable.rating_bar_fill);
            }
        });

    }
    public void  visibility(){
        if(s.equals("language_page")){
            about_us.setVisibility(View.GONE);
            rate_us_tv.setVisibility(View.GONE);
            rate_us_layout.setVisibility(View.GONE);
            rate_us_button.setVisibility(View.GONE);
            lv.setVisibility(View.VISIBLE);

        }
        if(s.equals("rate_us_page")){

            about_us.setVisibility(View.GONE);
            rate_us_tv.setVisibility(View.VISIBLE);
            rate_us_layout.setVisibility(View.VISIBLE);
            rate_us_button.setVisibility(View.VISIBLE);
            lv.setVisibility(View.GONE);
        }
        if(s.equals("about_us_page")){

            about_us.setVisibility(View.VISIBLE);
            rate_us_tv.setVisibility(View.GONE);
            rate_us_layout.setVisibility(View.GONE);
            rate_us_button.setVisibility(View.GONE);
            lv.setVisibility(View.GONE);
        }
    }
}
