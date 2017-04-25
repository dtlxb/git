package cn.gogoal.im.bean;

import java.util.List;

/**
 * Created by dave.
 * Date: 2017/4/24.
 * Desc: description
 */

public class SocialLiveBean {

    private int code;
    private List<SocialLiveData> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<SocialLiveData> getData() {
        return data;
    }

    public void setData(List<SocialLiveData> data) {
        this.data = data;
    }
}
