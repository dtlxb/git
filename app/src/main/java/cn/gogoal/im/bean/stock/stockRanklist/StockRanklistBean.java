package cn.gogoal.im.bean.stock.stockRanklist;

import java.util.List;

/**
 * author wangjd on 2017/6/15 0015.
 * Staff_id 1375
 * phone 18930640263
 * description :[涨跌振换]
 */
public class StockRanklistBean {

    /**
     * gray_counts : 271
     * amplitude_list : [{"rate":24.066924066924056,"current_price":11.19,"stock_code":"300657","stock_name":"N弘信"},{"rate":23.98843930635838,"current_price":9.96,"stock_code":"300655","stock_name":"N晶瑞"},{"rate":16.026080988332183,"current_price":32.05,"stock_code":"603896","stock_name":"寿仙谷"},{"rate":14.248021108179435,"current_price":20.85,"stock_code":"300140","stock_name":"中环装备"},{"rate":13.983548766157455,"current_price":8.81,"stock_code":"600365","stock_name":"通葡股份"},{"rate":13.706494438464299,"current_price":28.61,"stock_code":"300531","stock_name":"优博讯"},{"rate":12.30972865651887,"current_price":13.87,"stock_code":"300464","stock_name":"星徽精密"},{"rate":12.051406401551889,"current_price":38.79,"stock_code":"300563","stock_name":"神宇股份"},{"rate":12.049689440993795,"current_price":7.44,"stock_code":"600854","stock_name":"春兰股份"},{"rate":11.952191235059761,"current_price":23.72,"stock_code":"300346","stock_name":"南大光电"}]
     * down_list : [{"rate":-10.033444816053521,"current_price":10.76,"stock_code":"600139","stock_name":"西部资源"},{"rate":-10.033444816053514,"current_price":8.07,"stock_code":"002685","stock_name":"华东重机"},{"rate":-10.020449897750497,"current_price":8.8,"stock_code":"000760","stock_name":"斯太尔"},{"rate":-10.016068559185864,"current_price":16.8,"stock_code":"600735","stock_name":"新华锦"},{"rate":-10.010822510822518,"current_price":16.63,"stock_code":"600241","stock_name":"时代万恒"},{"rate":-9.999999999999998,"current_price":32.49,"stock_code":"603345","stock_name":"安井食品"},{"rate":-9.999999999999995,"current_price":26.91,"stock_code":"002742","stock_name":"三圣股份"},{"rate":-9.999999999999993,"current_price":38.7,"stock_code":"002761","stock_name":"多喜爱"},{"rate":-9.998270195467914,"current_price":52.03,"stock_code":"002802","stock_name":"洪汇新材"},{"rate":-9.99507631708517,"current_price":36.56,"stock_code":"603726","stock_name":"朗迪集团"}]
     * green_counts : 2651
     * change_list : [{"rate":0.5294,"current_price":45.81,"stock_code":"002871","stock_name":"伟隆股份"},{"rate":0.4865,"current_price":32.05,"stock_code":"603896","stock_name":"寿仙谷"},{"rate":0.4671,"current_price":73.01,"stock_code":"603180","stock_name":"金牌厨柜"},{"rate":0.3889,"current_price":20.48,"stock_code":"603505","stock_name":"金石资源"},{"rate":0.3873,"current_price":41.45,"stock_code":"300651","stock_name":"金陵体育"},{"rate":0.3842,"current_price":48.47,"stock_code":"002869","stock_name":"金溢科技"},{"rate":0.3103,"current_price":42.61,"stock_code":"603086","stock_name":"先达股份"},{"rate":0.3087,"current_price":30.98,"stock_code":"603385","stock_name":"惠达卫浴"},{"rate":0.3003,"current_price":56.39,"stock_code":"603903","stock_name":"XD中持股"},{"rate":0.2939,"current_price":30.96,"stock_code":"002865","stock_name":"钧达股份"}]
     * increase_list : [{"rate":44.01544401544402,"current_price":11.19,"stock_code":"300657","stock_name":"N弘信"},{"rate":43.93063583815031,"current_price":9.96,"stock_code":"300655","stock_name":"N晶瑞"},{"rate":10.08695652173913,"current_price":6.33,"stock_code":"600853","stock_name":"龙建股份"},{"rate":10.026385224274419,"current_price":20.85,"stock_code":"300140","stock_name":"中环装备"},{"rate":10.024752475247531,"current_price":17.78,"stock_code":"603488","stock_name":"展鹏科技"},{"rate":10.013717421124822,"current_price":32.08,"stock_code":"002873","stock_name":"新天药业"},{"rate":10.011312217194567,"current_price":19.45,"stock_code":"300643","stock_name":"万通智控"},{"rate":10.010764262648024,"current_price":40.88,"stock_code":"300649","stock_name":"杭州园林"},{"rate":10.005955926146514,"current_price":18.47,"stock_code":"603269","stock_name":"海鸥股份"},{"rate":10.00000000000001,"current_price":30.69,"stock_code":"300647","stock_name":"超频三"}]
     * red_counts : 311
     */

    private int gray_counts;
    private int green_counts;
    private int red_counts;
    private List<StockRankData> amplitude_list;
    private List<StockRankData> down_list;
    private List<StockRankData> change_list;
    private List<StockRankData> increase_list;

    public int getGray_counts() {
        return gray_counts;
    }

    public void setGray_counts(int gray_counts) {
        this.gray_counts = gray_counts;
    }

    public int getGreen_counts() {
        return green_counts;
    }

    public void setGreen_counts(int green_counts) {
        this.green_counts = green_counts;
    }

    public int getRed_counts() {
        return red_counts;
    }

    public void setRed_counts(int red_counts) {
        this.red_counts = red_counts;
    }

    public List<StockRankData> getAmplitude_list() {
        return amplitude_list;
    }

    public void setAmplitude_list(List<StockRankData> amplitude_list) {
        this.amplitude_list = amplitude_list;
    }

    public List<StockRankData> getDown_list() {
        return down_list;
    }

    public void setDown_list(List<StockRankData> down_list) {
        this.down_list = down_list;
    }

    public List<StockRankData> getChange_list() {
        return change_list;
    }

    public void setChange_list(List<StockRankData> change_list) {
        this.change_list = change_list;
    }

    public List<StockRankData> getIncrease_list() {
        return increase_list;
    }

    public void setIncrease_list(List<StockRankData> increase_list) {
        this.increase_list = increase_list;
    }

}
