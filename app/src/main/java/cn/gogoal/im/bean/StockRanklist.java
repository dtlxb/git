package cn.gogoal.im.bean;

import java.util.List;

/**
 * author wangjd on 2017/3/9 0009.
 * Staff_id 1375
 * phone 18930640263
 */
public class StockRanklist {

    private int gray_counts;//平家数

    private int red_counts;//涨家数

    private int green_counts;//跌家数

    private List<StockList> amplitude_list ;//振幅榜

    private List<StockList> down_list ;//跌幅榜

    private List<StockList> change_list ;//换手率

    private List<StockList> increase_list ;//涨幅榜

    public int getGray_counts() {
        return gray_counts;
    }

    public void setGray_counts(int gray_counts) {
        this.gray_counts = gray_counts;
    }

    public int getRed_counts() {
        return red_counts;
    }

    public void setRed_counts(int red_counts) {
        this.red_counts = red_counts;
    }

    public int getGreen_counts() {
        return green_counts;
    }

    public void setGreen_counts(int green_counts) {
        this.green_counts = green_counts;
    }

    public List<StockList> getAmplitude_list() {
        return amplitude_list;
    }

    public void setAmplitude_list(List<StockList> amplitude_list) {
        this.amplitude_list = amplitude_list;
    }

    public List<StockList> getDown_list() {
        return down_list;
    }

    public void setDown_list(List<StockList> down_list) {
        this.down_list = down_list;
    }

    public List<StockList> getChange_list() {
        return change_list;
    }

    public void setChange_list(List<StockList> change_list) {
        this.change_list = change_list;
    }

    public List<StockList> getIncrease_list() {
        return increase_list;
    }

    public void setIncrease_list(List<StockList> increase_list) {
        this.increase_list = increase_list;
    }
}
