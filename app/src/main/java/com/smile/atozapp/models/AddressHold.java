package com.smile.atozapp.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.smile.atozapp.R;
import com.smile.atozapp.controller.AppUtil;
import com.smile.atozapp.controller.TempData;
import com.smile.atozapp.controller.TempOrder;

public class AddressHold extends RecyclerView.ViewHolder {

    TempOrder to;

    public AddressHold(@NonNull View itemView) {
        super(itemView);
    }

    public void setdetails(final Context c1 ,final String id1,final String city1,final String lat1,final String lang1,final String address1 , final String cno1 )
    {
        final ConstraintLayout card = itemView.findViewById(R.id.address_row_card);
        TextView address = itemView.findViewById(R.id.address_row_add);
        TextView cno = itemView.findViewById(R.id.address_row_cno);
        TextView latlang = itemView.findViewById(R.id.address_row_latlang);
        address.setText(address1);
        cno.setText("Contact Number : "+cno1);
        latlang.setText("Latitude : "+lat1 +"  "+"Longitude : "+lang1);

        to = new TempOrder(c1);
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                card.setBackgroundResource(R.drawable.text_select);
                new TempData(c1).addlocation( id1,city1,address1);
                to.addaddress(id1);
            }
        });

    }

}
