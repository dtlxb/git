package cn.gogoal.im.bean.companytags;

import java.util.List;

/**
 * Created by huangxx on 2017/6/29.
 */

public class IncidentBean {

    private String message;
    private int code;
    private List<IncidentData> data;

    public IncidentBean() {
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

    public List<IncidentData> getData() {
        return data;
    }

    public void setData(List<IncidentData> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "IncidentBean{" +
                "message='" + message + '\'' +
                ", code=" + code +
                ", data=" + data +
                '}';
    }
}
