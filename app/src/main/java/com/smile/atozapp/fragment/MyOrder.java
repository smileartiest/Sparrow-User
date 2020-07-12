package com.smile.atozapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.smile.atozapp.LoginMain;
import com.smile.atozapp.R;
import com.smile.atozapp.controller.AppUtil;
import com.smile.atozapp.controller.TempData;
import com.smile.atozapp.models.MyOrderHold;
import com.smile.atozapp.parameters.OrderPatameters;

public class MyOrder extends Fragment {

    View v;
    RecyclerView mylist;
    Query q;
    ConstraintLayout nodata;
    TextView gomarket;

    public MyOrder() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.frag_myorder, container, false);
        mylist = v.findViewById(R.id.myorder_list);
        mylist.setLayoutManager(new LinearLayoutManager(getContext()));
        mylist.setHasFixedSize(true);
        nodata = v.findViewById(R.id.myorder_no_order);
        gomarket = v.findViewById(R.id.no_order_gomarket);

        q = AppUtil.ORDERURl.orderByChild("uid").equalTo(new TempData(getContext()).getuid());

        getdata();
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<OrderPatameters, MyOrderHold> frecycle = new FirebaseRecyclerAdapter<OrderPatameters, MyOrderHold>(
                OrderPatameters.class, R.layout.row_myorder, MyOrderHold.class, q
        ) {
            @Override
            protected void populateViewHolder(MyOrderHold oh, OrderPatameters op, int i) {
                oh.setdetails(getContext(), op.getId(), op.getUid(), op.getName(), op.getSize(), op.getQnt(), op.getAm(), op.getBam(), op.getAddres(), op.getPmode(), op.getSts());
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
                startActivity(new Intent(getContext() , LoginMain.class));
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
                    nodata.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
