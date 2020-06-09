package com.doit.doitappfin.utils;

public class ProfileData {

    String name,email,number,image,addr,city,iscomplete,sex,date;

    public ProfileData() {
    }

    public ProfileData(String name, String email, String number, String image, String addr, String city, String iscomplete, String sex, String date) {
        this.name = name;
        this.email = email;
        this.number = number;
        this.image = image;
        this.addr = addr;
        this.city = city;
        this.iscomplete = iscomplete;
        this.sex = sex;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getIscomplete() {
        return iscomplete;
    }

    public void setIscomplete(String iscomplete) {
        this.iscomplete = iscomplete;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
