package cn.gogoal.im.bean.companytags;

import cn.gogoal.im.bean.BaseBeanList;

/**
 * Created by huangxx on 2017/7/3.
 */

public class StockHolderBean extends BaseBeanList {

    private float stock_total_ratio;
    private String date;

    public StockHolderBean() {
    }

    public StockHolderBean(float stock_total_ratio, String date) {
        this.stock_total_ratio = stock_total_ratio;
        this.date = date;
    }

    public float getStock_total_ratio() {
        return stock_total_ratio;
    }

    public void setStock_total_ratio(float stock_total_ratio) {
        this.stock_total_ratio = stock_total_ratio;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "StockHolderBean{" +
                "stock_total_ratio=" + stock_total_ratio +
                ", date='" + date + '\'' +
                '}';
    }
}
