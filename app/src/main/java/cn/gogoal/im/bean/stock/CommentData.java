package cn.gogoal.im.bean.stock;

import java.util.ArrayList;

/**
 * Created by daiwei on 2015/10/14.
 */
public class CommentData {
    /*{
            "account_id": 44181,
            "author_id": null,
            "avoter": "http://118.26.238.118:10001/Upload/Head/Photo/NOFTX.gif",
            "content": "评论内容",
            "date": "2015-10-1716:26:15",
            "id": 1388,
            "parent_id": 0,
            "praise_sum": 0,
            "sons": [
            ],
            "username": "jackson"
        },
,*/
    private String account_id;
    private String author_id;
    private String avoter;
    private String content;
    private String date;
    private String id;
    private int parent_id;
    private int praise_sum;
    private ArrayList<CommentData_Sons> sons;
    private String username;

    public String getAccount_id() {
        return account_id;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }

    public String getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(String author_id) {
        this.author_id = author_id;
    }

    public String getAvoter() {
        return avoter;
    }

    public void setAvoter(String avoter) {
        this.avoter = avoter;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getParent_id() {
        return parent_id;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
    }

    public int getPraise_sum() {
        return praise_sum;
    }

    public void setPraise_sum(int praise_sum) {
        this.praise_sum = praise_sum;
    }

    public ArrayList<CommentData_Sons> getSons() {
        return sons;
    }

    public void setSons(ArrayList<CommentData_Sons> sons) {
        this.sons = sons;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "CommentData{" +
                "account_id='" + account_id + '\'' +
                ", author_id='" + author_id + '\'' +
                ", avoter='" + avoter + '\'' +
                ", content='" + content + '\'' +
                ", date='" + date + '\'' +
                ", id='" + id + '\'' +
                ", parent_id=" + parent_id +
                ", praise_sum=" + praise_sum +
                ", sons=" + sons +
                ", username='" + username + '\'' +
                '}';
    }
}
