package hply.com.niugu.stock;

/**
 * Created by huangxx on 2017/2/15.
 */
public class StockMinuteData {
    private String avg_price;
    private String price;
    private String price_change_rate;
    private long volume;
    private String date;

    public String getAvg_price() {
        return avg_price;
    }

    public void setAvg_price(String avg_price) {
        this.avg_price = avg_price;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPrice_change_rate() {
        return price_change_rate;
    }

    public void setPrice_change_rate(String price_change_rate) {
        this.price_change_rate = price_change_rate;
    }

    public long getVolume() {
        return volume;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "StockMinuteData{" +
                "avg_price=" + avg_price +
                ", price=" + price +
                ", price_change_rate=" + price_change_rate +
                ", volume=" + volume +
                ", date='" + date + '\'' +
                '}';
    }
}
