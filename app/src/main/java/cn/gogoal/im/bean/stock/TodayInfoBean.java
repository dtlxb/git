package cn.gogoal.im.bean.stock;

/**
 * Created by huangxx on 2017/6/21.
 */

public class TodayInfoBean {
    private String fullcode;
    private String tdate;
    private double flow_into_large_fund;
    private double flow_out_large_fund;
    private double flow_into_middle_fund;
    private double flow_out_middle_fund;
    private double flow_into_small_fund;
    private double flow_out_small_fund;


    public String getFullcode() {
        return fullcode;
    }

    public void setFullcode(String fullcode) {
        this.fullcode = fullcode;
    }

    public String getTdate() {
        return tdate;
    }

    public void setTdate(String tdate) {
        this.tdate = tdate;
    }

    public double getFlow_into_large_fund() {
        return flow_into_large_fund;
    }

    public void setFlow_into_large_fund(double flow_into_large_fund) {
        this.flow_into_large_fund = flow_into_large_fund;
    }

    public double getFlow_out_large_fund() {
        return flow_out_large_fund;
    }

    public void setFlow_out_large_fund(double flow_out_large_fund) {
        this.flow_out_large_fund = flow_out_large_fund;
    }

    public double getFlow_into_middle_fund() {
        return flow_into_middle_fund;
    }

    public void setFlow_into_middle_fund(double flow_into_middle_fund) {
        this.flow_into_middle_fund = flow_into_middle_fund;
    }

    public double getFlow_out_middle_fund() {
        return flow_out_middle_fund;
    }

    public void setFlow_out_middle_fund(double flow_out_middle_fund) {
        this.flow_out_middle_fund = flow_out_middle_fund;
    }

    public double getFlow_into_small_fund() {
        return flow_into_small_fund;
    }

    public void setFlow_into_small_fund(double flow_into_small_fund) {
        this.flow_into_small_fund = flow_into_small_fund;
    }

    public double getFlow_out_small_fund() {
        return flow_out_small_fund;
    }

    public void setFlow_out_small_fund(double flow_out_small_fund) {
        this.flow_out_small_fund = flow_out_small_fund;
    }

    @Override
    public String toString() {
        return "TodayInfoBean{" +
                "fullcode='" + fullcode + '\'' +
                ", tdate='" + tdate + '\'' +
                ", flow_into_large_fund=" + flow_into_large_fund +
                ", flow_out_large_fund=" + flow_out_large_fund +
                ", flow_into_middle_fund=" + flow_into_middle_fund +
                ", flow_out_middle_fund=" + flow_out_middle_fund +
                ", flow_into_small_fund=" + flow_into_small_fund +
                ", flow_out_small_fund=" + flow_out_small_fund +
                '}';
    }
}
