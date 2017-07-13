package cn.gogoal.im.bean.stock;

import java.util.List;

/**
 * Created by wangjd on 2016/6/22 0022.
 */
public class TimeDetialBean {

    public TimeDetialBean() {
    }

    private int code;

    private List<TimeDetialData> data ;

    private String message;

    public void setCode(int code){
        this.code = code;
    }
    public int getCode(){
        return this.code;
    }
    public void setData(List<TimeDetialData> data){
        this.data = data;
    }
    public List<TimeDetialData> getData(){
        return this.data;
    }
    public void setMessage(String message){
        this.message = message;
    }
    public String getMessage(){
        return this.message;
    }

}
