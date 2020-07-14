package com.smile.atozapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.smile.atozapp.controller.AppUtil;
import com.smile.atozapp.controller.TempData;

public class Registration extends AppCompatActivity {

    TextInputLayout name,email;
    Button registration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);

        name = findViewById(R.id.reg_name);
        email = findViewById(R.id.reg_email);
        registration = findViewById(R.id.reg_regbtn);

    }

    @Override
    protected void onResume() {
        super.onResume();

        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name1 = name.getEditText().getText().toString();
                String email1 = email.getEditText().getText().toString();
                if(name1.length()!=0){
                    if(email1.length()!=0){
                        AppUtil.REGURL.child(getIntent().getStringExtra("ph")).child("name").setValue(name1);
                        AppUtil.REGURL.child(getIntent().getStringExtra("ph")).child("email").setValue(email1);
                        new TempData(Registration.this).addlogsts("login");
                        movepage();
                    }else {email.getEditText().setError("enter valid email");}
                }else {name.getEditText().setError("enter valid name");}
            }
        });

    }

    void movepage(){
        startActivity(new Intent(getApplicationContext() , LoginMain.class));finishAffinity();
    }
}
