package cn.gogoal.im.bean.stock;

import java.io.Serializable;

/**
 * author wangjd on 2017/4/7 0007.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public class Stock implements Serializable{
    private String current_price;
    private String stock_code;
    private String stock_name;
    private int stock_type;

    public String getCurrent_price() {
        return current_price;
    }

    public void setCurrent_price(String current_price) {
        this.current_price = current_price;
    }

    public String getStock_code() {
        return stock_code;
    }

    public void setStock_code(String stock_code) {
        this.stock_code = stock_code;
    }

    public String getStock_name() {
        return stock_name;
    }

    public void setStock_name(String stock_name) {
        this.stock_name = stock_name;
    }

    public int getStock_type() {
        return stock_type;
    }

    public void setStock_type(int stock_type) {
        this.stock_type = stock_type;
    }

    public enum StockType{

        SUSPENDED(0),  //停牌

        NORMAL(1),      //正常

        SUSPENSION(2),  //暂停上市

        DELISTING(-1),  //退市

        UNLISTED(-2);   //未上市

        private int type;

        StockType(int type) {
            this.type = type;
        }
    }
}
