package com.smile.atozapp.models;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.smile.atozapp.R;

public class OfferHold extends RecyclerView.ViewHolder {
    public OfferHold(@NonNull View itemView) {
        super(itemView);
    }

    public void setdetails(final Context c1,final String id1, final String url1){
        ImageView pic = itemView.findViewById(R.id.orow_pic);
        final LottieAnimationView animationView = itemView.findViewById(R.id.orow_gif);
        Glide.with(c1).load(url1).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                animationView.setVisibility(View.VISIBLE);
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                animationView.setVisibility(View.GONE);
                return false;
            }
        }).into(pic);
    }

}
