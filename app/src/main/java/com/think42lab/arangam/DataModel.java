package com.think42lab.arangam;

/**
 * Created by Lenovo on 12/2/2016.
 */

public class DataModel {
    private String name;
    private String date;
    private String price;

    public DataModel(String name, String price, String date) {
        this.name = name;
        this.price = price;
        this.date = date;
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

    public void setPrice(String price) {
        this.price = price;
    }
}
