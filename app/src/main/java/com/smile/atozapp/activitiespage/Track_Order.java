package com.smile.atozapp.activitiespage;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.smile.atozapp.R;
import com.smile.atozapp.controller.AppUtil;
import com.smile.atozapp.controller.DirectionsJSONParser;
import com.smile.atozapp.controller.LocationClass;
import com.smile.atozapp.controller.TempData;
import com.smile.atozapp.helper.CSAlertDialog;
import com.smile.atozapp.parameters.BillingParameters;
import com.smile.atozapp.parameters.DeliveryParameters;
import com.smile.atozapp.parameters.EmpParameters;
import com.smile.atozapp.parameters.OrderPatameters;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.smile.atozapp.activitiespage.Splase.MY_PERMISSIONS_REQUEST_LOCATION;

public class Track_Order extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    View view_new, view_taken, view_pending, view_complete;
    LottieAnimationView animationView;
    TextView top_title, top_amount, title_status, message_txt, delivery_location, my_location;
    CSAlertDialog dialog;
    private FusedLocationProviderClient fusedLocationProviderClient;
    LocationManager lm;
    Location currentloc;
    LatLng origin;
    LatLng dest;
    PolylineOptions lineOptions;
    boolean startTrack = false;
    ArrayList<LatLng> points;
    String delivery_id;
    Double delivery_lat = null ,delivery_lang = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.track__order);

        dialog = new CSAlertDialog(Track_Order.this);

        view_new = findViewById(R.id.track_order_new_view);
        view_taken = findViewById(R.id.track_order_taken_view);
        view_pending = findViewById(R.id.track_order_pending_view);
        view_complete = findViewById(R.id.track_order_complete_view);

        animationView = findViewById(R.id.track_order_animation);
        top_title = findViewById(R.id.track_order_top_title);
        top_amount = findViewById(R.id.track_order_top_amount);
        title_status = findViewById(R.id.track_order_title);
        message_txt = findViewById(R.id.track_order_message);

        delivery_location = findViewById(R.id.track_d_location);
        my_location = findViewById(R.id.track_my_location);

        delivery_location.setVisibility(View.INVISIBLE);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            checkLocationPermission();
            return;
        } else {
            checkLocationPermission();
        }
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                currentloc = location;
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                        .findFragmentById(R.id.track_order_map);
                mapFragment.getMapAsync(Track_Order.this);
            }
        });

    }

    void getLocationRoute() {

        if (new TempData(Track_Order.this).getDid() == null) {
            Toast.makeText(this, "no data found", Toast.LENGTH_SHORT).show();
        } else {
            AppUtil.EMPURL.child(new TempData(Track_Order.this).getDid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        if (snapshot.getValue() != null) {
                            if (checkLocationPermission()) {
                                final EmpParameters e = snapshot.getValue(EmpParameters.class);
                                delivery_lat = Double.parseDouble(e.getLat());
                                delivery_lang = Double.parseDouble(e.getLang());
                                Toast.makeText(Track_Order.this, "Location Updated Retry Or Return back This page", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            checkLocationPermission();
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    }

    /*void setRouteMap(LatLng destination_route){

            origin = new LatLng(currentloc.getLatitude(), currentloc.getLongitude());
            this.dest = destination_route;

            // Getting URL to the Google Directions API
            String url = getUrl(origin, dest);
            Log.d("onMapClick", url.toString());
            FetchUrl FetchUrl = new FetchUrl();

            // Start downloading json data from Google Directions API
            FetchUrl.execute(url);
            //move map camera
            mMap.moveCamera(CameraUpdateFactory.newLatLng(origin));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
            startTrack = true;

    }

    private String getUrl(LatLng origin, LatLng dest) {


        //  String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters + "&key=" + MY_API_KEY
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;


        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters
                + "&key=" + "AIzaSyAIW0Ru7C-9tFrHaiLU8OI0aqYThSUirwU";


        return url;
    }

    private class FetchUrl extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.e("Background Task data", data.toString());
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);


        }
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            Log.d("downloadUrl", data.toString());
            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                Log.d("ParserTask", jsonData[0].toString());
                DirectionsJSONParser parser = new DirectionsJSONParser();
                Log.d("ParserTask", parser.toString());

                // Starts parsing data
                routes = parser.parse(jObject);
                Log.d("ParserTask", "Executing routes");
                Log.d("ParserTask", routes.toString());

            } catch (Exception e) {
                Log.d("ParserTask", e.toString());
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            //  ArrayList<LatLng> points;
            lineOptions = null;

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.RED);

                Log.d("onPostExecute", "onPostExecute lineoptions decoded");

            }

            // Drawing polyline in the Google Map for the i-th route
            if (lineOptions != null) {
                mMap.addPolyline(lineOptions);
            } else {
                Log.d("onPostExecute", "without Polylines drawn");
            }
        }
    }*/

    void complete_dialog() {
        CFAlertDialog.Builder builder = new CFAlertDialog.Builder(this);
        builder.setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT);
        builder.setIcon(R.drawable.sparrowiconsmall);
        builder.setCornerRadius(20);
        builder.setTitle("Congratulation !");
        builder.setMessage("Your Order was successfully delivered. Thanks for your valuable Order.");
        builder.addButton("EXIT", -1, -1, CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog1, int which) {
                dialog1.dismiss();
                dialog.CancelDialog();
                finish();
            }
        });
        builder.show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (getIntent().hasExtra("oid")) {
            top_title.setText("your Order ID ( " + getIntent().getStringExtra("oid") + " )");
        } else {
            top_title.setText("No order found");
        }

        if (!checkLocationPermission()) {
            checkLocationPermission();
        }
        if (getIntent().getStringExtra("oid") != null) {
            AppUtil.ORDERURl.child(getIntent().getStringExtra("oid")).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        if (snapshot.getValue() != null) {
                            OrderPatameters o = snapshot.getValue(OrderPatameters.class);
                            if (o.getSts().equals("new")) {
                                title_status.setText("Your Order was waiting to taken by Market.");
                                message_txt.setText("Please wait.Process going on");
                                animationView.setAnimation(R.raw.search_gif);
                                animationView.playAnimation();
                                animationView.loop(true);
                            } else if (o.getSts().equals("taken")) {
                                view_taken.setBackground(new ColorDrawable(Color.BLACK));
                                title_status.setText("Your Order was taken by Market.");
                                message_txt.setText("Please wait. Your Parcel was packing.");
                                animationView.setAnimation(R.raw.packing_order);
                                animationView.playAnimation();
                                animationView.loop(true);
                            } else if (o.getSts().equals("pending")) {
                                view_taken.setBackground(new ColorDrawable(Color.BLACK));
                                view_pending.setBackground(new ColorDrawable(Color.BLACK));
                                title_status.setText("Your Order packing completed  by Market.");
                                message_txt.setText("your Order was taken by Delivery boy.");
                                animationView.setAnimation(R.raw.pickup_complete_gif);
                                animationView.playAnimation();
                                animationView.loop(true);
                            } else if (o.getSts().equals("complete")) {
                                view_taken.setBackground(new ColorDrawable(Color.BLACK));
                                view_pending.setBackground(new ColorDrawable(Color.BLACK));
                                view_complete.setBackground(new ColorDrawable(Color.BLACK));
                                title_status.setText("Your Order was deliver Successfully");
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialog.ShowDialog();
                                        complete_dialog();
                                    }
                                }, 3000);
                            } else if (o.getSts().equals("cancel")) {
                                title_status.setText("Your Order was Canceled by Market.");
                            }
                            if (o.getDid() != null) {
                                delivery_location.setVisibility(View.VISIBLE);
                                delivery_id = o.getDid();
                                new TempData(Track_Order.this).addDeliveryID(o.getDid());
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            AppUtil.BILLINGURl.child(getIntent().getStringExtra("oid")).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.getValue() != null) {
                        BillingParameters b = snapshot.getValue(BillingParameters.class);
                        top_amount.setText("To Pay Rs. " + b.getTotal_amount());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

        delivery_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentloc != null) {
                    if(delivery_lat!=null && delivery_lang!=null) {
                        final LatLng empLocation = new LatLng(delivery_lat, delivery_lang);
                        if (empLocation != null) {
                            mMap.addMarker(new MarkerOptions().title("Delivery Boy").position(empLocation).icon(bitmapDescriptorFromVector(Track_Order.this, R.drawable.delivery_location_icon)));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(empLocation, 15f));
                        }
                    }else {
                        getLocationRoute();
                    }
                } else {
                    checkLocationPermission();
                }
            }
        });

        my_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentloc != null) {
                    LatLng myLocation = new LatLng(currentloc.getLatitude(), currentloc.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(myLocation).title("USER").icon(bitmapDescriptorFromVector(Track_Order.this, R.drawable.home_location_icon)));
                    mMap.setIndoorEnabled(true);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15f));
                } else {
                    checkLocationPermission();
                }
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (currentloc != null) {
            LatLng myLocation = new LatLng(currentloc.getLatitude(), currentloc.getLongitude());
            mMap.addMarker(new MarkerOptions().position(myLocation).title("USER").icon(bitmapDescriptorFromVector(Track_Order.this, R.drawable.home_location_icon)));
            mMap.setIndoorEnabled(true);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15f));
        } else {
            checkLocationPermission();
        }
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

}