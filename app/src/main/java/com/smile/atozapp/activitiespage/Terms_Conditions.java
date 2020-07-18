package com.smile.atozapp.activitiespage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;

import com.smile.atozapp.R;

public class Terms_Conditions extends AppCompatActivity {

    Toolbar mytoolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.terms__conditions);

        mytoolbar = findViewById(R.id.terms_toolbar);
        setSupportActionBar(mytoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mytoolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);


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