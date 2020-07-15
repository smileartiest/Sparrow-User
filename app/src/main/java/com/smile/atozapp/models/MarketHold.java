package com.smile.atozapp.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.smile.atozapp.R;
import com.smile.atozapp.controller.AppUtil;
import com.smile.atozapp.controller.TempData;
import com.smile.atozapp.controller.TempOrder;
import com.smile.atozapp.parameters.CartParameters;

import static android.content.Context.MODE_PRIVATE;

public class MarketHold extends RecyclerView.ViewHolder {

    ImageView pic, complete_sts;
    TextView name, category, price, stocksts;
    Spinner qntyty;
    LottieAnimationView loading_gif;
    TempData td;
    Button addbtn;


    public MarketHold(@NonNull View itemView) {
        super(itemView);
        pic = itemView.findViewById(R.id.row_market_pic);
        name = itemView.findViewById(R.id.row_market_name);
        category = itemView.findViewById(R.id.row_market_category);
        qntyty = itemView.findViewById(R.id.row_market_qnty);
        price = itemView.findViewById(R.id.row_market_price);
        complete_sts = itemView.findViewById(R.id.row_market_complete);
        loading_gif = itemView.findViewById(R.id.row_market_loading_gif);
        stocksts = itemView.findViewById(R.id.row_market_stock);
        addbtn = itemView.findViewById(R.id.row_market_addbtn);
    }

    public void setdetails(final Context c1, final String id1, final String mpic, final String mname, String mtype, final String mcatg, String mqnt, String mam, final String stock1) {
        td = new TempData(c1);

        Glide.with(c1).load(mpic).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                loading_gif.setVisibility(View.VISIBLE);
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                loading_gif.setVisibility(View.GONE);
                return false;
            }
        }).into(pic);
        name.setText(mname);
        category.setText(mcatg);

        final String[] qntlist = mqnt.split(",");
        final String[] amlist = mam.split(",");
        ArrayAdapter<String> ad = new ArrayAdapter<>(c1, R.layout.spinlist, qntlist);
        qntyty.setAdapter(ad);

        price.setText(amlist[0]);

        qntyty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                checkItem(id1);
                price.setText(amlist[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (stock1.equals("outstock")) {
            stocksts.setVisibility(View.VISIBLE);
            addbtn.setVisibility(View.INVISIBLE);
        } else {
            stocksts.setVisibility(View.GONE);
        }

        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addbtn.setVisibility(View.GONE);
                complete_sts.setVisibility(View.VISIBLE);
                String key = AppUtil.CARTURL.push().getKey();
                CartParameters c = new CartParameters(key, id1, mpic, mname, mcatg, price.getText().toString(), qntyty.getSelectedItem().toString(), "1");
                AppUtil.CARTURL.child(new TempData(c1).getuid()).child(key).setValue(c);
                new TempOrder(c1).addcartid(key);
            }
        });
        checkItem(id1);
    }

    void checkItem(String id1) {
        Query q = AppUtil.CARTURL.child(td.getuid()).orderByChild("mid").equalTo(id1);
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    String tempq = null;
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        CartParameters c = data.getValue(CartParameters.class);
                        if(c.getSize().equals(qntyty.getSelectedItem().toString())){
                            tempq = c.getSize();
                        }
                    }
                    if (qntyty.getSelectedItem().toString().equals(tempq)) {
                        addbtn.setVisibility(View.INVISIBLE);
                        complete_sts.setVisibility(View.VISIBLE);
                    }else{
                        addbtn.setVisibility(View.VISIBLE);
                        complete_sts.setVisibility(View.INVISIBLE);
                    }
                } else {
                    addbtn.setVisibility(View.VISIBLE);
                    complete_sts.setVisibility(View.INVISIBLE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
