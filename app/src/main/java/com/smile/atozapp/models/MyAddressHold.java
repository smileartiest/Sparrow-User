package com.smile.atozapp.models;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.smile.atozapp.R;

public class MyAddressHold extends RecyclerView.ViewHolder {

    public MyAddressHold(@NonNull View itemView) {
        super(itemView);
    }

    public void setdetails(final Context c1 , final String id1, final String uid1, final String lat1, final String lang1, final String address1 , final String cno1){
        TextView address = itemView.findViewById(R.id.myaddress_row_add);
        TextView cno = itemView.findViewById(R.id.myaddress_row_cno);
        TextView latlang = itemView.findViewById(R.id.myaddress_row_latlang);
        final ImageView delete = itemView.findViewById(R.id.myaddress_row_delete);

        address.setText(address1);
        cno.setText("Contact Number : "+cno1);
        latlang.setText("Latitude : "+lat1 +"  "+"Longitude : "+lang1);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference df = FirebaseDatabase.getInstance().getReference("address");
                df.child(uid1).child(id1).removeValue();
            }
        });

    }

}
