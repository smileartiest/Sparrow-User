package com.smile.atozapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.smile.atozapp.controller.AppUtil;
import com.smile.atozapp.models.OfferHold;
import com.smile.atozapp.parameters.OfferParameters;
import com.smile.atozapp.R;

public class Offer extends Fragment {

    View v;
    RecyclerView offerlist;

    public Offer() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.frag_offer , container , false);
        offerlist = v.findViewById(R.id.offer_list);
        offerlist.setHasFixedSize(true);
        offerlist.setLayoutManager(new LinearLayoutManager(getContext()));

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<OfferParameters, OfferHold> frecyle = new FirebaseRecyclerAdapter<OfferParameters, OfferHold>(
                OfferParameters.class , R.layout.offer_row , OfferHold.class , AppUtil.OFFERURL
        ) {
            @Override
            protected void populateViewHolder(OfferHold offerHold, OfferParameters offerParameters, int i) {
                offerHold.setdetails(getContext() , offerParameters.getId() , offerParameters.getPicurl());
            }
        };
        offerlist.setAdapter(frecyle);

    }

}
