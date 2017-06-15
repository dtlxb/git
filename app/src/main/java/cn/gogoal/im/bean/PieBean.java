package cn.gogoal.im.bean;

/**
 * Created by huangxx on 2017/6/14.
 */

public class PieBean {

    private String title;
    private float pieValue;
    private int colorValue;

    public PieBean(String title, float pieValue, int colorValue) {
        this.title = title;
        this.pieValue = pieValue;
        this.colorValue = colorValue;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getPieValue() {
        return pieValue;
    }

    public void setPieValue(float pieValue) {
        this.pieValue = pieValue;
    }

    public int getColorValue() {
        return colorValue;
    }

    public void setColorValue(int colorValue) {
        this.colorValue = colorValue;
    }

    @Override
    public String toString() {
        return "PieBean{" +
                "title='" + title + '\'' +
                ", pieValue=" + pieValue +
                ", colorValue=" + colorValue +
                '}';
    }
}
