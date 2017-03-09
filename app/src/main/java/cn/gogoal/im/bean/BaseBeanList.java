package cn.gogoal.im.bean;

import java.util.List;

/**
 * author wangjd on 2017/3/8 0008.
 * Staff_id 1375
 * phone 18930640263
 */
public class BaseBeanList<T> {

    private String message;

    private List<T> data ;

    private int code;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
