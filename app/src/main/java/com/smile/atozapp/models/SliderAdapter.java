package com.smile.atozapp.models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;

import com.smile.atozapp.R;


public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderAdapterVH> {

    private Context context;
    ArrayList<String> img = new ArrayList<>();


    public SliderAdapter(Context c , ArrayList<String> imglist)
    {
        this.context = c;
        this.img = imglist;
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {

        View inflat = LayoutInflater.from(parent.getContext()).inflate(R.layout.slide_image , null);

        return new SliderAdapterVH(inflat);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, int position) {

        Glide.with(context).load(img.get(position)).into(viewHolder.imageViewBackground);

    }

    @Override
    public int getCount() {
        return img.size();
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        View itemView;
        ImageView imageViewBackground;

        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.image_slide);
            this.itemView = itemView;
        }
    }
}
