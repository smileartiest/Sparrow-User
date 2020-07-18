package com.smile.atozapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.smile.atozapp.R;
import com.smile.atozapp.controller.AppUtil;
import com.smile.atozapp.models.NotifyHold;
import com.smile.atozapp.parameters.NotificationParameters;

public class Notification extends Fragment {

    View view;
    RecyclerView list;
    ConstraintLayout nodata;

    public Notification() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.frag_notification , container , false);

        list = view.findViewById(R.id.notify_list);
        //nodata = v.findViewById(R.id.notify_nodata);

        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(getContext()));

        getdata();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<NotificationParameters, NotifyHold> frecycle = new FirebaseRecyclerAdapter<NotificationParameters, NotifyHold>(
                NotificationParameters.class , R.layout.row_notification , NotifyHold.class , AppUtil.NOTIFYURL
        ) {
            @Override
            protected void populateViewHolder(NotifyHold nh, NotificationParameters np, int i) {
                nh.setdetails(getContext() , np.getId() , np.getTitle() , np.getMessage() , np.getUrl());
            }
        };
        list.setAdapter(frecycle);

    }

    void getdata(){
        AppUtil.NOTIFYURL.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){
                    list.setVisibility(View.VISIBLE);
                    //nodata.setVisibility(View.GONE);
                }else{
                    list.setVisibility(View.INVISIBLE);
                    //nodata.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
