package cn.gogoal.im.bean.companytags;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * Created by huangxx on 2017/6/30.
 */

public class PieData implements Comparable<PieData> {

    private int report_year;
    private List<RevenueData> data;

    public PieData() {
    }

    public PieData(int report_year, List<RevenueData> data) {
        this.report_year = report_year;
        this.data = data;
    }

    public int getReport_year() {
        return report_year;
    }

    public void setReport_year(int report_year) {
        this.report_year = report_year;
    }

    public List<RevenueData> getData() {
        return data;
    }


    public void setData(List<RevenueData> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "PieData{" +
                "report_year=" + report_year +
                ", data=" + data +
                '}';
    }

    @Override
    public int compareTo(@NonNull PieData o) {
        if (this.report_year < o.report_year)
            return 1;
        if (this.report_year > o.report_year)
            return -1;
        else
            return 0;
    }
}


