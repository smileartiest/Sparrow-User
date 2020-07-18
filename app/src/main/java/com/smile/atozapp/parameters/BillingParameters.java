package com.smile.atozapp.parameters;

public class BillingParameters {

    String id,oid,name,size,quantity,amount,sub_total,delivery_charge,discount,total_amount,sts,bdate,btime;

    public BillingParameters() {
    }

    public BillingParameters(String id, String oid, String name, String size, String quantity, String amount, String sub_total, String delivery_charge, String discount, String total_amount, String sts, String bdate, String btime) {
        this.id = id;
        this.oid = oid;
        this.name = name;
        this.size = size;
        this.quantity = quantity;
        this.amount = amount;
        this.sub_total = sub_total;
        this.delivery_charge = delivery_charge;
        this.discount = discount;
        this.total_amount = total_amount;
        this.sts = sts;
        this.bdate = bdate;
        this.btime = btime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getAmount() {
        return amount;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getSub_total() {
        return sub_total;
    }

    public void setSub_total(String sub_total) {
        this.sub_total = sub_total;
    }

    public String getDelivery_charge() {
        return delivery_charge;
    }

    public void setDelivery_charge(String delivery_charge) {
        this.delivery_charge = delivery_charge;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getSts() {
        return sts;
    }

    public void setSts(String sts) {
        this.sts = sts;
    }

    public String getBdate() {
        return bdate;
    }

    public void setBdate(String bdate) {
        this.bdate = bdate;
    }

    public String getBtime() {
        return btime;
    }

    public void setBtime(String btime) {
        this.btime = btime;
    }
}
