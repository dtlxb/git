package cn.gogoal.im.bean.stock;

import java.io.Serializable;

/**
 * author wangjd on 2017/4/10 0010.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public class MyStockData implements Serializable,Cloneable {
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
    private String turnover;
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
    private String low_price;
    private String turnover_rate;
    private String volume;
    private String full_code;
    private StockTag tag; //好公司 标志

    private int group_id;
    private String date;
    private int remind_headlines;
    private int remind_notice;
    private int stock_class;
    private String stock_code;
    private int stock_sort;
    private String stock_name;
    private String change_value;
    private double price;
    private String change_rate;
    private int stock_type;

    //添加选择的字段
    private boolean check = false;

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

    public String getTurnover() {
        return turnover;
    }

    public void setTurnover(String turnover) {
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

    public String getLow_price() {
        return low_price;
    }

    public void setLow_price(String low_price) {
        this.low_price = low_price;
    }

    public String getTurnover_rate() {
        return turnover_rate;
    }

    public void setTurnover_rate(String turnover_rate) {
        this.turnover_rate = turnover_rate;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getFull_code() {
        return full_code;
    }

    public void setFull_code(String full_code) {
        this.full_code = full_code;
    }

    public StockTag getTag() {
        return tag;
    }

    public void setTag(StockTag tag) {
        this.tag = tag;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
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

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    @Override
    public MyStockData clone() {
        try {
            return (MyStockData) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }
}