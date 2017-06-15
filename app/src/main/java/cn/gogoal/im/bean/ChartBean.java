package cn.gogoal.im.bean;

/**
 * Created by huangxx on 2017/6/13.
 */

public class ChartBean {

    private float barValue;
    private float secondBarValue;
    private float thirdBarValue;
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

    public ChartBean(float barValue, float secondBarValue, float thirdBarValue, String date) {
        this.barValue = barValue;
        this.secondBarValue = secondBarValue;
        this.thirdBarValue = thirdBarValue;
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

    public float getSecondBarValue() {
        return secondBarValue;
    }

    public void setSecondBarValue(float secondBarValue) {
        this.secondBarValue = secondBarValue;
    }

    public float getThirdBarValue() {
        return thirdBarValue;
    }

    public void setThirdBarValue(float thirdBarValue) {
        this.thirdBarValue = thirdBarValue;
    }

    @Override
    public String toString() {
        return "ChartBean{" +
                "barValue=" + barValue +
                ", secondBarValue=" + secondBarValue +
                ", thirdBarValue=" + thirdBarValue +
                ", lineValue=" + lineValue +
                ", date='" + date + '\'' +
                '}';
    }
}
