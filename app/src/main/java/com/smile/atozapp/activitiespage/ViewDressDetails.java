package com.smile.atozapp.activitiespage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.smile.atozapp.R;
import com.smile.atozapp.controller.AppUtil;
import com.smile.atozapp.models.DressHold;
import com.smile.atozapp.parameters.DressParameters;

public class ViewDressDetails extends AppCompatActivity {

    RecyclerView list;
    TextView listcount,title_text;
    Toolbar mytoolbar;

    ConstraintLayout nodata;
    TextView tryagain;
    Query q;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_dress_details);

        mytoolbar = findViewById(R.id.dress_full_toolbar);
        setSupportActionBar(mytoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mytoolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);

        listcount = findViewById(R.id.dress_full_listcount);
        list = findViewById(R.id.dress_full_list);
        nodata = findViewById(R.id.dress_full_noitem);
        tryagain = findViewById(R.id.no_item_tryagainbtn);
        title_text = findViewById(R.id.dress_full_title);

        list.setHasFixedSize(true);

        title_text.setText(getIntent().getStringExtra("tit"));

        if(getIntent().getStringExtra("f").equals("dress")) {
            if (getIntent().getStringExtra("k").equals("all")) {
                viewall();
            } else {
                viewdetails(getIntent().getStringExtra("k"));
            }
        }else if(getIntent().getStringExtra("f").equals("electronics")){
            if (getIntent().getStringExtra("k").equals("all")) {
                viewall1();
            } else {
                viewdetails1(getIntent().getStringExtra("k"));
            }
        }

        mytoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        tryagain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ViewDressDetails.this , LoginMain.class));finish();
            }
        });

    }

    public void viewall(){
        getcount(AppUtil.DRESSURL);
        FirebaseRecyclerAdapter<DressParameters, DressHold> frecycle = new FirebaseRecyclerAdapter<DressParameters, DressHold>(
                DressParameters.class, R.layout.row_dress, DressHold.class, AppUtil.DRESSURL
        ) {
            @Override
            protected void populateViewHolder(DressHold dressHold, DressParameters dp, int i) {
                dressHold.setdetails(getApplicationContext(), dp.getId(), dp.getDname(), dp.getDtype(), dp.getDam(), dp.getDoff(), dp.getDpic() , dp.getStock() ,dp.getCat());
            }
        };
        list.setAdapter(frecycle);
    }

    public void viewall1(){
        getcount(AppUtil.ELECTRONICURL);
        FirebaseRecyclerAdapter<DressParameters, DressHold> frecycle = new FirebaseRecyclerAdapter<DressParameters, DressHold>(
                DressParameters.class, R.layout.row_dress, DressHold.class, AppUtil.ELECTRONICURL
        ) {
            @Override
            protected void populateViewHolder(DressHold dressHold, DressParameters dp, int i) {
                dressHold.setdetails(getApplicationContext(), dp.getId(), dp.getDname(), dp.getDtype(), dp.getDam(), dp.getDoff(), dp.getDpic() , dp.getStock() , dp.getCat());
            }
        };
        list.setAdapter(frecycle);
    }

    public void viewdetails(String categ) {
        q = AppUtil.DRESSURL.orderByChild("categ").equalTo(categ);
        getcount(q);
        FirebaseRecyclerAdapter<DressParameters, DressHold> frecycle = new FirebaseRecyclerAdapter<DressParameters, DressHold>(
                DressParameters.class, R.layout.row_dress, DressHold.class, q
        ) {
            @Override
            protected void populateViewHolder(DressHold dressHold, DressParameters dp, int i) {
                dressHold.setdetails(getApplicationContext(), dp.getId(), dp.getDname(), dp.getDtype(), dp.getDam(), dp.getDoff(), dp.getDpic(),dp.getStock(),dp.getCat());
            }
        };
        list.setAdapter(frecycle);
    }

    public void viewdetails1(String categ) {
        q = AppUtil.ELECTRONICURL.orderByChild("categ").equalTo(categ);
        getcount(q);
        FirebaseRecyclerAdapter<DressParameters, DressHold> frecycle = new FirebaseRecyclerAdapter<DressParameters, DressHold>(
                DressParameters.class, R.layout.row_dress, DressHold.class, q
        ) {
            @Override
            protected void populateViewHolder(DressHold dressHold, DressParameters dp, int i) {
                dressHold.setdetails(getApplicationContext(), dp.getId(), dp.getDname(), dp.getDtype(), dp.getDam(), dp.getDoff(), dp.getDpic(),dp.getStock(),dp.getCat());
            }
        };
        list.setAdapter(frecycle);
    }

    public void getcount(Query df) {
        df.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    listcount.setText("Total Dress collection's  " + dataSnapshot.getChildrenCount());
                } else {
                    nodata.setVisibility(View.VISIBLE);
                    listcount.setText("Total Products : 0");
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
