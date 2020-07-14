package com.smile.atozapp.controller;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AppUtil {

    public static DatabaseReference REGURL = FirebaseDatabase.getInstance().getReference("usregister");
    public static DatabaseReference OFFERURL = FirebaseDatabase.getInstance().getReference("offer");
    public static DatabaseReference CARTURL =  FirebaseDatabase.getInstance().getReference("cart");
    public static DatabaseReference ORDERURl = FirebaseDatabase.getInstance().getReference("order");
    public static DatabaseReference ADDRESURL = FirebaseDatabase.getInstance().getReference("address");
    public static DatabaseReference MARKETURL = FirebaseDatabase.getInstance().getReference("market");
    public static DatabaseReference DRESSURL = FirebaseDatabase.getInstance().getReference("dress");
    public static DatabaseReference LOCURL = FirebaseDatabase.getInstance().getReference("locations");
    public static DatabaseReference EMPURL = FirebaseDatabase.getInstance().getReference("employe");
    public static DatabaseReference CONTURL = FirebaseDatabase.getInstance().getReference("contactus");
    public static DatabaseReference DRESSPICURL = FirebaseDatabase.getInstance().getReference("dresspic");
    public static DatabaseReference STSURL = FirebaseDatabase.getInstance().getReference("storestatus");
    public static DatabaseReference CHARGES = FirebaseDatabase.getInstance().getReference("charge");

}
