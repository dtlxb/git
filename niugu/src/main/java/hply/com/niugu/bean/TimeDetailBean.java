package hply.com.niugu.bean;

import java.util.List;

/**
 * Created by wangjd on 2016/6/22 0022.
 */
public class TimeDetailBean {

    public TimeDetailBean() {
    }

    private int code;

    private List<TimeDetailData> data ;

    private String message;

    public void setCode(int code){
        this.code = code;
    }
    public int getCode(){
        return this.code;
    }
    public void setData(List<TimeDetailData> data){
        this.data = data;
    }
    public List<TimeDetailData> getData(){
        return this.data;
    }
    public void setMessage(String message){
        this.message = message;
    }
    public String getMessage(){
        return this.message;
    }

}
