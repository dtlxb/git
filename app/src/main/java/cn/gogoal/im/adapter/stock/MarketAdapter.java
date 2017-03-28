package cn.gogoal.im.adapter.stock;

/*
 * 作者 wangjd on 2017/3/5 0005 16:27.
 * 联系方式 18930640263 ;
 * <p>
 * 简介:大盘适配器
 */

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.gogoal.im.R;
import cn.gogoal.im.adapter.recycleviewAdapterHelper.CommonAdapter;
import cn.gogoal.im.adapter.recycleviewAdapterHelper.base.ViewHolder;
import cn.gogoal.im.bean.market.StockMarketBean;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.StringUtils;
import cn.gogoal.im.common.UIHelper;

/**
 * author wangjd on 2017/3/13 0013.
 * Staff_id 1375
 * phone 18930640263
 * description:大盘
 */

public class MarketAdapter extends CommonAdapter<StockMarketBean.DataBean.HangqingBean> {

    private Context context;
    
    public MarketAdapter(Context context,List<StockMarketBean.DataBean.HangqingBean> datas) {
        super(context, R.layout.item_stock_market, datas);
        this.context=context;
    }

    @Override
    protected void convert(ViewHolder holder, final StockMarketBean.DataBean.HangqingBean hangQing, int position) {
        // TODO: 2017/3/10 0010 字体颜色，文字为0时
        View holderView = holder.getView(R.id.item_stock_market);
        ViewGroup.LayoutParams params = holderView.getLayoutParams();
        params.width = (AppDevice.getWidth(context) - AppDevice.dp2px(context, 1)) / 2;
        holderView.setLayoutParams(params);
        holder.setText(R.id.tv_stock_market_name, hangQing.getName());

        TextView tvPrice=holder.getView(R.id.tv_stock_market_price);
        tvPrice.setText(StringUtils.saveSignificand(hangQing.getPrice(), 2));
        tvPrice.setTextColor(ContextCompat.getColor(context,hangQing.getPrice_change()>0?R.color.stock_red:R.color.stock_green));

        holder.setText(R.id.tv_stock_market_price_change, StringUtils.saveSignificand(hangQing.getPrice_change(), 2));
        holder.setText(R.id.tv_stock_market_price_change_rate, StringUtils.saveSignificand(hangQing.getPrice_change_rate(), 2) + "%");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.toast(v.getContext(),hangQing.getName());
            }
        });
    }
}
