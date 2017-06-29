package cn.gogoal.im.bean.companytags;

/**
 * Created by huangxx on 2017/6/29.
 */

public class IncidentData {

    private int id;
    private String event_type;
    private String event_date;
    private String stock_code;
    private String event_title;
    private String stock_name;

    public IncidentData() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEvent_type() {
        return event_type;
    }

    public void setEvent_type(String event_type) {
        this.event_type = event_type;
    }

    public String getEvent_date() {
        return event_date;
    }

    public void setEvent_date(String event_date) {
        this.event_date = event_date;
    }

    public String getStock_code() {
        return stock_code;
    }

    public void setStock_code(String stock_code) {
        this.stock_code = stock_code;
    }

    public String getEvent_title() {
        return event_title;
    }

    public void setEvent_title(String event_title) {
        this.event_title = event_title;
    }

    public String getStock_name() {
        return stock_name;
    }

    public void setStock_name(String stock_name) {
        this.stock_name = stock_name;
    }

    @Override
    public String toString() {
        return "IncidentData{" +
                "id=" + id +
                ", event_type='" + event_type + '\'' +
                ", event_date='" + event_date + '\'' +
                ", stock_code='" + stock_code + '\'' +
                ", event_title='" + event_title + '\'' +
                ", stock_name='" + stock_name + '\'' +
                '}';
    }
}
