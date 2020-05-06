package com.example.projet_final;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class multi_activity extends AppCompatActivity {
    private ListView lv;
    private ArrayAdapter ad;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_activity);
        lv=(ListView)findViewById(R.id.change_language_list);
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
}
