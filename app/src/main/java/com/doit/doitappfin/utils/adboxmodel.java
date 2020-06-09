package com.doit.doitappfin.utils;

public class adboxmodel {

    private  String image,click,name;

    public adboxmodel(String image, String click, String name) {
        this.image = image;
        this.click = click;
        this.name = name;
    }

    public adboxmodel() {
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getClick() {
        return click;
    }

    public void setClick(String click) {
        this.click = click;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
