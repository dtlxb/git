package hply.com.niugu.bean;

/**
 * Created by daiwei on 2016/2/26.
 */
public class CommentStock {
    /*{
        "stock_name":"好想你",
            "stock_code":"002582"
    },*/

    private String stock_name;
    private String stock_code;

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
        return "CommentStock{" +
                "stock_name='" + stock_name + '\'' +
                ", stock_code='" + stock_code + '\'' +
                '}';
    }
}
