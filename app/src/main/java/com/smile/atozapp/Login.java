package com.smile.atozapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smile.atozapp.controller.TempData;

public class Login extends AppCompatActivity {

    TextInputLayout mobno;
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        mobno = findViewById(R.id.login_mobno);
        login = findViewById(R.id.login_loginbtn);

    }

    @Override
    protected void onResume() {
        super.onResume();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String no = mobno.getEditText().getText().toString();
                if (no.length() != 0 && no.length() == 10) {
                    startActivity(new Intent(getApplicationContext(), OtpPage.class).putExtra("ph", no));
                    finish();
                } else {
                    mobno.getEditText().setError("enter valid number");
                }
            }
        });

    }
}
