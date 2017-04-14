package hply.com.niugu.bean;

/**
 * Created by daiwei on 2015/10/8.
 */
public class StockDetailResearchData {
    /*"data": [
     {
         "author":"马鲲鹏",
         "browse_url":null,
         "core":0,
         "create_date":"2015-06-29",
         "download_file":0,
         "guid":"B58480C9-DD7D-4907-9077-0353248A45CE",
         "look_abstract":0,
         "open_file":0,
         "organ_id":109,
         "organ_name":"国金证券",
         "recommend":0,
         "report_summary":"",
         "report_title":"平安银行：物联网金融开启大宗商品动产融资业务新时代",
         "stock_code":"000001",
         "stock_name":"平安银行",
         "stock_price":11.74,
         "stock_rate":0.08525149190110827
         "stock_type":2
     },
    */

    private String author;
    private String core;
    private String create_date;
    private String download_file;
    private String guid;
    private String look_abstract;
    private String open_file;
    private String organ_id;
    private String organ_name;
    private String recommend;
    private String report_title;
    private String stock_code;
    private String stock_name;
    private String stock_price;
    private String stock_type;
    private String stock_rate;
    private int favor_sum;
    private int praise_sum;
    private int share_sum;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCore() {
        return core;
    }

    public void setCore(String core) {
        this.core = core;
    }

    public String getCreate_date() {
        return create_date;
    }

    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }

    public String getDownload_file() {
        return download_file;
    }

    public void setDownload_file(String download_file) {
        this.download_file = download_file;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getLook_abstract() {
        return look_abstract;
    }

    public void setLook_abstract(String look_abstract) {
        this.look_abstract = look_abstract;
    }

    public String getOpen_file() {
        return open_file;
    }

    public void setOpen_file(String open_file) {
        this.open_file = open_file;
    }

    public String getOrgan_id() {
        return organ_id;
    }

    public void setOrgan_id(String organ_id) {
        this.organ_id = organ_id;
    }

    public String getOrgan_name() {
        return organ_name;
    }

    public void setOrgan_name(String organ_name) {
        this.organ_name = organ_name;
    }

    public String getRecommend() {
        return recommend;
    }

    public void setRecommend(String recommend) {
        this.recommend = recommend;
    }

    public String getReport_title() {
        return report_title;
    }

    public void setReport_title(String report_title) {
        this.report_title = report_title;
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

    public String getStock_price() {
        return stock_price;
    }

    public void setStock_price(String stock_price) {
        this.stock_price = stock_price;
    }

    public String getStock_rate() {
        return stock_rate;
    }

    public void setStock_rate(String stock_rate) {
        this.stock_rate = stock_rate;
    }

    public String getStock_type() {
        return stock_type;
    }

    public void setStock_type(String stock_type) {
        this.stock_type = stock_type;
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
        return "StockDetailResearchData{" +
                "author='" + author + '\'' +
                ", core='" + core + '\'' +
                ", create_date='" + create_date + '\'' +
                ", download_file='" + download_file + '\'' +
                ", guid='" + guid + '\'' +
                ", look_abstract='" + look_abstract + '\'' +
                ", open_file='" + open_file + '\'' +
                ", organ_id='" + organ_id + '\'' +
                ", organ_name='" + organ_name + '\'' +
                ", recommend='" + recommend + '\'' +
                ", report_title='" + report_title + '\'' +
                ", stock_code='" + stock_code + '\'' +
                ", stock_name='" + stock_name + '\'' +
                ", stock_price='" + stock_price + '\'' +
                ", stock_type='" + stock_type + '\'' +
                ", stock_rate='" + stock_rate + '\'' +
                ", favor_sum=" + favor_sum +
                ", praise_sum=" + praise_sum +
                ", share_sum=" + share_sum +
                '}';
    }
}
