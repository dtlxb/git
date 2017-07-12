package cn.gogoal.im.adapter.market.rebuild;

import cn.gogoal.im.adapter.baseAdapter.entity.MultiItemEntity;
import cn.gogoal.im.bean.stock.stockRanklist.StockRankData;

/**
 * author wangjd on 2017/7/11 0011.
 * Staff_id 1375
 * phone 18930640263
 * description :${涨跌振换实体}.
 */
public class RankData implements MultiItemEntity {

    public static final int TYPE_ITEM_TITLE = 0x0;

    public static final int RANK_TYPE_INCREASE_LIST = 0x1;

    public static final int RANK_TYPE_DOWN_LIST = 0x2;

    public static final int RANK_TYPE_CHANGE_LIST = 0x3;

    public static final int RANK_TYPE_AMPLITUDE_LIST = 0x4;


    private int itemType;//item类型，title还是股票

    private StockRankData stock;
    private String title;

    public RankData(int itemType, String title) {
        this.itemType = itemType;
        this.title = title;
    }

    public RankData(int itemType, StockRankData stock) {
        this.itemType = itemType;
        this.stock = stock;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public StockRankData getStock() {
        return stock;
    }

    public void setStock(StockRankData stock) {
        this.stock = stock;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
