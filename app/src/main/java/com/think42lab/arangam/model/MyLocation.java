package com.think42lab.arangam.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Lenovo on 12/19/2016.
 */
public class MyLocation implements Serializable {

    @SerializedName("X")
    private String X;

    @SerializedName("Y")
    private String Y;

    public MyLocation(String x, String y) {
        X = x;
        Y = y;
    }

    public String getX() {
        return X;
    }

    public void setX(String x) {
        X = x;
    }

    public String getY() {
        return Y;
    }

    public void setY(String y) {
        Y = y;
    }

    @Override
    public String toString() {
        return "{" +
                "X='" + X + '\'' +
                ", Y='" + Y + '\'' +
                '}';
    }
}
