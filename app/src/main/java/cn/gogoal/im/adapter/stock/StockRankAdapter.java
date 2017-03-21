package cn.gogoal.im.adapter.stock;

/**
 * 作者 wangjd on 2017/3/5 0005 16:27.
 * 联系方式 18930640263 ;
 * <p>
 * 简介:涨跌振换列表适配器
 */

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import cn.gogoal.im.R;
import cn.gogoal.im.adapter.recycleviewAdapterHelper.CommonAdapter;
import cn.gogoal.im.adapter.recycleviewAdapterHelper.base.ViewHolder;
import cn.gogoal.im.bean.market.RankList;
import cn.gogoal.im.bean.market.StockMarketBean;
import cn.gogoal.im.common.StockUtils;
import cn.gogoal.im.common.StringUtils;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.ui.NormalItemDecoration;

/**
 * author wangjd on 2017/3/13 0013.
 * Staff_id 1375
 * phone 18930640263
 * description:涨跌换振
 */
public class StockRankAdapter extends CommonAdapter<RankList> {

    private Context context;

    public StockRankAdapter(Context context, List<RankList> datas) {
        super(context, R.layout.item_stock_rank, datas);
        this.context = context;
    }

    @Override
    protected void convert(ViewHolder holder, final RankList data, int position) {
        RecyclerView recyclerView = holder.getView(R.id.item_stock_rank_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new RankListAdapter(data.getBeen(), position));

        recyclerView.addItemDecoration(new NormalItemDecoration(context));
        holder.setText(R.id.tv_stock_ranklist_title, data.getTitle());

        holder.setOnClickListener(R.id.head_view_market, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.toast(context, data.getTitle());
            }
        });
    }

    private class RankListAdapter extends CommonAdapter<StockMarketBean.DataBean.StockRanklistBean.StockRankBean> {

        private int parentPosition;

        private RankListAdapter(List<StockMarketBean.DataBean.StockRanklistBean.StockRankBean> datas, int parentPosition) {
            super(context, R.layout.item_stock_rank_list, datas);
            this.parentPosition = parentPosition;
        }

        @Override
        protected void convert(ViewHolder holder, StockMarketBean.DataBean.StockRanklistBean.StockRankBean data, final int position) {
            holder.setText(R.id.tv_stock_ranklist_stockName, data.getStock_name());
            holder.setText(R.id.tv_stock_ranklist_stockCode, data.getStock_code());
            holder.setText(R.id.tv_stock_ranklist_currentPrice, StringUtils.saveSignificand(data.getCurrent_price(), 2));

            TextView tvRate = holder.getView(R.id.tv_stock_ranklist_rate);

            switch (parentPosition) {
                case 0:
                case 1:
                    tvRate.setText(StockUtils.plusMinus(String.valueOf(data.getRate())));
                    tvRate.setTextColor(ContextCompat.getColor(context, data.getRate() > 0 ? R.color.stock_red : R.color.stock_green));
                    break;
                case 2://换手率
                    holder.setText(R.id.tv_stock_ranklist_rate, StringUtils.saveSignificand(data.getRate()*100, 2)+"%");
                    break;
                case 3://振幅榜
                    holder.setText(R.id.tv_stock_ranklist_rate, StringUtils.saveSignificand(data.getRate(), 2)+"%");
                    break;
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UIHelper.toast(context, "[" + parentPosition + "," + position + "]");
                }
            });

        }
    }
}
