package cn.gogoal.im.bean.stock;

import java.util.List;

/**
 * Created by wangjd on 2016/6/21.
 */
public class ConsensusBean {
    private String message;

    private List<ConsensusData> data ;

    private int code;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ConsensusData> getData() {
        return data;
    }

    public void setData(List<ConsensusData> data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
