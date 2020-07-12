package com.smile.atozapp;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.smile.atozapp.controller.AppUtil;
import com.smile.atozapp.models.EmployeParameters;
import com.smile.atozapp.parameters.OrderPatameters;

public class ViewMoreDetails extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    TextView itemnams, odate, dbname, dbphno, ddate;
    private static String TAG = "vmoredetails";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_more_details);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.vmore_map);
        mapFragment.getMapAsync(this);

        itemnams = findViewById(R.id.vmore_itemnams);
        odate = findViewById(R.id.vmore_order_date);
        dbname = findViewById(R.id.vmore_dboyname);
        dbphno = findViewById(R.id.vmore_dboyphno);
        ddate = findViewById(R.id.vmore_ddate);

        AppUtil.ORDERURl.child(getIntent().getStringExtra("id")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    OrderPatameters o = dataSnapshot.getValue(OrderPatameters.class);
                    itemnams.setText(": " + o.getName());
                    odate.setText(": " + o.getDdate());
                    if (o.getDdate() != null) {
                        ddate.setText(": " + o.getDdate());
                    } else {
                        ddate.setText("not deliverd");
                    }
                    if (o.getDid() != null) {
                        getempname(o.getDid());
                    } else {
                        dbname.setText("not update");
                        dbphno.setText("not update");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG , databaseError.toString());
            }
        });

    }

    void getempname(String id) {
        AppUtil.EMPURL.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    EmployeParameters e = dataSnapshot.getValue(EmployeParameters.class);
                    dbname.setText(": " + e.getEname());
                    dbphno.setText(e.getEusname());

                    if(e.getLat()!=null){
                        LatLng userpoint = new LatLng(Double.parseDouble(e.getLat()), Double.parseDouble(e.getLang()));
                        mMap.addMarker(new MarkerOptions().position(userpoint).title("Delivery Boy").icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.usloc_icon)));
                        mMap.setIndoorEnabled(true);
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userpoint, 15f));
                    }

                } else {
                    dbname.setText("not update");
                    dbphno.setText("not update");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG , databaseError.toString());
            }
        });
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        // Add a marker in Sydney and move the camera
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
