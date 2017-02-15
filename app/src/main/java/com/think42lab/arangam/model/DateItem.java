package com.think42lab.arangam.model;

/**
 * Created by Nishanth on 12/18/2016.
 */
public class DateItem extends ListItem{
    public DateItem(String date) {
        this.date = date;
    }

    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public int getType() {
        return TYPE_DATE;
    }
}
