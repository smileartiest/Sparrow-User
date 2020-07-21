package com.smile.atozapp.activitiespage;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.smile.atozapp.R;
import com.smile.atozapp.controller.AppUtil;
import com.smile.atozapp.controller.TempData;
import com.smile.atozapp.models.TrackHold;
import com.smile.atozapp.parameters.OrderPatameters;

public class MyOrder_Page extends AppCompatActivity {

    View v;
    RecyclerView mylist;
    Query q;
    ConstraintLayout nodata;
    TextView gomarket;
    Toolbar mytoolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myorder_page);

        mytoolbar = findViewById(R.id.myorder_toolbar);
        setSupportActionBar(mytoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mytoolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);

        mylist = findViewById(R.id.myorder_list);
        mylist.setLayoutManager(new LinearLayoutManager(this));
        mylist.setHasFixedSize(true);
        nodata = findViewById(R.id.myorder_no_order);
        gomarket = findViewById(R.id.no_order_gomarket);

        q = AppUtil.ORDERURl.orderByChild("uid").equalTo(new TempData(this).getuid());

        getdata();

    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<OrderPatameters , TrackHold> frecycle = new FirebaseRecyclerAdapter<OrderPatameters, TrackHold>(
                OrderPatameters.class , R.layout.row_track_order , TrackHold.class , q
        ) {
            @Override
            protected void populateViewHolder(TrackHold trackHold, OrderPatameters op, int i) {
                trackHold.setdetails(MyOrder_Page.this , op.getId() , op.getName() , op.getSts());
            }
        };
        mylist.setAdapter(frecycle);

    }

    @Override
    public void onResume() {
        super.onResume();

        gomarket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyOrder_Page.this , LoginMain.class));
            }
        });

        mytoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    void getdata() {
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    nodata.setVisibility(View.GONE);
                } else {
                    mylist.setVisibility(View.GONE);
                    nodata.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
