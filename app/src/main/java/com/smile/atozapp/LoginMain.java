package com.smile.atozapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.smile.atozapp.controller.AppUtil;
import com.smile.atozapp.controller.TempData;
import com.smile.atozapp.fragment.Category;
import com.smile.atozapp.fragment.ContactUs;
import com.smile.atozapp.fragment.Disclaimer;
import com.smile.atozapp.fragment.Home;
import com.smile.atozapp.fragment.MyAccount;
import com.smile.atozapp.fragment.MyOrder;
import com.smile.atozapp.fragment.Offer;
import com.smile.atozapp.fragment.Search;

public class LoginMain extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    TextView locantion_city,location_local,search_products;
    ConstraintLayout location_card,search_box;

    ConstraintLayout order_dialog;
    TextView dialog_title;
    Button dialog_complete;

    Toolbar myaction;
    TempData t;

    BottomNavigationView bottomNavigationView;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public final String TAG = "Login Main";

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.bot_nav_home:
                    search_box.setVisibility(View.VISIBLE);
                    loadfragment(new Home());
                    getSupportActionBar().show();
                    return true;
                case R.id.bot_nav_notification:
                    search_box.setVisibility(View.GONE);
                    order_dialog.setVisibility(View.GONE);
                    return true;
                case R.id.bot_nav_category:
                    search_box.setVisibility(View.GONE);
                    order_dialog.setVisibility(View.GONE);
                    loadfragment(new Category());
                    getSupportActionBar().hide();
                    return true;
                case R.id.bot_nav_search:
                    search_box.setVisibility(View.GONE);
                    order_dialog.setVisibility(View.GONE);
                    loadfragment(new Search());
                    getSupportActionBar().hide();
                    return true;
                case R.id.bot_nav_offer:
                    search_box.setVisibility(View.GONE);
                    order_dialog.setVisibility(View.GONE);
                    loadfragment(new Offer());
                    getSupportActionBar().hide();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);

        myaction = findViewById(R.id.toolbar);
        setSupportActionBar(myaction);
        getSupportActionBar().show();

        order_dialog = findViewById(R.id.main_didalog);
        dialog_title = order_dialog.findViewById(R.id.main_d_title);
        dialog_complete = order_dialog.findViewById(R.id.main_d_conformbtn);

        locantion_city = findViewById(R.id.main_city);
        location_local = findViewById(R.id.main_local);
        location_card = findViewById(R.id.main_address);
        search_products = findViewById(R.id.main_search);
        search_box = findViewById(R.id.main_search_box);

        t = new TempData(LoginMain.this);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, myaction, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }
                        String token = task.getResult().getToken();
                        t.addtoken(token);
                        Log.d(TAG, token);
                    }
                });
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);

        if(t.getcity()!=null){
            locantion_city.setText(t.getcity());
            location_local.setText(t.getadd());
            Log.d("if","working");
        }else{
            Log.d("else","working");
            locantion_city.setText("Please Add Location First");
            location_local.setText("");
        }

        AppUtil.CARTURL.child(new TempData(LoginMain.this).getuid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue()!=null){
                    dialog_title.setText("Total Cart Items "+ snapshot.getChildrenCount());
                    order_dialog.setVisibility(View.VISIBLE);
                }else{
                    order_dialog.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        loadfragment(new Home());

    }

    public void loadfragment(Fragment frag) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, frag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(t.getcity()!=null){
            locantion_city.setText(t.getcity());
            location_local.setText(t.getadd());
        }else{
            locantion_city.setText("Set Location");
            location_local.setText("");
        }

        location_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginMain.this, MyLocation.class));
            }
        });

        search_products.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_box.setVisibility(View.GONE);
                loadfragment(new Search());
                getSupportActionBar().hide();
            }
        });

        AppUtil.CARTURL.child(new TempData(LoginMain.this).getuid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue()!=null){
                    dialog_title.setText("Total Cart Items "+ snapshot.getChildrenCount());
                    order_dialog.setVisibility(View.VISIBLE);
                }else{
                    order_dialog.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        dialog_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext() , MyCart.class));
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.loginmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.login_menu_mycart:
                startActivity(new Intent(getApplicationContext(), MyCart.class));
                break;
            case R.id.login_menu_myprofile:
                search_box.setVisibility(View.GONE);
                order_dialog.setVisibility(View.GONE);
                loadfragment(new MyAccount());
                getSupportActionBar().hide();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        if (item.getItemId() == R.id.nav_home) {
            search_box.setVisibility(View.VISIBLE);
            loadfragment(new Home());
            getSupportActionBar().show();
        } else if (item.getItemId() == R.id.nav_category) {
            search_box.setVisibility(View.GONE);
            order_dialog.setVisibility(View.GONE);
            loadfragment(new Category());
            getSupportActionBar().hide();
        } else if (item.getItemId() == R.id.nav_offer) {
            search_box.setVisibility(View.GONE);
            order_dialog.setVisibility(View.GONE);
            loadfragment(new Offer());
            getSupportActionBar().hide();
        } else if(item.getItemId() == R.id.nav_myprofile){
            search_box.setVisibility(View.GONE);
            order_dialog.setVisibility(View.GONE);
            loadfragment(new MyAccount());
            getSupportActionBar().hide();
        } else if(item.getItemId() == R.id.nav_cart){
            startActivity(new Intent(getApplicationContext(), MyCart.class));
        }else if(item.getItemId() == R.id.nav_disclaimer){
            search_box.setVisibility(View.GONE);
            order_dialog.setVisibility(View.GONE);
            loadfragment(new Disclaimer());
            getSupportActionBar().hide();
        } else if (item.getItemId() == R.id.nav_myorder) {
            search_box.setVisibility(View.GONE);
            order_dialog.setVisibility(View.GONE);
            loadfragment(new MyOrder());
            getSupportActionBar().hide();
        } else if (item.getItemId() == R.id.nav_logout) {
            final Dialog d = new Dialog(LoginMain.this);
            d.setContentView(R.layout.calertdialog);
            d.getWindow().setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
            TextView message = d.findViewById(R.id.calert_message);
            message.setText("Are You Sure ? Want to Logout ?");
            Button yes = d.findViewById(R.id.calert_yes);
            Button no = d.findViewById(R.id.calert_no);
            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new TempData(LoginMain.this).logout();
                    startActivity(new Intent(getApplicationContext(), Login.class));
                    finish();
                }
            });
            no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    d.dismiss();
                }
            });
            d.show();
        } else if (item.getItemId() == R.id.nav_contactus) {
            search_box.setVisibility(View.GONE);
            order_dialog.setVisibility(View.GONE);
            loadfragment(new ContactUs());
            getSupportActionBar().hide();
        } else if (item.getItemId() == R.id.nav_share) {
            try {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Get Wash Customer Application");
                String shareMessage = "\nLet me recommend you this application\n\n";
                shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "choose one"));
            } catch (Exception e) {
                //e.toString();
            }
        } else if (item.getItemId() == R.id.nav_rateapp) {
            Uri uri = Uri.parse("market://details?id=" + getApplicationContext().getPackageName());
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
                        Uri.parse("http://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName())));
            }
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        final Dialog d = new Dialog(LoginMain.this);
        d.setContentView(R.layout.calertdialog);
        d.getWindow().setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        TextView message = d.findViewById(R.id.calert_message);
        message.setText("Are You Sure ? Want to close this app ?");
        Button yes = d.findViewById(R.id.calert_yes);
        Button no = d.findViewById(R.id.calert_no);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
                finish();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        d.show();
    }
}
