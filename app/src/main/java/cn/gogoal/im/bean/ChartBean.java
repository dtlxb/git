package cn.gogoal.im.bean;

/**
 * Created by huangxx on 2017/6/13.
 */

public class ChartBean {

    private float barValue;
    private float lineValue;
    private String date;

    public ChartBean(float barValue) {
        this.barValue = barValue;
    }

    public ChartBean(float barValue, String date) {
        this.barValue = barValue;
        this.date = date;
    }

    public ChartBean(float barValue, float lineValue, String date) {
        this.barValue = barValue;
        this.lineValue = lineValue;
        this.date = date;
    }

    public float getBarValue() {
        return barValue;
    }

    public void setBarValue(float barValue) {
        this.barValue = barValue;
    }

    public float getLineValue() {
        return lineValue;
    }

    public void setLineValue(float lineValue) {
        this.lineValue = lineValue;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "ChartBean{" +
                "barValue=" + barValue +
                ", lineValue=" + lineValue +
                ", date='" + date + '\'' +
                '}';
    }
}
