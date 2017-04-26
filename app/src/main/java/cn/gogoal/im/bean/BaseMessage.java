package cn.gogoal.im.bean;

import java.util.Map;

/**
 * @author wangjd on 2016/12/12 0012.
 * @Staff_id 1375
 * @phone 18930640263
 */

public class BaseMessage<T> {
    private String code;
    private Map<String, T> others;
    private Integer type;
    private String msg;

    public BaseMessage() {
    }

    public BaseMessage(String code, Map<String, T> others) {
        this.code = code;
        this.others = others;
    }

    public BaseMessage(String code, Integer type, String msg) {
        this.code = code;
        this.type = type;
        this.msg = msg;
    }


    public BaseMessage(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public BaseMessage(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Map<String, T> getOthers() {
        return others;
    }

    public void setOthers(Map<String, T> others) {
        this.others = others;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
