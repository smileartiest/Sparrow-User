package com.smile.atozapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
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
import com.smile.atozapp.controller.TempData;

import java.util.concurrent.TimeUnit;

public class OtpPage extends AppCompatActivity {

    EditText txt;
    TextView otptime;
    Button verifybtn,resend;
    ConstraintLayout screen;

    FirebaseAuth mauth;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks mcalback;
    String verify_code = "";
    DatabaseReference df;

    ProgressDialog pd;
    CountDownTimer ctime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp_page);

        txt = findViewById(R.id.otp_txt);
        verifybtn = findViewById(R.id.otp_verifybtn);
        resend = findViewById(R.id.otp_resendotp);
        otptime = findViewById(R.id.otp_time);

        screen = findViewById(R.id.otp_screen);

        mauth = FirebaseAuth.getInstance();

        mcalback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                Snackbar snackbar1 = Snackbar.make(screen, phoneAuthCredential.toString(), Snackbar.LENGTH_LONG);
                snackbar1.show();
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Snackbar snackbar1 = Snackbar.make(screen, e.getMessage(), Snackbar.LENGTH_LONG);
                snackbar1.show();
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verify_code = s;
                Snackbar snackbar1 = Snackbar.make(screen, "Code Send Successfull", Snackbar.LENGTH_SHORT);
                snackbar1.show();
                hour(60);
                resend.setVisibility(View.INVISIBLE);
            }
        };

        pd = new ProgressDialog(OtpPage.this);
        pd.setTitle("Loading");
        pd.setMessage("Please wait....");

        sendotp(getIntent().getStringExtra("ph"));

    }

    @Override
    protected void onResume() {
        super.onResume();

        verifybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.show();
                String otp1 = txt.getText().toString().trim();
                if(otp1.length()!=0 && otp1.length()==6){
                    ctime.cancel();
                    verifiednumber(verify_code,otp1);
                }else {
                    ctime.cancel();
                    pd.dismiss();
                    Snackbar snackbar1 = Snackbar.make(screen, "Please enter OTP First", Snackbar.LENGTH_SHORT);
                    snackbar1.show();
                }
            }
        });

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendotp(getIntent().getStringExtra("ph"));
            }
        });

    }

    public void hour(int s)
    {
        ctime = new CountDownTimer(s*1000, 1000) {
            public void onTick(long millisUntilFinished) {
                otptime.setText("waitting for otp  :  "+millisUntilFinished / 1000);
            }
            public void onFinish() {
                resend.setVisibility(View.VISIBLE);
                otptime.setText("Re Send OTP");
            }
        }.start();
    }

    public void sendotp(String otpstring)
    {
        PhoneAuthProvider.getInstance().verifyPhoneNumber("+91"+otpstring,60, TimeUnit.SECONDS , OtpPage.this ,mcalback);
    }

    public void verifiednumber(String verify_code1 , String input_code1)
    {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verify_code1 , input_code1);
        sendWithPhone(credential);
    }

    public void sendWithPhone(PhoneAuthCredential credential)
    {
        mauth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            signinprocess1();
                        }
                        else
                        {
                            pd.dismiss();
                            Snackbar snackbar1 = Snackbar.make(screen, "OTP Not Matched", Snackbar.LENGTH_SHORT);
                            snackbar1.show();
                        }
                    }
                }) ;
    }

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
                    pd.dismiss();
                    startActivity(new Intent(getApplicationContext() , LoginMain.class));finish();
                }
                else
                {
                    signupProcess();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                pd.dismiss();
            }
        });
    }

    public void signupProcess()
    {
        AppUtil.REGURL.child(getIntent().getStringExtra("ph"))
        .child("phno").setValue(getIntent().getStringExtra("ph"));
        Toast.makeText(getApplicationContext() , "Account Create succesfull" ,Toast.LENGTH_SHORT).show();
        TempData t = new TempData(OtpPage.this);
        t.addlogsts("login");
        t.adduid(getIntent().getStringExtra("ph"));
        pd.dismiss();
        startActivity(new Intent(getApplicationContext() , Registration.class).putExtra("ph",getIntent().getStringExtra("ph")));finish();
    }

}
