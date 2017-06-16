package cn.gogoal.im.fragment.main;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.simple.eventbus.Subscriber;

import butterknife.BindView;
import butterknife.OnClick;
import cn.gogoal.im.R;
import cn.gogoal.im.activity.Test2Activity;
import cn.gogoal.im.activity.copy.StockSearchActivity;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.common.AnimationUtils;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.SPTools;
import cn.gogoal.im.common.StockUtils;
import cn.gogoal.im.fragment.stock.MarketFragment2;
import cn.gogoal.im.fragment.stock.MyStockFragment;
import cn.gogoal.im.ui.widget.UnSlidingViewPager;

import static cn.gogoal.im.R.id.img_mystock_refresh;

/**
 * author wangjd on 2017/4/27 0027.
 * Staff_id 1375
 * phone 18930640263
 * description :自选tab
 */
public class MainStockFragment extends BaseFragment {

    @BindView(R.id.tv_mystock_edit)
    ImageView tvMystockEdit;

    public ImageView getTvMystockEdit() {
        return tvMystockEdit;
    }

    @BindView(R.id.tab_my_stock)
    TabLayout tabMyStock;

    @BindView(img_mystock_refresh)
    ImageView imgMystockRefresh;

    @BindView(R.id.layout_title)
    RelativeLayout layoutTitle;

    @BindView(R.id.vp_my_stock_tab)
    UnSlidingViewPager vpMyStockTab;

    String[] marketTabs = {"自选股", "市场"};

    private long INTERVAL_TIME;

    private MyStockFragment myStockFragment;
    private MarketFragment2 huShenFragment;

    private RotateAnimation animation;

    @Override
    public int bindLayout() {
        return R.layout.fragment_main_stock;
    }

    @Override
    public void doBusiness(final Context mContext) {

        INTERVAL_TIME = SPTools.getLong("interval_time", 15000);

        myStockFragment = new MyStockFragment();
//        marketFragment = new MarketFragment();
        huShenFragment = new MarketFragment2();

        MainStockTabAdapter tabAdapter = new MainStockTabAdapter(getChildFragmentManager());

        vpMyStockTab.setAdapter(tabAdapter);

        tabMyStock.setupWithViewPager(vpMyStockTab);

        for (int i = 0; i < marketTabs.length; i++) {
            TabLayout.Tab tab = tabMyStock.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(tabAdapter.getTabView(i));
            }
        }

        tabMyStock.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    tvMystockEdit.setVisibility(View.VISIBLE);
                } else {
                    tvMystockEdit.setVisibility(View.GONE);
                }
            }

            public void onTabUnselected(TabLayout.Tab tab) {
            }

            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        refreshAll(AppConst.REFRESH_TYPE_FIRST);

        //test
        imgMystockRefresh.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                startActivity(new Intent(mContext, Test2Activity.class));
                return true;
            }
        });
    }

    @Subscriber(tag = "updata_refresh_mode")
    void updataRefreshMode(String msg) {
        handler.removeCallbacks(runnable);
        if (StockUtils.isTradeTime()) {//交易时间段
            handler.postDelayed(runnable, INTERVAL_TIME);//启动定时刷新
        }
    }

    @OnClick({R.id.img_mystock_search, R.id.img_mystock_refresh})
    void click(View view) {
        switch (view.getId()) {
            case R.id.img_mystock_search:
                startActivity(new Intent(view.getContext(), StockSearchActivity.class));
                break;
            case R.id.img_mystock_refresh:
                refreshAll(AppConst.REFRESH_TYPE_PARENT_BUTTON);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        INTERVAL_TIME = SPTools.getLong("interval_time", 15000);

        if (StockUtils.isTradeTime()) {//交易时间段
            handler.postDelayed(runnable, INTERVAL_TIME);//启动定时刷新
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            handler.removeCallbacks(runnable);
        } else {
            INTERVAL_TIME = SPTools.getLong("interval_time", 15000);

            if (StockUtils.isTradeTime()) {//交易时间段
                handler.postDelayed(runnable, INTERVAL_TIME);//启动定时刷新
            }
        }
    }

    private void refreshAll(int type) {
        //嵌套的自选股、行情模块更新
        if (tabMyStock.getTabAt(0).isSelected()) {
            myStockFragment.refreshMyStock(type);
        } else {
            huShenFragment.getMarketInformation(type);
        }
        //刷新动画
        startAnimation(null);
    }

    @Subscriber(tag = "market_stop_animation_refresh")
    public void stopAnimation(String msg) {
        if (animation != null) {
            AnimationUtils.getInstance().cancleLoadingAnime(
                    animation,
                    imgMystockRefresh,
                    R.mipmap.img_refresh);
        } else {
            imgMystockRefresh.clearAnimation();
            imgMystockRefresh.setImageResource(R.mipmap.img_refresh);
        }
        imgMystockRefresh.setEnabled(true);
        imgMystockRefresh.setClickable(true);
    }

    @Subscriber(tag = "market_start_animation_refresh")
    public void startAnimation(String msg) {
        animation = AnimationUtils.getInstance().setLoadingAnime(
                imgMystockRefresh, R.mipmap.img_loading_refresh);
        animation.startNow();
        imgMystockRefresh.setClickable(false);
        imgMystockRefresh.setEnabled(false);
    }

    //定时刷新
    Handler handler = new Handler();

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                handler.postDelayed(this, INTERVAL_TIME);

                refreshAll(AppConst.REFRESH_TYPE_AUTO);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void onStop() {
        super.onStop();
        handler.removeCallbacks(runnable);
    }

    public void changeItem(int index) {
        if (index >= 0 && index < tabMyStock.getTabCount()) {
            try {
                TabLayout.Tab tabAt = tabMyStock.getTabAt(index);
                if (tabAt != null) tabAt.select();
            } catch (Exception e) {
                e.getMessage();
            }
        }
    }

    /**
     * 自选股、行情切换adapter
     */
    private class MainStockTabAdapter extends FragmentPagerAdapter {

        private MainStockTabAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return position == 0 ? myStockFragment : huShenFragment;
        }

        @Override
        public int getCount() {
            return marketTabs.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return marketTabs[position];
        }

        private View getTabView(int position) {
            TextView tv = (TextView) LayoutInflater.from(getContext())
                    .inflate(R.layout.item_tablayout_main_stock_top,
                            new LinearLayout(getContext()), false);

            switch (position) {
                case 0:
                    tv.setBackgroundResource(R.drawable.selector_rdb_mystock_top_left);
                    break;
                case 1:
                    tv.setBackgroundResource(R.drawable.selector_rdb_mystock_top_right);
                    break;
            }
            tv.setText(marketTabs[position]);
            return tv;
        }
    }

//    /**
//     * 弹出大盘的recyclerview适配器
//     */
//    private class GridMarketAdapter extends CommonAdapter<MyStockMarketBean.MyStockMarketData, BaseViewHolder> {
//
//        private GridMarketAdapter(List<MyStockMarketBean.MyStockMarketData> datas) {
//            super(R.layout.item_mystock_market_4rv, datas);
//        }
//
//        @Override
//        protected void convert(BaseViewHolder holder, final MyStockMarketBean.MyStockMarketData data, final int position) {
//            View itemView = holder.getView(R.id.item_mystock_market_4rv);
//
//            itemView.setTag(position);
//
//            if (!TextUtils.isEmpty(data.getFullcode())) {
//
//                holder.setText(R.id.tv_mystock_market_name, data.getName());
//                holder.setText(R.id.tv_mystock_market_price, StringUtils.saveSignificand(data.getPrice(), 2));
//
//                holder.setText(R.id.tv_mystock_market_price_change$rate,
//                        StockUtils.plusMinus(data.getPrice_change(), false) + "  " +
//                                StockUtils.plusMinus(data.getPrice_change_rate(), true));
//
//                holder.setTextResColor(R.id.tv_mystock_market_price_change$rate,
//                        StockUtils.getStockRateColor(String.valueOf(data.getPrice_change_rate())));
//
//                holder.setTextResColor(R.id.tv_mystock_market_price,
//                        StockUtils.getStockRateColor(String.valueOf(data.getPrice_change_rate())));
//
//                itemView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dismissMarket();
//                        SPTools.saveInt("change_market_item", position);
//                        try {
//                            myStockFragment.changeIitem(data);
//                        } catch (Exception e) {
//                            e.getMessage();
//                        }
//                    }
//                });
//            }
//        }
//    }

}
