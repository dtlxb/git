package cn.gogoal.im.bean.companytags;

import java.util.List;

/**
 * Created by huangxx on 2017/6/28.
 */

public class HistoryBean {
    private String message;
    private int code;
    private List<HistoryData> data;

    public HistoryBean() {
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

    public List<HistoryData> getData() {
        return data;
    }

    public void setData(List<HistoryData> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "HistoryBean{" +
                "message='" + message + '\'' +
                ", code=" + code +
                ", data=" + data +
                '}';
    }
}
