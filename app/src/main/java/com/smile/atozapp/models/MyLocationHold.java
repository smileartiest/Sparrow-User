package com.smile.atozapp.models;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.smile.atozapp.R;
import com.smile.atozapp.controller.AppUtil;
import com.smile.atozapp.controller.TempData;

public class MyLocationHold extends RecyclerView.ViewHolder {

    Context mcontext;
    ConstraintLayout box;
    ImageView delete_btn;

    public MyLocationHold(@NonNull View itemView) {
        super(itemView);
        box = itemView.findViewById(R.id.row_location_box);
        delete_btn = itemView.findViewById(R.id.row_location_delete);
    }

    public void setdetails(final  Context c1 ,final String aid1 ,final String address1 ,final String city1){
        this.mcontext = c1;
        TextView city = itemView.findViewById(R.id.row_location_city);
        TextView area = itemView.findViewById(R.id.row_location_area);
        ImageView pic = itemView.findViewById(R.id.row_location_img);
        final CardView cardView = itemView.findViewById(R.id.row_location_card);
        pic.setImageResource(R.drawable.my_location_round_icon);
        city.setText(city1);
        area.setText(address1);
        box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TempData(c1).addlocation( aid1,city1,address1);
                cardView.setBackgroundResource(R.drawable.text_select);
                openConformationDialog();
            }
        });
        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppUtil.ADDRESURL.child(new TempData(mcontext).getuid()).child(aid1).removeValue();
                Snackbar.make(itemView ,"removed successfully " , Snackbar.LENGTH_SHORT).show();
            }
        });

    }

    public void openConformationDialog(){
        Snackbar.make(itemView ,"added successfully" , Snackbar.LENGTH_SHORT).show();
    }


}
