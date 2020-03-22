package com.example.projet_final;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class sign_up extends AppCompatActivity {
    private Button signup;
    private EditText name,phone,email,pass,confpass;
    private Data_Base_Helper myDb;
    private Fragment frag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        myDb=new Data_Base_Helper(this);
        signup=(Button)findViewById(R.id.SU_Button_sign_up);
        name=(EditText)findViewById(R.id.editText_name);
        phone=(EditText)findViewById(R.id.editText_numphone);
        email=(EditText)findViewById(R.id.editText_mail);
        pass=(EditText)findViewById(R.id.editText_pass);
        confpass=(EditText)findViewById(R.id.editText_confirmpass);
        try {
            //AddData();
        }catch (Exception e){

            Toast.makeText(sign_up.this, "Data not Inserted", Toast.LENGTH_LONG).show();

        }

    }


    public  void AddData() {
        signup.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isInserted = myDb.insertData(name.getText().toString(),
                                Integer.parseInt(phone.getText().toString()),
                                email.getText().toString(),pass.getText().toString() );
                        if(isInserted == true){
                            Toast.makeText(sign_up.this,"Data Inserted",Toast.LENGTH_LONG).show();
                            Intent i=new Intent(".MapsActivity");
                            startActivity(i);
                            //finish();
                        }
                        else {
                            Toast.makeText(sign_up.this, "Data not Inserted", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );

    }


}
