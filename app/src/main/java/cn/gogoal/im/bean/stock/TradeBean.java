package cn.gogoal.im.bean.stock;

import java.util.List;

/**
 * Created by huangxx on 2017/6/21.
 */

public class TradeBean {

    private MoneyBean data;
    private String message;
    private int code;

    public MoneyBean getData() {
        return data;
    }

    public void setData(MoneyBean data) {
        this.data = data;
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

    @Override
    public String toString() {
        return "TradeBean{" +
                "data=" + data +
                ", message='" + message + '\'' +
                ", code=" + code +
                '}';
    }
}
