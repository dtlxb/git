package cn.gogoal.im.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.gogoal.im.R;
import cn.gogoal.im.activity.copy.StockDetailMarketIndexActivity;
import cn.gogoal.im.activity.stock.RankListDetialActivity;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.bean.stock.MarkteBean;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.NormalIntentUtils;
import cn.gogoal.im.common.StockUtils;
import cn.gogoal.im.common.StringUtils;

/**
 * author wangjd on 2017/4/5 0005.
 * Staff_id 1375
 * phone 18930640263
 * description :行情整体适配器
 */
public class MarketAdapter extends CommonAdapter<MarkteBean, BaseViewHolder> {

    private Context context;

    private int padding;
    public MarketAdapter(Context context, List<MarkteBean> datas) {
        super(R.layout.item_stock_rank, datas);
        this.context = context;
        padding=AppDevice.dp2px(context,5);
    }

    @Override
    protected void convert(BaseViewHolder holder, final MarkteBean data, final int position) {
        RecyclerView recyclerView = holder.getView(R.id.item_stock_rank_rv);
        recyclerView.setFocusable(false);
        recyclerView.setFocusableInTouchMode(false);

        recyclerView.setTag(position);

        switch (position) {
            case 0:
                recyclerView.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL,false));
                recyclerView.setPadding(padding,padding,padding,padding);
                recyclerView.setBackgroundColor(Color.WHITE);
                List<MarkteBean.MarketItemData> marketItemDatas = data.getDatas();
                Collections.sort(marketItemDatas, new Comparator<MarkteBean.MarketItemData>() {
                    @Override
                    public int compare(MarkteBean.MarketItemData o1, MarkteBean.MarketItemData o2) {
                        return Integer.compare(o1.getIndex(),o2.getIndex());
                    }
                });
                recyclerView.setAdapter(new GridMarketAdapter(marketItemDatas));
                break;
            case 1:
                recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
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
                recyclerView.setAdapter(new RankListAdapter(data.getDatas(), position));
                break;
        }

        setRecycle(recyclerView.getLayoutManager(), recyclerView);

//        switch (position) {
//            case 0:
//                holder.getView(R.id.head_view_market).setVisibility(View.GONE);
//                break;
//            default:
//                holder.getView(R.id.head_view_market).setVisibility(View.VISIBLE);
//                holder.setText(R.id.tv_stock_ranklist_title, data.getTitle());
//                break;
//        }

        if (holder.getAdapterPosition() == 0) {
            holder.getView(R.id.head_view_market).setVisibility(View.GONE);
        } else {
            holder.getView(R.id.head_view_market).setVisibility(View.VISIBLE);
            holder.setText(R.id.tv_stock_ranklist_title, data.getTitle());
        }

        //标题部分点击
        holder.setOnClickListener(R.id.head_view_market, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RankListDetialActivity.class);
                intent.putExtra("MODULE_TITLE", data.getTitle());
                if (position == 1) {
                    intent.putExtra("MODULE_TYPE", RankListDetialActivity.MODULE_TYPE_HOT_INDUSTRY_TITLE);
                } else {
                    intent.putExtra("RANK_LIST_TYPE", position - 2);
                    intent.putExtra("MODULE_TYPE", RankListDetialActivity.MODULE_TYPE_TTILE_RANK_LIST);
                }
                context.startActivity(intent);
            }
        });
    }

    /**
     * 大盘部分
     */
    private class GridMarketAdapter extends CommonAdapter<MarkteBean.MarketItemData, BaseViewHolder> {

        private int innerSize;
        private GridMarketAdapter(List<MarkteBean.MarketItemData> datas) {
            super(R.layout.item_stock_market, datas);
            innerSize=(AppDevice.getWidth(context)-AppDevice.dp2px(context,40))/3;
        }

        @Override
        protected void convert(BaseViewHolder holder, final MarkteBean.MarketItemData data, int position) {
            holder.itemView.setTag(position);

            ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
            params.width = innerSize;
            holder.itemView.setLayoutParams(params);

            holder.setText(R.id.tv_stock_market_name, data.getName());
            holder.setText(R.id.tv_stock_market_price_change$change_rate,
                    StringUtils.saveSignificand(data.getPriceChange(), 2)+"\u3000"+
                            StockUtils.plusMinus(data.getRate(),true));

            holder.setText(R.id.tv_stock_market_price, StringUtils.saveSignificand(data.getPrice(), 2));

            holder.getView(R.id.layout_stock_detail).setBackgroundResource(
                    StockUtils.getStockRateColor(data.getChangeRate()));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, StockDetailMarketIndexActivity.class);
                    intent.putExtra("stockName", data.getName());
                    intent.putExtra("stockCode", data.getCode());
                    context.startActivity(intent);
                }
            });
        }
    }

    /**
     * 热门行业部分
     */
    private class GridHotIndustryAdapter extends CommonAdapter<MarkteBean.MarketItemData, BaseViewHolder> {

        private GridHotIndustryAdapter(List<MarkteBean.MarketItemData> datas) {
            super(R.layout.item_stock_hotindustry, datas);
        }

        @Override
        protected void convert(BaseViewHolder holder, final MarkteBean.MarketItemData data, int position) {
            holder.itemView.setTag(position);
            ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
            params.width = (AppDevice.getWidth(context) - 2) / 3;
            holder.itemView.setLayoutParams(params);

            holder.setText(R.id.tv_hot_industry_name, data.getName());
            holder.setText(R.id.tv_hot_industry_rate, StockUtils.plusMinus(data.getIndustryRate(), true));

            holder.setTextResColor(R.id.tv_hot_industry_rate, StockUtils.getStockRateColor(data.getChangeRate()));
            holder.setTextResColor(R.id.tv_hot_industry_curentPrice$rate, StockUtils.getStockRateColor(data.getChangeRate()));

            holder.setText(R.id.tv_hot_industry_stockname, data.getStockName());
            holder.setText(R.id.tv_hot_industry_curentPrice$rate,
                    StringUtils.saveSignificand(data.getPrice(), 2) + " " + StockUtils.plusMinus(data.getRate(), true));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, RankListDetialActivity.class);
                    intent.putExtra("MODULE_TITLE", data.getName());
                    intent.putExtra("MODULE_TYPE", RankListDetialActivity.MODULE_TYPE_HOT_INDUSTRY_GRID);
                    intent.putExtra("INDUSTRY_NAME", data.getName());
                    context.startActivity(intent);
                }
            });

        }
    }

    /**
     * [涨跌振换]部分
     */
    private class RankListAdapter extends CommonAdapter<MarkteBean.MarketItemData, BaseViewHolder> {

        private int typePostion;

        private RankListAdapter(List<MarkteBean.MarketItemData> datas, int typePostion) {
//            super(R.layout.item_stock_rank_list, datas);
            super(R.layout.item_stock_rank_list, datas);
            this.typePostion = typePostion;
        }

        @Override
        protected void convert(BaseViewHolder holder, final MarkteBean.MarketItemData data, int position) {
            holder.itemView.setTag(position);

            holder.setText(R.id.tv_mystock_stockname, data.getStockName());
            holder.setText(R.id.tv_mystock_stockcode, data.getCode());
            holder.setText(R.id.tv_mystock_price, StringUtils.saveSignificand(data.getPrice(), 2));



            switch (typePostion) {
                case 2:
                case 3:
                    holder.setText(R.id.tv_mystock_rate, StockUtils.plusMinus(data.getRate(), true));
                    holder.setBackgroundRes(R.id.tv_mystock_rate, StockUtils.getStockRateBackgroundRes(data.getChangeRate()));
                    holder.setTextResColor(R.id.tv_mystock_price, StockUtils.getStockRateColor(data.getChangeRate()));
                    break;
                case 4://换手率
                    holder.setText(R.id.tv_mystock_rate, StringUtils.saveSignificand(
                            StringUtils.parseStringDouble(data.getRate()) * 100, 2) + "%");
                    holder.setBackgroundRes(R.id.tv_mystock_rate, R.drawable.shape_my_stock_price_gray);
                    break;
                case 5://振幅榜
                    holder.setText(R.id.tv_mystock_rate, StringUtils.saveSignificand(data.getRate(), 2) + "%");
                    holder.setBackgroundRes(R.id.tv_mystock_rate, R.drawable.shape_my_stock_price_gray);
                    break;
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NormalIntentUtils.go2StockDetail(context,data.getCode(),data.getStockName());
                }
            });
        }
    }

    private void setRecycle(RecyclerView.LayoutManager layoutManager, RecyclerView recyclerView) {
        if (layoutManager instanceof LinearLayoutManager) {
            ((LinearLayoutManager) layoutManager).setSmoothScrollbarEnabled(true);
        }
        layoutManager.setAutoMeasureEnabled(true);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
    }
}
