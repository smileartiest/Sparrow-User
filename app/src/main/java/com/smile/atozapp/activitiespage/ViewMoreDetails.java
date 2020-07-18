package com.smile.atozapp.activitiespage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.smile.atozapp.R;
import com.smile.atozapp.controller.AppUtil;
import com.smile.atozapp.controller.TempData;
import com.smile.atozapp.parameters.AddressParameters;
import com.smile.atozapp.parameters.BillingParameters;
import com.smile.atozapp.parameters.DeliveryParameters;
import com.smile.atozapp.parameters.OrderPatameters;

public class ViewMoreDetails extends AppCompatActivity {

    TextView  odate, dbname, dbphno, ddate, item_name,item_price,subtotal,delivery_charge,discount_amount,total_amount,delivery_time,title,address_title,address_local;
    private static String TAG = "vmoredetails";
    Toolbar mytoolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_more_details);

        title = findViewById(R.id.vmore_title);

        title.setText("Your Order ID is . "+getIntent().getStringExtra("id"));

        mytoolbar = findViewById(R.id.vmore_toolbar);
        setSupportActionBar(mytoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mytoolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);

        odate = findViewById(R.id.vmore_order_date);
        item_name = findViewById(R.id.vmore_order_item_names);
        item_price = findViewById(R.id.vmore_order_item_amounts);

        dbname = findViewById(R.id.vmore_dboyname);
        dbphno = findViewById(R.id.vmore_dboyphno);
        ddate = findViewById(R.id.vmore_ddate);
        delivery_time = findViewById(R.id.vmore_delivery_time);

        subtotal = findViewById(R.id.vmore_subtotal);
        delivery_charge = findViewById(R.id.vmore_dcharge);
        discount_amount = findViewById(R.id.vmore_discount);
        total_amount = findViewById(R.id.vmore_totalamount);

        address_title = findViewById(R.id.vmore_adress_title);
        address_local = findViewById(R.id.vmore_adress_local);

        AppUtil.ORDERURl.child(getIntent().getStringExtra("id")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    OrderPatameters o = dataSnapshot.getValue(OrderPatameters.class);

                    odate.setText(o.getOdate());

                    try {
                        final String[] temp_name = o.getName().split(",");
                        final String[] temp_size = o.getSize().split(",");
                        final String[] temp_qnty = o.getQnt().split(",");
                        final String[] temp_amount = o.getAm().split(",");

                        StringBuilder temp_namelist = new StringBuilder();
                        StringBuilder temp_amountlist = new StringBuilder();

                        for (int i = 0; i < temp_name.length; i++) {
                            temp_namelist.append(temp_name[i]+" . ( "+temp_size[i]+" ) x "+temp_qnty[i]+"\n\n");
                            temp_amountlist.append("$ "+temp_amount[i]+"\n\n");
                        }
                        item_name.setText(temp_namelist.toString());
                        item_price.setText(temp_amountlist.toString());

                        getaddress(o.getAddres());

                    }catch (Exception e){
                        Log.d("View Details Error ", e.getMessage());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG , databaseError.toString());
            }
        });

        AppUtil.DELIVERYURl.child(getIntent().getStringExtra("id")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue()!=null){
                    DeliveryParameters d = snapshot.getValue(DeliveryParameters.class);
                    dbname.setText(d.getName());
                    dbphno.setText(d.getPhone());
                    ddate.setText(d.getDdate());
                    delivery_time.setText("Your order was delivered successful at "+d.getDtime());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        AppUtil.BILLINGURl.child(getIntent().getStringExtra("id")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue()!=null){
                    BillingParameters b = snapshot.getValue(BillingParameters.class);
                    subtotal.setText("$ "+b.getSub_total());
                    delivery_charge.setText(" + "+b.getDelivery_charge());
                    discount_amount.setText(" - "+b.getDiscount());
                    total_amount.setText("$ "+b.getTotal_amount());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        mytoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    void getaddress(String addid){
        AppUtil.ADDRESURL.child(new TempData(ViewMoreDetails.this).getuid()).child(addid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue()!=null){
                    AddressParameters a = snapshot.getValue(AddressParameters.class);
                    address_title.setText(a.getCity());
                    address_local.setText(a.getAddress());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
