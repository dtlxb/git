package cn.gogoal.im.bean.f10;

/**
 * Created by dave.
 * Date: 2017/6/30.
 * Desc: description
 */

public class PeerCompariData {

    private String ranking;
    private String stock_title;
    private String stock_proportion;

    public PeerCompariData(String ranking, String stock_title, String stock_proportion) {
        this.ranking = ranking;
        this.stock_title = stock_title;
        this.stock_proportion = stock_proportion;
    }

    public String getRanking() {
        return ranking;
    }

    public void setRanking(String ranking) {
        this.ranking = ranking;
    }

    public String getStock_title() {
        return stock_title;
    }

    public void setStock_title(String stock_title) {
        this.stock_title = stock_title;
    }

    public String getStock_proportion() {
        return stock_proportion;
    }

    public void setStock_proportion(String stock_proportion) {
        this.stock_proportion = stock_proportion;
    }
}
