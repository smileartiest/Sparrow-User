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

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.smile.atozapp.activitiespage.DressFullDetails;
import com.smile.atozapp.R;
import com.smile.atozapp.controller.AppUtil;
import com.smile.atozapp.controller.TempData;
import com.smile.atozapp.parameters.CartParameters;

public class DressHold extends RecyclerView.ViewHolder {

    public DressHold(@NonNull View itemView) {
        super(itemView);
    }

    public void setdetails(final Context c1, final String id1, final String name1, final String type1, final String am1, final String off1, final String pic1, final String stock1 , final String cat1) {

        final ConstraintLayout card = itemView.findViewById(R.id.drow_card);

        ImageView pic = itemView.findViewById(R.id.drow_pic);
        TextView name = itemView.findViewById(R.id.drow_name);
        TextView type = itemView.findViewById(R.id.drow_type);
        TextView am = itemView.findViewById(R.id.drow_amount);
        final LottieAnimationView loading_gif = itemView.findViewById(R.id.drow_loading_gif);
        ImageView indication_icon = itemView.findViewById(R.id.drow_icon_idication);
        final TextView stcoksts = itemView.findViewById(R.id.drow_stock);

        String[] amlist = am1.split(",");

        Glide.with(c1).load(pic1).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                if(cat1.equals("dress")){
                    loading_gif.setVisibility(View.VISIBLE);
                    loading_gif.setAnimation(R.raw.dress_loading);
                    loading_gif.playAnimation();
                    loading_gif.loop(true);
                }else {
                    loading_gif.setVisibility(View.VISIBLE);
                    loading_gif.setAnimation(R.raw.image_loading_gif);
                    loading_gif.playAnimation();
                    loading_gif.loop(true);
                }
                return false;
            }
            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                loading_gif.setVisibility(View.GONE);
                return false;
            }
        }).into(pic);
        name.setText(name1);
        type.setText(type1);
        am.setText("starting  $ " + amlist[0]);

        if (stock1.equals("instock")) {
            stcoksts.setVisibility(View.GONE);
        } else {
            stcoksts.setVisibility(View.VISIBLE);
        }

        if(cat1!=null) {
            if (cat1.equals("dress")) {
                indication_icon.setImageResource(R.drawable.yellow_dot);
            } else if (cat1.equals("electronics")) {
                indication_icon.setImageResource(R.drawable.blue_dot);
            }
        }

        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stock1.equals("instock")) {
                    if(cat1.equals("dress")) {
                        c1.startActivity(new Intent(c1, DressFullDetails.class).putExtra("f","dress").putExtra("id", id1).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    }else if (cat1.equals("electronics")) {
                        c1.startActivity(new Intent(c1, DressFullDetails.class).putExtra("f","electronics").putExtra("id", id1).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    }
                }else{
                    Toast.makeText(c1, "Out Of Stock", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}
