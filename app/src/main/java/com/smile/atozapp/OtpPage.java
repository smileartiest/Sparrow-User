package com.smile.atozapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smile.atozapp.controller.AppUtil;
import com.smile.atozapp.controller.SendSMS;
import com.smile.atozapp.controller.TempData;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class OtpPage extends AppCompatActivity {

    TextInputLayout otp_txt;
    TextView otp_time;
    Button verify_btn;
    LottieAnimationView animationView;

    String verify_code = null;

    CountDownTimer count_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp_page);
        
        animationView = findViewById(R.id.otp_animation);
        otp_txt = findViewById(R.id.otp_otp_text);
        otp_time = findViewById(R.id.otp_otp_timing);
        verify_btn = findViewById(R.id.otp_otp_btn);

        animationView.setAnimation(R.raw.mobile_phone);
        animationView.playAnimation();
        animationView.loop(true);

        if(getIntent().getStringExtra("ph")!=null) {
            verify_code = getRandomNumberString();
            new SendSMS(OtpPage.this, getIntent().getStringExtra("ph"), "Hai welcome to Sparrow . Your verification code is " + verify_code + " . Please don't share share this code.");
            hour(90);
            animationView.setAnimation(R.raw.otp_send);
            animationView.playAnimation();
            animationView.loop(true);
        }else{
            finish();
            startActivity(new Intent(getApplicationContext() , Login.class));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        verify_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(verify_btn.getText().toString().equals("RESENT OTP")){
                    verify_btn.setVisibility(View.GONE);
                    new SendSMS(OtpPage.this, getIntent().getStringExtra("ph"), "Hai welcome to Sparrow . Your verification code is " + getRandomNumberString() + " . Please don't share share this code.");
                    hour(90);
                }
                else if(verify_btn.getText().toString().equals("TRY AFTER SOME TIME")){
                    finish();
                } else{
                    if(TextUtils.isEmpty(otp_txt.getEditText().getText().toString())){
                        Snackbar.make(v,"Please enter OTP" ,Snackbar.LENGTH_SHORT).show();
                    }else{
                        if(verify_code!=null) {
                            count_time.cancel();
                            if (otp_txt.getEditText().getText().toString().equals(verify_code)) {
                                animationView.setAnimation(R.raw.otp_verification);
                                animationView.playAnimation();
                                animationView.loop(true);
                                new TempData(OtpPage.this).adduid(getIntent().getStringExtra("ph"));
                                signinprocess1();
                            }else{
                                Snackbar.make(v,"OTP not matched" ,Snackbar.LENGTH_SHORT).show();
                            }
                        }else{
                            new SendSMS(OtpPage.this, getIntent().getStringExtra("ph"), "Hai welcome to Sparrow . Your verification code is " + getRandomNumberString() + " . Please don't share share this code.");
                            hour(90);
                        }
                    }
                }
            }
        });

    }

    //Generate Random number
    public static String getRandomNumberString() {
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        return String.format("%06d", number);
    }

    //OTP timing Process
    public void hour(int s)
    {
        count_time = new CountDownTimer(s*1000, 1000) {
            public void onTick(long millisUntilFinished) {
                otp_time.setText("waiting for otp  :  "+millisUntilFinished / 1000);
            }
            public void onFinish() {
                verify_code = null;
                if(verify_btn.getText().toString().equals("RESENT OTP")){
                    verify_btn.setVisibility(View.VISIBLE);
                    verify_btn.setText("TRY AFTER SOME TIME");
                }else{
                    verify_btn.setText("RESENT OTP");
                }
                otp_time.setVisibility(View.GONE);
            }
        }.start();
    }

    //Check this number already exits or not
    public void signinprocess1()
    {
        AppUtil.REGURL.child(getIntent().getStringExtra("ph"))
        .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("name").getValue()!=null)
                {
                    TempData t = new TempData(OtpPage.this);
                    t.addlogsts("login");
                    t.adduid(getIntent().getStringExtra("ph"));
                    startActivity(new Intent(getApplicationContext() , LoginMain.class));finish();
                }
                else
                {
                    signupProcess();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    //Create a new account
    public void signupProcess()
    {
        AppUtil.REGURL.child(getIntent().getStringExtra("ph"))
        .child("phno").setValue(getIntent().getStringExtra("ph"));
        Toast.makeText(getApplicationContext() , "Account Create successfully" ,Toast.LENGTH_SHORT).show();
        TempData t = new TempData(OtpPage.this);
        t.addlogsts("login");
        t.adduid(getIntent().getStringExtra("ph"));
        startActivity(new Intent(getApplicationContext() , Registration.class).putExtra("ph",getIntent().getStringExtra("ph")));finish();
    }

}
