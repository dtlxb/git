package cn.gogoal.im.bean.stock;

public class OHLCData {

    private float amplitude;//振幅
    private float close_price;//收盘价
    private String date;//日期
    private float high_price;//最高加
    private float low_price;//最低价
    private float open_price;//开盘价
    private float price_change;//涨跌
    private float price_change_rate;//涨跌幅（%）
    private float turnover;//成交额 （万元）
    private float turnover_rate;//换手率
    private float volume;//成交量（股）
    private float avg_price_5;//5日均价
    private float avg_price_10;//10日均价
    private float avg_price_20;
    private float avg_price_30;
    private float avg_price_60;


    public float getAmplitude() {
        return amplitude;
    }

    public void setAmplitude(float amplitude) {
        this.amplitude = amplitude;
    }

    public float getClose_price() {
        return close_price;
    }

    public void setClose_price(float close_price) {
        this.close_price = close_price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public float getHigh_price() {
        return high_price;
    }

    public void setHigh_price(float high_price) {
        this.high_price = high_price;
    }

    public float getLow_price() {
        return low_price;
    }

    public void setLow_price(float low_price) {
        this.low_price = low_price;
    }

    public float getOpen_price() {
        return open_price;
    }

    public void setOpen_price(float open_price) {
        this.open_price = open_price;
    }

    public float getPrice_change() {
        return price_change;
    }

    public void setPrice_change(float price_change) {
        this.price_change = price_change;
    }

    public float getPrice_change_rate() {
        return price_change_rate;
    }

    public void setPrice_change_rate(float price_change_rate) {
        this.price_change_rate = price_change_rate;
    }

    public float getTurnover() {
        return turnover;
    }

    public void setTurnover(float turnover) {
        this.turnover = turnover;
    }

    public float getTurnover_rate() {
        return turnover_rate;
    }

    public void setTurnover_rate(float turnover_rate) {
        this.turnover_rate = turnover_rate;
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public float getAvg_price_5() {
        return avg_price_5;
    }

    public void setAvg_price_5(float avg_price_5) {
        this.avg_price_5 = avg_price_5;
    }

    public float getAvg_price_10() {
        return avg_price_10;
    }

    public void setAvg_price_10(float avg_price_10) {
        this.avg_price_10 = avg_price_10;
    }

    public float getAvg_price_20() {
        return avg_price_20;
    }

    public void setAvg_price_20(float avg_price_20) {
        this.avg_price_20 = avg_price_20;
    }

    public float getAvg_price_30() {
        return avg_price_30;
    }

    public void setAvg_price_30(float avg_price_30) {
        this.avg_price_30 = avg_price_30;
    }

    public float getAvg_price_60() {
        return avg_price_60;
    }

    public void setAvg_price_60(float avg_price_60) {
        this.avg_price_60 = avg_price_60;
    }
}
