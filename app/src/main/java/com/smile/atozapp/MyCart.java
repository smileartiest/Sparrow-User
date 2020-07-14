package com.smile.atozapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.PorterDuff;
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
import com.smile.atozapp.controller.TimeDate;
import com.smile.atozapp.models.CartHold;
import com.smile.atozapp.parameters.CartParameters;
import com.smile.atozapp.parameters.ChargeParameters;

public class MyCart extends AppCompatActivity {

    RecyclerView cartlist;
    Button remove, order;
    TextView count;
    ConstraintLayout nodata, bill_box, address_box;
    TextView gomarket;

    TextView sb_total,d_charge,discount_amount,total_amount , location_city,location_local;
    Button choose_address_btn;

    String Charge_d_charge = null,Charge_discount = null;

    StringBuilder idlist;
    StringBuilder namelist;
    StringBuilder amlist;
    StringBuilder sizelist;
    StringBuilder qntylist;

    String[] qnttemp;
    String[] amtemp;

    String qnt1,am1;

    Toolbar mytoolbar;

    TempData t;

    ProgressDialog pd;

    TempData td;
    TempOrder to;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_cart);

        mytoolbar = findViewById(R.id.cart_toolbar);
        setSupportActionBar(mytoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mytoolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);

        cartlist = findViewById(R.id.cart_list);
        remove = findViewById(R.id.cart_remove);
        order = findViewById(R.id.cart_order);
        count = findViewById(R.id.cart_count);
        bill_box = findViewById(R.id.cart_bill_box);
        address_box = findViewById(R.id.cart_address_box);
        sb_total = findViewById(R.id.cart_sb_total);
        d_charge = findViewById(R.id.cart_sb_d_charge);
        discount_amount = findViewById(R.id.cart_sb_discount);
        total_amount = findViewById(R.id.cart_total_amount);
        location_city = findViewById(R.id.cart_city_name);
        location_local = findViewById(R.id.cart_local_name);
        choose_address_btn = findViewById(R.id.cart_address_btn);
        t = new TempData(MyCart.this);
        nodata = findViewById(R.id.cart_no_cart);
        gomarket = findViewById(R.id.bg_no_basket_gomarket);

        cartlist.setLayoutManager(new LinearLayoutManager(this));
        cartlist.setHasFixedSize(true);

        to = new TempOrder(MyCart.this);
        td = new TempData(MyCart.this);

        pd = new ProgressDialog(MyCart.this);
        pd.setTitle("Loading");
        pd.setMessage("Please wait.....");

        mytoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

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
                ch.setdetails(getApplicationContext(), cp.getId(), cp.getMid(), cp.getPic(), cp.getName(), cp.getType(), cp.getAm(), cp.getSize(), cp.getQnt());
            }
        };
        cartlist.setAdapter(frecycle);

    }

    public void getcount() {
        AppUtil.CARTURL.child(t.getuid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    count.setText("Count . " + dataSnapshot.getChildrenCount());
                    order.setVisibility(View.VISIBLE);
                    remove.setVisibility(View.VISIBLE);
                    address_box.setVisibility(View.VISIBLE);
                    bill_box.setVisibility(View.VISIBLE);
                    getList();
                } else {
                    count.setText("Count . 0");
                    nodata.setVisibility(View.VISIBLE);
                    order.setVisibility(View.INVISIBLE);
                    remove.setVisibility(View.INVISIBLE);
                    address_box.setVisibility(View.GONE);
                    bill_box.setVisibility(View.GONE);
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
                startActivity(new Intent(MyCart.this, LoginMain.class));
                finish();
            }
        });

        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(td.getaid()!=null) {
                    TimeDate timedate = new TimeDate(MyCart.this);
                    DatabaseReference df = AppUtil.ORDERURl;
                    String key = df.push().getKey();
                    df.child(key).child("id").setValue(key);
                    df.child(key).child("uid").setValue(t.getuid());
                    df.child(key).child("name").setValue(namelist.toString());
                    df.child(key).child("size").setValue(sizelist.toString());
                    df.child(key).child("qnt").setValue(qntylist.toString());
                    df.child(key).child("am").setValue(amlist.toString());
                    df.child(key).child("addres").setValue(td.getaid());
                    df.child(key).child("pmode").setValue("cashon");
                    df.child(key).child("sts").setValue("new");
                    df.child(key).child("odate").setValue(timedate.getdate());
                    df.child(key).child("otime").setValue(timedate.gettime());
                    df.child(key).child("bam").setValue(total_amount.getText().toString());
                    AppUtil.CARTURL.child(td.getuid()).removeValue();
                    to.tclear();
                }else{
                    Toast.makeText(MyCart.this, "Choose address first", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if(td.getaid()!=null){
            location_city.setText(td.getcity());
            location_local.setText(td.getadd());
            choose_address_btn.setText("CHANGE");
        }else{
            location_city.setText("Please choose address");
            location_local.setVisibility(View.INVISIBLE);
            choose_address_btn.setText("CHOOSE");
        }

        choose_address_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AddressPage.class));
            }
        });
    }

    public void getList() {
        AppUtil.CARTURL.child(t.getuid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    idlist = new StringBuilder();
                    namelist = new StringBuilder();
                    amlist = new StringBuilder();
                    sizelist = new StringBuilder();
                    qntylist = new StringBuilder();
                    qnt1 = null;
                    am1 = null;
                    qnttemp = null;
                    amtemp = null;
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                pd.dismiss();
            }
        });
        GetCahrges_Details();
    }

    void GetCahrges_Details(){

        AppUtil.CHARGES.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue()!= null ){
                    ChargeParameters c = snapshot.getValue(ChargeParameters.class);
                    Charge_discount = c.getDiscount();
                    Charge_d_charge = c.getDcharge();
                    Calculate_amount();
                }else{
                    Charge_d_charge = "0";
                    Charge_discount = "0";
                    Calculate_amount();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    void Calculate_amount(){
        qnt1 = qntylist.toString();
        am1 = amlist.toString();

        qnttemp = qnt1.split(",");
        amtemp = am1.split(",");

        int temp = 0;

        for(int i = 0 ; i < amtemp.length ; i++){
            temp = temp + (Integer.parseInt(qnttemp[i])*Integer.parseInt(amtemp[i]));
        }

        sb_total.setText(String.valueOf(temp));

        d_charge.setText("+ "+Charge_d_charge);

        discount_amount.setText("- "+Charge_discount);

        total_amount.setText(String.valueOf((temp+Integer.parseInt(Charge_d_charge)) - Integer.parseInt(Charge_discount) ));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
