package com.smile.atozapp.models;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.smile.atozapp.R;
import com.smile.atozapp.activitiespage.Track_Order;
import com.smile.atozapp.activitiespage.ViewMoreDetails;

public class TrackHold extends RecyclerView.ViewHolder {

    Context mcContect;
    TextView title,message;
    Button track_btn;

    public TrackHold(@NonNull View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.r_track_title);
        message = itemView.findViewById(R.id.r_track_message);
        track_btn = itemView.findViewById(R.id.r_track_btn);
    }

    public void setdetails(Context context1, final String oid1 , String name1 , String sts1){
        this.mcContect = context1;
        title.setText("You pay this Order Rs. ");
        message.setText(name1);

        if (sts1.equals("cancel")) {
            track_btn.setText("view details");
        }else if(sts1.equals("complete")){
            track_btn.setText("view details");
        }else{
            track_btn.setVisibility(View.VISIBLE);
        }

        track_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(itemView ,"clicked",Snackbar.LENGTH_SHORT).show();
                if(track_btn.getText().toString().equals("view details")){
                    mcContect.startActivity(new Intent(mcContect , ViewMoreDetails.class).putExtra("id",oid1).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }else{
                    mcContect.startActivity(new Intent(mcContect , Track_Order.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("oid",oid1));
                }

            }
        });

    }

}
