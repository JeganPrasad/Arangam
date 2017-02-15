package com.think42lab.arangam;

/**
 * Created by Lenovo on 12/2/2016.
 */

public class DataModelSchedule {
    private String name;
    private String date;
    private String price;
    private boolean isPressed;

    public DataModelSchedule(String name, String price, String date) {
        this.name = name;
        this.price = price;
        this.date = date;
        this.isPressed = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPrice() {
        return price;
    }

    // public boolean getFa() {
    //   return fa;
    //}
    //public void setFa() {
    //  this.fa=false;

    //}

    public void setPrice(String price) {
        this.price = price;
    }

    public boolean isPressed() {
        return isPressed;
    }

    public void setPressed(boolean pressed) {
        this.isPressed = pressed;
    }
}
