package cn.gogoal.im.bean.stock;

/**
 * Author wangjd on 2017/4/10 0010.
 * EmployeeNumber 1375
 * Phone 18930640263
 * Description :==我的自选股新闻、公告、研报实体==
 */
public class MyStockTabBean {
    private String newsTitle;
    private String stockCode;
    private String stockName;
    private String date;
    private String newsId;

    public MyStockTabBean(String newsTitle, String stockCode, String stockName, String date, String newsId) {
        this.newsTitle = newsTitle;
        this.stockCode = stockCode;
        this.stockName = stockName;
        this.date = date;
        this.newsId = newsId;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
    }

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNewsId() {
        return newsId;
    }

    public void setNewsId(String newsId) {
        this.newsId = newsId;
    }
}