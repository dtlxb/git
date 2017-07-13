package cn.gogoal.im.bean.stock;

import java.util.ArrayList;

/**
 * Created by daiwei on 2015/10/10.
 */
public class StockDetailMarketIndexBean {
    /*{
        "code":0,
            "data":[
        ],
        "message":"成功"
    }*/
    private int code;
    private ArrayList<StockDetailMarketIndexData> data;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public ArrayList<StockDetailMarketIndexData> getData() {
        return data;
    }

    public void setData(ArrayList<StockDetailMarketIndexData> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "StockDetailMarketIndexBean{" +
                "code='" + code + '\'' +
                ", data=" + data +
                ", message='" + message + '\'' +
                '}';
    }
}
