package com.think42lab.arangam.model;

/**
 * Created by Nishanth on 12/18/2016.
 */

public abstract class ListItem {

    public static final int TYPE_DATE = 0;
    public static final int TYPE_GENERAL = 1;

    abstract public int getType();
}

