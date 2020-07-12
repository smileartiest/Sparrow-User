package com.smile.atozapp.models;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.smile.atozapp.DressFullDetails;
import com.smile.atozapp.R;

public class DressHold extends RecyclerView.ViewHolder {

    public DressHold(@NonNull View itemView) {
        super(itemView);
    }

    public void setdetails(final Context c1, final String id1, final String name1, final String type1, final String am1, final String off1, final String pic1, final String stock1) {

        final ConstraintLayout card = itemView.findViewById(R.id.drow_card);

        ImageView pic = itemView.findViewById(R.id.drow_pic);
        TextView name = itemView.findViewById(R.id.drow_name);
        TextView type = itemView.findViewById(R.id.drow_type);
        TextView am = itemView.findViewById(R.id.drow_amount);
        TextView off = itemView.findViewById(R.id.drow_offer);
        final TextView stcoksts = itemView.findViewById(R.id.drow_stock);
        final ImageView like = itemView.findViewById(R.id.drow_likebtn);

        String[] amlist = am1.split(",");

        Glide.with(c1).load(pic1).into(pic);
        name.setText(name1);
        type.setText(type1);
        am.setText("starting  $ " + amlist[0]);
        off.setText(off1);

        if (stock1.equals("instock")) {
            stcoksts.setVisibility(View.GONE);
        } else {
            stcoksts.setVisibility(View.VISIBLE);
        }

        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                like.setImageResource(R.drawable.like_icon);
            }
        });

        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stock1.equals("instock")) {
                    c1.startActivity(new Intent(c1, DressFullDetails.class).putExtra("id", id1).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }else{
                    Toast.makeText(c1, "Out Of Stock", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}
