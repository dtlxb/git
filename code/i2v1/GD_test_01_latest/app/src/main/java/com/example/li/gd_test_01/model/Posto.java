package com.example.li.gd_test_01.model;

/**
 * Created by LI on 2017/7/4.
 */

import java.util.Date;
import java.util.List;

import com.amap.api.maps2d.model.LatLng;

public class Posto {


    public String name;                 //名字
    public Date time;                   //拍摄时间
    public LatLng location;             //拍摄地点（经纬度）
    public String comment;              //照片评论

    public String image_path;           //照片存储路径（手机上）
    public List<Route> belong_routes;   //包含了该照片的Route
    String image;

}
