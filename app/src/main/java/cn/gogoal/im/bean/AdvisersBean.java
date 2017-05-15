package cn.gogoal.im.bean;

import java.util.List;

/**
 * author wangjd on 2017/5/12 0012.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public class AdvisersBean {

    /**
     * message : 成功
     * data : [{"ass_saler_mobile":"","ass_saler_photo":"http://info.china-yjy.com/Upload/Photo/newnophoto.gif","ass_saler_user_id":0,"saler_mobile":"4001818595","saler_photo":"http://info.china-yjy.com/Upload/Photo/ph2011322100116.JPG","saler_user_id":34125,"saler_name":"陈乃姚","ass_saler_name":""}]
     * code : 0
     */

    private String message;
    private int code;
    private List<Advisers> data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<Advisers> getData() {
        return data;
    }

    public void setData(List<Advisers> data) {
        this.data = data;
    }
}
