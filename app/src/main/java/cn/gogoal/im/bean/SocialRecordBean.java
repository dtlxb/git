package cn.gogoal.im.bean;

import java.util.ArrayList;

/**
 * Created by dave.
 * Date: 2017/5/3.
 * Desc: description
 */

public class SocialRecordBean {

    private int code;
    private ArrayList<SocialRecordData> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public ArrayList<SocialRecordData> getData() {
        return data;
    }

    public void setData(ArrayList<SocialRecordData> data) {
        this.data = data;
    }
}
