package com.smile.atozapp.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.smile.atozapp.BuildConfig;
import com.smile.atozapp.R;
import com.smile.atozapp.activitiespage.FAQ_page;
import com.smile.atozapp.activitiespage.MyCart;
import com.smile.atozapp.activitiespage.MyOrder_Page;
import com.smile.atozapp.activitiespage.Terms_Conditions;
import com.smile.atozapp.addressdetails.AddAdress;
import com.smile.atozapp.controller.AppUtil;
import com.smile.atozapp.controller.TempData;
import com.smile.atozapp.parameters.AddressParameters;

public class MyAccount extends Fragment {

    View v;

    TextView name,email,phno,name_initial,version_name;
    ImageView edit_details,my_cart,my_order,fag_page,terms_condition;

    TempData td;

    public MyAccount() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.frag_account , container , false);

        td = new TempData(getContext());

        name = v.findViewById(R.id.profile_name);
        email = v.findViewById(R.id.profile_email);
        phno = v.findViewById(R.id.profile_phno);
        name_initial = v.findViewById(R.id.profile_name_initial);

        edit_details = v.findViewById(R.id.profile_edite_details);
        my_cart = v.findViewById(R.id.profile_mycart);
        my_order = v.findViewById(R.id.profile_myorder);
        fag_page = v.findViewById(R.id.profile_faq);
        terms_condition = v.findViewById(R.id.profile_terms_condition);
        version_name = v.findViewById(R.id.profile_version_name);

        version_name.setText("Sparrow Hyper Market . version "+ BuildConfig.VERSION_NAME);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        AppUtil.REGURL.child(td.getuid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null) {

                    if (dataSnapshot.child("name").getValue() != null) {
                        name.setText(dataSnapshot.child("name").getValue().toString());
                        String temp = dataSnapshot.child("name").getValue().toString();
                        name_initial.setText(String.valueOf(temp.charAt(0)).toUpperCase());
                    } else {
                        name.setText("");
                    }
                    if (dataSnapshot.child("email").getValue() != null) {
                        email.setText(dataSnapshot.child("email").getValue().toString());
                    } else {
                        email.setText("");
                    }
                    if (dataSnapshot.child("phno").getValue() != null) {
                        phno.setText(dataSnapshot.child("phno").getValue().toString());
                    } else {
                        phno.setText("");
                    }
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

        edit_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        my_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), MyCart.class));
            }
        });

        my_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), MyOrder_Page.class));
            }
        });

        fag_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext() , FAQ_page.class));
            }
        });

        terms_condition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext() , Terms_Conditions.class));
            }
        });
    }
}
