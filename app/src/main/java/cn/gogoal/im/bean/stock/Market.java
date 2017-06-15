package cn.gogoal.im.bean.stock;

import java.util.List;

import cn.gogoal.im.bean.stock.stockRanklist.StockRanklistBean;

/**
 * author wangjd on 2017/6/15 0015.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public class Market {
    /**
     * stockRanklist : {"gray_counts":271,"amplitude_list":[{"rate":24.066924066924056,"current_price":11.19,"stock_code":"300657","stock_name":"N弘信"},{"rate":23.98843930635838,"current_price":9.96,"stock_code":"300655","stock_name":"N晶瑞"},{"rate":16.026080988332183,"current_price":32.05,"stock_code":"603896","stock_name":"寿仙谷"},{"rate":14.248021108179435,"current_price":20.85,"stock_code":"300140","stock_name":"中环装备"},{"rate":13.983548766157455,"current_price":8.81,"stock_code":"600365","stock_name":"通葡股份"},{"rate":13.706494438464299,"current_price":28.61,"stock_code":"300531","stock_name":"优博讯"},{"rate":12.30972865651887,"current_price":13.87,"stock_code":"300464","stock_name":"星徽精密"},{"rate":12.051406401551889,"current_price":38.79,"stock_code":"300563","stock_name":"神宇股份"},{"rate":12.049689440993795,"current_price":7.44,"stock_code":"600854","stock_name":"春兰股份"},{"rate":11.952191235059761,"current_price":23.72,"stock_code":"300346","stock_name":"南大光电"}],"down_list":[{"rate":-10.033444816053521,"current_price":10.76,"stock_code":"600139","stock_name":"西部资源"},{"rate":-10.033444816053514,"current_price":8.07,"stock_code":"002685","stock_name":"华东重机"},{"rate":-10.020449897750497,"current_price":8.8,"stock_code":"000760","stock_name":"斯太尔"},{"rate":-10.016068559185864,"current_price":16.8,"stock_code":"600735","stock_name":"新华锦"},{"rate":-10.010822510822518,"current_price":16.63,"stock_code":"600241","stock_name":"时代万恒"},{"rate":-9.999999999999998,"current_price":32.49,"stock_code":"603345","stock_name":"安井食品"},{"rate":-9.999999999999995,"current_price":26.91,"stock_code":"002742","stock_name":"三圣股份"},{"rate":-9.999999999999993,"current_price":38.7,"stock_code":"002761","stock_name":"多喜爱"},{"rate":-9.998270195467914,"current_price":52.03,"stock_code":"002802","stock_name":"洪汇新材"},{"rate":-9.99507631708517,"current_price":36.56,"stock_code":"603726","stock_name":"朗迪集团"}],"green_counts":2651,"change_list":[{"rate":0.5294,"current_price":45.81,"stock_code":"002871","stock_name":"伟隆股份"},{"rate":0.4865,"current_price":32.05,"stock_code":"603896","stock_name":"寿仙谷"},{"rate":0.4671,"current_price":73.01,"stock_code":"603180","stock_name":"金牌厨柜"},{"rate":0.3889,"current_price":20.48,"stock_code":"603505","stock_name":"金石资源"},{"rate":0.3873,"current_price":41.45,"stock_code":"300651","stock_name":"金陵体育"},{"rate":0.3842,"current_price":48.47,"stock_code":"002869","stock_name":"金溢科技"},{"rate":0.3103,"current_price":42.61,"stock_code":"603086","stock_name":"先达股份"},{"rate":0.3087,"current_price":30.98,"stock_code":"603385","stock_name":"惠达卫浴"},{"rate":0.3003,"current_price":56.39,"stock_code":"603903","stock_name":"XD中持股"},{"rate":0.2939,"current_price":30.96,"stock_code":"002865","stock_name":"钧达股份"}],"increase_list":[{"rate":44.01544401544402,"current_price":11.19,"stock_code":"300657","stock_name":"N弘信"},{"rate":43.93063583815031,"current_price":9.96,"stock_code":"300655","stock_name":"N晶瑞"},{"rate":10.08695652173913,"current_price":6.33,"stock_code":"600853","stock_name":"龙建股份"},{"rate":10.026385224274419,"current_price":20.85,"stock_code":"300140","stock_name":"中环装备"},{"rate":10.024752475247531,"current_price":17.78,"stock_code":"603488","stock_name":"展鹏科技"},{"rate":10.013717421124822,"current_price":32.08,"stock_code":"002873","stock_name":"新天药业"},{"rate":10.011312217194567,"current_price":19.45,"stock_code":"300643","stock_name":"万通智控"},{"rate":10.010764262648024,"current_price":40.88,"stock_code":"300649","stock_name":"杭州园林"},{"rate":10.005955926146514,"current_price":18.47,"stock_code":"603269","stock_name":"海鸥股份"},{"rate":10.00000000000001,"current_price":30.69,"stock_code":"300647","stock_name":"超频三"}],"red_counts":311}
     * hostIndustrylist : [{"industry_rate":"1.6174900787438387","rate":"5.217807563427477","current_price":"21.98","stock_code":"600036","stock_name":"招商银行","industry_code":"801780","industry_name":"银行"},{"industry_rate":"1.155345142160049","rate":"5.858449034879785","current_price":"31.26","stock_code":"600809","stock_name":"山西汾酒","industry_code":"801120","industry_name":"食品饮料"},{"industry_rate":"0.8213716966766214","rate":"4.89192263936293","current_price":"18.44","stock_code":"600291","stock_name":"西水股份","industry_code":"801790","industry_name":"非银金融"},{"industry_rate":"0.5527118221124033","rate":"9.989258861439309","current_price":"20.48","stock_code":"603505","stock_name":"金石资源","industry_code":"801020","industry_name":"采掘"},{"industry_rate":"-0.56976476756643","rate":"9.937888198757754","current_price":"5.31","stock_code":"002305","stock_name":"南国置业","industry_code":"801180","industry_name":"房地产"},{"industry_rate":"-0.5897064537618205","rate":"2.974925626859325","current_price":"24.23","stock_code":"002543","stock_name":"万和电气","industry_code":"801110","industry_name":"家用电器"}]
     * hangqing : [{"amplitude":2.081334387362085,"avg_price":18.860006313681204,"close_price":4538.0166,"code":"930715","fullcode":"csi930715","high_price":4537.3925,"insert_time":"2017-05-23 13:43:45","low_price":4442.9412,"name":"CS朝阳88","open_price":4532.3818,"price":4460.8576,"price_change":-77.159,"price_change_rate":-1.7000000000000002,"source":"csi","symbol_type":2,"tdate":"2017-05-23 00:00:00","turnover":1095620.3602,"type":"01","update_time":"2017-05-23 13:43:38","volume":580922584},{"amplitude":1.0857094291738791,"close_price":3075.6756,"code":"000001","fall":616,"flat":62,"fullcode":"sh000001","high_price":3084.2352,"insert_time":"2017-05-23 13:43:45","low_price":3050.8423,"name":"上证指数","open_price":3069.3935,"price":3060.6696,"price_change":-15.005999999999858,"price_change_rate":-0.4878928063804862,"rose":81,"source":"sh","stage":"T","symbol_type":2,"tdate":"2017-05-23 00:00:00","turnover":1.2724758091119999E7,"update_time":"2017-05-23 13:43:42","volume":11270999700},{"amplitude":0.9742033004022812,"close_price":3411.2387,"code":"000300","fullcode":"sh000300","high_price":3434.7662,"insert_time":"2017-05-23 13:43:45","low_price":3401.5338,"name":"沪深300","open_price":3405.0254,"price":3413.2277,"price_change":1.9890000000000327,"price_change_rate":0.05830726533443798,"source":"sh","stage":"T","symbol_type":2,"tdate":"2017-05-23 00:00:00","turnover":8040158.755960001,"update_time":"2017-05-23 13:43:42","volume":7248003700},{"amplitude":1.4435890950557617,"close_price":9899.6453,"code":"399001","deal_count":3229978,"fall":1640,"flat":199,"fullcode":"sz399001","high_price":9914.4954,"insert_time":"2017-05-23 13:43:45","low_price":9771.5852,"md_stream_id":"900","name":"深证成指","open_price":9878.3797,"price":9806.0464,"price_change":-93.59890000000087,"price_change_rate":-0.9454773091718838,"rose":168,"source":"sz","stage":"T","symbol_type":2,"tdate":"2017-05-23 00:00:00","turnover":1.538645864906777E7,"update_time":"2017-05-23 13:43:45","volume":11359328686}]
     */

    private StockRanklistBean stockRanklist;
    private List<HostIndustrylistBean> hostIndustrylist;
    private List<HangqingBean> hangqing;

    public StockRanklistBean getStockRanklist() {
        return stockRanklist;
    }

    public void setStockRanklist(StockRanklistBean stockRanklist) {
        this.stockRanklist = stockRanklist;
    }

    public List<HostIndustrylistBean> getHostIndustrylist() {
        return hostIndustrylist;
    }

    public void setHostIndustrylist(List<HostIndustrylistBean> hostIndustrylist) {
        this.hostIndustrylist = hostIndustrylist;
    }

    public List<HangqingBean> getHangqing() {
        return hangqing;
    }

    public void setHangqing(List<HangqingBean> hangqing) {
        this.hangqing = hangqing;
    }
}
