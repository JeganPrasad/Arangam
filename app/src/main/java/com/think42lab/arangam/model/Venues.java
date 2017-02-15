package com.think42lab.arangam.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by krishna on 12/9/16.
 */

public class Venues implements Serializable {
    @SerializedName("name")
    private String Name;

    @SerializedName("id")
    private String id;


    @SerializedName("dummy")
    private String location;

    @SerializedName("location")
    private MyLocation myLocation;


    public MyLocation getMyLocation() {
        return myLocation;
    }

    public void setMyLocation(MyLocation myLocation) {
        this.myLocation = myLocation;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "{" +
                "name='" + Name + '\'' +
                ", id='" + id + '\'' +
                ", location='" + location + '\'' +
                ", myLocation=" + myLocation +
                '}';
    }
}
