package cn.gogoal.im.bean;

/**
 * Created by dave.
 * Date: 2017/6/23.
 * Desc: description
 */

public class FinanceData {

    private String title;
    private String head;
    private String name;
    private String content;

    public FinanceData(String title, String head, String name, String content) {
        this.title = title;
        this.head = head;
        this.name = name;
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
