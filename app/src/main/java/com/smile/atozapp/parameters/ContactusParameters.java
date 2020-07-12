package com.smile.atozapp.parameters;

public class ContactusParameters {
    String email,whatsapp,mobno;

    public ContactusParameters() {
    }

    public ContactusParameters(String email, String whatsapp, String mobno) {
        this.email = email;
        this.whatsapp = whatsapp;
        this.mobno = mobno;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWhatsapp() {
        return whatsapp;
    }

    public void setWhatsapp(String whatsapp) {
        this.whatsapp = whatsapp;
    }

    public String getMobno() {
        return mobno;
    }

    public void setMobno(String mobno) {
        this.mobno = mobno;
    }
}
