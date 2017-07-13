package cn.gogoal.im.bean.stock;

import java.util.ArrayList;

/**
 * Created by daiwei on 2015/9/17.
 */
public class SearchStockBean {
    /*{
        "code": 0,
            "data": [
        ],
        "message": "成功"
    }*/
    private String code;
    private ArrayList<SearchStockData> data;
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ArrayList<SearchStockData> getData() {
        return data;
    }

    public void setData(ArrayList<SearchStockData> data) {
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
        return "SearchStockBean{" +
                "code='" + code + '\'' +
                ", data=" + data +
                ", message='" + message + '\'' +
                '}';
    }
}
