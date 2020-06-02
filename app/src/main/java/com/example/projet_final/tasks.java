package com.example.projet_final;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class tasks extends AppCompatActivity {

    private ListView listView;
    private DatabaseReference databaseReference_listview;
    private FirebaseListAdapter<String> firebaseListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);
        listView=findViewById(R.id.listview);
        fill_listview();

    }
    public void fill_listview(){
        databaseReference_listview= FirebaseDatabase.getInstance().getReference().child("test");
        Query query = databaseReference_listview
                .limitToLast(50)
                .orderByKey();
        FirebaseListOptions<String> options =
                new FirebaseListOptions.Builder<String>()
                        .setLayout(android.R.layout.simple_list_item_1)
                        .setQuery(query, String.class)
                        .build();
        firebaseListAdapter=new FirebaseListAdapter<String>(options) {
            @Override
            protected void populateView(@NonNull View v, @NonNull String model, int position) {
                TextView t=v.findViewById(android.R.id.text1);
                t.setText(model);
            }
        };
        listView.setAdapter(firebaseListAdapter);


    }

    @Override
    protected void onStart() {
        firebaseListAdapter.startListening();
        super.onStart();
    }
}
