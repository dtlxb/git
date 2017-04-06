package cn.gogoal.im.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.List;

import cn.gogoal.im.R;
import cn.gogoal.im.adapter.recycleviewAdapterHelper.CommonAdapter;
import cn.gogoal.im.adapter.recycleviewAdapterHelper.base.ViewHolder;
import cn.gogoal.im.bean.market.MarkteBean;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.StockUtils;
import cn.gogoal.im.common.StringUtils;
import cn.gogoal.im.common.UIHelper;

/**
 * author wangjd on 2017/4/5 0005.
 * Staff_id 1375
 * phone 18930640263
 * description :行情整体适配器.
 */
public class MarketAdapter extends CommonAdapter<MarkteBean> {

    private Context context;

    public MarketAdapter(Context context, List<MarkteBean> datas) {
        super(context, R.layout.item_stock_rank, datas);
        this.context = context;
    }

    @Override
    protected void convert(ViewHolder holder, MarkteBean data, int position) {
        RecyclerView recyclerView = holder.getView(R.id.item_stock_rank_list);
        recyclerView.setTag(position);
        switch (position) {
            case 0:
                recyclerView.setLayoutManager(new GridLayoutManager(context,2));
                recyclerView.setAdapter(new GridMarketAdapter(data.getDatas()));
                break;
            case 1:
                recyclerView.setLayoutManager(new GridLayoutManager(context,3));
                recyclerView.setAdapter(new GridHotIndustryAdapter(data.getDatas()));
                break;
            case 2:
            case 3:
            case 4:
            case 5:
                LinearLayoutManager layoutManager = new LinearLayoutManager(context);
                layoutManager.setSmoothScrollbarEnabled(true);
                layoutManager.setAutoMeasureEnabled(true);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(new RankListAdapter(data.getDatas(),position));
                break;
        }
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);

        if (position!=0) {
            holder.setText(R.id.tv_stock_ranklist_title, data.getTitle());
        }else {
            holder.getView(R.id.head_view_market).setVisibility(View.GONE);
        }
    }

    /**大盘部分*/
    private class GridMarketAdapter extends CommonAdapter<MarkteBean.MarketItemData> {

        private GridMarketAdapter(List<MarkteBean.MarketItemData> datas) {
            super(context, R.layout.item_stock_market, datas);
        }

        @Override
        protected void convert(ViewHolder holder, final MarkteBean.MarketItemData data, int position) {
            holder.itemView.setTag(position);
            AppDevice.setViewWidth$Height(holder.itemView,(AppDevice.getWidth(context)-1)/2 ,-2);

            holder.setText(R.id.tv_stock_market_name,data.getName());
            holder.setText(R.id.tv_stock_market_price_change, StringUtils.saveSignificand(data.getPriceChange(),2));

            holder.setText(R.id.tv_stock_market_price, StringUtils.saveSignificand(data.getPrice(), 2));
            holder.setText(R.id.tv_stock_market_price_change_rate, StringUtils.saveSignificand(data.getRate(), 2) + "%");

            holder.setTextColor(R.id.tv_stock_market_price,data.getPriceColor()==0?R.color.textColor_333333:data.getPriceColor());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UIHelper.toast(v.getContext(),data.getName());
                }
            });
        }
    }

    /**热门行业部分*/
    private class GridHotIndustryAdapter extends CommonAdapter<MarkteBean.MarketItemData> {

        private GridHotIndustryAdapter(List<MarkteBean.MarketItemData> datas) {
            super(context, R.layout.item_stock_hotindustry, datas);
        }

        @Override
        protected void convert(ViewHolder holder, final MarkteBean.MarketItemData data, int position) {
            holder.itemView.setTag(position);
            AppDevice.setViewWidth$Height(holder.itemView,(AppDevice.getWidth(context)-2)/3 , -2);
            holder.setText(R.id.tv_hot_industry_name,data.getName());
            holder.setText(R.id.tv_hot_industry_rate, StockUtils.plusMinus(data.getIndustryRate()));
            holder.setTextColor(R.id.tv_hot_industry_rate,data.getPriceColor()==0?R.color.textColor_333333:data.getPriceColor());
            holder.setText(R.id.tv_hot_industry_stockname,data.getStockName());
            holder.setText(R.id.tv_hot_industry_curentPrice$rate,
                    StringUtils.saveSignificand(data.getPrice(),2) + " " + StockUtils.plusMinus(data.getRate()));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UIHelper.toast(context,data.getName());
                }
            });
        }
    }

    /**[涨跌振换]部分*/
    private class RankListAdapter extends CommonAdapter<MarkteBean.MarketItemData> {

        private int typePostion;

        private RankListAdapter(List<MarkteBean.MarketItemData> datas,int typePostion) {
            super(context, R.layout.item_stock_rank_list, datas);
            this.typePostion=typePostion;
        }

        @Override
        protected void convert(ViewHolder holder, MarkteBean.MarketItemData data, int position) {
            holder.itemView.setTag(position);

            holder.setText(R.id.tv_stock_ranklist_stockName,data.getStockName());
            holder.setText(R.id.tv_stock_ranklist_stockCode,data.getCode());
            holder.setText(R.id.tv_stock_ranklist_currentPrice,StringUtils.saveSignificand(data.getPrice(), 2));


            switch (typePostion) {
                case 2:
                case 3:
                    holder.setText(R.id.tv_stock_ranklist_rate,StockUtils.plusMinus(data.getRate()));
                    holder.setTextColor(R.id.tv_stock_ranklist_rate,data.getPriceColor()==0?R.color.textColor_333333:data.getPriceColor());
                    break;
                case 4://换手率
                    holder.setText(R.id.tv_stock_ranklist_rate, StringUtils.saveSignificand(data.getRate()*100, 2)+"%");
                    break;
                case 5://振幅榜
                    holder.setText(R.id.tv_stock_ranklist_rate, StringUtils.saveSignificand(data.getRate(), 2)+"%");
                    break;
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO 个股
                }
            });
        }
    }
}