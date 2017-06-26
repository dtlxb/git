package cn.gogoal.im.bean.stock;

import android.support.annotation.ColorInt;

/**
 * author wangjd on 2017/4/11 0011.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public class StockDetail2Text {
    private String key;
    private String value;
    private @ColorInt
    int  statusColor;

    public StockDetail2Text(String key, String value, int statusColor) {
        this.key = key;
        this.value = value;
        this.statusColor = statusColor;
    }

    public int getStatusColor() {
        return statusColor;
    }

    public void setStatusColor(int statusColor) {
        this.statusColor = statusColor;
    }

    public StockDetail2Text(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
