package com.smile.atozapp.models;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.smile.atozapp.R;
import com.smile.atozapp.activitiespage.LoginMain;
import com.smile.atozapp.activitiespage.MyCart;
import com.smile.atozapp.activitiespage.Track_Order;
import com.smile.atozapp.activitiespage.ViewMoreDetails;
import com.smile.atozapp.controller.AppUtil;
import com.smile.atozapp.controller.TempData;
import com.smile.atozapp.parameters.BillingParameters;
import com.smile.atozapp.retrofit.ApiUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrackHold extends RecyclerView.ViewHolder {

    Context mcContect;
    TextView title,message,order_id;
    Button track_btn,cancel_btn;

    public TrackHold(@NonNull View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.r_track_title);
        message = itemView.findViewById(R.id.r_track_message);
        track_btn = itemView.findViewById(R.id.r_track_btn);
        order_id = itemView.findViewById(R.id.r_track_id);
        cancel_btn = itemView.findViewById(R.id.r_track_cancel_btn);
    }

    public void setdetails(Context context1, final String oid1 , String name1 , String sts1){
        this.mcContect = context1;

        order_id.setText("Order ID . "+oid1);

        AppUtil.BILLINGURl.child(oid1).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue()!=null){
                    BillingParameters b = snapshot.getValue(BillingParameters.class);
                    title.setText("You pay this Order Rs. "+b.getTotal_amount());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        message.setText(name1);

        if (sts1.equals("cancel")) {
            cancel_btn.setVisibility(View.INVISIBLE);
            track_btn.setVisibility(View.VISIBLE);
            track_btn.setText("re order");
        }else if(sts1.equals("complete")){
            cancel_btn.setVisibility(View.INVISIBLE);
            track_btn.setText("view details");
        }else if(sts1.equals("new")){
            cancel_btn.setVisibility(View.VISIBLE);
            track_btn.setVisibility(View.VISIBLE);
        }else{
            cancel_btn.setVisibility(View.INVISIBLE);
            track_btn.setVisibility(View.VISIBLE);
        }

        track_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(itemView ,"clicked",Snackbar.LENGTH_SHORT).show();
                if(track_btn.getText().toString().equals("view details")){
                    mcContect.startActivity(new Intent(mcContect , ViewMoreDetails.class).putExtra("id",oid1).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }else if(track_btn.getText().toString().equals("re order")){
                    alerConform(oid1);
                } else{
                    mcContect.startActivity(new Intent(mcContect , Track_Order.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("oid",oid1));
                }
            }
        });

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertdialog(oid1);
            }
        });

    }

    void alerConform(final String oid){
        CFAlertDialog.Builder builder = new CFAlertDialog.Builder(mcContect);
        builder.setCornerRadius(20);
        builder.setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT);
        builder.setTitle("Hey , there !");
        builder.setMessage("Sure you want to conform your Order . If you click continue  to complete your order."+" Your Order ID is : "+oid);
        builder.addButton("continue", -1, -1, CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                AppUtil.ORDERURl.child(oid).child("sts").setValue("new");
                sendnotify(oid,"successfully complete my order my Order ID is "+oid);
            }
        });
        builder.addButton("NOT NOW", -1, -1, CFAlertDialog.CFAlertActionStyle.DEFAULT, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    void alertdialog(final String oid){
        CFAlertDialog.Builder builder = new CFAlertDialog.Builder(mcContect);
        builder.setCornerRadius(20);
        builder.setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT);
        builder.setTitle("Hey , there !");
        builder.setMessage("Sure you want to cancel your Order . If you click to continue to cancel your order."+" Your Order ID is : "+oid);
        builder.addButton("continue", -1, -1, CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                AppUtil.ORDERURl.child(oid).child("sts").setValue("cancel");
                sendnotify(oid,"successfully cancel my order my Order ID is "+oid);
            }
        });
        builder.addButton("NOT NOW", -1, -1, CFAlertDialog.CFAlertActionStyle.DEFAULT, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    void sendnotify(String oid ,String mesg){
        Call<String> call = ApiUtil.getServiceClass().sendpush("Hai "+new TempData(mcContect).getuid() , mesg);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()){
                    Snackbar.make(itemView , "Successfully" , Snackbar.LENGTH_SHORT).show();
                }else{
                    Snackbar.make(itemView , "Successfully" , Snackbar.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("Error My Cart ", t.getMessage());
            }
        });
    }

}
