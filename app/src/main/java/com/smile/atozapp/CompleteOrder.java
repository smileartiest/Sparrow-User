package com.smile.atozapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.smile.atozapp.controller.AppUtil;
import com.smile.atozapp.controller.TempData;
import com.smile.atozapp.controller.TempOrder;
import com.smile.atozapp.controller.TimeDate;
import com.smile.atozapp.parameters.OrderPatameters;

import java.util.Calendar;

public class CompleteOrder extends AppCompatActivity {

    TextView serv,am,subtotal,delvcharge,discount,total;
    String name1,qnt1,size1,am1;
    StringBuilder servlist1,amlist1;
    String[] nametemp;
    String[] qnttemp;
    String[] sizetemp;
    String[] amtemp;

    Button cashon,online;
    TempData td;
    TempOrder to;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.complete_order);

        serv = findViewById(R.id.bill_servlist);
        am = findViewById(R.id.bill_amlist);
        subtotal = findViewById(R.id.bill_subtotal);
        delvcharge = findViewById(R.id.bill_delivcharge);
        discount = findViewById(R.id.bill_discount);
        total = findViewById(R.id.bill_total);

        cashon = findViewById(R.id.bill_cashon);
        online = findViewById(R.id.bill_online);

        to = new TempOrder(CompleteOrder.this);
        td = new TempData(CompleteOrder.this);

        AppUtil.ORDERURl.child(to.getoid())
        .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){
                    OrderPatameters o = dataSnapshot.getValue(OrderPatameters.class);
                    name1 = o.getName();
                    qnt1 = o.getQnt();
                    size1 = o.getSize();
                    am1 = o.getAm();

                    nametemp = name1.split(",");
                    qnttemp = qnt1.split(",");
                    sizetemp = size1.split(",");
                    amtemp = am1.split(",");

                    servlist1 = new StringBuilder();
                    amlist1 = new StringBuilder();

                    for(int i = 0 ; i<nametemp.length ; i++){
                        servlist1.append(nametemp[i]+"\n"+qnttemp[i]+" X "+sizetemp[i]+"\n");
                        amlist1.append(String.valueOf(Integer.parseInt(qnttemp[i])*Integer.parseInt(amtemp[i]))+"\n"+"\n");
                    }

                    serv.setText(servlist1.toString());
                    am.setText(amlist1.toString());

                    int temp = 0;

                    for(int j = 0 ; j < amtemp.length ; j++){
                        temp = temp + (Integer.parseInt(qnttemp[j])*Integer.parseInt(amtemp[j]));
                    }

                    subtotal.setText(String.valueOf(temp));

                    delvcharge.setText("30");

                    discount.setText("0");

                    total.setText(String.valueOf(temp+30));

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder ad = new AlertDialog.Builder(CompleteOrder.this);
                ad.setTitle("Oops !!");
                ad.setMessage("Not Available Online Cash");
                ad.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                ad.show();
            }
        });

        cashon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TimeDate timedate = new TimeDate(CompleteOrder.this);

                DatabaseReference df = AppUtil.ORDERURl.child(to.getoid());
                df.child("pmode").setValue("cashon");
                df.child("sts").setValue("new");
                df.child("odate").setValue(timedate.getdate());
                df.child("otime").setValue(timedate.gettime());
                df.child("bam").setValue(total.getText().toString());

                AppUtil.CARTURL.child(td.getuid()).removeValue();

                AlertDialog.Builder ad = new AlertDialog.Builder(CompleteOrder.this);
                ad.setTitle("Complete !!");
                ad.setMessage("Successfull complete your Order");
                ad.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        to.tclear();
                        startActivity(new Intent(getApplicationContext() , LoginMain.class));
                        finish();
                    }
                });
                ad.show();
            }
        });

    }
}
