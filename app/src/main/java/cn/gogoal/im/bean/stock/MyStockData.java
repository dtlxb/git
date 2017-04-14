package cn.gogoal.im.bean.stock;

import java.io.Serializable;

/**
 * author wangjd on 2017/4/10 0010.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public class MyStockData implements Serializable {
    /**
     * group_id : 137269
     * date : 2016-10-13 17:50:46
     * remind_headlines : 1
     * remind_notice : 1
     * stock_class : 0
     * stock_code : 002101
     * stock_sort : 8
     * stock_name : 广东鸿图
     * change_value : 0.09999999999999787
     * price : 23.15
     * change_rate : 0.43383947939261547
     * stock_type : 1
     */

    private boolean check;
    private int group_id;
    private String date;
    private int remind_headlines;
    private int remind_notice;
    private int stock_class;
    private String stock_code;
    private int stock_sort;
    private String stock_name;
    private String change_value;
    private String price;
    private String change_rate;
    private int stock_type;

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getRemind_headlines() {
        return remind_headlines;
    }

    public void setRemind_headlines(int remind_headlines) {
        this.remind_headlines = remind_headlines;
    }

    public int getRemind_notice() {
        return remind_notice;
    }

    public void setRemind_notice(int remind_notice) {
        this.remind_notice = remind_notice;
    }

    public int getStock_class() {
        return stock_class;
    }

    public void setStock_class(int stock_class) {
        this.stock_class = stock_class;
    }

    public String getStock_code() {
        return stock_code;
    }

    public void setStock_code(String stock_code) {
        this.stock_code = stock_code;
    }

    public int getStock_sort() {
        return stock_sort;
    }

    public void setStock_sort(int stock_sort) {
        this.stock_sort = stock_sort;
    }

    public String getStock_name() {
        return stock_name;
    }

    public void setStock_name(String stock_name) {
        this.stock_name = stock_name;
    }

    public String getChange_value() {
        return change_value;
    }

    public void setChange_value(String change_value) {
        this.change_value = change_value;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getChange_rate() {
        return change_rate;
    }

    public void setChange_rate(String change_rate) {
        this.change_rate = change_rate;
    }

    public int getStock_type() {
        return stock_type;
    }

    public void setStock_type(int stock_type) {
        this.stock_type = stock_type;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof MyStockData) && ((MyStockData) obj).getStock_code().equalsIgnoreCase(this.getStock_code());
    }
}
