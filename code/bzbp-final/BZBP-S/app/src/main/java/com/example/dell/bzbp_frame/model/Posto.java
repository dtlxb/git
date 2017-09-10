package com.example.dell.bzbp_frame.model;

import android.location.Location;
import android.provider.ContactsContract;

import com.amap.api.maps.model.LatLng;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by dell on 2017/7/5.
 */

public class Posto implements Serializable {

    private int pid;                    //数据库id
    private String username;            //拍摄者信息



    private String name;                //名字·
    private String comment;             //评论（char[300]）·

    private String image;               //图片文件转化成的byte[]·

    private String path_local;          //本机图片路径
    private String path_server;         //服务器端图片路径

    private Double latitude;        //纬度·
    private Double longitude;       //经度·

    private Long date;              //拍摄日期（Datestamp）·


    private int belong_rid;    //关联route的id（-1代表无关联）·

    public int getBelong_rid() {
        return belong_rid;
    }

    public void setBelong_rid(int belong_rid) {
        this.belong_rid = belong_rid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPath_local() {
        return path_local;
    }

    public void setPath_local(String path_local) {
        this.path_local = path_local;
    }

    public String getPath_server() {
        return path_server;
    }

    public void setPath_server(String path_server) {
        this.path_server = path_server;
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

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

}
