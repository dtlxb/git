package cn.gogoal.im.bean.companytags;

/**
 * Created by huangxx on 2017/6/29.
 */

public class HistoryData {
    private String report_publish;
    private int year;
    private String report_period;
    private int quarter;

    public HistoryData() {
    }

    public HistoryData(String report_publish, int year, String report_period, int quarter) {
        this.report_publish = report_publish;
        this.year = year;
        this.report_period = report_period;
        this.quarter = quarter;
    }

    public String getReport_publish() {
        return report_publish;
    }

    public void setReport_publish(String report_publish) {
        this.report_publish = report_publish;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getReport_period() {
        return report_period;
    }

    public void setReport_period(String report_period) {
        this.report_period = report_period;
    }

    public int getQuarter() {
        return quarter;
    }

    public void setQuarter(int quarter) {
        this.quarter = quarter;
    }

    @Override
    public String toString() {
        return "HistoryData{" +
                "report_publish='" + report_publish + '\'' +
                ", year=" + year +
                ", report_period='" + report_period + '\'' +
                ", quarter=" + quarter +
                '}';
    }
}
