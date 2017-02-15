package com.gogoal.app.bean;

import java.util.List;

/**
 * Created by huangxx on 2017/2/15.
 */
public class StockMinuteBean {
    private int code;
    private List<StockMinuteData> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<StockMinuteData> getData() {
        return data;
    }

    public void setData(List<StockMinuteData> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "StockMinuteBean{" +
                "code=" + code +
                ", data=" + data +
                '}';
    }
}
