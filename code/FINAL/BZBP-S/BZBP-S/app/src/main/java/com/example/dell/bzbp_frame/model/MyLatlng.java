package com.example.dell.bzbp_frame.model;

import com.amap.api.maps2d.model.LatLng;

import java.io.Serializable;

/**
 * Created by LI on 2017/7/11.
 */

public class MyLatlng implements Serializable {
    private static final long serialVersionUID = -2901878664207916734L;

    public Double latitude;
    public Double longitude;

    public MyLatlng(Double latitude, Double longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public LatLng toLatlng(){
        return new LatLng(latitude,longitude);
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
