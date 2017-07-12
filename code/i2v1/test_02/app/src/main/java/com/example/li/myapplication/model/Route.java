package com.example.li.myapplication.model;

import android.os.Parcelable;

import com.amap.api.maps2d.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.li.myapplication.model.MyLatlng;

/**
 * Created by LI on 2017/7/4.
 */

public class Route implements  Serializable {
    private static final long serialVersionUID = -8575978901888824848L;

    public int rid;
    public String name;
    public ArrayList<MyLatlng> location_list;
    public String comment;
    public Date start, end;
    public ArrayList<Posto> postos;

    public Route(){
        location_list = new ArrayList<MyLatlng>();
        postos = new ArrayList<Posto>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public ArrayList<Posto> getPostos() {
        return postos;
    }

    public void setPostos(ArrayList<Posto> postos) {
        this.postos = postos;
    }
}
