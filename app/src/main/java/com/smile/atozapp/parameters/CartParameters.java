package com.smile.atozapp.parameters;

public class CartParameters {
    String id,mid,pic,name,type,am,size,qnt;

    public CartParameters() {
    }

    public CartParameters(String id, String mid, String pic, String name, String type, String am, String size, String qnt) {
        this.id = id;
        this.mid = mid;
        this.pic = pic;
        this.name = name;
        this.type = type;
        this.am = am;
        this.size = size;
        this.qnt = qnt;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAm() {
        return am;
    }

    public void setAm(String am) {
        this.am = am;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getQnt() {
        return qnt;
    }

    public void setQnt(String qnt) {
        this.qnt = qnt;
    }
}
