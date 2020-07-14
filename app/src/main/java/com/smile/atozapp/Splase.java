package com.smile.atozapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.smile.atozapp.controller.TempData;

public class Splase extends AppCompatActivity {

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    Runnable r;
    Handler h;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splase);

        h = new Handler();
        r = new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext() , View_Tutorial.class));finishAffinity();
            }
        };

        if(new TempData(Splase.this).getuid()!=null) {
            Log.d("UID", new TempData(Splase.this).getuid());
        }

        if(new TempData(Splase.this).getlogsts()!=null){
            if (new TempData(Splase.this).getlogsts().equals("login")) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        startActivity(new Intent(getApplicationContext(), LoginMain.class));finishAffinity();
                    }
                },2000);
            }
        }else{
            h.postDelayed(r,3000);
        }
        if(!checkLocationPermission()){
            checkLocationPermission();
        }

    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

}
