package cn.gogoal.im.bean.f10;

/**
 * Created by dave.
 * Date: 2017/6/23.
 * Desc: description
 */

public class FinanceData {

    private String title;
    private String content;
    private int financeType;

    public FinanceData(String title, String content, int financeType) {
        this.title = title;
        this.content = content;
        this.financeType = financeType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getFinanceType() {
        return financeType;
    }

    public void setFinanceType(int financeType) {
        this.financeType = financeType;
    }
}
