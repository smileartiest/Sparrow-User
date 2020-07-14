package com.smile.atozapp.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.smile.atozapp.ViewDressDetails;
import com.smile.atozapp.ViewMarketDetails;
import com.smile.atozapp.controller.AppUtil;
import com.smile.atozapp.models.DressHold;
import com.smile.atozapp.parameters.DressParameters;
import com.smile.atozapp.R;
import com.smile.atozapp.parameters.OfferParameters;
import com.smile.atozapp.models.SliderAdapter;

import java.util.ArrayList;

public class Home extends Fragment {

    View v;
    SliderView sliderView;
    ArrayList<String> imglist = new ArrayList<>();
    TextView viewall_market,viewall_food,viewall_market1,viewall_dress,viewall_electronics;

    public Home() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.frag_home , container , false);
        sliderView = v.findViewById(R.id.home_brandSlider);

        viewall_market = v.findViewById(R.id.home_viewall_market);
        viewall_food = v.findViewById(R.id.home_viewall_market_foods);
        viewall_market1 = v.findViewById(R.id.home_viewall_market1);
        viewall_electronics = v.findViewById(R.id.home_viewall_electronics);
        viewall_dress = v.findViewById(R.id.home_viewall_dress);

        getimages();
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onResume() {
        super.onResume();

        viewall_market.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext() , ViewMarketDetails.class).putExtra("k","all"));
            }
        });

        viewall_market1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext() , ViewMarketDetails.class).putExtra("k","all"));
            }
        });

        viewall_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext() , ViewMarketDetails.class).putExtra("k","all"));
            }
        });

        viewall_electronics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(getContext() , ViewDressDetails.class).putExtra("k","all"));
            }
        });

        viewall_dress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext() , ViewDressDetails.class).putExtra("k","all"));
            }
        });

    }

    public void getimages(){
        AppUtil.OFFERURL.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){
                    for (DataSnapshot data : dataSnapshot.getChildren()){
                        imglist.add(data.child("picurl").getValue().toString());
                    }
                    viewimage();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void viewimage()
    {
        SliderAdapter s = new SliderAdapter(getActivity() , imglist);

        sliderView.setSliderAdapter(s);

        sliderView.setIndicatorAnimation(IndicatorAnimations.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(5); //set scroll delay in seconds :
        sliderView.startAutoCycle();

    }

}
