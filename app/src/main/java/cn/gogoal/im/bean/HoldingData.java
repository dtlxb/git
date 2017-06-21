package cn.gogoal.im.bean;

/**
 * Created by dave.
 * Date: 2017/6/19.
 * Desc: description
 */
public class HoldingData {
    /*{
        "change_stock":-7680,
            "name":"白国华",
            "stock_change_approach":"竞价交易",
            "senior_relation":"配偶",
            "date":"2014-11-28",
            "senior":"高管",
            "trading":12.25,
            "average":null
    },*/

    private String change_stock;
    private String name;
    private String senior_relationstock_change_approach;
    private String senior_relation;
    private String date;
    private String senior;
    private String trading;
    private String average;

    public HoldingData(String date, String name, String change_stock) {
        this.change_stock = change_stock;
        this.name = name;
        this.date = date;
    }

    public String getChange_stock() {
        return change_stock;
    }

    public void setChange_stock(String change_stock) {
        this.change_stock = change_stock;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSenior_relationstock_change_approach() {
        return senior_relationstock_change_approach;
    }

    public void setSenior_relationstock_change_approach(String senior_relationstock_change_approach) {
        this.senior_relationstock_change_approach = senior_relationstock_change_approach;
    }

    public String getSenior_relation() {
        return senior_relation;
    }

    public void setSenior_relation(String senior_relation) {
        this.senior_relation = senior_relation;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSenior() {
        return senior;
    }

    public void setSenior(String senior) {
        this.senior = senior;
    }

    public String getTrading() {
        return trading;
    }

    public void setTrading(String trading) {
        this.trading = trading;
    }

    public String getAverage() {
        return average;
    }

    public void setAverage(String average) {
        this.average = average;
    }
}
