package hply.com.niugu.bean;

/**
 * Created by daiwei on 2015/9/30.
 */
public class StockDetailNewsStock {
    /*"stock": [
    {
        "stock_code": "002285",
            "stock_name": "世联行"
            "stock_price":35.98,
         "stock_rate":-0.8542298153761367
         "stock_type":1
    }
    ],*/
    private String stock_code;
    private String stock_name;
    private String stock_price;
    private String stock_rate;
    private String stock_type;

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

    public String getStock_price() {
        return stock_price;
    }

    public void setStock_price(String stock_price) {
        this.stock_price = stock_price;
    }

    public String getStock_rate() {
        return stock_rate;
    }

    public void setStock_rate(String stock_rate) {
        this.stock_rate = stock_rate;
    }

    public String getStock_type() {
        return stock_type;
    }

    public void setStock_type(String stock_type) {
        this.stock_type = stock_type;
    }

    @Override
    public String toString() {
        return "StockDetailNewsStock{" +
                "stock_code='" + stock_code + '\'' +
                ", stock_name='" + stock_name + '\'' +
                ", stock_price='" + stock_price + '\'' +
                ", stock_rate='" + stock_rate + '\'' +
                ", stock_type='" + stock_type + '\'' +
                '}';
    }
}
