package com.smile.atozapp.models;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.smile.atozapp.R;
import com.smile.atozapp.controller.TempData;

public class MyLocationHold extends RecyclerView.ViewHolder {

    public MyLocationHold(@NonNull View itemView) {
        super(itemView);
    }

    public void setdetails(final  Context c1 ,final String address1 ,final String city1){
        TextView loc = itemView.findViewById(R.id.row_location_name);
        ImageView pic = itemView.findViewById(R.id.row_location_img);
        pic.setImageResource(R.drawable.my_location_round_icon);
        loc.setText(address1);
        loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TempData(c1).addlocation(city1);
            }
        });
    }

}
