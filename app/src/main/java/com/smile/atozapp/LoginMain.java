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
import com.smile.atozapp.fragment.Home;
import com.smile.atozapp.fragment.MyAccount;
import com.smile.atozapp.fragment.MyOrder;
import com.smile.atozapp.fragment.Offer;
import com.smile.atozapp.fragment.Search;
import com.smile.atozapp.parameters.ContactusParameters;
import com.smile.atozapp.parameters.LocationParameters;

public class LoginMain extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    TextView callccare, sendwhatsapp;
    TextView profilepic;
    TextView locationname;
    ConstraintLayout nodata, storests;
    TextView editlocation;
    FrameLayout fram;

    String mobno, whatsapp;
    ProgressDialog pd;

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
                    loadfragment(new Home());
                    getSupportActionBar().show();
                    return true;
                case R.id.bot_nav_cart:
                    startActivity(new Intent(getApplicationContext(), MyCart.class));
                    return true;
                case R.id.bot_nav_category:
                    loadfragment(new Category());
                    getSupportActionBar().hide();
                    return true;
                case R.id.bot_nav_search:
                    loadfragment(new Search());
                    getSupportActionBar().hide();
                    return true;
                case R.id.bot_nav_offer:
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

        t = new TempData(LoginMain.this);

        pd = new ProgressDialog(LoginMain.this);
        pd.setTitle("Loading");
        pd.setMessage("Please wait.....");
        pd.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                pd.dismiss();
            }
        }, 4000);

        fram = findViewById(R.id.frameLayout);
        profilepic = findViewById(R.id.login_main_profiletxt);
        locationname = findViewById(R.id.login_main_location);
        nodata = findViewById(R.id.main_not_here);
        editlocation = findViewById(R.id.nhere_edit_loc);

        storests = findViewById(R.id.main_store_closed);
        callccare = findViewById(R.id.bg_store_close_call);
        sendwhatsapp = findViewById(R.id.bg_store_close_whatsapp);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, myaction, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);

        AppUtil.REGURL.child(new TempData(LoginMain.this).getuid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child("name").getValue() != null) {
                            String temp = dataSnapshot.child("name").getValue().toString();
                            profilepic.setText(String.valueOf(temp.charAt(0)).toUpperCase());
                        } else {
                            profilepic.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });

        locationname.setText(t.getloc());
        AppUtil.LOCURL.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    if (t.getloc() != null) {
                        boolean sts = false;
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            LocationParameters l = data.getValue(LocationParameters.class);
                            if (t.getloc().equals(l.getName())) {
                                sts = true;
                            }
                        }
                        if (sts) {
                            appsts();
                        } else {
                            fram.setVisibility(View.GONE);
                            nodata.setVisibility(View.VISIBLE);
                            bottomNavigationView.setVisibility(View.GONE);
                            getSupportActionBar().hide();
                        }

                    } else {
                        nodata.setVisibility(View.VISIBLE);
                        fram.setVisibility(View.GONE);
                        bottomNavigationView.setVisibility(View.GONE);
                        getSupportActionBar().hide();
                    }

                } else {
                    nodata.setVisibility(View.VISIBLE);
                    fram.setVisibility(View.GONE);
                    bottomNavigationView.setVisibility(View.GONE);
                    getSupportActionBar().hide();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("loginmain", databaseError.getMessage());
            }
        });
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

    }

    public void appsts() {
        AppUtil.STSURL.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    if (dataSnapshot.child("sts").getValue().toString().equals("available")) {
                        fram.setVisibility(View.VISIBLE);
                        nodata.setVisibility(View.GONE);
                        storests.setVisibility(View.GONE);
                        bottomNavigationView.setVisibility(View.VISIBLE);
                        loadfragment(new Home());
                    } else {
                        getdetails();
                        storests.setVisibility(View.VISIBLE);
                        getSupportActionBar().hide();
                        fram.setVisibility(View.GONE);
                        nodata.setVisibility(View.GONE);
                        bottomNavigationView.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
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

        callccare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mobno.length() != 0) {
                    if (checkLocationPermission()) {
                        Intent in = new Intent(Intent.ACTION_CALL).setData(Uri.parse("tel:" + mobno));
                        startActivity(in);
                    } else {
                        checkLocationPermission();
                        Toast.makeText(getApplicationContext(), "dont make a call", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        sendwhatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (whatsapp.length() != 0) {
                    Uri uri = Uri.parse("smsto:" + whatsapp);
                    Intent i = new Intent(Intent.ACTION_SENDTO, uri);
                    i.setPackage("com.whatsapp");
                    startActivity(Intent.createChooser(i, "hai sparrow"));
                } else {
                    Toast.makeText(getApplicationContext(), "don't send whatsapp", Toast.LENGTH_SHORT).show();
                }
            }
        });

        profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadfragment(new MyAccount());
                getSupportActionBar().hide();
            }
        });

        locationname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginMain.this, MyLocation.class));
            }
        });

        editlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginMain.this, MyLocation.class));
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
            case R.id.login_menu_myorder:
                loadfragment(new MyOrder());
                getSupportActionBar().hide();
                break;
            case R.id.login_menu_logout:
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
            loadfragment(new Category());
            getSupportActionBar().hide();
        } else if (item.getItemId() == R.id.nav_offer) {
            loadfragment(new Offer());
            getSupportActionBar().hide();
        } else if (item.getItemId() == R.id.nav_myprofile) {
            loadfragment(new MyAccount());
            getSupportActionBar().hide();
        } else if (item.getItemId() == R.id.nav_cart) {
            startActivity(new Intent(getApplicationContext(), MyCart.class));
        } else if (item.getItemId() == R.id.nav_contactus) {
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

    void getdetails() {
        AppUtil.CONTURL.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    ContactusParameters c = dataSnapshot.getValue(ContactusParameters.class);
                    mobno = c.getMobno();
                    whatsapp = c.getWhatsapp();
                } else {
                    mobno = "";
                    whatsapp = "";
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CALL_PHONE)) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CALL_PHONE},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CALL_PHONE},
                        MY_PERMISSIONS_REQUEST_LOCATION);

            }
            return false;
        } else {

            return true;
        }
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
