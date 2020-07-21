package com.smile.atozapp.parameters;

public class EmpParameters {
    String id,ename,epass,epic,ests,eusname,lat,lang;

    public EmpParameters() {
    }

    public EmpParameters(String id, String ename, String epass, String epic, String ests, String eusname, String lat, String lang) {
        this.id = id;
        this.ename = ename;
        this.epass = epass;
        this.epic = epic;
        this.ests = ests;
        this.eusname = eusname;
        this.lat = lat;
        this.lang = lang;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public String getEpass() {
        return epass;
    }

    public void setEpass(String epass) {
        this.epass = epass;
    }

    public String getEpic() {
        return epic;
    }

    public void setEpic(String epic) {
        this.epic = epic;
    }

    public String getEsts() {
        return ests;
    }

    public void setEsts(String ests) {
        this.ests = ests;
    }

    public String getEusname() {
        return eusname;
    }

    public void setEusname(String eusname) {
        this.eusname = eusname;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }
}
