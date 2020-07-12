package com.smile.atozapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.smile.atozapp.R;
import com.smile.atozapp.ViewDressDetails;
import com.smile.atozapp.ViewMarketDetails;

public class Category extends Fragment {

    View v;

    TextView fruit,foodgrains,bakery,beverage,snaks,beauty,household,eqqs,babycare,viewmens,viewwomens,viewkids;

    public Category() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.frag_category , container , false);

        fruit = v.findViewById(R.id.card_fruits);
        foodgrains = v.findViewById(R.id.card_foodgrains);
        bakery = v.findViewById(R.id.card_bakery);
        beverage = v.findViewById(R.id.card_beverage);
        snaks = v.findViewById(R.id.card_snaks);
        beauty = v.findViewById(R.id.card_beauty);
        household = v.findViewById(R.id.card_household);
        eqqs = v.findViewById(R.id.card_eqq);
        babycare = v.findViewById(R.id.card_babycare);
        viewmens = v.findViewById(R.id.dress_mens);
        viewwomens = v.findViewById(R.id.dress_womens);
        viewkids = v.findViewById(R.id.dress_kids);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        fruit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext() , ViewMarketDetails.class).putExtra("k","Fruits & Vegetables"));
            }
        });

        foodgrains.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext() , ViewMarketDetails.class).putExtra("k","Foodgrains , Oil & Masala"));
            }
        });

        bakery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext() , ViewMarketDetails.class).putExtra("k","Bakery , Cakes & Dairy"));
            }
        });

        beverage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext() , ViewMarketDetails.class).putExtra("k","Beverages"));
            }
        });

        snaks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext() , ViewMarketDetails.class).putExtra("k","Snaks & Branded Foods"));
            }
        });

        beauty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext() , ViewMarketDetails.class).putExtra("k","Beauty & Hygiene"));
            }
        });

        household.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext() , ViewMarketDetails.class).putExtra("k","Cleaning & Household"));
            }
        });

        eqqs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext() , ViewMarketDetails.class).putExtra("k","Eggs , Meat & Fish"));
            }
        });

        babycare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext() , ViewMarketDetails.class).putExtra("k","Baby Care & Personal Care"));
            }
        });

        viewmens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext() , ViewDressDetails.class).putExtra("k","Mens"));
            }
        });

        viewwomens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext() , ViewDressDetails.class).putExtra("k","Womens"));
            }
        });

        viewkids.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext() , ViewDressDetails.class).putExtra("k","Kids"));
            }
        });

    }
}
