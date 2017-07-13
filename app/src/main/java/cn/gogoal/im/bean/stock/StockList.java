package cn.gogoal.im.bean.stock;

import java.util.List;

/**
 * Created by huangxx on 2015/9/16.
 */
public class StockList {
    private List<AmplitudeData> amplitude_list;
    private List<ChangeData> change_list;
    private List<DownData> down_list;
    private List<IncreaseData> increase_list;
    private String red_counts;
    private String gray_counts;
    private String green_counts;

    public List<AmplitudeData> getAmplitude_list() {
        return amplitude_list;
    }

    public void setAmplitude_list(List<AmplitudeData> amplitude_list) {
        this.amplitude_list = amplitude_list;
    }

    public List<ChangeData> getChange_list() {
        return change_list;
    }

    public void setChange_list(List<ChangeData> change_list) {
        this.change_list = change_list;
    }

    public List<DownData> getDown_list() {
        return down_list;
    }

    public void setDown_list(List<DownData> down_list) {
        this.down_list = down_list;
    }

    public List<IncreaseData> getIncrease_list() {
        return increase_list;
    }

    public void setIncrease_list(List<IncreaseData> increase_list) {
        this.increase_list = increase_list;
    }

    public String getRed_counts() {
        return red_counts;
    }

    public void setRed_counts(String red_counts) {
        this.red_counts = red_counts;
    }

    public String getGray_counts() {
        return gray_counts;
    }

    public void setGray_counts(String gray_counts) {
        this.gray_counts = gray_counts;
    }

    public String getGreen_counts() {
        return green_counts;
    }

    public void setGreen_counts(String green_counts) {
        this.green_counts = green_counts;
    }

    @Override
    public String toString() {
        return "StockList{" +
                "amplitude_list=" + amplitude_list +
                ", change_list=" + change_list +
                ", down_list=" + down_list +
                ", increase_list=" + increase_list +
                ", red_counts='" + red_counts + '\'' +
                ", gray_counts='" + gray_counts + '\'' +
                ", green_counts='" + green_counts + '\'' +
                '}';
    }
}
