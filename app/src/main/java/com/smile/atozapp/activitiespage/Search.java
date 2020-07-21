package com.smile.atozapp.activitiespage;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.smile.atozapp.R;
import com.smile.atozapp.controller.AppUtil;
import com.smile.atozapp.helper.CSAlertDialog;
import com.smile.atozapp.models.DressHold;
import com.smile.atozapp.models.MarketHold;
import com.smile.atozapp.parameters.DressParameters;
import com.smile.atozapp.parameters.MarketParameters;

import java.util.ArrayList;
import java.util.List;

public class Search extends AppCompatActivity {

    View v;
    AutoCompleteTextView searchbar ;
    ImageView searchbtn;
    ArrayList<String> marketlist = new ArrayList<>();
    ConstraintLayout nodata;

    RecyclerView list;
    TextView searchcount;
    Toolbar mytoolbar;

    Query q1,q2,q3;
    CSAlertDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_page);

        mytoolbar = findViewById(R.id.search_toolbar);
        setSupportActionBar(mytoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mytoolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);

        searchbar = findViewById(R.id.search_search);
        list = findViewById(R.id.search_list);
        searchcount = findViewById(R.id.search_totalcount);
        searchbtn = findViewById(R.id.search_search_btn);
        nodata = findViewById(R.id.search_noitem);

        nodata.setVisibility(View.VISIBLE);

        mytoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        dialog = new CSAlertDialog(this);
        dialog.ShowDialog();

        AppUtil.MARKETURL.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){
                    for(DataSnapshot data : dataSnapshot.getChildren()){
                        marketlist.add(data.child("mname").getValue().toString());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

        AppUtil.DRESSURL.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue()!=null){
                    for(DataSnapshot data : snapshot.getChildren()){
                        marketlist.add(data.child("dname").getValue().toString());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        AppUtil.ELECTRONICURL.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue()!=null){
                    for(DataSnapshot data : snapshot.getChildren()){
                        marketlist.add(data.child("dname").getValue().toString());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        ArrayAdapter<String> ad = new ArrayAdapter<>(Search.this , R.layout.spinlist , marketlist);
        searchbar.setAdapter(ad);
        searchbar.setThreshold(1);
        dialog.CancelDialog();

    }

    @Override
    public void onResume() {
        super.onResume();

        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setdetails1(searchbar.getText().toString());
            }
        });

    }

    public void setdetails1(final String itemname){
        q1 = AppUtil.MARKETURL.orderByChild("mname").equalTo(itemname);
        q2 = AppUtil.DRESSURL.orderByChild("dname").equalTo(itemname);
        q3 = AppUtil.ELECTRONICURL.orderByChild("dname").equalTo(itemname);

        q1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue()!=null){
                    searchcount.setText("Total search products : "+snapshot.getChildrenCount());
                    setDetailsMarket();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        q2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue()!=null){
                    searchcount.setText("Total search products : "+snapshot.getChildrenCount());
                    setDetailsDress();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        q3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue()!=null){
                    searchcount.setText("Total search products : "+snapshot.getChildrenCount());
                    setDetailsElectronic();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    void setDetailsMarket(){
        FirebaseRecyclerAdapter<MarketParameters, MarketHold> frecycle = new FirebaseRecyclerAdapter<MarketParameters, MarketHold>(
                MarketParameters.class , R.layout.row_marketitems , MarketHold.class , q1
        ) {
            @Override
            protected void populateViewHolder(MarketHold mh, MarketParameters mp, int i) {
                mh.setdetails(Search.this , mp.getId(),mp.getMpic(),mp.getMname(),mp.getMtype(),mp.getMcatg(),"veg" ,mp.getMqnt(),mp.getMam() , mp.getStock());
            }
        };
        list.setLayoutManager(new LinearLayoutManager(Search.this));list.setHasFixedSize(true);
        list.setAdapter(frecycle);
        nodata.setVisibility(View.GONE);
    }
    void setDetailsDress(){
        FirebaseRecyclerAdapter<DressParameters, DressHold> frecycle = new FirebaseRecyclerAdapter<DressParameters, DressHold>(
                DressParameters.class , R.layout.row_dress , DressHold.class , q2
        ) {
            @Override
            protected void populateViewHolder(DressHold mh, DressParameters dp, int i) {
                mh.setdetails(Search.this , dp.getId(), dp.getDname(), dp.getDtype(), dp.getDam(), dp.getDoff(), dp.getDpic() , dp.getStock() ,dp.getCat());
            }
        };
        list.setLayoutManager(new GridLayoutManager(Search.this , 2 ));list.setHasFixedSize(true);
        list.setAdapter(frecycle);
        nodata.setVisibility(View.GONE);
    }

    void setDetailsElectronic(){
        FirebaseRecyclerAdapter<DressParameters, DressHold> frecycle = new FirebaseRecyclerAdapter<DressParameters, DressHold>(
                DressParameters.class , R.layout.row_dress , DressHold.class , q3
        ) {
            @Override
            protected void populateViewHolder(DressHold mh, DressParameters dp, int i) {
                mh.setdetails(Search.this , dp.getId(), dp.getDname(), dp.getDtype(), dp.getDam(), dp.getDoff(), dp.getDpic() , dp.getStock() ,dp.getCat());
            }
        };
        list.setLayoutManager(new GridLayoutManager(Search.this , 2 ));list.setHasFixedSize(true);
        list.setAdapter(frecycle);
        nodata.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
