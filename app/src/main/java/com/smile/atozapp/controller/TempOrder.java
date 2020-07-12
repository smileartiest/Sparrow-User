package com.smile.atozapp.controller;

import android.content.Context;
import android.content.SharedPreferences;

public class TempOrder {

    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor ed;
    Context mcontext;

    public TempOrder(Context mcontext) {
        this.mcontext = mcontext;
        sharedPreferences = mcontext.getSharedPreferences("order" , Context.MODE_PRIVATE);
        ed = sharedPreferences.edit();
        ed.apply();
    }

    public void addoid(String id){
        ed.putString("oid" , id).apply();
    }

    public String getoid(){
        return sharedPreferences.getString("oid",null);
    }

    public void addaddress(String aid){
        ed.putString("aid" , aid).apply();
    }

    public String getaddress(){
        return sharedPreferences.getString("aid" , null);
    }

    public void addcartid(String cid){
        ed.putString("cid" , cid).apply();
    }

    public String getcid(){
        return sharedPreferences.getString("cid",null);
    }

    public void adddpic(String pic , String id){
        ed.putString("dpic" , pic).apply();
        ed.putString("did" , id ).apply();
    }

    public String getmid(){
        return sharedPreferences.getString("did" , null);
    }

    public String getpic(){
        return sharedPreferences.getString("dpic" ,null);
    }

    public void adddresssizeam(String dsiz,String dam){
        ed.putString("dsiz" , dsiz).apply();
        ed.putString("dam" , dam).apply();
    }

    public String getdresssize(){
        return sharedPreferences.getString("dsiz",null);
    }

    public String getdressam(){
        return sharedPreferences.getString("dam",null);
    }

    public void tclear(){
        ed.clear().apply();
    }

}
