package cn.gogoal.im.bean.f10;

/**
 * Created by dave.
 * Date: 2017/6/19.
 * Desc: description
 */

public class FundHolderData {
    /*{
        "fund_name":"鹏华普天收益证券投资基金",
            "stock_total_ratio":0,
            "fund_code":"160603",
            "stock_tradable_ratio":0,
            "store_holding_value":0.04,
            "stock_holding_quantity":0,
            "stock_net_ratio":0,
            "date":"2016-12-31"
    }*/

    private String fund_name;
    private String stock_total_ratio;
    private String fund_code;
    private String stock_tradable_ratio;
    private String store_holding_value;
    private String stock_holding_quantity;
    private String stock_net_ratio;
    private String date;

    public FundHolderData(String fund_name, String fund_code, String stock_holding_quantity) {
        this.fund_name = fund_name;
        this.fund_code = fund_code;
        this.stock_holding_quantity = stock_holding_quantity;
    }

    public String getFund_name() {
        return fund_name;
    }

    public void setFund_name(String fund_name) {
        this.fund_name = fund_name;
    }

    public String getStock_total_ratio() {
        return stock_total_ratio;
    }

    public void setStock_total_ratio(String stock_total_ratio) {
        this.stock_total_ratio = stock_total_ratio;
    }

    public String getFund_code() {
        return fund_code;
    }

    public void setFund_code(String fund_code) {
        this.fund_code = fund_code;
    }

    public String getStock_tradable_ratio() {
        return stock_tradable_ratio;
    }

    public void setStock_tradable_ratio(String stock_tradable_ratio) {
        this.stock_tradable_ratio = stock_tradable_ratio;
    }

    public String getStore_holding_value() {
        return store_holding_value;
    }

    public void setStore_holding_value(String store_holding_value) {
        this.store_holding_value = store_holding_value;
    }

    public String getStock_holding_quantity() {
        return stock_holding_quantity;
    }

    public void setStock_holding_quantity(String stock_holding_quantity) {
        this.stock_holding_quantity = stock_holding_quantity;
    }

    public String getStock_net_ratio() {
        return stock_net_ratio;
    }

    public void setStock_net_ratio(String stock_net_ratio) {
        this.stock_net_ratio = stock_net_ratio;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
