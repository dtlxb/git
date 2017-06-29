package cn.gogoal.im.bean.stock.bigdata.subject;

import java.util.List;

import cn.gogoal.im.bean.stock.Stock;

/**
 * author wangjd on 2017/6/28 0028.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public class SubjectDetailData {
    /**
     * theme_summarize : {"img_url":"http://ggimage.go-goal.cn/prod/theme/1478219626152.png","describe":"即&ldquo;工业4.0&rdquo;，是以智能制造为主导的第四次工业革命，或革命性的生产方法。包含了由集中式控制向分散式增强型控制的基本模式转变，目标是建立一个高度灵活的个性化和数字化的产品与服务的生产模式。在这种模式中，传统的行业界限将消失，并会产生各种新的活动领域和合作形式。该模式旨在通过充分利用信息通讯技术和网络空间虚拟系统&mdash;信息物理系统（Cyber-Physical System)相结合的手段，将制造业向智能化转型。该主题主要涉及：机器人、工业自动化、3D打印、智能机床、物联网等相关产业链个股。"}
     * theme_words : ["机器人","3D打印","物联网","高端制造"]
     * insert_date : 2017-06-27 09:38:44
     * theme_code : BK0158
     * stocks : [{"stock_code":"300024","stock_name":"机器人"},{"stock_code":"600835","stock_name":"上海机电"},{"stock_code":"300097","stock_name":"智云股份"},{"stock_code":"002073","stock_name":"软控股份"},{"stock_code":"002559","stock_name":"亚威股份"},{"stock_code":"002527","stock_name":"新时达"},{"stock_code":"300124","stock_name":"汇川技术"},{"stock_code":"002334","stock_name":"英威腾"},{"stock_code":"002031","stock_name":"巨轮智能"},{"stock_code":"000410","stock_name":"沈阳机床"},{"stock_code":"300161","stock_name":"华中数控"},{"stock_code":"000837","stock_name":"秦川机床"},{"stock_code":"300278","stock_name":"华昌达"},{"stock_code":"002270","stock_name":"法因数控"},{"stock_code":"002097","stock_name":"山河智能"},{"stock_code":"002577","stock_name":"雷柏科技"},{"stock_code":"300280","stock_name":"南通锻压"},{"stock_code":"002611","stock_name":"东方精工"},{"stock_code":"000821","stock_name":"京山轻机"}]
     * plate_type : 1
     * theme_name : 智能制造
     */

    private ThemeSummarizeBean theme_summarize;
    private String insert_date;
    private String theme_code;
    private int plate_type;
    private String theme_name;
    private List<String> theme_words;
    private List<Stock> stocks;

    public ThemeSummarizeBean getTheme_summarize() {
        return theme_summarize;
    }

    public void setTheme_summarize(ThemeSummarizeBean theme_summarize) {
        this.theme_summarize = theme_summarize;
    }

    public String getInsert_date() {
        return insert_date;
    }

    public void setInsert_date(String insert_date) {
        this.insert_date = insert_date;
    }

    public String getTheme_code() {
        return theme_code;
    }

    public void setTheme_code(String theme_code) {
        this.theme_code = theme_code;
    }

    public int getPlate_type() {
        return plate_type;
    }

    public void setPlate_type(int plate_type) {
        this.plate_type = plate_type;
    }

    public String getTheme_name() {
        return theme_name;
    }

    public void setTheme_name(String theme_name) {
        this.theme_name = theme_name;
    }

    public List<String> getTheme_words() {
        return theme_words;
    }

    public void setTheme_words(List<String> theme_words) {
        this.theme_words = theme_words;
    }

    public List<Stock> getStocks() {
        return stocks;
    }

    public void setStocks(List<Stock> stocks) {
        this.stocks = stocks;
    }

}
