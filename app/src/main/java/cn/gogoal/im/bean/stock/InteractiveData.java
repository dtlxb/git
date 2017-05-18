package cn.gogoal.im.bean.stock;

import java.util.List;

/**
 * author wangjd on 2017/5/18 0018.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public class InteractiveData {
    /**
     * id : 591a9a4315604307409a3637
     * author : null
     * origin_link : irm.cninfo.com.cn/ircs/interaction/viewQuestionForSzse.do?questionId=5640755&condition.replyOrderType=1&condition.searchRange=0
     * title : to2710问深圳华强(000062)请问深圳华强的财务指标的现金流量表中的每股现金流和投资现金流金额，筹资现金流金额为什么同比减少，是因为什么原因导致的
     * stock : [{"stock_code":"000062","stock_name":"深圳华强"}]
     * source : 9
     * origin_id : 6119694
     * sumary : 深圳华强答to2710:
     您好，2016年年度：每股现金流同比减少，主要因为公司收购了深圳市湘海电子有限公司等分销企业，分销行业在备货、账期等方面具的行业特性所致；投资活动产生的现金流量净额同比减少，主要是由于2015年出售招商轮船股票及投资理财较多所致。2017年一季度：投资活动产生的现金流量净额同比减少，主要是由于并购深圳市鹏源电子有限公司所致；筹资活动产生的现金流量净额同比减少，主要
     * origin : null
     * answer : 深圳华强答to2710:
     您好，2016年年度：每股现金流同比减少，主要因为公司收购了深圳市湘海电子有限公司等分销企业，分销行业在备货、账期等方面具的行业特性所致；投资活动产生的现金流量净额同比减少，主要是由于2015年出售招商轮船股票及投资理财较多所致。2017年一季度：投资活动产生的现金流量净额同比减少，主要是由于并购深圳市鹏源电子有限公司所致；筹资活动产生的现金流量净额同比减少，主要是由于湘海电子（香港）有限公司偿还债务所致。谢谢。
     * date : 2017-05-16 14:10:00
     */

    private String id;
    private String author;
    private String origin_link;
    private String title;
    private int source;
    private int origin_id;
    private String sumary;
    private String origin;
    private String answer;
    private String date;
    private List<StockEle> stock;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getOrigin_link() {
        return origin_link;
    }

    public void setOrigin_link(String origin_link) {
        this.origin_link = origin_link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public int getOrigin_id() {
        return origin_id;
    }

    public void setOrigin_id(int origin_id) {
        this.origin_id = origin_id;
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

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<StockEle> getStock() {
        return stock;
    }

    public void setStock(List<StockEle> stock) {
        this.stock = stock;
    }
}
