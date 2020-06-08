package com.example.projet_final;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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
    private DatabaseReference databaseReference_listview,getterref;
    private FirebaseListAdapter<String> firebaseListAdapter;
    private FirebaseAuth mAuth;
    private Notification notification;
    private ValueEventListener valueEventListener;
    private ArrayList<String> userID;
    private ArrayList<DataSnapshot> userDataSnapshot;
    private HashMap<String,Notification> Hm;
    private DatabaseReference reference;
    private View.OnClickListener cancel,Confirm;
    private DatabaseReference itemRef;
    private Dialog d;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);
        init();
        final CountDownLatch done=new CountDownLatch(1);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Hm.clear();
                Log.i("tasksAct","dataChang");
                Notification notification;
                for (DataSnapshot notifications : dataSnapshot.getChildren()) {
                    Log.i("tasksAct","for");
                    notification = notifications.getValue(Notification.class);
                    Hm.put(notifications.getKey(),notification);
                }
                done.countDown();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                done.countDown();
            }

        });
        try {
            done.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        final CountDownLatch done2=new CountDownLatch(1);
        getterref=FirebaseDatabase.getInstance().getReference().child("spesfReq");
        getterref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userID.clear();
                userDataSnapshot.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    userID.add(data.getKey());
                    userDataSnapshot.add(data);
                }
                done2.countDown();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        try {
            done2.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        fill_listview();
        itemclicklistview();

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
                itemRef = firebaseListAdapter.getRef(position);
                notification=Hm.get(itemRef.getKey());
                Log.i("tasksAct","await");
                Log.i("tasksAct","await end");
                //thread.interrupt();
                Toast toast = Toast.makeText(tasks.this, notification.getPhone(), Toast.LENGTH_SHORT);
                toast.show();
                d=new Dialog(tasks.this);
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
                if(!notification.getTaked().equals("not yet")){
                    t=d.findViewById(R.id.cancel_button);
                    t.setVisibility(View.GONE);
                    t=d.findViewById(R.id.send_request_button);
                    t.setVisibility(View.GONE);

                }else{
                    t=d.findViewById(R.id.cancel_button);
                    t.setOnClickListener(cancel);
                    t=d.findViewById(R.id.send_request_button);
                    t.setOnClickListener(Confirm);
                }

                d.show();
            }
        });

    }
    private void init(){
        userID=new ArrayList<>();
        userDataSnapshot=new ArrayList<>();
        cancel=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(notification.getJob().equals("no job")) {
                    reference.child(itemRef.getKey()).child("taked").setValue("cancel");
                }else{
                    itemRef.removeValue();
                }
                d.dismiss();
            }
        };
        Confirm=new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference.child(itemRef.getKey()).child("taked").setValue("accepted");
                FirebaseDatabase.getInstance().getReference().child("GlobalReq").child(itemRef.getKey()).removeValue();
                for(int i=0;i<userID.size();i++){
                    if(userDataSnapshot.get(i).hasChild(itemRef.getKey())){
                        if(!userID.get(i).equals(mAuth.getCurrentUser().getUid())) {
                            FirebaseDatabase.getInstance().getReference().child("spesfReq").child(userID.get(i)).child(itemRef.getKey()).removeValue();
                            try {
                                FirebaseDatabase.getInstance().getReference().child("task").child(userID.get(i)).child(itemRef.getKey()).removeValue();
                            }catch (Exception e){
                                //don t have this childe
                            }
                        }
                    }
                }
                d.dismiss();
            }
        };
        listView=findViewById(R.id.listview);
        mAuth=FirebaseAuth.getInstance();
        reference=FirebaseDatabase.getInstance().getReference().child("spesfReq").child(mAuth.getCurrentUser().getUid());
        Hm=new HashMap<>();
    }

    @Override
    protected void onStart() {
        firebaseListAdapter.startListening();
        super.onStart();
    }

}
