package com.smile.atozapp.parameters;

public class ChargeParameters {

    String dcharge,discount;

    public ChargeParameters() {
    }

    public ChargeParameters(String dcharge, String discount) {
        this.dcharge = dcharge;
        this.discount = discount;
    }

    public String getDcharge() {
        return dcharge;
    }

    public void setDcharge(String dcharge) {
        this.dcharge = dcharge;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }
}
