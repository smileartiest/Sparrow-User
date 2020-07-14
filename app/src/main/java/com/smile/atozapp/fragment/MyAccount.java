package com.smile.atozapp.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.smile.atozapp.R;
import com.smile.atozapp.addressdetails.AddAdress;
import com.smile.atozapp.controller.AppUtil;
import com.smile.atozapp.controller.TempData;
import com.smile.atozapp.models.MyAddressHold;
import com.smile.atozapp.parameters.AddressParameters;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyAccount extends Fragment {

    View v;

    RecyclerView addresslist;
    TextView name,email,phno,name_initial;
    FloatingActionButton addaddress;
    Query q;

    ProgressDialog pd;
    StorageReference sr,sr1;
    Uri picurl;
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
        addresslist = v.findViewById(R.id.profile_adlist);
        addaddress = v.findViewById(R.id.profile_addressbtn);
        name_initial = v.findViewById(R.id.profile_name_initial);

        addresslist.setLayoutManager(new LinearLayoutManager(getContext()));
        addresslist.setHasFixedSize(true);

        sr = FirebaseStorage.getInstance().getReference("userpic");

        pd = new ProgressDialog(getContext());
        pd.setTitle("Uploading");
        pd.setMessage("Loading Please Wait....");


        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<AddressParameters , MyAddressHold> frecycle = new FirebaseRecyclerAdapter<AddressParameters, MyAddressHold>(
                AddressParameters.class , R.layout.myaddress_row , MyAddressHold.class , AppUtil.ADDRESURL.child(td.getuid())
        ) {
            @Override
            protected void populateViewHolder(MyAddressHold myAddressHold, AddressParameters addressParameters, int i) {
                myAddressHold.setdetails(getContext() , addressParameters.getId() , addressParameters.getUid() , addressParameters.getLat() , addressParameters.getLang() , addressParameters.getAddress() , addressParameters.getCno());
            }
        };
        addresslist.setAdapter(frecycle);

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

        addaddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext() , AddAdress.class));
            }
        });
    }
}
