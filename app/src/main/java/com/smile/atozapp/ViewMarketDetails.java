package com.smile.atozapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.smile.atozapp.controller.AppUtil;
import com.smile.atozapp.controller.TempData;
import com.smile.atozapp.models.MarketHold;
import com.smile.atozapp.parameters.MarketParameters;

public class ViewMarketDetails extends AppCompatActivity {

    RecyclerView list;
    TextView totalcount;
    ConstraintLayout nodata;
    TextView tryagain;
    Toolbar mytoolbar;
    ConstraintLayout order_dialog;
    TextView dialog_title;
    Button dialog_complete;

    Query q;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_market_details);

        order_dialog = findViewById(R.id.view_market_didalog);
        dialog_title = order_dialog.findViewById(R.id.view_market_d_title);
        dialog_complete = order_dialog.findViewById(R.id.view_market_d_conformbtn);

        mytoolbar = findViewById(R.id.view_market_toolbar);
        setSupportActionBar(mytoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mytoolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);

        mytoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        list = findViewById(R.id.view_market_list);
        totalcount = findViewById(R.id.view_market_total);
        nodata = findViewById(R.id.view_market_noitem);
        tryagain = findViewById(R.id.no_item_tryagainbtn);

        list.setLayoutManager(new LinearLayoutManager(this));
        list.setHasFixedSize(true);

        if (getIntent().getStringExtra("k").equals("all")) {
            q = AppUtil.MARKETURL;
        } else {
            q = AppUtil.MARKETURL.orderByChild("mcatg").equalTo(getIntent().getStringExtra("k"));
        }

        tryagain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ViewMarketDetails.this , LoginMain.class));finish();
            }
        });

        getcount();

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<MarketParameters, MarketHold> frecycle = new FirebaseRecyclerAdapter<MarketParameters, MarketHold>(
                MarketParameters.class, R.layout.row_marketitems, MarketHold.class, q
        ) {
            @Override
            protected void populateViewHolder(MarketHold mh, MarketParameters mp, int i) {
                mh.setdetails(getApplicationContext(), mp.getId(), mp.getMpic(), mp.getMname(), mp.getMtype(), mp.getMcatg(), mp.getMqnt(), mp.getMam() , mp.getStock());
            }
        };
        list.setAdapter(frecycle);

    }

    @Override
    protected void onResume() {
        super.onResume();

        AppUtil.CARTURL.child(new TempData(ViewMarketDetails.this).getuid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue()!=null){
                    dialog_title.setText("Total Cart Items "+ snapshot.getChildrenCount());
                    order_dialog.setVisibility(View.VISIBLE);
                }else{
                    order_dialog.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        dialog_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext() , MyCart.class));
            }
        });

    }

    public void getcount() {
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    totalcount.setText("Total search products : " + dataSnapshot.getChildrenCount());
                } else {
                    totalcount.setText("Total search 0");
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
