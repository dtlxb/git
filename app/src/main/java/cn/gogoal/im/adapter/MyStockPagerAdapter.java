package cn.gogoal.im.adapter;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.gogoal.im.R;
import cn.gogoal.im.activity.copy.StockDetailMarketIndexActivity;
import cn.gogoal.im.bean.stock.MyStockMarketBean;
import cn.gogoal.im.common.StockUtils;
import cn.gogoal.im.common.StringUtils;

/**
 * author wangjd on 2017/4/21 0021.
 * Staff_id 1375
 * phone 18930640263
 * description :自选股 大盘样式用ViewPager
 */
public class MyStockPagerAdapter extends PagerAdapter {

    private List<MyStockMarketBean.MyStockMarketData> datas;

    public MyStockPagerAdapter(List<MyStockMarketBean.MyStockMarketData> datas) {
        this.datas = datas;
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public int getCount() {
        return datas == null ? 0 : datas.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_mystock_market_4banner, container, false);

        view.setBackgroundResource(R.color.stock_market_bg);

        TextView tvName = (TextView) view.findViewById(R.id.tv_mystock_market_name);
        TextView tvPrice = (TextView) view.findViewById(R.id.tv_mystock_market_price);
        TextView tvChangePrice$Rate = (TextView) view.findViewById(R.id.tv_mystock_market_price_change$rate);

        tvName.setText(datas.get(position).getName());
        tvPrice.setText(StringUtils.saveSignificand(datas.get(position).getPrice(), 2));

        tvChangePrice$Rate.setText(StockUtils.plusMinus(String.valueOf(datas.get(position).getPrice_change()) + "", false) + "  " +
                StockUtils.plusMinus(String.valueOf(datas.get(position).getPrice_change_rate()), true));

        tvPrice.setTextColor(ContextCompat.getColor(container.getContext(),
                StockUtils.getStockRateColor(String.valueOf(datas.get(position).getPrice_change_rate()))));

        tvChangePrice$Rate.setTextColor(ContextCompat.getColor(container.getContext(),
                StockUtils.getStockRateColor(String.valueOf(datas.get(position).getPrice_change_rate()))));

        container.addView(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), StockDetailMarketIndexActivity.class);
                intent.putExtra("stockName", datas.get(position).getName());
                intent.putExtra("stockCode", datas.get(position).getFullcode());
                v.getContext().startActivity(intent);
            }
        });
        return view;
    }

}
