package com.example.li.gd_test_01.model;

import com.amap.api.maps2d.model.LatLng;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by LI on 2017/7/4.
 */

public class Route {
    public String name;
    public List<LatLng> location_list;
    public String comment;
    public Date start, end;
    public List<Posto> postos;

    public Route(){
        location_list = new LinkedList<LatLng>();
        postos = new LinkedList<Posto>();
    }
}
