package hply.com.niugu.bean;

import java.util.ArrayList;

/**
 * Created by daiwei on 2015/9/21.
 */
public class StockDecisionBean {
    /*{
        "code": 0,
            "data": [
        ],
        "message": "成功"
    }*/
    private int code;
    private ArrayList<StockDecisionData> data;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public ArrayList<StockDecisionData> getData() {
        return data;
    }

    public void setData(ArrayList<StockDecisionData> data) {
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
