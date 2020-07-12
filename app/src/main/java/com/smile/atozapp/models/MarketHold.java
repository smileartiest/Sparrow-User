package com.smile.atozapp.models;

import android.content.Context;
import android.content.SharedPreferences;
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

import com.bumptech.glide.Glide;
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

    public MarketHold(@NonNull View itemView) {
        super(itemView);
    }

    public void setdetails(final Context c1, final String id, final String mpic, final String mname, String mtype, final String mcatg, String mqnt, String mam , final String stock1) {

        ImageView pic = itemView.findViewById(R.id.row_market_pic);
        TextView name = itemView.findViewById(R.id.row_market_name);
        TextView category = itemView.findViewById(R.id.row_market_category);
        final Spinner qntyty = itemView.findViewById(R.id.row_market_qnty);
        final TextView price = itemView.findViewById(R.id.row_market_price);
        final TextView qntrytext = itemView.findViewById(R.id.row_market_totalqn);
        final LinearLayout qntytybox = itemView.findViewById(R.id.row_market_qntytybox);
        final TextView addicon = itemView.findViewById(R.id.row_market_addicon);
        final TextView removeicon = itemView.findViewById(R.id.row_market_removeicon);
        TextView stocksts = itemView.findViewById(R.id.row_market_stock);

        final TempData td = new TempData(c1);
        final TempOrder to = new TempOrder(c1);


        final Button addbtn = itemView.findViewById(R.id.row_market_addbtn);

        Glide.with(c1).load(mpic).into(pic);
        name.setText(mname);
        category.setText(mcatg);

        String[] qntlist = mqnt.split(",");
        final String[] amlist = mam.split(",");
        ArrayAdapter<String> ad = new ArrayAdapter<>(c1, R.layout.spinlist, qntlist);
        qntyty.setAdapter(ad);

        price.setText(amlist[0]);

        if(stock1.equals("outstock")){
            stocksts.setVisibility(View.VISIBLE);
            addbtn.setVisibility(View.INVISIBLE);
            qntytybox.setVisibility(View.INVISIBLE);
        }else{
            stocksts.setVisibility(View.GONE);
        }

        qntyty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                price.setText(amlist[position]);
                Query q = AppUtil.CARTURL.child(td.getuid()).orderByChild("am").equalTo(amlist[position]);
                q.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null) {
                            addbtn.setVisibility(View.INVISIBLE);
                            qntytybox.setVisibility(View.VISIBLE);
                            qntrytext.setText("2");
                            if (dataSnapshot.getChildrenCount() == 1) {
                                for (DataSnapshot data : dataSnapshot.getChildren()) {
                                    qntrytext.setText(data.child("qnt").getValue().toString());
                                }
                            } else if (dataSnapshot.getChildrenCount() == 2) {

                            }
                        } else {
                            if(stock1.equals("instock")) {
                                addbtn.setVisibility(View.VISIBLE);
                                qntytybox.setVisibility(View.INVISIBLE);
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addbtn.setVisibility(View.INVISIBLE);
                qntytybox.setVisibility(View.VISIBLE);
                qntrytext.setText("1");
                String key = AppUtil.CARTURL.push().getKey();
                CartParameters c = new CartParameters(key, id, mpic, mname, mcatg, price.getText().toString(), qntyty.getSelectedItem().toString(), "1");
                AppUtil.CARTURL.child(new TempData(c1).getuid()).child(key).setValue(c);
                new TempOrder(c1).addcartid(key);
            }
        });

        addicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = Integer.parseInt(qntrytext.getText().toString()) + 1;
                qntrytext.setText(String.valueOf(temp));
                AppUtil.CARTURL.child(td.getuid()).child(to.getcid()).child("qnt").setValue(String.valueOf(temp));
            }
        });
        removeicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Integer.parseInt(qntrytext.getText().toString()) > 0) {
                    int temp = Integer.parseInt(qntrytext.getText().toString()) - 1;
                    qntrytext.setText(String.valueOf(temp));
                    AppUtil.CARTURL.child(td.getuid()).child(to.getcid()).child("qnt").setValue(String.valueOf(temp));

                    if (temp == 0) {
                        AppUtil.CARTURL.child(td.getuid()).child(to.getcid()).removeValue();
                    }

                } else if (Integer.parseInt(qntrytext.getText().toString()) == 0) {
                    AppUtil.CARTURL.child(td.getuid()).child(to.getcid()).removeValue();
                }
            }
        });

        Query q = AppUtil.CARTURL.child(td.getuid()).orderByChild("mid").equalTo(id);
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    addbtn.setVisibility(View.INVISIBLE);
                    qntytybox.setVisibility(View.VISIBLE);
                    qntrytext.setText("2");
                    if (dataSnapshot.getChildrenCount() == 1) {
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            qntrytext.setText(data.child("qnt").getValue().toString());
                        }
                    } else if (dataSnapshot.getChildrenCount() == 2) {

                    }
                } else {
                    if(stock1.equals("instock")) {
                        addbtn.setVisibility(View.VISIBLE);
                        qntytybox.setVisibility(View.INVISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
