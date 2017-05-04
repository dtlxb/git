package cn.gogoal.im.bean;

import java.util.List;

/**
 * Created by dave.
 * Date: 2017/5/3.
 * Desc: description
 */

public class SocialRecordBean {

    private int code;
    private List<SocialRecordData> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<SocialRecordData> getData() {
        return data;
    }

    public void setData(List<SocialRecordData> data) {
        this.data = data;
    }
}
