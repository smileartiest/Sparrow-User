package com.smile.atozapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

public class ViewMoreDetails extends AppCompatActivity {

    private GoogleMap mMap;
    TextView itemnams, odate, dbname, dbphno, ddate;
    private static String TAG = "vmoredetails";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_more_details);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
