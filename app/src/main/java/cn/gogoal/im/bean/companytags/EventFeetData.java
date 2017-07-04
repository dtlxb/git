package cn.gogoal.im.bean.companytags;

import java.util.List;

import cn.gogoal.im.bean.BaseBeanList;

/**
 * Created by huangxx on 2017/6/29.
 */

public class EventFeetData extends BaseBeanList {

    private int id;
    private List<String> event_type;
    private String event_date;
    private String match_text;
    private String event_title;
    private String stock_code;
    private String stock_name;
    private int match;
    private int type;
    private int read_mark;

    public EventFeetData() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getEvent_type() {
        return event_type;
    }

    public void setEvent_type(List<String> event_type) {
        this.event_type = event_type;
    }

    public String getEvent_date() {
        return event_date;
    }

    public void setEvent_date(String event_date) {
        this.event_date = event_date;
    }

    public String getMatch_text() {
        return match_text;
    }

    public void setMatch_text(String match_text) {
        this.match_text = match_text;
    }

    public String getEvent_title() {
        return event_title;
    }

    public void setEvent_title(String event_title) {
        this.event_title = event_title;
    }

    public String getStock_code() {
        return stock_code;
    }

    public void setStock_code(String stock_code) {
        this.stock_code = stock_code;
    }

    public String getStock_name() {
        return stock_name;
    }

    public void setStock_name(String stock_name) {
        this.stock_name = stock_name;
    }

    public int getMatch() {
        return match;
    }

    public void setMatch(int match) {
        this.match = match;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getRead_mark() {
        return read_mark;
    }

    public void setRead_mark(int read_mark) {
        this.read_mark = read_mark;
    }

    @Override
    public String toString() {
        return "EventFeetData{" +
                "id=" + id +
                ", event_type=" + event_type +
                ", event_date='" + event_date + '\'' +
                ", match_text='" + match_text + '\'' +
                ", event_title='" + event_title + '\'' +
                ", stock_code='" + stock_code + '\'' +
                ", stock_name='" + stock_name + '\'' +
                ", match=" + match +
                ", type=" + type +
                ", read_mark=" + read_mark +
                '}';
    }
}
