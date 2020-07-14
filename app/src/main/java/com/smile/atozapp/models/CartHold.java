package com.smile.atozapp.models;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.smile.atozapp.R;
import com.smile.atozapp.controller.AppUtil;
import com.smile.atozapp.controller.TempData;
import com.smile.atozapp.controller.TempOrder;

public class CartHold extends RecyclerView.ViewHolder {

    TempData td;
    TempOrder to;

    public CartHold(@NonNull View itemView) {
        super(itemView);
    }

    public void setdetails(final Context c1 , final String id1,final String mid1, String pic1, final String name1, String type1, final String am1, final String size1, final String qnt1){

        TextView name = itemView.findViewById(R.id.cart_row_name);
        TextView type = itemView.findViewById(R.id.cart_row_type);
        final TextView count = itemView.findViewById(R.id.cart_row_count);

        TextView add = itemView.findViewById(R.id.cart_row_add);
        TextView remove = itemView.findViewById(R.id.cart_row_remove);

        name.setText(type1);
        type.setText(name1+" . "+size1+" . Rs "+am1);

        count.setText(qnt1);

        to = new TempOrder(c1);
        td = new TempData(c1);

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
