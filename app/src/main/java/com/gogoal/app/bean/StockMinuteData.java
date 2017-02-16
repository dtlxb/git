package com.gogoal.app.bean;

/**
 * Created by huangxx on 2017/2/15.
 */
public class StockMinuteData {
    private float avg_price;
    private float price;
    private float price_change_rate;
    private long volume;
    private String date;

    public float getAvg_price() {
        return avg_price;
    }

    public void setAvg_price(float avg_price) {
        this.avg_price = avg_price;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getPrice_change_rate() {
        return price_change_rate;
    }

    public void setPrice_change_rate(float price_change_rate) {
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
