package com.example.dell.bzbp_frame.model;

import java.io.Serializable;
import java.util.ArrayList;

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
    public ArrayList<Integer> pids;

    public Route(){
        location_list = new ArrayList<MyLatlng>();
        pids = new ArrayList<Integer>();
    }

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    public ArrayList<MyLatlng> getLocation_list() {
        return location_list;
    }

    public void setLocation_list(ArrayList<MyLatlng> location_list) {
        this.location_list = location_list;
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

    public ArrayList<Integer> getPids() {
        return pids;
    }

    public void setPids(ArrayList<Integer> pids) {
        this.pids = pids;
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


}