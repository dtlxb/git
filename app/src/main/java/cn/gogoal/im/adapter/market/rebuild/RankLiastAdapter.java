package cn.gogoal.im.adapter.market.rebuild;

import android.content.Intent;
import android.view.View;

import java.util.List;

import cn.gogoal.im.R;
import cn.gogoal.im.activity.stock.RankListDetialActivity;
import cn.gogoal.im.adapter.baseAdapter.BaseMultiItemQuickAdapter;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.bean.stock.stockRanklist.StockRankData;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.NormalIntentUtils;
import cn.gogoal.im.common.StockUtils;
import cn.gogoal.im.common.StringUtils;

import static cn.gogoal.im.adapter.market.rebuild.RankData.TYPE_ITEM_TITLE;

/**
 * author wangjd on 2017/7/11 0011.
 * Staff_id 1375
 * phone 18930640263
 * description :涨跌振换
 */
public class RankLiastAdapter extends BaseMultiItemQuickAdapter<RankData, BaseViewHolder> {

    private static final String[] rankTitle = {"涨幅榜", "跌幅榜", "换手率", "振幅榜"};

    public RankLiastAdapter(List<RankData> data) {
        super(data);
        addItemType(TYPE_ITEM_TITLE, R.layout.header_view_market);
        addItemType(RankData.RANK_TYPE_INCREASE_LIST, R.layout.item_stock_rank_list);
        addItemType(RankData.RANK_TYPE_DOWN_LIST, R.layout.item_stock_rank_list);
        addItemType(RankData.RANK_TYPE_CHANGE_LIST, R.layout.item_stock_rank_list);
        addItemType(RankData.RANK_TYPE_AMPLITUDE_LIST, R.layout.item_stock_rank_list);
    }

    @Override
    protected void convert(BaseViewHolder holder, final RankData data, int position) {
        switch (holder.getItemViewType()) {
            case TYPE_ITEM_TITLE:
                holder.setText(R.id.tv_stock_ranklist_title, data.getTitle());
                break;
            default:
                final StockRankData stockData = data.getStock();

                holder.setText(R.id.tv_mystock_stockname, stockData.getStock_name());
                holder.setText(R.id.tv_mystock_stockcode, stockData.getStock_code());
                holder.setText(R.id.tv_mystock_price, StringUtils.saveSignificand(stockData.getCurrent_price(), 2));

                holder.getView(R.id.layout_item_rank_value_with_bg).setPadding(
                        AppDevice.dp2px(10), 0, 0, 0);

                switch (holder.getItemViewType()) {
                    case RankData.RANK_TYPE_INCREASE_LIST:
                    case RankData.RANK_TYPE_DOWN_LIST:
                        holder.setText(R.id.tv_mystock_rate, StockUtils.plusMinus(stockData.getRate(), true));
                        holder.setBackgroundRes(R.id.tv_mystock_rate, StockUtils.getStockRateBackgroundRes(stockData.getRate()));
                        holder.setTextResColor(R.id.tv_mystock_price, StockUtils.getStockRateColor(stockData.getRate()));
                        break;
                    case RankData.RANK_TYPE_CHANGE_LIST://换手率
                        holder.setText(R.id.tv_mystock_rate, StringUtils.saveSignificand(
                                StringUtils.parseStringDouble(stockData.getRate()) * 100, 2) + "%");
                        holder.setBackgroundRes(R.id.tv_mystock_rate, R.drawable.shape_my_stock_price_gray);
                        holder.setTextResColor(R.id.tv_mystock_price, R.color.textColor_333333);
                        break;
                    case RankData.RANK_TYPE_AMPLITUDE_LIST://振幅榜
                        holder.setText(R.id.tv_mystock_rate, StringUtils.saveSignificand(stockData.getRate(), 2) + "%");
                        holder.setBackgroundRes(R.id.tv_mystock_rate, R.drawable.shape_my_stock_price_gray);
                        holder.setTextResColor(R.id.tv_mystock_price, R.color.textColor_333333);
                        break;
                }
                break;
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data.getItemType() != RankData.TYPE_ITEM_TITLE) {
                    NormalIntentUtils.go2StockDetail(v.getContext(), data.getStock().getStock_code(), data.getStock().getStock_name());
                } else {
                    Intent intent = new Intent(v.getContext(), RankListDetialActivity.class);
                    intent.putExtra("MODULE_TITLE", data.getTitle());
                    intent.putExtra("MODULE_TYPE", RankListDetialActivity.MODULE_TYPE_TTILE_RANK_LIST);
                    intent.putExtra("RANK_LIST_TYPE", searchIndex(rankTitle,data.getTitle()));
                    v.getContext().startActivity(intent);
                }
            }
        });
    }

    private int searchIndex(String[] rankTitle, String data) {
        for (int i=0;i<rankTitle.length;i++){
            if (rankTitle[i].equalsIgnoreCase(data)){
                return i;
            }
        }
        return -1;
    }
}
