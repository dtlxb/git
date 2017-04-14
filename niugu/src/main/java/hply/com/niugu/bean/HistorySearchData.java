package hply.com.niugu.bean;

/**
 * Created by daiwei on 2016/1/4.
 */
public class HistorySearchData {
    private String stock_name;
    private String stock_code;

    public HistorySearchData(String stock_name, String stock_code) {
        this.stock_name=stock_name;
        this.stock_code=stock_code;
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

    @Override
    public String toString() {
        return "HistorySearchData{" +
                "stock_name='" + stock_name + '\'' +
                ", stock_code='" + stock_code + '\'' +
                '}';
    }
}
