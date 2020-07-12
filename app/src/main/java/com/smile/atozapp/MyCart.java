package com.smile.atozapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.smile.atozapp.addressdetails.AddressPage;
import com.smile.atozapp.controller.AppUtil;
import com.smile.atozapp.controller.TempData;
import com.smile.atozapp.controller.TempOrder;
import com.smile.atozapp.models.CartHold;
import com.smile.atozapp.parameters.CartParameters;

public class MyCart extends AppCompatActivity {

    RecyclerView cartlist;
    Button remove, order;
    TextView count;
    ConstraintLayout nodata;
    TextView gomarket;

    StringBuilder idlist;
    StringBuilder namelist;
    StringBuilder amlist;
    StringBuilder sizelist;
    StringBuilder qntylist;
    TempData t;

    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_cart);

        cartlist = findViewById(R.id.cart_list);
        remove = findViewById(R.id.cart_remove);
        order = findViewById(R.id.cart_order);
        count = findViewById(R.id.cart_count);
        t = new TempData(MyCart.this);
        nodata = findViewById(R.id.cart_no_cart);
        gomarket = findViewById(R.id.bg_no_basket_gomarket);

        cartlist.setLayoutManager(new LinearLayoutManager(this));
        cartlist.setHasFixedSize(true);

        pd = new ProgressDialog(MyCart.this);
        pd.setTitle("Loading");
        pd.setMessage("Please wait.....");

        getcount();

    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<CartParameters, CartHold> frecycle = new FirebaseRecyclerAdapter<CartParameters, CartHold>(
                CartParameters.class, R.layout.row_cart, CartHold.class, AppUtil.CARTURL.child(t.getuid())
        ) {
            @Override
            protected void populateViewHolder(CartHold ch, CartParameters cp, int i) {
                ch.setdetails(getApplicationContext(), cp.getId(),cp.getMid() ,cp.getPic(), cp.getName(), cp.getType(), cp.getAm(), cp.getSize(), cp.getQnt());
            }
        };
        cartlist.setAdapter(frecycle);

    }

    public void getcount() {
        AppUtil.CARTURL.child(t.getuid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    count.setText("Total cart Items : " + dataSnapshot.getChildrenCount());
                    if(dataSnapshot.getChildrenCount()==1){
                        order.setVisibility(View.INVISIBLE);
                        remove.setVisibility(View.INVISIBLE);
                    }
                } else {
                    count.setText("Total cart Items : 0");
                    nodata.setVisibility(View.VISIBLE);
                    order.setVisibility(View.INVISIBLE);
                    remove.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtil.CARTURL.child(t.getuid()).removeValue();
            }
        });

        gomarket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyCart.this , LoginMain.class));finish();
            }
        });

        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.show();
                getList();
                Handler h = new Handler();
                h.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (sizelist.capacity() != 0) {
                            DatabaseReference df = AppUtil.ORDERURl;
                            String key = df.push().getKey();
                            df.child(key).child("id").setValue(key);
                            df.child(key).child("uid").setValue(t.getuid());
                            df.child(key).child("name").setValue(namelist.toString());
                            df.child(key).child("size").setValue(sizelist.toString());
                            df.child(key).child("qnt").setValue(qntylist.toString());
                            df.child(key).child("am").setValue(amlist.toString());
                            df.child(key).child("addres").setValue("new");
                            df.child(key).child("pmode").setValue("new");
                            new TempOrder(MyCart.this).addoid(key);
                            pd.dismiss();
                            startActivity(new Intent(getApplicationContext(), AddressPage.class));
                            finish();
                        }else{
                            pd.dismiss();
                            Toast.makeText(MyCart.this, "try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, 5000);
            }
        });

    }

    public void getList() {
        idlist = new StringBuilder();
        namelist = new StringBuilder();
        amlist = new StringBuilder();
        sizelist = new StringBuilder();
        qntylist = new StringBuilder();

        AppUtil.CARTURL.child(t.getuid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot data : dataSnapshot.getChildren()) {
                        CartParameters c = data.getValue(CartParameters.class);
                        idlist.append(c.getId()).append(",");
                        namelist.append(c.getName()).append(",");
                        amlist.append(c.getAm()).append(",");
                        sizelist.append(c.getSize()).append(",");
                        qntylist.append(c.getQnt()).append(",");
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                pd.dismiss();
            }
        });

    }

}
