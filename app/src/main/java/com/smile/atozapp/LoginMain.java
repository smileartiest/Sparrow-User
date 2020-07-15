package com.smile.atozapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.crowdfire.cfalertdialog.CFAlertDialog;
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
import com.smile.atozapp.controller.TempOrder;
import com.smile.atozapp.fragment.Category;
import com.smile.atozapp.fragment.ContactUs;
import com.smile.atozapp.fragment.Disclaimer;
import com.smile.atozapp.fragment.Home;
import com.smile.atozapp.fragment.MyAccount;
import com.smile.atozapp.fragment.MyOrder;
import com.smile.atozapp.fragment.Offer;
import com.smile.atozapp.fragment.Search;
import com.smile.atozapp.helper.CSAlertDialog;
import com.smile.atozapp.parameters.OrderPatameters;

public class LoginMain extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    TextView locantion_city,location_local;
    ConstraintLayout location_card;

    ConstraintLayout order_dialog,track_dialog;
    TextView dialog_title,dialog_message,track_message;
    Button dialog_complete,track_btn;

    Toolbar myaction;
    TempData t;

    BottomNavigationView bottomNavigationView;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public final String TAG = "Login Main";

    CSAlertDialog dialog;

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.bot_nav_home:
                    loadfragment(new Home());
                    getSupportActionBar().show();
                    return true;
                case R.id.bot_nav_notification:
                    order_dialog.setVisibility(View.GONE);
                    return true;
                case R.id.bot_nav_category:
                    order_dialog.setVisibility(View.GONE);
                    loadfragment(new Category());
                    getSupportActionBar().hide();
                    return true;
                case R.id.bot_nav_search:
                    order_dialog.setVisibility(View.GONE);
                    loadfragment(new Search());
                    getSupportActionBar().hide();
                    return true;
                case R.id.bot_nav_offer:
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

        dialog = new CSAlertDialog(this);
        dialog.ShowDialog();

        order_dialog = findViewById(R.id.main_didalog);
        dialog_title = findViewById(R.id.main_d_title);
        dialog_message = findViewById(R.id.main_d_message);
        dialog_complete = findViewById(R.id.main_d_conformbtn);

        track_dialog = findViewById(R.id.main_track_box);
        track_message = findViewById(R.id.main_track_message);
        track_btn = findViewById(R.id.main_track_btn);

        locantion_city = findViewById(R.id.main_city);
        location_local = findViewById(R.id.main_local);
        location_card = findViewById(R.id.main_address);

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
                    dialog.CancelDialog();
                    dialog_title.setText("Total Cart Items "+ snapshot.getChildrenCount());
                    order_dialog.setVisibility(View.VISIBLE);
                }else{
                    dialog.CancelDialog();
                    order_dialog.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        loadfragment(new Home());
        Log.d("State ","Oncreate");

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
        Log.d("State ","Onresume");
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

        if(new TempOrder(LoginMain.this).getoid()!=null){
            Log.d("Order ID ",new TempOrder(LoginMain.this).getoid());
            track_dialog.setVisibility(View.VISIBLE);
            AppUtil.ORDERURl.child(new TempOrder(LoginMain.this).getoid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        if(snapshot.getValue()!=null){
                            OrderPatameters o = snapshot.getValue(OrderPatameters.class);
                            if(o.getSts().equals("new")){
                                track_message.setText("Your Order was waiting to taken by Market.");
                                Log.d("Track Status ","Your Order was waiting to taken by Market.");
                            }else if (o.getSts().equals("taken")){
                                track_message.setText("Your Order was taken by Market.....");
                                Log.d("Track Status ","Your Order was taken by Market.");
                            }else if (o.getSts().equals("pending")){
                                track_message.setText("Your Order packing completed  by Market.");
                                Log.d("Track Status ","Your Order packing completed  by Market.");
                            }else if (o.getSts().equals("complete")){
                                track_message.setText("Your Order was deliver Successfully ");
                                Log.d("Track Status ","Your Order was deliver Successfully ");
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        new TempOrder(LoginMain.this).tclear();
                                    }
                                },1000);
                            }
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }

        track_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext() , Track_Order.class).putExtra("oid",new TempOrder(LoginMain.this).getoid()));
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
            loadfragment(new Home());
            getSupportActionBar().show();
        } else if (item.getItemId() == R.id.nav_category) {
            order_dialog.setVisibility(View.GONE);
            loadfragment(new Category());
            getSupportActionBar().hide();
        } else if (item.getItemId() == R.id.nav_offer) {
            order_dialog.setVisibility(View.GONE);
            loadfragment(new Offer());
            getSupportActionBar().hide();
        } else if(item.getItemId() == R.id.nav_myprofile){
            order_dialog.setVisibility(View.GONE);
            loadfragment(new MyAccount());
            getSupportActionBar().hide();
        } else if(item.getItemId() == R.id.nav_cart){
            startActivity(new Intent(getApplicationContext(), MyCart.class));
        }else if(item.getItemId() == R.id.nav_disclaimer){
            order_dialog.setVisibility(View.GONE);
            loadfragment(new Disclaimer());
            getSupportActionBar().hide();
        } else if (item.getItemId() == R.id.nav_myorder) {
            order_dialog.setVisibility(View.GONE);
            loadfragment(new MyOrder());
            getSupportActionBar().hide();
        } else if (item.getItemId() == R.id.nav_logout) {
            CFAlertDialog.Builder builder = new CFAlertDialog.Builder(this);
            builder.setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT);
            builder.setIcon(R.drawable.sparrowiconsmall);
            builder.setCornerRadius(20);
            builder.setTitle("Hey , There !");
            builder.setMessage("Are You sure, want to close this app ?");
            builder.addButton("EXIT", -1, -1, CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    new TempData(LoginMain.this).logout();
                    startActivity(new Intent(getApplicationContext(), Login.class));
                    finish();
                }
            });
            builder.addButton("NOT NOW", -1, -1, CFAlertDialog.CFAlertActionStyle.DEFAULT, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();
        } else if (item.getItemId() == R.id.nav_contactus) {
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
        CFAlertDialog.Builder builder = new CFAlertDialog.Builder(this);
        builder.setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT);
        builder.setIcon(R.drawable.sparrowiconsmall);
        builder.setCornerRadius(20);
        builder.setTitle("Hey , There !");
        builder.setMessage("Are You sure, want to close this app ?");
        builder.addButton("EXIT", -1, -1, CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finishAffinity();
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
}
