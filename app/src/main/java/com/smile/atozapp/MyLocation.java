package com.smile.atozapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
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
import com.smile.atozapp.controller.AppUtil;
import com.smile.atozapp.controller.TempData;
import com.smile.atozapp.models.MyLocationHold;
import com.smile.atozapp.parameters.AddressParameters;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MyLocation extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    Location currentloc;
    Geocoder geocoder;
    List<Address> addresses;

    double latvalue, langvalue;
    LocationManager lm;

    ProgressDialog pd;

    TextView clickmylocation;
    RecyclerView locationlist;
    ImageView mylocpic;
    TempData t;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_location);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                currentloc = location;
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.my_location_map);
                mapFragment.getMapAsync(MyLocation.this);
            }
        });

        clickmylocation = findViewById(R.id.my_location_current);
        locationlist = findViewById(R.id.my_location_list);
        mylocpic = findViewById(R.id.my_location_current_img);

        t = new TempData(MyLocation.this);

        locationlist.setHasFixedSize(true);
        locationlist.setLayoutManager(new LinearLayoutManager(MyLocation.this));

        mylocpic.setImageResource(R.drawable.my_location_round_icon);

        pd = new ProgressDialog(MyLocation.this);
        pd.setTitle("Loading");
        pd.setMessage("Please wait.....");

        getloclist();

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

        clickmylocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.show();
                LatLng posistion = new LatLng(currentloc.getLatitude(), currentloc.getLongitude());
                mMap.addMarker(new MarkerOptions().position(posistion).title("Its Me"));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(posistion, 15f));
                try {
                    geocoder = new Geocoder(MyLocation.this, Locale.ENGLISH);
                    addresses = geocoder.getFromLocation(currentloc.getLatitude(), currentloc.getLongitude(), 1);
                    StringBuilder str = new StringBuilder();
                    if (geocoder.isPresent()) {
                        Address returnAddress = addresses.get(0);
                        String localityString = returnAddress.getLocality();
                        //String state = returnAddress.getAdminArea();
                        String city = returnAddress.getSubAdminArea();
                        //String country = returnAddress.getCountryName();
                        //String region_code = returnAddress.getCountryCode();
                        //String zipcode = returnAddress.getPostalCode();
                        pd.dismiss();
                        t.addlocation(localityString);
                        Toast.makeText(MyLocation.this, localityString, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext() , LoginMain.class));finish();
                    } else {
                        pd.dismiss();
                        Toast.makeText(getApplicationContext(), "geocoder not present", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    pd.dismiss();
                    Toast.makeText(MyLocation.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("tag", e.getMessage());
                }
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng posistion = new LatLng(currentloc.getLatitude(), currentloc.getLongitude());
        mMap.addMarker(new MarkerOptions().position(posistion).title("Its Me"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(posistion, 15f));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext() , LoginMain.class));finish();finish();
    }
}
