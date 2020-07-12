package com.smile.atozapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.smile.atozapp.controller.AppUtil;
import com.smile.atozapp.models.DressHold;
import com.smile.atozapp.parameters.DressParameters;

public class ViewDressDetails extends AppCompatActivity {

    RecyclerView list;
    TextView listcount;

    ConstraintLayout nodata;
    TextView tryagain;
    Query q;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_dress_details);

        listcount = findViewById(R.id.dress_full_listcount);
        list = findViewById(R.id.dress_full_list);
        nodata = findViewById(R.id.dress_full_noitem);
        tryagain = findViewById(R.id.no_item_tryagainbtn);

        list.setHasFixedSize(true);

        if (getIntent().getStringExtra("k").equals("all")) {
            viewall();
        } else {
            viewdetails(getIntent().getStringExtra("k"));
        }

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
                dressHold.setdetails(getApplicationContext(), dp.getId(), dp.getDname(), dp.getDtype(), dp.getDam(), dp.getDoff(), dp.getDpic() , dp.getStock());
            }
        };
        list.setAdapter(frecycle);
    }

    public void viewdetails(String categ) {
        q = AppUtil.DRESSURL.orderByChild("catg").equalTo(categ);
        getcount(q);
        FirebaseRecyclerAdapter<DressParameters, DressHold> frecycle = new FirebaseRecyclerAdapter<DressParameters, DressHold>(
                DressParameters.class, R.layout.row_dress, DressHold.class, q
        ) {
            @Override
            protected void populateViewHolder(DressHold dressHold, DressParameters dp, int i) {
                dressHold.setdetails(getApplicationContext(), dp.getId(), dp.getDname(), dp.getDtype(), dp.getDam(), dp.getDoff(), dp.getDpic(),dp.getStock());
            }
        };
        list.setAdapter(frecycle);
    }

    public void getcount(Query df) {
        df.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    listcount.setText("Total Products : " + dataSnapshot.getChildrenCount());
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

}
