package cn.gogoal.im.bean.stock;

import java.util.ArrayList;

/**
 * Created by daiwei on 2015/9/30.
 */
public class StockDetailNewsData {
    /*
    "data":[
     {
         "id":1947063,
         "title":"目标沪市主板同策咨询再度冲刺A股IPO",
         "sumary":"中国证券网讯（记者朱楠）作为全国排名前五、却是唯一一家不曾登陆资本市场的房地产咨询代理公司，同策咨询IPO之路渐行渐近。记者近日从相关渠道获悉，同策咨询已于今年3月递交了上市申请材料，拟登陆沪市主板。如果此番能够如愿，同策、世联行这两大房地产代理公司将形成“沪同策、深世联”的南北格局。\r\n中国证券网讯（记者朱楠）作为全国排名前五、却是唯一一家不曾登陆资本市场的房地产咨询代理公司，同策咨询IPO之",
         "origin":"中国证券网",
         "author":null,
         "date":"2015-06-1711:48:50",
         "stock":    [
         {
               "stock_code":"002285",
               "stock_name":"世联行"
         },
         {
               "stock_code":"000981",
               "stock_name":"银亿股份"
         }
     ],
         "source":7,
         "origin_id":3888962,
         "origin_link":"finance.ifeng.com/a/20150617/13782668_0.shtml",
         "type":11
         "favor_sum":0,
         "praise_sum":0,
         "share_sum":0,
     },
    ,*/
    private String id;
    private String title;
    private String sumary;
    private String origin;
    private String author;
    private String date;
    private ArrayList<StockDetailNewsStock> stock;
    private String source;
    private String origin_id;
    private String origin_link;
    private String type;
    private int favor_sum;
    private int praise_sum;
    private int share_sum;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSumary() {
        return sumary;
    }

    public void setSumary(String sumary) {
        this.sumary = sumary;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<StockDetailNewsStock> getStock() {
        return stock;
    }

    public void setStock(ArrayList<StockDetailNewsStock> stock) {
        this.stock = stock;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getOrigin_id() {
        return origin_id;
    }

    public void setOrigin_id(String origin_id) {
        this.origin_id = origin_id;
    }

    public String getOrigin_link() {
        return origin_link;
    }

    public void setOrigin_link(String origin_link) {
        this.origin_link = origin_link;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getFavor_sum() {
        return favor_sum;
    }

    public void setFavor_sum(int favor_sum) {
        this.favor_sum = favor_sum;
    }

    public int getPraise_sum() {
        return praise_sum;
    }

    public void setPraise_sum(int praise_sum) {
        this.praise_sum = praise_sum;
    }

    public int getShare_sum() {
        return share_sum;
    }

    public void setShare_sum(int share_sum) {
        this.share_sum = share_sum;
    }

    @Override
    public String toString() {
        return "StockDetailNewsData{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", sumary='" + sumary + '\'' +
                ", origin='" + origin + '\'' +
                ", author='" + author + '\'' +
                ", date='" + date + '\'' +
                ", stock=" + stock +
                ", source='" + source + '\'' +
                ", origin_id='" + origin_id + '\'' +
                ", origin_link='" + origin_link + '\'' +
                ", type='" + type + '\'' +
                ", favor_sum=" + favor_sum +
                ", praise_sum=" + praise_sum +
                ", share_sum=" + share_sum +
                '}';
    }
}
