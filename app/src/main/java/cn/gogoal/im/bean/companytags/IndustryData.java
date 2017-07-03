package cn.gogoal.im.bean.companytags;

import cn.gogoal.im.bean.BaseBeanList;

/**
 * Created by huangxx on 2017/6/29.
 */

public class IndustryData extends BaseBeanList{

    private double EUR_Mn;
    private String industry_involved;
    private String is_head;
    private String stock_code;
    private String stock_name;

    public IndustryData() {
    }

    public IndustryData(double EUR_Mn, String industry_involved, String is_head, String stock_code, String stock_name) {
        this.EUR_Mn = EUR_Mn;
        this.industry_involved = industry_involved;
        this.is_head = is_head;
        this.stock_code = stock_code;
        this.stock_name = stock_name;
    }

    public double getEUR_Mn() {
        return EUR_Mn;
    }

    public void setEUR_Mn(double EUR_Mn) {
        this.EUR_Mn = EUR_Mn;
    }

    public String getIndustry_involved() {
        return industry_involved;
    }

    public void setIndustry_involved(String industry_involved) {
        this.industry_involved = industry_involved;
    }

    public String getIs_head() {
        return is_head;
    }

    public void setIs_head(String is_head) {
        this.is_head = is_head;
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

    @Override
    public String toString() {
        return "IndustryData{" +
                "EUR_Mn=" + EUR_Mn +
                ", industry_involved='" + industry_involved + '\'' +
                ", is_head='" + is_head + '\'' +
                ", stock_code='" + stock_code + '\'' +
                ", stock_name='" + stock_name + '\'' +
                '}';
    }
}
