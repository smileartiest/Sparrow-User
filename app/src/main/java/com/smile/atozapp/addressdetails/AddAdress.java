package com.smile.atozapp.addressdetails;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.smile.atozapp.MyLocation;
import com.smile.atozapp.R;
import com.smile.atozapp.controller.AppUtil;
import com.smile.atozapp.controller.TempData;
import com.smile.atozapp.parameters.AddressParameters;


import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.os.Build.*;

public class AddAdress extends FragmentActivity implements GoogleMap.OnMyLocationButtonClickListener,
        GoogleMap.OnMyLocationClickListener,OnMapReadyCallback{

    private GoogleMap mMap;
    TextInputLayout flatno,area,cno;
    TextView cityname;
    Button completebtn;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private FusedLocationProviderClient fusedLocationProviderClient;
    LocationManager lm;
    Location currentloc;

    Geocoder geocoder;
    List<Address> addresses;

    TempData t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_adress);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        flatno = findViewById(R.id.address_houseno);
        area = findViewById(R.id.address_area);
        cno = findViewById(R.id.address_cno);
        completebtn = findViewById(R.id.address_conformbtn);
        cityname = findViewById(R.id.address_city);

        t = new TempData(AddAdress.this);

        if (VERSION.SDK_INT >= VERSION_CODES.M) {
            if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
                return;
            }else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)) {
                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                } else {
                    // No explanation needed; request the permission
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            MY_PERMISSIONS_REQUEST_LOCATION);

                    // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                    // app-defined int constant. The callback method gets the
                    // result of the request.
                }
            }
        }
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                currentloc = location;
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.my_location_map);
                mapFragment.getMapAsync(AddAdress.this);
                try {
                    geocoder = new Geocoder(AddAdress.this, Locale.ENGLISH);
                    addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    StringBuilder str = new StringBuilder();
                    if (geocoder.isPresent()) {
                        Address returnAddress = addresses.get(0);
                        String localityString = returnAddress.getLocality();
                        //String state = returnAddress.getAdminArea();
                        String city = returnAddress.getSubAdminArea();
                        //String country = returnAddress.getCountryName();
                        //String region_code = returnAddress.getCountryCode();
                        //String zipcode = returnAddress.getPostalCode();
                        t.addlocation(localityString);
                        cityname.setText(localityString);
                    } else {
                        Toast.makeText(getApplicationContext(),
                                "geocoder not present", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    Toast.makeText(AddAdress.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        completebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fno1 = flatno.getEditText().getText().toString();
                String area1 = area.getEditText().getText().toString();
                String cno1 = cno.getEditText().getText().toString();

                if(fno1.length()!=0){
                    if(area1.length()!=0){
                        if(cno1.length()!=0){

                            String key = AppUtil.ADDRESURL.push().getKey();
                            AddressParameters ad = new AddressParameters(key , t.getuid(), String.valueOf(currentloc.getLatitude()), String.valueOf(currentloc.getLongitude()),cityname.getText().toString() ,fno1+","+area1 , cno1);
                            AppUtil.ADDRESURL.child(t.getuid()).child(key).setValue(ad);
                            finish();

                        }else { cno.getEditText().setError("enter valid detail"); }
                    }else { area.getEditText().setError("enter valid detail"); }
                }else { flatno.getEditText().setError("enter valid details"); }
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(googleMap.MAP_TYPE_NORMAL);

        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);

        LatLng posistion = new LatLng(currentloc.getLatitude() , currentloc.getLongitude());
        mMap.addMarker(new MarkerOptions().position(posistion).title("Its Me"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(posistion , 15f));

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != MY_PERMISSIONS_REQUEST_LOCATION) {
            return;
        }
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

}
