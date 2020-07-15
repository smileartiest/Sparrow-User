package com.smile.atozapp.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.smile.atozapp.R;
import com.smile.atozapp.addressdetails.AddAdress;
import com.smile.atozapp.controller.AppUtil;
import com.smile.atozapp.controller.TempData;
import com.smile.atozapp.parameters.AddressParameters;

public class MyAccount extends Fragment {

    View v;

    TextView name,email,phno,name_initial;

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
}
