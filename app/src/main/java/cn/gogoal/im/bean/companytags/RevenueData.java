package cn.gogoal.im.bean.companytags;

import cn.gogoal.im.bean.BaseBeanList;

/**
 * Created by huangxx on 2017/6/30.
 */

public class RevenueData extends BaseBeanList {

    private String session_name;
    private int report_year;
    private double incomezb;
    private String stock_code;

    public RevenueData() {
    }

    public String getSession_name() {
        return session_name;
    }

    public void setSession_name(String session_name) {
        this.session_name = session_name;
    }

    public int getReport_year() {
        return report_year;
    }

    public void setReport_year(int report_year) {
        this.report_year = report_year;
    }

    public double getIncomezb() {
        return incomezb;
    }

    public void setIncomezb(double incomezb) {
        this.incomezb = incomezb;
    }

    public String getStock_code() {
        return stock_code;
    }

    public void setStock_code(String stock_code) {
        this.stock_code = stock_code;
    }

    @Override
    public String toString() {
        return "RevenueData{" +
                "session_name='" + session_name + '\'' +
                ", report_year=" + report_year +
                ", incomezb=" + incomezb +
                ", stock_code='" + stock_code + '\'' +
                '}';
    }
}
