package cn.gogoal.im.bean.stock;

import java.util.List;

/**
 * Created by Lizn on 2015/10/12.
 */
public class OHLCBean {
    private int code;
    private List<OHLCData> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<OHLCData> getData() {
        return data;
    }

    public void setData(List<OHLCData> data) {
        this.data = data;
    }
}
