package cn.gogoal.im.bean.market;

import java.util.List;

/**
 * author wangjd on 2017/3/10 0010.
 * Staff_id 1375
 * phone 18930640263
 */
public class RankList {
    private String title;
    private List<StockMarketBean.DataBean.StockRanklistBean.StockRankBean> been;

    public RankList(String title, List<StockMarketBean.DataBean.StockRanklistBean.StockRankBean> been) {
        this.title = title;
        this.been = been;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<StockMarketBean.DataBean.StockRanklistBean.StockRankBean> getBeen() {
        return been;
    }

    public void setBeen(List<StockMarketBean.DataBean.StockRanklistBean.StockRankBean> been) {
        this.been = been;
    }
}
