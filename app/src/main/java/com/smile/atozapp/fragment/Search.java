package com.smile.atozapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.smile.atozapp.R;
import com.smile.atozapp.controller.AppUtil;
import com.smile.atozapp.models.MarketHold;
import com.smile.atozapp.parameters.MarketParameters;

import java.util.ArrayList;

public class Search extends Fragment {

    View v;
    AutoCompleteTextView searchbar ;
    ImageView searchbtn;
    ArrayList<String> searchlist = new ArrayList<>();
    ConstraintLayout nodata;

    RecyclerView list;
    TextView searchcount;

    Query q;

    public Search() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.frag_search , container , false);

        searchbar = v.findViewById(R.id.search_search);
        list = v.findViewById(R.id.search_list);
        searchcount = v.findViewById(R.id.search_totalcount);
        searchbtn = v.findViewById(R.id.search_search_btn);
        nodata = v.findViewById(R.id.search_noitem);

        list.setLayoutManager(new LinearLayoutManager(getContext()));
        list.setHasFixedSize(true);

        nodata.setVisibility(View.VISIBLE);

        getsearchlist();

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setdetails(searchbar.getText().toString());
            }
        });

    }

    public void setdetails(String itemname){

        q = AppUtil.MARKETURL.orderByChild("mname").equalTo(itemname);
        getcount(q);
        FirebaseRecyclerAdapter<MarketParameters, MarketHold> frecycle = new FirebaseRecyclerAdapter<MarketParameters, MarketHold>(
                MarketParameters.class , R.layout.row_marketitems , MarketHold.class , q
        ) {
            @Override
            protected void populateViewHolder(MarketHold mh, MarketParameters mp, int i) {
                mh.setdetails(getContext() , mp.getId(),mp.getMpic(),mp.getMname(),mp.getMtype(),mp.getMcatg(),mp.getMqnt(),mp.getMam() , mp.getStock());
            }
        };
        list.setAdapter(frecycle);
    }

    public void getcount(Query q1)
    {
        q1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null)
                {
                    nodata.setVisibility(View.GONE);
                    searchcount.setText("Total search products : "+dataSnapshot.getChildrenCount());
                }else {
                    nodata.setVisibility(View.VISIBLE);
                    searchcount.setText("Total search 0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void getsearchlist(){
        AppUtil.MARKETURL.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){
                    for(DataSnapshot data : dataSnapshot.getChildren()){
                        searchlist.add(data.child("mname").getValue().toString());
                    }
                    ArrayAdapter<String> ad = new ArrayAdapter<>(getContext() , R.layout.spinlist , searchlist);
                    searchbar.setAdapter(ad);
                    searchbar.setThreshold(1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
