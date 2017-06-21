package cn.gogoal.im.bean.stock;

/**
 * Created by dave.
 * Date: 2017/6/15.
 * Desc: description
 */
public class HolderResearchData {

    /*{
        "stock_holder_type":"流通A股,流通受限股份",
            "stock_holding_quantity":851049.31,
            "stock_holder_name":"中国平安保险(集团)股份有限公司-集团本级-自有资金",
            "date":"2017-03-31",
            "stock_add_reduce":0,
            "stock_holder_ratio":49.56,
            "stock_change_ratio":0
    },*/

    private String stock_holder_type;
    private String stock_holding_quantity;
    private String stock_holder_name;
    private String date;
    private String stock_add_reduce;
    private String stock_holder_ratio;
    private String stock_change_ratio;

    public HolderResearchData(String stock_holding_quantity, String stock_holder_ratio) {
        this.stock_holding_quantity = stock_holding_quantity;
        this.stock_holder_ratio = stock_holder_ratio;
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

    public String getStock_change_ratio() {
        return stock_change_ratio;
    }

    public void setStock_change_ratio(String stock_change_ratio) {
        this.stock_change_ratio = stock_change_ratio;
    }
}
