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

    private String mText;
    private Time mTime;

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    public Time getTime() {
        return mTime;
    }

    public void setTime(Time time) {
        mTime = time;
    }

    private List<Posto> mPostoList = new ArrayList();

    public List<Posto> getPostos() {
        return mPostoList;
    }

    public void setPostos(List<Posto> postos) {
        this.mPostoList = postos;
    }

    private List<Location> mLocationList = new ArrayList();

    public List<Location> getLocations() {
        return mLocationList;
    }

    public void setLocations(List<Location> locations) {
        this.mLocationList = locations;
    }
}