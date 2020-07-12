package com.smile.atozapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.smile.atozapp.controller.AppUtil;
import com.smile.atozapp.controller.TempData;
import com.smile.atozapp.controller.TempOrder;
import com.smile.atozapp.parameters.CartParameters;
import com.smile.atozapp.parameters.DressParameters;

import java.util.ArrayList;

public class DressFullDetails extends AppCompatActivity {

    ImageView pic;
    TextView name,type,price,offer,s,m,l,xl,xxl,need_count,need_add,need_remove,pic1,pic2,pic3;
    Button conform;
    DatabaseReference df;
    SharedPreferences sf;
    int i=1;
    String ps,pm,pl,pxl,pxxl;
    ArrayList<String> urllist = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dress_full_details);

        pic = findViewById(R.id.more_pic);
        name = findViewById(R.id.more_dname);
        type = findViewById(R.id.more_dtype);
        price = findViewById(R.id.more_dprice);
        offer = findViewById(R.id.more_doffer);
        s = findViewById(R.id.more_size_s);
        m = findViewById(R.id.more_size_m);
        l = findViewById(R.id.more_size_l);
        xl = findViewById(R.id.more_size_xl);
        xxl = findViewById(R.id.more_size_xxl);
        need_count = findViewById(R.id.more_need_count);
        need_add = findViewById(R.id.more_need_add);
        need_remove = findViewById(R.id.more_need_remove);
        conform = findViewById(R.id.more_conformbtn);

        pic1 = findViewById(R.id.more_pic1);
        pic2 = findViewById(R.id.more_pic2);
        pic3 = findViewById(R.id.more_pic3);

        need_count.setText(String.valueOf(i));

        df = AppUtil.DRESSURL.child(getIntent().getStringExtra("id"));
        sf = getSharedPreferences("order",MODE_PRIVATE);

        s.setVisibility(View.GONE);
        m.setVisibility(View.GONE);
        l.setVisibility(View.GONE);
        xl.setVisibility(View.GONE);
        xxl.setVisibility(View.GONE);

        urllist.clear();

        df.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){
                    DressParameters d = dataSnapshot.getValue(DressParameters.class);
                    Glide.with(getApplicationContext()).load(d.getDpic()).into(pic);
                    name.setText(d.getDname());
                    type.setText(d.getDtype());
                    price.setText(d.getDam());
                    offer.setText(d.getDoff());
                    new TempOrder(DressFullDetails.this).adddpic(d.getDpic() , d.getId());

                    urllist.add(d.getDpic());

                    String tempsize = d.getSize();
                    String tempam = d.getDam();

                    final String[] sizesplit = tempsize.split(",");
                    final String[] amsplit = tempam.split(",");

                    if(sizesplit.length>0){
                        for(int i = 0 ; i <sizesplit.length ; i++){
                            if(sizesplit[i].equals("s")){
                                s.setVisibility(View.VISIBLE);
                                ps = amsplit[i];
                            }else if(sizesplit[i].equals("m")){
                                m.setVisibility(View.VISIBLE);
                                pm = amsplit[i];
                            }else if(sizesplit[i].equals("l")){
                                l.setVisibility(View.VISIBLE);
                                pl = amsplit[i];
                            }else if(sizesplit[i].equals("xl")){
                                xl.setVisibility(View.VISIBLE);
                                pxl = amsplit[i];
                            }else if(sizesplit[i].equals("xxl")){
                                xxl.setVisibility(View.VISIBLE);
                                pxxl = amsplit[i];
                            }
                        }
                    }else{
                        if(d.getSize().equals("s")){
                            s.setVisibility(View.VISIBLE);
                            m.setVisibility(View.GONE);
                            l.setVisibility(View.GONE);
                            xl.setVisibility(View.GONE);
                            xxl.setVisibility(View.GONE);
                        }else if(d.getSize().equals("m")){
                            m.setVisibility(View.VISIBLE);
                            s.setVisibility(View.GONE);
                            l.setVisibility(View.GONE);
                            xl.setVisibility(View.GONE);
                            xxl.setVisibility(View.GONE);
                        }else if(d.getSize().equals("l")){
                            l.setVisibility(View.VISIBLE);
                            s.setVisibility(View.GONE);
                            m.setVisibility(View.GONE);
                            xl.setVisibility(View.GONE);
                            xxl.setVisibility(View.GONE);
                        }else if(d.getSize().equals("xl")){
                            xl.setVisibility(View.VISIBLE);
                            s.setVisibility(View.GONE);
                            m.setVisibility(View.GONE);
                            l.setVisibility(View.GONE);
                            xxl.setVisibility(View.GONE);
                        }else if(d.getSize().equals("xxl")){
                            xxl.setVisibility(View.VISIBLE);
                            s.setVisibility(View.GONE);
                            m.setVisibility(View.GONE);
                            l.setVisibility(View.GONE);
                            xl.setVisibility(View.GONE);
                        }
                    }
                }
                else {
                    name.setText("");
                    type.setText("");
                    price.setText("");
                    offer.setText("");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        AppUtil.DRESSPICURL.child(getIntent().getStringExtra("id")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){
                    for(DataSnapshot data : dataSnapshot.getChildren()){
                        urllist.add(data.getValue().toString());
                    }
                    if(urllist.size()==2){
                        pic3.setVisibility(View.GONE);
                    }else if(urllist.size()==3){
                        pic1.setVisibility(View.VISIBLE);
                        pic2.setVisibility(View.VISIBLE);
                        pic3.setVisibility(View.VISIBLE);
                    }

                }else{
                    pic2.setVisibility(View.GONE);
                    pic3.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        pic1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(getApplicationContext()).load(urllist.get(0)).into(pic);
            }
        });

        pic2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(getApplicationContext()).load(urllist.get(1)).into(pic);
            }
        });

        pic3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(getApplicationContext()).load(urllist.get(2)).into(pic);
            }
        });

        conform.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(i>0){
                    String key = AppUtil.CARTURL.push().getKey();
                    CartParameters c = new CartParameters(key,new TempOrder(DressFullDetails.this).getmid(),new TempOrder(DressFullDetails.this).getpic(),name.getText().toString(),type.getText().toString(),new TempOrder(DressFullDetails.this).getdressam(),new TempOrder(DressFullDetails.this).getdresssize(),need_count.getText().toString());
                    AppUtil.CARTURL.child(new TempData(DressFullDetails.this).getuid()).child(key).setValue(c);
                    finish();
                }
                else {
                    AlertDialog.Builder ad = new AlertDialog.Builder(DressFullDetails.this);
                    ad.setTitle("Oops !!");
                    ad.setMessage("Please choose atleast one");
                    ad.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    ad.show();
                }
            }
        });

        s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TempOrder(DressFullDetails.this).adddresssizeam("S",ps);
                price.setText(ps);
                s.setBackgroundResource(R.drawable.text_select);
                m.setBackgroundResource(R.drawable.borderline);
                l.setBackgroundResource(R.drawable.borderline);
                xl.setBackgroundResource(R.drawable.borderline);
                xxl.setBackgroundResource(R.drawable.borderline);
            }
        });

        m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TempOrder(DressFullDetails.this).adddresssizeam("M",pm);
                price.setText(pm);
                s.setBackgroundResource(R.drawable.borderline);
                m.setBackgroundResource(R.drawable.text_select);
                l.setBackgroundResource(R.drawable.borderline);
                xl.setBackgroundResource(R.drawable.borderline);
                xxl.setBackgroundResource(R.drawable.borderline);
            }
        });

        l.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TempOrder(DressFullDetails.this).adddresssizeam("L",pl);
                price.setText(pl);
                s.setBackgroundResource(R.drawable.borderline);
                m.setBackgroundResource(R.drawable.borderline);
                l.setBackgroundResource(R.drawable.text_select);
                xl.setBackgroundResource(R.drawable.borderline);
                xxl.setBackgroundResource(R.drawable.borderline);
            }
        });

        xl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TempOrder(DressFullDetails.this).adddresssizeam("XL",pxl);
                price.setText(pxl);
                s.setBackgroundResource(R.drawable.borderline);
                m.setBackgroundResource(R.drawable.borderline);
                l.setBackgroundResource(R.drawable.borderline);
                xl.setBackgroundResource(R.drawable.text_select);
                xxl.setBackgroundResource(R.drawable.borderline);
            }
        });

        xxl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TempOrder(DressFullDetails.this).adddresssizeam("XXL",pxxl);
                price.setText(pxxl);
                s.setBackgroundResource(R.drawable.borderline);
                m.setBackgroundResource(R.drawable.borderline);
                l.setBackgroundResource(R.drawable.borderline);
                xl.setBackgroundResource(R.drawable.borderline);
                xxl.setBackgroundResource(R.drawable.text_select);
            }
        });

        need_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int temp = i+1;
                i=temp;
                need_count.setText(String.valueOf(i));
            }
        });

        need_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(i>0) {
                    int temp = i - 1;
                    i = temp;
                    need_count.setText(String.valueOf(i));
                }
                else{
                    i=1;
                    need_count.setText(String.valueOf(i));
                }
            }
        });

    }
}
