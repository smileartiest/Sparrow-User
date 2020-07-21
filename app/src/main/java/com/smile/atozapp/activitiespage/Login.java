package com.smile.atozapp.activitiespage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.smile.atozapp.R;
import com.smile.atozapp.controller.AppUtil;
import com.smile.atozapp.controller.TempData;

public class Login extends AppCompatActivity {

    TextInputLayout mobno;
    CheckBox accept_box;
    TextView click_to_view;
    Button login;
    String check_sts = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        mobno = findViewById(R.id.login_mobno);
        login = findViewById(R.id.login_loginbtn);
        accept_box = findViewById(R.id.login_terms_condition);
        click_to_view = findViewById(R.id.login_terms_condition_click);

    }

    @Override
    protected void onResume() {
        super.onResume();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String no = mobno.getEditText().getText().toString();
                if (no.length() != 0 && no.length() == 10) {
                    if(check_sts!=null) {
                        startActivity(new Intent(getApplicationContext(), OtpPage.class).putExtra("ph", no));
                        finish();
                    }else {
                        Snackbar.make(v , "Please accept terms & conditions.",Snackbar.LENGTH_SHORT).show();
                    }
                } else {
                    mobno.getEditText().setError("enter valid number");
                }
            }
        });
        accept_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(accept_box.isChecked()){
                    check_sts = "yes";
                }else{
                    check_sts = null;
                }
            }
        });
        click_to_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://sparrowhypermarket.000webhostapp.com/sparrow/termscondition.php");
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://sparrowhypermarket.000webhostapp.com/sparrow/termscondition.php")));
                }
            }
        });
    }

}
