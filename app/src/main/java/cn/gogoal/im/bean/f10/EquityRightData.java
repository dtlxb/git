package cn.gogoal.im.bean.f10;

/**
 * Created by dave.
 * Date: 2017/7/3.
 * Desc: description
 */
public class EquityRightData {
    /*{
        "tradable_A_stock":1691798.97,
            "stock_total":1717041.14,
            "tradable_stock":1691798.97,
            "date":"2017-01-09",
            "change_reason":"其它上市"
    }*/

    private String tradable_A_stock;
    private String stock_total;
    private String tradable_stock;
    private String date;
    private String change_reason;

    public EquityRightData(String stock_total, String tradable_stock, String tradable_A_stock, String change_reason) {
        this.tradable_A_stock = tradable_A_stock;
        this.stock_total = stock_total;
        this.tradable_stock = tradable_stock;
        this.change_reason = change_reason;
    }

    public String getTradable_A_stock() {
        return tradable_A_stock;
    }

    public void setTradable_A_stock(String tradable_A_stock) {
        this.tradable_A_stock = tradable_A_stock;
    }

    public String getStock_total() {
        return stock_total;
    }

    public void setStock_total(String stock_total) {
        this.stock_total = stock_total;
    }

    public String getTradable_stock() {
        return tradable_stock;
    }

    public void setTradable_stock(String tradable_stock) {
        this.tradable_stock = tradable_stock;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getChange_reason() {
        return change_reason;
    }

    public void setChange_reason(String change_reason) {
        this.change_reason = change_reason;
    }
}
