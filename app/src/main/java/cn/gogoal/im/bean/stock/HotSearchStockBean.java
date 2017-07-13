package cn.gogoal.im.bean.stock;

import java.util.ArrayList;

/**
 * Created by daiwei on 2016/2/22.
 */
public class HotSearchStockBean {
    /*{
        "message": "成功",
        "data": [
         ],
         "code": 0
    }*/
    private String message;
    private ArrayList<HotSearchStockData> data;
    private int code;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<HotSearchStockData> getData() {
        return data;
    }

    public void setData(ArrayList<HotSearchStockData> data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "HotSearchStockBean{" +
                "message='" + message + '\'' +
                ", data=" + data +
                ", code='" + code + '\'' +
                '}';
    }
}
