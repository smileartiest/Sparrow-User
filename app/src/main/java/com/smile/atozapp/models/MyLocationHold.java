package com.smile.atozapp.models;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.smile.atozapp.R;
import com.smile.atozapp.controller.TempData;

public class MyLocationHold extends RecyclerView.ViewHolder {

    public MyLocationHold(@NonNull View itemView) {
        super(itemView);
    }

    public void setdetails(final  Context c1 ,final String address1 ,final String city1){
        TextView city = itemView.findViewById(R.id.row_location_city);
        TextView area = itemView.findViewById(R.id.row_location_area);
        ImageView pic = itemView.findViewById(R.id.row_location_img);
        pic.setImageResource(R.drawable.my_location_round_icon);
        city.setText(city1);
        area.setText(address1);
        city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TempData(c1).addlocation(city1,address1);
                openConformationDialog();
            }
        });
    }

    public void openConformationDialog(){

    }


}
