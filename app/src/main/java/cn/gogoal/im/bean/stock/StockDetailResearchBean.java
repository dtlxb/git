package cn.gogoal.im.bean.stock;

import java.util.ArrayList;

/**
 * Created by daiwei on 2015/10/8.
 */
public class StockDetailResearchBean {
    /*{
        "code": 0,
            "data": [
        {  }
            ],
            "message": "成功"
        }*/
    private String code;
    private ArrayList<StockDetailResearchData> data;
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ArrayList<StockDetailResearchData> getData() {
        return data;
    }

    public void setData(ArrayList<StockDetailResearchData> data) {
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
        return "StockDetailResearchBean{" +
                "code='" + code + '\'' +
                ", data=" + data +
                ", message='" + message + '\'' +
                '}';
    }
}
