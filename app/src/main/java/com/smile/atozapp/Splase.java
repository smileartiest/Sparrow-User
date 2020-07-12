package com.smile.atozapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

public class Splase extends AppCompatActivity {

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    Runnable r;
    Handler h;

    TextView permissionbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splase);

        h = new Handler();
        r = new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext() , Login.class));finish();
            }
        };

        permissionbtn = findViewById(R.id.splase_permission);
        permissionbtn.setVisibility(View.INVISIBLE);

        if(!checkLocationPermission())
        {
            checkLocationPermission();
        }else {
            h.postDelayed(r,3000);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        permissionbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!checkLocationPermission())
                {
                    checkLocationPermission();
                }else {
                    h.postDelayed(r,3000);
                }
            }
        });

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
                permissionbtn.setVisibility(View.VISIBLE);
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
                permissionbtn.setVisibility(View.VISIBLE);
            }
            return false;
        } else {
            return true;
        }
    }

}
