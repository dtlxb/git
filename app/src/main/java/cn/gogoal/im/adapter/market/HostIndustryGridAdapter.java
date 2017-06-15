package cn.gogoal.im.adapter.market;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.hply.alilayout.DelegateAdapter;
import com.hply.alilayout.LayoutHelper;
import com.hply.alilayout.layout.GridLayoutHelper;

import java.util.List;

import cn.gogoal.im.R;
import cn.gogoal.im.bean.stock.HostIndustrylistBean;
import cn.gogoal.im.common.StockUtils;
import cn.gogoal.im.common.StringUtils;

/**
 * author wangjd on 2017/6/15 0015.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public class HostIndustryGridAdapter extends DelegateAdapter.Adapter<MainViewHolder> {

    private List<HostIndustrylistBean> hostIndustrylist;

    public HostIndustryGridAdapter(List<HostIndustrylistBean> hostIndustrylist) {
        this.hostIndustrylist = hostIndustrylist;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        GridLayoutHelper layoutHelper = new GridLayoutHelper(3);
        layoutHelper.setHGap(1);
        return layoutHelper;
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MainViewHolder(
                LayoutInflater.from(
                        parent.getContext())
                        .inflate(R.layout.item_stock_hotindustry, parent, false));
    }

    @Override
    public void onBindViewHolder(MainViewHolder holder, int position) {
        HostIndustrylistBean hotData = hostIndustrylist.get(position);

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
    }

    @Override
    public int getItemCount() {
        return 6;
    }
}
