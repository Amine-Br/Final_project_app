package com.example.projet_final;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CountDownLatch;

public class tasks extends AppCompatActivity {

    private ListView listView;
    private TextView task;
    private DatabaseReference databaseReference_listview,getterref;
    private FirebaseListAdapter<String> firebaseListAdapter;
    private FirebaseAuth mAuth;
    private Notification notification;
    private ValueEventListener valueEventListener;
    final CountDownLatch done=new CountDownLatch(1);
    private HashMap<String,Notification> Hm;
    private DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);
        listView=findViewById(R.id.listview);
        task=findViewById(R.id.task_tv);
        mAuth=FirebaseAuth.getInstance();
        reference=FirebaseDatabase.getInstance().getReference().child("spesfReq").child(mAuth.getCurrentUser().getUid());
        Hm=new HashMap<>();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.i("tasksAct","dataChang");
                Notification notification;
                for (DataSnapshot notifications : dataSnapshot.getChildren()) {
                    Log.i("tasksAct","for");
                    notification = notifications.getValue(Notification.class);
                    Hm.put(notifications.getKey(),notification);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        fill_listview();
        //itemclicklistview();
        itemclicklistview();
        change_language();

    }
    public void fill_listview(){
        databaseReference_listview= FirebaseDatabase.getInstance().getReference().child("task").child(mAuth.getCurrentUser().getUid());
        Query query = databaseReference_listview
                .limitToLast(50);
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
    public void itemclicklistview(){

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DatabaseReference itemRef = firebaseListAdapter.getRef(position);
                final DatabaseReference getrRef=FirebaseDatabase.getInstance().getReference().child("spesfReq").child(mAuth.getCurrentUser().getUid())
                        .child(itemRef.getKey());

                notification=Hm.get(itemRef.getKey());
                Log.i("tasksAct","await");
                Log.i("tasksAct","await end");
                //thread.interrupt();
                Toast toast = Toast.makeText(tasks.this, notification.getPhone(), Toast.LENGTH_SHORT);
                toast.show();
                Dialog d=new Dialog(tasks.this);
                d.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                d.setContentView(R.layout.tasks_popup);
                TextView t = d.findViewById(R.id.userphone);
                t.setText(notification.getPhone());
                t = d.findViewById(R.id.servicetype);
                t.setText(notification.getJob());
                t = d.findViewById(R.id.DATE);
                t.setText(notification.getDate());
                t = d.findViewById(R.id.Details);
                t.setText(notification.getDetails());
                d.show();
            }
        });

    }

    @Override
    protected void onStart() {
        firebaseListAdapter.startListening();
        super.onStart();
    }
    public  void change_language(){
        switch (multi_activity.lang){

            case "en":

                task.setText("New Tasks:");


                break;
            case "fr":

                task.setText ("Nouvelles tâches:");

                break;
        }
    }

}
