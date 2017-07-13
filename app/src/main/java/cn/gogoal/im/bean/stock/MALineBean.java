package cn.gogoal.im.bean.stock;

import java.util.List;

public class MALineBean {

    /**
     * 线表示数据
     */
    private List<Float> lineData;

    /**
     * 线的标题
     */
    private String title;

    /**
     * 线表示颜色
     */
    private int lineColor;

    public MALineBean() {
        super();
    }

    public MALineBean(List<Float> lineData, String title, int lineColor) {
        this.lineData = lineData;
        this.title = title;
        this.lineColor = lineColor;
    }

    public List<Float> getLineData() {
        return lineData;
    }

    public void setLineData(List<Float> lineData) {
        this.lineData = lineData;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getLineColor() {
        return lineColor;
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
    }

    public float getMaxValue(int startIndex, int showNum) {
        float maxValue = 0;
        for (int i = startIndex; i <= startIndex + showNum - 1; i++) {
            float f = lineData.get(i);
            maxValue = maxValue < f ? f : maxValue;
        }
        return maxValue;
    }
}
