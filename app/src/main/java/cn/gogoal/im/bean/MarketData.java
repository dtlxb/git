package cn.gogoal.im.bean;

import java.util.List;

/**
 * author wangjd on 2017/3/9 0009.
 * Staff_id 1375
 * phone 18930640263
 */
public class MarketData {

    private StockRanklist stockRanklist;

    private List<HostIndustrylist> hostIndustrylist ;

    private List<HangQing> hangqing ;

    public StockRanklist getStockRanklist() {
        return stockRanklist;
    }

    public void setStockRanklist(StockRanklist stockRanklist) {
        this.stockRanklist = stockRanklist;
    }

    public List<HostIndustrylist> getHostIndustrylist() {
        return hostIndustrylist;
    }

    public void setHostIndustrylist(List<HostIndustrylist> hostIndustrylist) {
        this.hostIndustrylist = hostIndustrylist;
    }

    public List<HangQing> getHangqing() {
        return hangqing;
    }

    public void setHangqing(List<HangQing> hangqing) {
        this.hangqing = hangqing;
    }
}
