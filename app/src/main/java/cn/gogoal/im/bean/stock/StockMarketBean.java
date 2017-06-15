package cn.gogoal.im.bean.stock;

/**
 * author wangjd on 2017/3/10 0010.
 * Staff_id 1375
 * phone 18930640263
 */
public class StockMarketBean {

    private String message;
    private Market data;
    private int code;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Market getData() {
        return data;
    }

    public void setData(Market data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

}
