package com.think42lab.arangam.model;

import com.google.gson.annotations.SerializedName;
import com.think42lab.arangam.Program;

import java.io.Serializable;

/**
 * Created by Lenovo on 12/9/2016.
 */

public class Artists implements Serializable {
    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    private Program program;

    public Artists() {

    }

    public Artists(String id, String name) {
        this.id = id;
        this.name = name;

    }

    public Artists(String id, String name, Program program) {
        this.id = id;
        this.name = name;
        this.program = program;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Program getProgram() {
        return program;
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    @Override
    public String toString() {
        return "{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", program=" + program +
                '}';
    }
}