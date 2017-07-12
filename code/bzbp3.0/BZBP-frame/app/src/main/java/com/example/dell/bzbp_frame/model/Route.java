package com.example.dell.bzbp_frame.model;

import android.location.Location;

import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by dell on 2017/7/5.
 */

public class Route implements Serializable {

    private static final long serialVersionUID = 3494746809864002552L;

    public int rid;
    public String username;
    public ArrayList<MyLatlng> location_list;
    public String comment;
    public Long start_time, end_time;
    public ArrayList<Posto> postos;

    public Route(){
        location_list = new ArrayList<MyLatlng>();
        postos = new ArrayList<Posto>();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getStart_time() {
        return start_time;
    }

    public void setStart_time(Long start_time) {
        this.start_time = start_time;
    }

    public Long getEnd_time() {
        return end_time;
    }

    public void setEnd_time(Long end_time) {
        this.end_time = end_time;
    }

    public ArrayList<Posto> getPostos() {
        return postos;
    }

    public void setPostos(ArrayList<Posto> postos) {
        this.postos = postos;
    }

}