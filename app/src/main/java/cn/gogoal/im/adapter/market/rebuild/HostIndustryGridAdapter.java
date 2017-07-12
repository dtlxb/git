package cn.gogoal.im.adapter.market.rebuild;

import android.content.Intent;
import android.view.View;

import java.util.List;

import cn.gogoal.im.R;
import cn.gogoal.im.activity.stock.RankListDetialActivity;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.bean.stock.HostIndustrylistBean;
import cn.gogoal.im.common.StockUtils;
import cn.gogoal.im.common.StringUtils;

/**
 * author wangjd on 2017/7/11 0011.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public class HostIndustryGridAdapter extends CommonAdapter<HostIndustrylistBean, BaseViewHolder> {

    public HostIndustryGridAdapter(List<HostIndustrylistBean> data) {
        super(R.layout.item_stock_hotindustry, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, final HostIndustrylistBean hotData, int position) {
        holder.setText(R.id.tv_hot_industry_name, hotData.getIndustry_name());
        holder.setText(R.id.tv_hot_industry_rate, StockUtils.plusMinus(hotData.getIndustry_rate(), true));

        holder.setTextResColor(R.id.tv_hot_industry_rate,
                StockUtils.getStockRateColor(hotData.getIndustry_rate()));

        holder.setTextResColor(R.id.tv_hot_industry_curentPrice$rate,
                StockUtils.getStockRateColor(hotData.getIndustry_rate()));

        holder.setText(R.id.tv_hot_industry_stockname, hotData.getStock_name());

        holder.setText(R.id.tv_hot_industry_curentPrice$rate,
                StringUtils.saveSignificand(hotData.getCurrent_price(), 2) + " " +
                        StockUtils.plusMinus(hotData.getRate(), true));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), RankListDetialActivity.class);
                intent.putExtra("MODULE_TITLE", hotData.getIndustry_name());
                intent.putExtra("MODULE_TYPE", RankListDetialActivity.MODULE_TYPE_HOT_INDUSTRY_GRID);
                intent.putExtra("INDUSTRY_NAME", hotData.getIndustry_name());
                v.getContext().startActivity(intent);
            }
        });
    }
}
