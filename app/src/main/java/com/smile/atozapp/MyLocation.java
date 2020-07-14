package com.smile.atozapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.smile.atozapp.addressdetails.AddAdress;
import com.smile.atozapp.controller.AppUtil;
import com.smile.atozapp.controller.TempData;
import com.smile.atozapp.models.MyLocationHold;
import com.smile.atozapp.parameters.AddressParameters;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MyLocation extends AppCompatActivity {

    TextView add_location;
    RecyclerView locationlist;
    ImageView mylocpic;
    Toolbar mytoolbar;
    TempData t;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_location);

        mytoolbar = findViewById(R.id.my_location_toolbar);
        setSupportActionBar(mytoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mytoolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);

        t = new TempData(MyLocation.this);

        add_location = findViewById(R.id.my_location_add_loc);
        locationlist = findViewById(R.id.my_location_list);
        mylocpic = findViewById(R.id.my_location_current_img);

        locationlist.setHasFixedSize(true);
        locationlist.setLayoutManager(new LinearLayoutManager(MyLocation.this));

        mylocpic.setImageResource(R.drawable.my_location_round_icon);

        getloclist();

        mytoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<AddressParameters, MyLocationHold> frecycle = new FirebaseRecyclerAdapter<AddressParameters, MyLocationHold>(
                AddressParameters.class, R.layout.row_location, MyLocationHold.class, AppUtil.ADDRESURL.child(t.getuid())
        ) {
            @Override
            protected void populateViewHolder(MyLocationHold mh, AddressParameters ap, int i) {
                mh.setdetails(MyLocation.this, ap.getAddress(), ap.getCity());
            }
        };
        locationlist.setAdapter(frecycle);

    }

    public void getloclist() {
        AppUtil.ADDRESURL.child(t.getuid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    locationlist.setVisibility(View.VISIBLE);
                } else {
                    locationlist.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        add_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext() , AddAdress.class));
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
