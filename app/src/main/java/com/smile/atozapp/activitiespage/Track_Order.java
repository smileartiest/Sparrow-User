package com.smile.atozapp.activitiespage;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.smile.atozapp.R;
import com.smile.atozapp.controller.AppUtil;
import com.smile.atozapp.helper.CSAlertDialog;
import com.smile.atozapp.parameters.BillingParameters;
import com.smile.atozapp.parameters.OrderPatameters;

public class Track_Order extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    View view_new,view_taken,view_pending,view_complete;
    LottieAnimationView animationView;
    TextView top_title,top_amount,title_status,message_txt;
    CSAlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.track__order);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.track_order_map);
        mapFragment.getMapAsync(this);

        dialog = new CSAlertDialog(Track_Order.this);

        view_new = findViewById(R.id.track_order_new_view);
        view_taken = findViewById(R.id.track_order_taken_view);
        view_pending = findViewById(R.id.track_order_pending_view);
        view_complete = findViewById(R.id.track_order_complete_view);

        animationView = findViewById(R.id.track_order_animation);
        top_title = findViewById(R.id.track_order_top_title);
        top_amount = findViewById(R.id.track_order_top_amount);
        title_status = findViewById(R.id.track_order_title);
        message_txt = findViewById(R.id.track_order_message);

        if(getIntent().hasExtra("oid")){
            top_title.setText("your Order ID ( "+getIntent().getStringExtra("oid")+" )");
        }else{
            top_title.setText("No order found");
        }

        if(getIntent().getStringExtra("oid")!=null){
            AppUtil.ORDERURl.child(getIntent().getStringExtra("oid")).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()) {
                        if (snapshot.getValue() != null) {
                            OrderPatameters o = snapshot.getValue(OrderPatameters.class);
                            if(o.getSts().equals("new")){
                                title_status.setText("Your Order was waiting to taken by Market.");
                                message_txt.setText("Please wait.Process going on");
                                animationView.setAnimation(R.raw.search_gif);
                                animationView.playAnimation();
                                animationView.loop(true);
                            }else if (o.getSts().equals("taken")){
                                view_taken.setBackground(new ColorDrawable(Color.BLACK));
                                title_status.setText("Your Order was taken by Market.");
                                message_txt.setText("Please wait. Your Parcel was packing.");
                                animationView.setAnimation(R.raw.packing_order);
                                animationView.playAnimation();
                                animationView.loop(true);
                            }else if (o.getSts().equals("pending")){
                                view_pending.setBackground(new ColorDrawable(Color.BLACK));
                                title_status.setText("Your Order packing completed  by Market.");
                                message_txt.setText("your Order was taken by Delivery boy.");
                                animationView.setAnimation(R.raw.pickup_complete_gif);
                                animationView.playAnimation();
                                animationView.loop(true);
                            }else if (o.getSts().equals("complete")){
                                view_complete.setBackground(new ColorDrawable(Color.BLACK));
                                title_status.setText("Your Order was deliver Successfully");
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialog.ShowDialog();
                                        complete_dialog();
                                    }
                                },3000);
                            }else if (o.getSts().equals("cancel")){
                                title_status.setText("Your Order was Canceled by Market.");
                            }
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            AppUtil.BILLINGURl.child(getIntent().getStringExtra("oid")).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.getValue()!=null){
                        BillingParameters b = snapshot.getValue(BillingParameters.class);
                        top_amount.setText("To Pay Rs. "+b.getTotal_amount());
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }
    }
    void complete_dialog(){
        CFAlertDialog.Builder builder = new CFAlertDialog.Builder(this);
        builder.setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT);
        builder.setIcon(R.drawable.sparrowiconsmall);
        builder.setCornerRadius(20);
        builder.setTitle("Congratulation !");
        builder.setMessage("Your Order was successfully delivered. Thanks for your valuable Order.");
        builder.addButton("EXIT", -1, -1, CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog1, int which) {
                dialog1.dismiss();
                dialog.CancelDialog();
                finish();
            }
        });
        builder.show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }
}