package hply.com.niugu.bean;

/**
 * Created by huangxx on 2015/9/15.
 */
public class StockData {
    private String current_price;
    private String rate;
    private String stock_code;
    private String stock_name;
    private int stock_type;

    public String getCurrent_price() {
        return current_price;
    }

    public void setCurrent_price(String current_price) {
        this.current_price = current_price;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

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

    public int getStock_type() {
        return stock_type;
    }

    public void setStock_type(int stock_type) {
        this.stock_type = stock_type;
    }

    @Override
    public String toString() {
        return "StockData{" +
                "current_price=" + current_price +
                ", rate=" + rate +
                ", stock_code='" + stock_code + '\'' +
                ", stock_name='" + stock_name + '\'' +
                '}';
    }
}
