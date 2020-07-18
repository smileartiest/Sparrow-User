package com.smile.atozapp.activitiespage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.View;

import com.smile.atozapp.R;

public class FAQ_page extends AppCompatActivity {

    Toolbar mytoolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.faq_page);

        mytoolbar = findViewById(R.id.fag_toolbar);
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
    protected void onStart() {
        super.onStart();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}