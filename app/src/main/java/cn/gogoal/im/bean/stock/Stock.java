package cn.gogoal.im.bean.stock;

import android.os.Parcel;
import android.os.Parcelable;

import cn.gogoal.im.common.StringUtils;

/**
 * author wangjd on 2017/4/7 0007.
 * Staff_id 1375
 * phone 18930640263
 * description :传值时需要的股票信息
 */
public class Stock implements Parcelable {

    private String current_price;
    private String stock_code;
    private String stock_name;
    private int stock_type;

    private String changeValue;
    private double closePrice;
    private String stock_rate;

    //双参
    public Stock(String stock_code, String stock_name) {
        this.stock_code = stock_code;
        this.stock_name = stock_name;
    }


    public String getCurrent_price() {
        return current_price;
    }

    public void setCurrent_price(String current_price) {
        this.current_price = current_price;
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

    public String getChangeValue() {
        return changeValue;
    }

    public void setChangeValue(String changeValue) {
        this.changeValue = changeValue;
    }

    public double getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(double closePrice) {
        this.closePrice = closePrice;
    }

    public String getStock_rate() {
        return StringUtils.save2Significand(stock_rate);
    }

    public void setStock_rate(String stock_rate) {
        this.stock_rate = stock_rate;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.current_price);
        dest.writeString(this.stock_code);
        dest.writeString(this.stock_name);
        dest.writeInt(this.stock_type);
        dest.writeString(this.changeValue);
        dest.writeDouble(this.closePrice);
        dest.writeString(this.stock_rate);
    }

    protected Stock(Parcel in) {
        this.current_price = in.readString();
        this.stock_code = in.readString();
        this.stock_name = in.readString();
        this.stock_type = in.readInt();
        this.changeValue = in.readString();
        this.closePrice = in.readDouble();
        this.stock_rate = in.readString();
    }

    public static final Creator<Stock> CREATOR = new Creator<Stock>() {
        @Override
        public Stock createFromParcel(Parcel source) {
            return new Stock(source);
        }

        @Override
        public Stock[] newArray(int size) {
            return new Stock[size];
        }
    };
}
