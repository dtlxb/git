package hply.com.niugu.bean;

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
    private String code;

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
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
