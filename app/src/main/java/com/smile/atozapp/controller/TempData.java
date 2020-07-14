package com.smile.atozapp.controller;

import android.content.Context;
import android.content.SharedPreferences;

public class TempData {

    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor ed;
    Context mcontext;

    public TempData(Context mcontext) {
        this.mcontext = mcontext;
        sharedPreferences = mcontext.getSharedPreferences("atoz" , Context.MODE_PRIVATE);
        ed = sharedPreferences.edit();
        ed.apply();
    }

    public void addlogsts(String sts){
        ed.putString("log",sts).apply();
    }

    public String getlogsts(){
        return sharedPreferences.getString("log",null);
    }

    public void adduid(String uid){
        ed.putString("uid" , uid).apply();
    }

    public String getuid(){
        return sharedPreferences.getString("uid" , null);
    }

    public void logout(){
        ed.clear().apply();
    }

    public void addlocation( String id, String city , String add){
        ed.putString("id" , id);
        ed.putString("city",city);
        ed.putString("add" , add);
        ed.apply();
    }

    public String getcity(){
        return sharedPreferences.getString("city",null);
    }

    public String getadd(){
        return sharedPreferences.getString("add",null);
    }

    public String getaid(){
        return sharedPreferences.getString("add",null);
    }

    public void addtoken(String tid){
        ed.putString("fid",tid).apply();
    }

    public String gettoken(){
        return sharedPreferences.getString("fid","");
    }

}
