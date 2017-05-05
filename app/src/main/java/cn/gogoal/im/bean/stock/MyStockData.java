package cn.gogoal.im.bean.stock;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * author wangjd on 2017/4/10 0010.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public class MyStockData implements Parcelable {
    /**
     * group_id : 137269
     * date : 2016-10-13 17:50:46
     * remind_headlines : 1
     * remind_notice : 1
     * stock_class : 0
     * stock_code : 002101
     * stock_sort : 8
     * stock_name : 广东鸿图
     * change_value : 0.09999999999999787
     * price : 23.15
     * change_rate : 0.43383947939261547
     * stock_type : 1
     */
    private String eps_y2;
    private int symbol_type;
    private String open_price;
    private int id;
    private String pe_y2;
    private String pe_y1;
    private int turnover;
    private String pe_y0;
    private int is_keypoint;
    private double close_price;
    private String eps_y0;
    private String industry_name;
    private String mcap;
    private String eps_y1;
    private String capitalization;
    private String high_price;
    private String insertdate;
    private String source;
    private int low_price;
    private String turnover_rate;
    private long volume;
    private String full_code;
//    private Tag tag; 好公司 标志

    //添加选择的字段
    private boolean check=false;

    private int group_id;
    private String date;
    private int remind_headlines;
    private int remind_notice;
    private int stock_class;
    private String stock_code;
    private int stock_sort;
    private String stock_name;
    private String change_value;
    private String price;
    private String change_rate;
    private int stock_type;

    public String getEps_y2() {
        return eps_y2;
    }

    public void setEps_y2(String eps_y2) {
        this.eps_y2 = eps_y2;
    }

    public int getSymbol_type() {
        return symbol_type;
    }

    public void setSymbol_type(int symbol_type) {
        this.symbol_type = symbol_type;
    }

    public String getOpen_price() {
        return open_price;
    }

    public void setOpen_price(String open_price) {
        this.open_price = open_price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPe_y2() {
        return pe_y2;
    }

    public void setPe_y2(String pe_y2) {
        this.pe_y2 = pe_y2;
    }

    public String getPe_y1() {
        return pe_y1;
    }

    public void setPe_y1(String pe_y1) {
        this.pe_y1 = pe_y1;
    }

    public int getTurnover() {
        return turnover;
    }

    public void setTurnover(int turnover) {
        this.turnover = turnover;
    }

    public String getPe_y0() {
        return pe_y0;
    }

    public void setPe_y0(String pe_y0) {
        this.pe_y0 = pe_y0;
    }

    public int getIs_keypoint() {
        return is_keypoint;
    }

    public void setIs_keypoint(int is_keypoint) {
        this.is_keypoint = is_keypoint;
    }

    public double getClose_price() {
        return close_price;
    }

    public void setClose_price(double close_price) {
        this.close_price = close_price;
    }

    public String getEps_y0() {
        return eps_y0;
    }

    public void setEps_y0(String eps_y0) {
        this.eps_y0 = eps_y0;
    }

    public String getIndustry_name() {
        return industry_name;
    }

    public void setIndustry_name(String industry_name) {
        this.industry_name = industry_name;
    }

    public String getMcap() {
        return mcap;
    }

    public void setMcap(String mcap) {
        this.mcap = mcap;
    }

    public String getEps_y1() {
        return eps_y1;
    }

    public void setEps_y1(String eps_y1) {
        this.eps_y1 = eps_y1;
    }

    public String getCapitalization() {
        return capitalization;
    }

    public void setCapitalization(String capitalization) {
        this.capitalization = capitalization;
    }

    public String getHigh_price() {
        return high_price;
    }

    public void setHigh_price(String high_price) {
        this.high_price = high_price;
    }

    public String getInsertdate() {
        return insertdate;
    }

    public void setInsertdate(String insertdate) {
        this.insertdate = insertdate;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getLow_price() {
        return low_price;
    }

    public void setLow_price(int low_price) {
        this.low_price = low_price;
    }

    public String getTurnover_rate() {
        return turnover_rate;
    }

    public void setTurnover_rate(String turnover_rate) {
        this.turnover_rate = turnover_rate;
    }

    public long getVolume() {
        return volume;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }

    public String getFull_code() {
        return full_code;
    }

    public void setFull_code(String full_code) {
        this.full_code = full_code;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getRemind_headlines() {
        return remind_headlines;
    }

    public void setRemind_headlines(int remind_headlines) {
        this.remind_headlines = remind_headlines;
    }

    public int getRemind_notice() {
        return remind_notice;
    }

    public void setRemind_notice(int remind_notice) {
        this.remind_notice = remind_notice;
    }

    public int getStock_class() {
        return stock_class;
    }

    public void setStock_class(int stock_class) {
        this.stock_class = stock_class;
    }

    public String getStock_code() {
        return stock_code;
    }

    public void setStock_code(String stock_code) {
        this.stock_code = stock_code;
    }

    public int getStock_sort() {
        return stock_sort;
    }

    public void setStock_sort(int stock_sort) {
        this.stock_sort = stock_sort;
    }

    public String getStock_name() {
        return stock_name;
    }

    public void setStock_name(String stock_name) {
        this.stock_name = stock_name;
    }

    public String getChange_value() {
        return change_value;
    }

    public void setChange_value(String change_value) {
        this.change_value = change_value;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getChange_rate() {
        return change_rate;
    }

    public void setChange_rate(String change_rate) {
        this.change_rate = change_rate;
    }

    public int getStock_type() {
        return stock_type;
    }

    public void setStock_type(int stock_type) {
        this.stock_type = stock_type;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof MyStockData) && ((MyStockData) obj).getStock_code().equalsIgnoreCase(this.getStock_code());
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.eps_y2);
        dest.writeInt(this.symbol_type);
        dest.writeString(this.open_price);
        dest.writeInt(this.id);
        dest.writeString(this.pe_y2);
        dest.writeString(this.pe_y1);
        dest.writeInt(this.turnover);
        dest.writeString(this.pe_y0);
        dest.writeInt(this.is_keypoint);
        dest.writeDouble(this.close_price);
        dest.writeString(this.eps_y0);
        dest.writeString(this.industry_name);
        dest.writeString(this.mcap);
        dest.writeString(this.eps_y1);
        dest.writeString(this.capitalization);
        dest.writeString(this.high_price);
        dest.writeString(this.insertdate);
        dest.writeString(this.source);
        dest.writeInt(this.low_price);
        dest.writeString(this.turnover_rate);
        dest.writeLong(this.volume);
        dest.writeString(this.full_code);
        dest.writeByte(this.check ? (byte) 1 : (byte) 0);
        dest.writeInt(this.group_id);
        dest.writeString(this.date);
        dest.writeInt(this.remind_headlines);
        dest.writeInt(this.remind_notice);
        dest.writeInt(this.stock_class);
        dest.writeString(this.stock_code);
        dest.writeInt(this.stock_sort);
        dest.writeString(this.stock_name);
        dest.writeString(this.change_value);
        dest.writeString(this.price);
        dest.writeString(this.change_rate);
        dest.writeInt(this.stock_type);
    }

    public MyStockData() {
    }

    protected MyStockData(Parcel in) {
        this.eps_y2 = in.readString();
        this.symbol_type = in.readInt();
        this.open_price = in.readString();
        this.id = in.readInt();
        this.pe_y2 = in.readString();
        this.pe_y1 = in.readString();
        this.turnover = in.readInt();
        this.pe_y0 = in.readString();
        this.is_keypoint = in.readInt();
        this.close_price = in.readDouble();
        this.eps_y0 = in.readString();
        this.industry_name = in.readString();
        this.mcap = in.readString();
        this.eps_y1 = in.readString();
        this.capitalization = in.readString();
        this.high_price = in.readString();
        this.insertdate = in.readString();
        this.source = in.readString();
        this.low_price = in.readInt();
        this.turnover_rate = in.readString();
        this.volume = in.readLong();
        this.full_code = in.readString();
        this.check = in.readByte() != 0;
        this.group_id = in.readInt();
        this.date = in.readString();
        this.remind_headlines = in.readInt();
        this.remind_notice = in.readInt();
        this.stock_class = in.readInt();
        this.stock_code = in.readString();
        this.stock_sort = in.readInt();
        this.stock_name = in.readString();
        this.change_value = in.readString();
        this.price = in.readString();
        this.change_rate = in.readString();
        this.stock_type = in.readInt();
    }

    public static final Creator<MyStockData> CREATOR = new Creator<MyStockData>() {
        @Override
        public MyStockData createFromParcel(Parcel source) {
            return new MyStockData(source);
        }

        @Override
        public MyStockData[] newArray(int size) {
            return new MyStockData[size];
        }
    };
}
