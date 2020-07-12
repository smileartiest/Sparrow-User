package com.smile.atozapp.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.smile.atozapp.R;
import com.smile.atozapp.controller.AppUtil;
import com.smile.atozapp.parameters.ContactusParameters;

import static com.smile.atozapp.LoginMain.MY_PERMISSIONS_REQUEST_LOCATION;

public class ContactUs extends Fragment {

    View v;
    String mobno, email, whatsapp;

    TextView call, whatsap, sendemail;

    public ContactUs() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.frag_contactus, container, false);

        call = v.findViewById(R.id.contact_call);
        whatsap = v.findViewById(R.id.contact_whatsapp);
        sendemail = v.findViewById(R.id.contact_email);

        getdetails();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mobno.length() != 0) {
                    if(checkLocationPermission()) {
                        Intent in = new Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:" + mobno));
                        startActivity(in);
                    }
                    else
                    {
                        checkLocationPermission();
                        Toast.makeText(getContext() , "dont make a call" , Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        whatsap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (whatsapp.length() != 0) {
                    Uri uri = Uri.parse("smsto:" + whatsapp);
                    Intent i = new Intent(Intent.ACTION_SENDTO, uri);
                    i.setPackage("com.whatsapp");
                    startActivity(Intent.createChooser(i, "hai sparrow"));
                }
            }
        });

        sendemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.length() != 0) {
                    Intent intent = new Intent(Intent.ACTION_SENDTO);
                    intent.setData(Uri.parse("mailto:" + email)); // only email apps should handle this
                    intent.putExtra(Intent.EXTRA_EMAIL, "");
                    intent.putExtra(Intent.EXTRA_SUBJECT, "Hi sparrow");
                    startActivity(intent);
                }
            }
        });

    }

    void getdetails() {
        AppUtil.CONTURL.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    ContactusParameters c = dataSnapshot.getValue(ContactusParameters.class);
                    email = c.getEmail();
                    mobno = c.getMobno();
                    whatsapp = c.getWhatsapp();
                } else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.CALL_PHONE)) {

                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.CALL_PHONE},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.CALL_PHONE},
                        MY_PERMISSIONS_REQUEST_LOCATION);

            }
            return false;
        } else {

            return true;
        }
    }

}
