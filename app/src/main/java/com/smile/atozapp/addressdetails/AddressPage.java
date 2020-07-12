package com.smile.atozapp.addressdetails;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.smile.atozapp.CompleteOrder;
import com.smile.atozapp.R;
import com.smile.atozapp.controller.AppUtil;
import com.smile.atozapp.controller.TempData;
import com.smile.atozapp.controller.TempOrder;
import com.smile.atozapp.models.AddressHold;
import com.smile.atozapp.parameters.AddressParameters;

public class AddressPage extends AppCompatActivity {

    RecyclerView list;
    Button conformbtn;
    FloatingActionButton addaddressbtn;

    ConstraintLayout nodata;

    Query q;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.address_page);

        list = findViewById(R.id.addresspage_list);
        conformbtn = findViewById(R.id.addresspage_conformbtn);
        addaddressbtn = findViewById(R.id.addresspage_addaddress);

        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(AddressPage.this));

        q = AppUtil.ADDRESURL.child(new TempData(AddressPage.this).getuid());

        getCount();

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<AddressParameters, AddressHold> frecycle = new FirebaseRecyclerAdapter<AddressParameters, AddressHold>(
                AddressParameters.class , R.layout.row_addres, AddressHold.class , q
        ) {
            @Override
            protected void populateViewHolder(AddressHold addressHold, AddressParameters addressParameters, int i) {
                addressHold.setdetails(getApplicationContext() , addressParameters.getId() , addressParameters.getUid() , addressParameters.getLat() , addressParameters.getLang() , addressParameters.getAddress() , addressParameters.getCno());
            }
        };
        list.setAdapter(frecycle);

    }

    public void getCount(){
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){

                }else {
                    //nodata.setVisibility(View.VISIBLE);
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

        addaddressbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext() , AddAdress.class));
            }
        });

        conformbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(new TempOrder(AddressPage.this).getaddress()!=null){
                    startActivity(new Intent(getApplicationContext() , CompleteOrder.class));finish();
                }else {
                    AlertDialog.Builder ad = new AlertDialog.Builder(AddressPage.this);
                    ad.setTitle("Oops !!");
                    ad.setMessage("Please choose Address");
                    ad.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    ad.show();
                }
            }
        });

    }
}
