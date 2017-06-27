package cn.gogoal.im.bean.stock;

/**
 * Author wangjd on 2017/4/10 0010.
 * EmployeeNumber 1375
 * Phone 18930640263
 * Description :==我的自选股新闻、公告、研报实体==
 */
public class MyStockTabNewsBean {

    private String newsTitle;

    private String date;

    private String newsId;

    private String origin_link;

    private Stock stock;

    //"http://www.cninfo.com.cn/finalpage/2017-05-03/1203473631.DOCX";
    //"http://www.cninfo.com.cn/finalpage/2017-05-04/1203474660.PDF"

    public MyStockTabNewsBean(Stock stock,String newsTitle, String date, String newsId) {
        this.stock=stock;
        this.newsTitle = newsTitle;
        this.date = date;
        this.newsId = newsId;
    }

    public MyStockTabNewsBean(Stock stock,String newsTitle, String date, String newsId, String origin_link) {
        this.stock=stock;
        this.newsTitle = newsTitle;
        this.date = date;
        this.newsId = newsId;
        this.origin_link = origin_link;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    public String getOrigin_link() {
        return origin_link;
    }

    public void setOrigin_link(String origin_link) {
        this.origin_link = origin_link;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public void setNewsTitle(String newsTitle) {
        this.newsTitle = newsTitle;
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
