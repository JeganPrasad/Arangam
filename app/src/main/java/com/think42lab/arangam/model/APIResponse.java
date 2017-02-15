package com.think42lab.arangam.model;

import java.io.Serializable;

/**
 * Created by think42lab on 20/12/16.
 */

public class APIResponse implements Serializable{
    private int status;
    private String message;
    private Data data;


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
