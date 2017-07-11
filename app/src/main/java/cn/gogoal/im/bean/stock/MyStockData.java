package cn.gogoal.im.bean.stock;

import android.os.Parcelable;

import java.io.Serializable;

/**
 * author wangjd on 2017/4/10 0010.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public class MyStockData implements Serializable,Cloneable {
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

    private int symbol_type;
//    private String open_price;
//    private int id;
//    private String pe_y2;
//    private String pe_y1;
//    private String turnover;
//    private String pe_y0;
//    private int is_keypoint;
    private double close_price;
//    private String eps_y0;
//    private String industry_name;
//    private String mcap;
//    private String eps_y1;
//    private String capitalization;
//    private String high_price;
//    private String insertdate;
    private String source;
//    private String low_price;
//    private String turnover_rate;
//    private String volume;
    private String full_code;

//    private int group_id;
//    private String date;
//    private int remind_headlines;
//    private int remind_notice;
//    private int stock_class;
    private String stock_code;
    private int stock_sort;
    private String stock_name;
//    private String change_value;
    private double price;
    private String change_rate;
    private int stock_type;

    //添加选择的字段
    private boolean check = false;

    public double getClose_price() {
        return close_price;
    }

    public void setClose_price(double close_price) {
        this.close_price = close_price;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getFull_code() {
        return full_code;
    }

    public void setFull_code(String full_code) {
        this.full_code = full_code;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
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

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public int getSymbol_type() {
        return symbol_type;
    }

    public void setSymbol_type(int symbol_type) {
        this.symbol_type = symbol_type;
    }

    @Override
    public MyStockData clone() {
        try {
            return (MyStockData) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }
}