package com.example.projet_final;

import androidx.annotation.NonNull;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class multi_activity extends AppCompatActivity {
    private ListView lv;
    private ArrayAdapter ad;
    private TextView about_us,rate_us_tv,bad,excellent;
    private ImageView star1,star2,star3,star4,star5;
    private Button rate_us_button;
    private ConstraintLayout rate_us_layout;
    static String s="nothing";
    private int rate=0;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_activity);
        init();
        change_languagelistview();
        rate_us();
        change_rating();
        visibility();
        change_language();

    }
    public void init(){
        lv=(ListView)findViewById(R.id.change_language_list);
        about_us=findViewById(R.id.aboutus_tv);
        rate_us_tv=findViewById(R.id.rateus_tv);
        star1=findViewById(R.id.star1);
        star2=findViewById(R.id.star2);
        star3=findViewById(R.id.star3);
        star4=findViewById(R.id.star4);
        star5=findViewById(R.id.star5);
        bad=findViewById(R.id.rateus_bad);
        excellent=findViewById(R.id.rateus_excellent);
        rate_us_layout=findViewById(R.id.rateus_layout);
        rate_us_button=findViewById(R.id.rateus_button);
        databaseReference=FirebaseDatabase.getInstance().getReference().child("rate").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                rate=dataSnapshot.getValue(Integer.class);
                changeImage(rate);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void change_languagelistview(){
        lv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        ad=new ArrayAdapter(this,android.R.layout.simple_list_item_1,new String[]{"English","Français"});

        lv.setAdapter(ad);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        MapsActivity.lang="en";
                        break;
                    case 1:
                        MapsActivity.lang="fr";
                        break;
                }
                finish();
            }
        });
    }
    public void rate_us(){
        Button rate_Button=findViewById(R.id.rateus_button);
        rate_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.setValue(rate);
                finish();
            }
        });
    }
    public void change_rating(){
        star1.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            changeImage(1);
            rate=1;
            }
    });
        star2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeImage(2);
                rate=2;
            }
        });
        star3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeImage(3);
                rate=3;
            }
        });
        star4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeImage(4);
                rate=4;
            }
        });
        star5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeImage(5);
                rate=5;
            }
        });

    }


    private void changeImage(int rate){
        switch (rate){
            case 0:
                star1.setImageResource(R.drawable.rating_bar);
                star2.setImageResource(R.drawable.rating_bar);
                star3.setImageResource(R.drawable.rating_bar);
                star4.setImageResource(R.drawable.rating_bar);
                star5.setImageResource(R.drawable.rating_bar);
                break;
            case 1:
                star1.setImageResource(R.drawable.rating_bar_fill);
                star2.setImageResource(R.drawable.rating_bar);
                star3.setImageResource(R.drawable.rating_bar);
                star4.setImageResource(R.drawable.rating_bar);
                star5.setImageResource(R.drawable.rating_bar);
                break;
            case 2:
                star1.setImageResource(R.drawable.rating_bar_fill);
                star2.setImageResource(R.drawable.rating_bar_fill);
                star3.setImageResource(R.drawable.rating_bar);
                star4.setImageResource(R.drawable.rating_bar);
                star5.setImageResource(R.drawable.rating_bar);
                break;
            case 3:
                star1.setImageResource(R.drawable.rating_bar_fill);
                star2.setImageResource(R.drawable.rating_bar_fill);
                star3.setImageResource(R.drawable.rating_bar_fill);
                star4.setImageResource(R.drawable.rating_bar);
                star5.setImageResource(R.drawable.rating_bar);
                break;
            case 4:
                star1.setImageResource(R.drawable.rating_bar_fill);
                star2.setImageResource(R.drawable.rating_bar_fill);
                star3.setImageResource(R.drawable.rating_bar_fill);
                star4.setImageResource(R.drawable.rating_bar_fill);
                star5.setImageResource(R.drawable.rating_bar);
                break;
            case 5:
                star1.setImageResource(R.drawable.rating_bar_fill);
                star2.setImageResource(R.drawable.rating_bar_fill);
                star3.setImageResource(R.drawable.rating_bar_fill);
                star4.setImageResource(R.drawable.rating_bar_fill);
                star5.setImageResource(R.drawable.rating_bar_fill);
                break;
        }
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
    public  void change_language(){
        switch (MapsActivity.lang){

            case "en":


                about_us.setText("This application was created for obtaining IT license, by Bouraoua Amine and Feddak Faycal Abdalghani under the supervision of Mrs. Khouloud Meskaldji.");
                rate_us_tv.setText("what do you think about our application");
                bad.setText("Bad");
                excellent.setText("Excellent");
                rate_us_button.setText("Send Rating");


                break;
            case "fr":
                about_us.setText ("Cette application a été créée pour l'obtention d'une licence informatique, par Bouraoua Amine et Feddak Faycal Abdalghani sous la supervision de Mme Khouloud Meskaldji.");
                rate_us_tv.setText ("que pensez-vous de notre application");
                bad.setText ("mauvaise");
                excellent.setText ("Excellente");
                rate_us_button.setText ("Envoyer la note");

                break;
        }
    }

}
