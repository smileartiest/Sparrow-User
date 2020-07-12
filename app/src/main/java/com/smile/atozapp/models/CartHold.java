package com.smile.atozapp.models;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.smile.atozapp.CompleteOrder;
import com.smile.atozapp.R;
import com.smile.atozapp.addressdetails.AddressPage;
import com.smile.atozapp.controller.AppUtil;
import com.smile.atozapp.controller.TempData;
import com.smile.atozapp.controller.TempOrder;

import static android.content.Context.MODE_PRIVATE;

public class CartHold extends RecyclerView.ViewHolder {

    TempData td;
    TempOrder to;

    public CartHold(@NonNull View itemView) {
        super(itemView);
    }

    public void setdetails(final Context c1 , final String id1,final String mid1, String pic1, final String name1, String type1, final String am1, final String size1, final String qnt1){

        ImageView pic = itemView.findViewById(R.id.cart_row_pic);
        TextView name = itemView.findViewById(R.id.cart_row_name);
        TextView type = itemView.findViewById(R.id.cart_row_type);
        TextView size = itemView.findViewById(R.id.cart_row_size);
        TextView amount = itemView.findViewById(R.id.cart_row_amount);
        TextView buy = itemView.findViewById(R.id.cart_row_buy);
        final TextView count = itemView.findViewById(R.id.cart_row_count);

        TextView add = itemView.findViewById(R.id.cart_row_add);
        TextView remove = itemView.findViewById(R.id.cart_row_remove);

        Glide.with(c1).load(pic1).into(pic);
        name.setText(":  "+name1);
        type.setText(":  "+type1);
        size.setText(":  "+size1);
        amount.setText(":  "+am1);

        count.setText(qnt1);

        to = new TempOrder(c1);
        td = new TempData(c1);

        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference df = AppUtil.ORDERURl;
                String key = df.push().getKey();
                df.child(key).child("id").setValue(key);
                df.child(key).child("uid").setValue(td.getuid());
                df.child(key).child("name").setValue(name1);
                df.child(key).child("size").setValue(size1);
                df.child(key).child("qnt").setValue(qnt1);
                df.child(key).child("am").setValue(am1);
                df.child(key).child("addres").setValue("new");
                df.child(key).child("pmode").setValue("new");
                to.addoid(key);
                c1.startActivity(new Intent(c1 , AddressPage.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = Integer.parseInt(qnt1)+1;
                count.setText(String.valueOf(temp));
                AppUtil.CARTURL.child(td.getuid()).child(id1).child("qnt").setValue(String.valueOf(temp));
            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Integer.parseInt(qnt1)>0) {
                    int temp =  Integer.parseInt(qnt1) - 1;
                    count.setText(String.valueOf(temp));
                    AppUtil.CARTURL.child(td.getuid()).child(id1).child("qnt").setValue(String.valueOf(temp));

                    if(temp==0){
                        AppUtil.CARTURL.child(td.getuid()).child(id1).removeValue();
                    }

                }
                else if(Integer.parseInt(qnt1)==0){
                    AppUtil.CARTURL.child(td.getuid()).child(id1).removeValue();
                }
            }
        });

    }

}
