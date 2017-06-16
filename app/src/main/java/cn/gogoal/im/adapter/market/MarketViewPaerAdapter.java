package cn.gogoal.im.adapter.market;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hply.alilayout.DelegateAdapter;
import com.hply.alilayout.LayoutHelper;
import com.hply.alilayout.layout.LinearLayoutHelper;

import java.util.List;

import cn.gogoal.im.R;
import cn.gogoal.im.activity.copy.StockDetailMarketIndexActivity;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.bean.stock.HangqingBean;
import cn.gogoal.im.common.StockUtils;
import cn.gogoal.im.common.StringUtils;
import cn.gogoal.im.ui.view.AutoScrollViewPager;

/**
 * author wangjd on 2017/6/15 0015.
 * Staff_id 1375
 * phone 18930640263
 * description :头部轮播
 */
public class MarketViewPaerAdapter extends DelegateAdapter.Adapter<MainViewHolder> {

    private List<HangqingBean> hangqingList;

    public MarketViewPaerAdapter(List<HangqingBean> hangqingList) {
        this.hangqingList = hangqingList;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    @Override
    public MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MainViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_market_viewpager, parent, false));
    }

    @Override
    public void onBindViewHolder(MainViewHolder viewHolder, int parentPosition) {
        AutoScrollViewPager viewPager = (AutoScrollViewPager) viewHolder.itemView;
        viewPager.stopAutoScroll();
        viewPager.setDotSelectedColor(Color.BLACK);
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return 2;
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
            public Object instantiateItem(ViewGroup container, int position) {
                RecyclerView recyclerView=new RecyclerView(container.getContext());
                recyclerView.setLayoutManager(new GridLayoutManager(
                        container.getContext(),3,
                        GridLayoutManager.VERTICAL,
                        false));
                container.addView(recyclerView);

                recyclerView.setHasFixedSize(true);
                recyclerView.setNestedScrollingEnabled(false);

                recyclerView.setAdapter(new MarketRvGridAdapter(
                        position==0?hangqingList.subList(0,3):hangqingList.subList(3,6)));

                return recyclerView;
            }
        });
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    private class MarketRvGridAdapter extends CommonAdapter<HangqingBean,BaseViewHolder>{

        public MarketRvGridAdapter(List<HangqingBean> data) {
            super(R.layout.item_stock_market,data);
        }

        @Override
        protected void convert(BaseViewHolder holder, final HangqingBean data, int position) {
            holder.setText(R.id.tv_stock_market_name, data.getName());
            holder.setText(R.id.tv_stock_market_price_change$change_rate,
                    StringUtils.saveSignificand(data.getPrice_change(), 2)+"\u3000"+
                            StockUtils.plusMinus(data.getPrice_change_rate(),true));

            holder.setText(R.id.tv_stock_market_price, StringUtils.saveSignificand(data.getPrice(), 2));

            holder.getView(R.id.layout_stock_detail).setBackgroundResource(
                    StockUtils.getStockRateColor(data.getPrice_change_rate()));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), StockDetailMarketIndexActivity.class);
                    intent.putExtra("stockName", data.getName());
                    intent.putExtra("stockCode", data.getFullcode());
                    v.getContext().startActivity(intent);
                }
            });
        }
    }
}
