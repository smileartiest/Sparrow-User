package com.smile.atozapp.models;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.smile.atozapp.R;
import com.smile.atozapp.ViewMoreDetails;
import com.smile.atozapp.controller.AppUtil;
import com.smile.atozapp.controller.TimeDate;

public class MyOrderHold extends RecyclerView.ViewHolder {

    public MyOrderHold(@NonNull View itemView) {
        super(itemView);
    }

    public void setdetails(final Context c1, final String id1, final String uid1, final String name1, final String size1, final String qnt1, final String am1, final String bam1, final String addres1, String pmode1, final String sts1) {
        TextView oid = itemView.findViewById(R.id.order_row_oid);
        TextView amount = itemView.findViewById(R.id.order_row_bam);
        TextView servlist = itemView.findViewById(R.id.order_row_servlist);
        TextView amlist = itemView.findViewById(R.id.order_row_amlist);
        TextView osts = itemView.findViewById(R.id.order_row_osts);
        final TextView cancel = itemView.findViewById(R.id.order_row_cancel);
        TextView viewmore = itemView.findViewById(R.id.order_row_viewmore);
        ImageView imgpic = itemView.findViewById(R.id.imageView36);

        final String[] nametemp = name1.split(",");
        final String[] qnttemp = qnt1.split(",");
        final String[] sizetemp = size1.split(",");
        final String[] amtemp = am1.split(",");

        final StringBuilder servlist1 = new StringBuilder();
        final StringBuilder amlist1 = new StringBuilder();

        for (int i = 0; i < nametemp.length; i++) {
            servlist1.append(nametemp[i] + "\n" + qnttemp[i] + " X " + sizetemp[i] + "\n");
            amlist1.append(String.valueOf(Integer.parseInt(qnttemp[i]) * Integer.parseInt(amtemp[i])) + "\n" + "\n");
        }
        servlist.setText(servlist1.toString());
        amlist.setText(amlist1.toString());

        amount.setText(bam1);
        osts.setText(sts1);

        imgpic.setImageResource(R.drawable.myorder_icon);

        if (sts1.equals("cancel")) {
            oid.setText("Canceled");
            cancel.setText("RE ORDER");
        } else {
            oid.setText(id1);
        }

        if (sts1.equals("taken") || sts1.equals("pending") || sts1.equals("cancel") || sts1.equals("complete")) {
            if (sts1.equals("pending") || sts1.equals("complete")) {
                cancel.setVisibility(View.INVISIBLE);
                if (sts1.equals("complete")) {
                    imgpic.setImageResource(R.drawable.complete_icon);
                }
            }
        }

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cancel.getText().toString().equals("RE ORDER")) {
                    AppUtil.ORDERURl.child(id1).child("sts").setValue("new");
                    Toast.makeText(c1, "Order successfull", Toast.LENGTH_SHORT).show();

                } else {
                    AppUtil.ORDERURl.child(id1).child("sts").setValue("cancel");
                    Toast.makeText(c1, "Cancel successfull", Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c1.startActivity(new Intent(c1 , ViewMoreDetails.class).putExtra("id",id1).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });

    }

}
