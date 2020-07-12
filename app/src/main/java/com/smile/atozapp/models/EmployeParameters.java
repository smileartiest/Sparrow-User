package com.smile.atozapp.models;

public class EmployeParameters {
    String id,epic,ename,eusname,epass,ests,lat,lang;

    public EmployeParameters() {
    }

    public EmployeParameters(String id, String epic, String ename, String eusname, String epass, String ests, String lat, String lang) {
        this.id = id;
        this.epic = epic;
        this.ename = ename;
        this.eusname = eusname;
        this.epass = epass;
        this.ests = ests;
        this.lat = lat;
        this.lang = lang;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEpic() {
        return epic;
    }

    public void setEpic(String epic) {
        this.epic = epic;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public String getEusname() {
        return eusname;
    }

    public void setEusname(String eusname) {
        this.eusname = eusname;
    }

    public String getEpass() {
        return epass;
    }

    public void setEpass(String epass) {
        this.epass = epass;
    }

    public String getEsts() {
        return ests;
    }

    public void setEsts(String ests) {
        this.ests = ests;
    }
}
