package hply.com.niugu.bean;

import java.io.Serializable;

/**
 * Created by wangjd on 2016/6/22 0022.
 */
public class TimeDetialData implements Serializable {

    public TimeDetialData() {
    }

    private double last_price_change;

    private String price;

    private String price_change;

    private int transaction_type;

    private String update_time;

    private int volume;

    public double getLast_price_change() {
        return last_price_change;
    }

    public void setLast_price_change(double last_price_change) {
        this.last_price_change = last_price_change;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPrice_change() {
        return price_change;
    }

    public void setPrice_change(String price_change) {
        this.price_change = price_change;
    }

    public int getTransaction_type() {
        return transaction_type;
    }

    public void setTransaction_type(int transaction_type) {
        this.transaction_type = transaction_type;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }
}
