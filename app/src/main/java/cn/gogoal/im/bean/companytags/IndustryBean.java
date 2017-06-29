package cn.gogoal.im.bean.companytags;

import java.util.List;

/**
 * Created by huangxx on 2017/6/29.
 */

public class IndustryBean {

    private String message;
    private int code;
    private List<IndustryData> data;

    public IndustryBean() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<IndustryData> getData() {
        return data;
    }

    public void setData(List<IndustryData> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "IndustryBean{" +
                "message='" + message + '\'' +
                ", code=" + code +
                ", data=" + data +
                '}';
    }
}
