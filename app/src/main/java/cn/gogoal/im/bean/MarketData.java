package cn.gogoal.im.bean;

import java.util.List;

/**
 * author wangjd on 2017/3/9 0009.
 * Staff_id 1375
 * phone 18930640263
 */
public class MarketData {

    private StockRanklist stockRanklist;

    private List<HotIndustrylist> hotIndustrylist;

    private List<HangQing> hangqing ;

    public StockRanklist getStockRanklist() {
        return stockRanklist;
    }

    public void setStockRanklist(StockRanklist stockRanklist) {
        this.stockRanklist = stockRanklist;
    }

    public List<HotIndustrylist> getHotIndustrylist() {
        return hotIndustrylist;
    }

    public void setHotIndustrylist(List<HotIndustrylist> hotIndustrylist) {
        this.hotIndustrylist = hotIndustrylist;
    }

    public List<HangQing> getHangqing() {
        return hangqing;
    }

    public void setHangqing(List<HangQing> hangqing) {
        this.hangqing = hangqing;
    }
}
