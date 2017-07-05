package com.example.dell.bzbp_frame.model;

import android.location.Location;
import android.provider.ContactsContract;
import org.w3c.dom.Text;

import java.io.Serializable;
import java.sql.Time;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by dell on 2017/7/5.
 */

public class Posto implements Serializable {
    private ContactsContract.CommonDataKinds.Photo mPhoto;
    private Location mLocation;
    private Text mText;
    private Time mDatetimestart;

    public ContactsContract.CommonDataKinds.Photo getPhoto() {
        return mPhoto;
    }

    public void setPhoto(ContactsContract.CommonDataKinds.Photo photo) {
        mPhoto = photo;
    }

    public Location getLocation() {
        return mLocation;
    }

    public void setLocation(Location location) {
        mLocation = location;
    }

    public Text getText() {
        return mText;
    }

    public void setText(Text text) {
        mText = text;
    }

    public Time getDatetimestart() {
        return mDatetimestart;
    }

    public void setDatetimestart(Time datetimestart) {
        mDatetimestart = datetimestart;
    }


    private Set<Route> mRoutes = new HashSet<Route>();
    public Set<Route> getRoutes() {
        return mRoutes;
    }

    public void setRoutes(Set<Route> mRoutes) {
        this.mRoutes = mRoutes;
    }
}
