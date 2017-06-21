package cn.gogoal.im.bean;

/**
 * Created by dave.
 * Date: 2017/6/19.
 * Desc: description
 */
public class TenTradableHolderData {
    /*{
        "other_tradable_stock":601024.66,
            "stock_holder_property":"保险公司;上市公司",
            "stock_holder_type":"流通A股",
            "stock_holding_quantity":825824.51,
            "stock_holder_name":"中国平安保险(集团)股份有限公司-集团本级-自有资金",
            "tradable_limit_stock":25242.17,
            "date":"2017-03-31",
            "stock_add_reduce":228680.93,
            "stock_holder_ratio":48.81,
            "ten_tradable_stock_holding_quantity":1090774.3,
            "stock_change_ratio":38.3
    }*/

    private String other_tradable_stock;
    private String stock_holder_property;
    private String stock_holder_type;
    private String stock_holding_quantity;
    private String stock_holder_name;
    private String tradable_limit_stock;
    private String date;
    private String stock_add_reduce;
    private String stock_holder_ratio;
    private String ten_tradable_stock_holding_quantity;
    private String stock_change_ratio;

    public TenTradableHolderData(String stock_holder_name, String stock_holding_quantity,
                                 String stock_holder_ratio) {
        this.stock_holding_quantity = stock_holding_quantity;
        this.stock_holder_name = stock_holder_name;
        this.stock_holder_ratio = stock_holder_ratio;
    }

    public String getOther_tradable_stock() {
        return other_tradable_stock;
    }

    public void setOther_tradable_stock(String other_tradable_stock) {
        this.other_tradable_stock = other_tradable_stock;
    }

    public String getStock_holder_property() {
        return stock_holder_property;
    }

    public void setStock_holder_property(String stock_holder_property) {
        this.stock_holder_property = stock_holder_property;
    }

    public String getStock_holder_type() {
        return stock_holder_type;
    }

    public void setStock_holder_type(String stock_holder_type) {
        this.stock_holder_type = stock_holder_type;
    }

    public String getStock_holding_quantity() {
        return stock_holding_quantity;
    }

    public void setStock_holding_quantity(String stock_holding_quantity) {
        this.stock_holding_quantity = stock_holding_quantity;
    }

    public String getStock_holder_name() {
        return stock_holder_name;
    }

    public void setStock_holder_name(String stock_holder_name) {
        this.stock_holder_name = stock_holder_name;
    }

    public String getTradable_limit_stock() {
        return tradable_limit_stock;
    }

    public void setTradable_limit_stock(String tradable_limit_stock) {
        this.tradable_limit_stock = tradable_limit_stock;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStock_add_reduce() {
        return stock_add_reduce;
    }

    public void setStock_add_reduce(String stock_add_reduce) {
        this.stock_add_reduce = stock_add_reduce;
    }

    public String getStock_holder_ratio() {
        return stock_holder_ratio;
    }

    public void setStock_holder_ratio(String stock_holder_ratio) {
        this.stock_holder_ratio = stock_holder_ratio;
    }

    public String getTen_tradable_stock_holding_quantity() {
        return ten_tradable_stock_holding_quantity;
    }

    public void setTen_tradable_stock_holding_quantity(String ten_tradable_stock_holding_quantity) {
        this.ten_tradable_stock_holding_quantity = ten_tradable_stock_holding_quantity;
    }

    public String getStock_change_ratio() {
        return stock_change_ratio;
    }

    public void setStock_change_ratio(String stock_change_ratio) {
        this.stock_change_ratio = stock_change_ratio;
    }
}
