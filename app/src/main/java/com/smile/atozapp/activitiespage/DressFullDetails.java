package com.smile.atozapp.activitiespage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.smile.atozapp.R;
import com.smile.atozapp.controller.AppUtil;
import com.smile.atozapp.controller.TempData;
import com.smile.atozapp.controller.TempOrder;
import com.smile.atozapp.parameters.CartParameters;
import com.smile.atozapp.parameters.DressParameters;

import java.util.ArrayList;

public class DressFullDetails extends AppCompatActivity {

    ImageView pic;
    TextView name,type,type_title,price,prize_title,offer,offer_title,size_title,need_count,need_add,need_remove,pic1,pic2,pic3;
    Button conform;
    DatabaseReference df;
    SharedPreferences sf;
    int i=1;
    ArrayList<String> urllist = new ArrayList<>();
    ArrayList<String> sizelist = new ArrayList<>();
    ArrayList<String> amlist = new ArrayList<>();

    Toolbar mytoolbar;
    Spinner spin_size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dress_full_details);

        mytoolbar = findViewById(R.id.more_toolbar);
        setSupportActionBar(mytoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mytoolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);

        pic = findViewById(R.id.more_pic);
        name = findViewById(R.id.more_dname);
        type = findViewById(R.id.more_dtype);
        price = findViewById(R.id.more_dprice);
        offer = findViewById(R.id.more_doffer);
        need_count = findViewById(R.id.more_need_count);
        need_add = findViewById(R.id.more_need_add);
        need_remove = findViewById(R.id.more_need_remove);
        conform = findViewById(R.id.more_conformbtn);
        spin_size = findViewById(R.id.more_size_spinner);

        pic1 = findViewById(R.id.more_pic1);
        pic2 = findViewById(R.id.more_pic2);
        pic3 = findViewById(R.id.more_pic3);

        type_title = findViewById(R.id.more_dtype_title);
        prize_title = findViewById(R.id.more_dprice_title);
        offer_title = findViewById(R.id.more_doffer_title);
        size_title = findViewById(R.id.more_size_title);

        need_count.setText(String.valueOf(i));

        if (getIntent().getStringExtra("f").equals("dress")) {
            df = AppUtil.DRESSURL.child(getIntent().getStringExtra("id"));
        } else if (getIntent().getStringExtra("f").equals("electronics")) {
            df = AppUtil.ELECTRONICURL.child(getIntent().getStringExtra("id"));
            type_title.setText("Mobile Type");
            prize_title.setText("Mobile Price");
            offer_title.setText("Mobile Offer");
            size_title.setText("Choose Your Custom Colour");
        }
        sf = getSharedPreferences("order",MODE_PRIVATE);


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
                    for(int i = 0 ; i <sizesplit.length ; i++){
                        sizelist.add(sizesplit[i]);
                        amlist.add(amsplit[i]);
                    }
                    ArrayAdapter<String> ad = new ArrayAdapter(DressFullDetails.this , R.layout.spinlist , sizesplit);
                    spin_size.setAdapter(ad);
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
                    CartParameters c = new CartParameters(key,new TempOrder(DressFullDetails.this).getmid(),new TempOrder(DressFullDetails.this).getpic(),name.getText().toString(),type.getText().toString(),getIntent().getStringExtra("f"),new TempOrder(DressFullDetails.this).getdressam(),new TempOrder(DressFullDetails.this).getdresssize(),need_count.getText().toString());
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

        spin_size.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                new TempOrder(DressFullDetails.this).adddresssizeam(sizelist.get(position) , amlist.get(position));
                price.setText(amlist.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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

        mytoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
