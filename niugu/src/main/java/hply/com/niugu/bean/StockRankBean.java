package hply.com.niugu.bean;

/**
 * Created by huangxx on 2015/9/15.
 */
public class StockRankBean {

    private int code;
    private StockList data;

    public StockList getData() {
        return data;
    }

    public void setData(StockList data) {
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
        return "StockRankBean{" +
                "code=" + code +
                ", data=" + data +
                '}';
    }
}
