package cn.gogoal.im.bean.stock;

import java.util.ArrayList;

/**
 * Created by daiwei on 2015/10/14.
 */
public class CommentBean {
    /*"code": 0,
            "data": [
            ],
            "message": "成功"*/
    private String code;
    private ArrayList<CommentData> data;
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ArrayList<CommentData> getData() {
        return data;
    }

    public void setData(ArrayList<CommentData> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "CommentBean{" +
                "code='" + code + '\'' +
                ", data=" + data +
                ", message='" + message + '\'' +
                '}';
    }
}
