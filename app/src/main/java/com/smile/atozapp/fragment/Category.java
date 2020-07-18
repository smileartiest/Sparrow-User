package com.smile.atozapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.smile.atozapp.R;
import com.smile.atozapp.activitiespage.ViewDressDetails;
import com.smile.atozapp.activitiespage.ViewMarketDetails;

public class Category extends Fragment {

    View v;

    TextView fruit,foodgrains,bakery,beverage,snaks,beauty,household,eqqs,babycare,viewmens,viewwomens,viewkids,electronic_pic1,electronic_pic2,electronic_pic3,electronic_pic4,electronic_pic5,electronic_pic6;

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

        electronic_pic1 = v.findViewById(R.id.electronic_mobile);
        electronic_pic2 = v.findViewById(R.id.electronic_headset);
        electronic_pic3 = v.findViewById(R.id.electronic_speaker);
        electronic_pic4 = v.findViewById(R.id.electronic_charger);
        electronic_pic5 = v.findViewById(R.id.electronic_mobileback_case);
        electronic_pic6 = v.findViewById(R.id.electronic_watch);

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
                startActivity(new Intent(getContext() , ViewDressDetails.class).putExtra("f","dress").putExtra("k","Mens"));
            }
        });

        viewwomens.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext() , ViewDressDetails.class).putExtra("f","dress").putExtra("k","Womens"));
            }
        });

        viewkids.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext() , ViewDressDetails.class).putExtra("f","dress").putExtra("k","Kids"));
            }
        });

        electronic_pic1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext() , ViewDressDetails.class).putExtra("f","electronics").putExtra("tit","Mobiles").putExtra("k","Mobiles"));
            }
        });

        electronic_pic2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext() , ViewDressDetails.class).putExtra("f","electronics").putExtra("tit","Headset").putExtra("k","Headset"));
            }
        });

        electronic_pic3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext() , ViewDressDetails.class).putExtra("f","electronics").putExtra("tit","Speakers").putExtra("k","Speakers"));
            }
        });

        electronic_pic4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext() , ViewDressDetails.class).putExtra("f","electronics").putExtra("tit","Charger").putExtra("k","Charger"));
            }
        });

        electronic_pic5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext() , ViewDressDetails.class).putExtra("f","electronics").putExtra("tit","Mobile Back Case").putExtra("k","Mobile Back Case"));
            }
        });

        electronic_pic6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext() , ViewDressDetails.class).putExtra("f","electronics").putExtra("tit","Mens & Womens watch").putExtra("k","Mens & Womens watch"));
            }
        });

    }
}
