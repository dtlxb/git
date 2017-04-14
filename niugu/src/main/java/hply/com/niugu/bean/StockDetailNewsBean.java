package hply.com.niugu.bean;

import java.util.ArrayList;

/**
 * Created by daiwei on 2015/9/30.
 */
public class StockDetailNewsBean {
    /*{
        "code": 0,
            "data": [
        ],
        "message": "成功"
    }*/
    private String code;
    private ArrayList<StockDetailNewsData> data;
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ArrayList<StockDetailNewsData> getData() {
        return data;
    }

    public void setData(ArrayList<StockDetailNewsData> data) {
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
        return "StockDetailNewsBean{" +
                "code='" + code + '\'' +
                ", data=" + data +
                ", message='" + message + '\'' +
                '}';
    }
}
