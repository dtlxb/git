package cn.gogoal.im.bean.companytags;

import cn.gogoal.im.bean.BaseBeanList;

/**
 * Created by huangxx on 2017/7/3.
 */

public class CompanyTagData extends BaseBeanList {

    private Integer Q1;
    private Integer Q2;
    private Integer Q3;
    private Integer Q4;
    private int year;

    public CompanyTagData() {
    }

    public CompanyTagData(int q1, int q2, int q3, int q4, int year) {
        Q1 = q1;
        Q2 = q2;
        Q3 = q3;
        Q4 = q4;
        this.year = year;
    }

    public Integer getQ1() {
        return Q1;
    }

    public void setQ1(Integer q1) {
        Q1 = q1 == null ? -2 : q1;
    }

    public Integer getQ2() {
        return Q2;
    }

    public void setQ2(Integer q2) {
        Q2 = q2 == null ? -2 : q2;
    }

    public Integer getQ3() {
        return Q3;
    }

    public void setQ3(Integer q3) {
        Q3 = q3 == null ? -2 : q3;
    }

    public Integer getQ4() {
        return Q4;
    }

    public void setQ4(Integer q4) {
        Q4 = q4 == null ? -2 : q4;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public String toString() {
        return "CompanyTagData{" +
                "Q1=" + Q1 +
                ", Q2=" + Q2 +
                ", Q3=" + Q3 +
                ", Q4=" + Q4 +
                ", year=" + year +
                '}';
    }
}
