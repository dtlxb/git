package hply.com.niugu.bean;

/**
 * Created by daiwei on 2015/9/17.
 */
public class SearchStockData {
    /*{
        "stock_spell": "WKA",
            "address": "深圳",
            "stock_name": "万科A",
            "stock_code": "000002",
            "is_defined": null
    },*/

    private String stock_spell;
    private String address;
    private String stock_name;
    private String stock_code;
    private String is_defined;

    public String getStock_spell() {
        return stock_spell;
    }

    public void setStock_spell(String stock_spell) {
        this.stock_spell = stock_spell;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStock_name() {
        return stock_name;
    }

    public void setStock_name(String stock_name) {
        this.stock_name = stock_name;
    }

    public String getStock_code() {
        return stock_code;
    }

    public void setStock_code(String stock_code) {
        this.stock_code = stock_code;
    }

    public String getIs_defined() {
        return is_defined;
    }

    public void setIs_defined(String is_defined) {
        this.is_defined = is_defined;
    }

    @Override
    public String toString() {
        return "SearchStockData{" +
                "stock_spell='" + stock_spell + '\'' +
                ", address='" + address + '\'' +
                ", stock_name='" + stock_name + '\'' +
                ", stock_code='" + stock_code + '\'' +
                ", is_defined='" + is_defined + '\'' +
                '}';
    }
}
