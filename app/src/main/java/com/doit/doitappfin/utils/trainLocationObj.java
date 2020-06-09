package com.doit.doitappfin.utils;

public class trainLocationObj {

String location,name;
    Double y;
String latlng,locid,price,discount;


    public trainLocationObj() {
    }

    public trainLocationObj(String location, String name, String latlng, String locid, String price, String discount, Double v) {
        this.location = location;
        this.name = name;
        this.latlng = latlng;
        this.locid = locid;
        this.price=price;
        this.discount=discount;
        this.y=y;

    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLatlng() {
        return latlng;
    }

    public void setLatlng(String latlng) {
        this.latlng = latlng;
    }

    public String getLocid() {
        return locid;
    }

    public void setLocid(String locid) {
        this.locid = locid;
    }
}
