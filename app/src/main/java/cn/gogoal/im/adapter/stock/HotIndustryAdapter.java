package cn.gogoal.im.adapter.stock;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.gogoal.im.R;
import cn.gogoal.im.adapter.recycleviewAdapterHelper.CommonAdapter;
import cn.gogoal.im.adapter.recycleviewAdapterHelper.base.ViewHolder;
import cn.gogoal.im.bean.market.StockMarketBean;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.StockUtils;
import cn.gogoal.im.common.UIHelper;

/**
 * author wangjd on 2017/3/13 0013.
 * Staff_id 1375
 * phone 18930640263
 * description:热门行业
 */
public class HotIndustryAdapter extends CommonAdapter<StockMarketBean.DataBean.HostIndustrylistBean> {

    private Context context;
    
    public HotIndustryAdapter(Context context,List<StockMarketBean.DataBean.HostIndustrylistBean> datas) {
        super(context, R.layout.item_stock_hotindustry, datas);
        this.context=context;
    }

    @Override
    protected void convert(ViewHolder holder, final StockMarketBean.DataBean.HostIndustrylistBean data, int position) {

        ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
        params.width = (AppDevice.getWidth(context) - AppDevice.dp2px(context, 2)) / 3;
        holder.setText(R.id.tv_hot_industry_name, data.getIndustry_name());

        TextView tvRate=holder.getView(R.id.tv_hot_industry_rate);
        tvRate.setText(StockUtils.plusMinus(data.getIndustry_rate()));

        if (TextUtils.isEmpty(data.getIndustry_rate())){
            tvRate.setTextColor(Color.GRAY);
        }else {
            tvRate.setTextColor(ContextCompat.getColor(context,Double.parseDouble(data.getIndustry_rate()) > 0 ?
                    R.color.stock_red:R.color.stock_green));
        }

        holder.setText(R.id.tv_hot_industry_stockname, data.getStock_name());
        holder.setText(R.id.tv_hot_industry_curentPrice$rate, data.getCurrent_price() + " " + StockUtils.plusMinus(data.getRate()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.toast(v.getContext(), data.getIndustry_name());
            }
        });
    }

}
