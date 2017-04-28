package cn.gogoal.im.fragment.main;

import android.content.Context;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.activity.MainActivity;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.base.AppManager;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.bean.stock.MyStockMarketBean;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.SPTools;
import cn.gogoal.im.common.StockUtils;
import cn.gogoal.im.common.StringUtils;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.fragment.stock.MarketFragment;
import cn.gogoal.im.fragment.stock.MyStockFragment;
import cn.gogoal.im.ui.XDividerItemDecoration;
import cn.gogoal.im.ui.widget.UnSlidingViewPager;

/**
 * author wangjd on 2017/4/27 0027.
 * Staff_id 1375
 * phone 18930640263
 * description :${annotated}.
 */
public class MainStockFragment extends BaseFragment {

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout refreshLayout;

    @BindView(R.id.tv_mystock_edit)
    TextView tvMystockEdit;

    @BindView(R.id.tab_my_stock)
    TabLayout tabMyStock;

    @BindView(R.id.img_mystock_refresh)
    ImageView imgMystockRefresh;

    @BindView(R.id.layout_title)
    RelativeLayout layoutTitle;

    @BindView(R.id.vp_my_stock_tab)
    UnSlidingViewPager vpMyStockTab;

    String[] marketTabs = {"自选股", "市场"};

    private long INTERVAL_TIME;

    //弹窗的recyclerView
    @BindView(R.id.rv_dialog_market)
    RecyclerView rvDialogMarket;
    //蒙版
    @BindView(R.id.view_dialog_mask)
    View viewDialogMask;
    private List<MyStockMarketBean.MyStockMarketData> listDialogMarketDatas;
    private GridMarketAdapter myAdaptetDialogMarket;
    private MyStockFragment myStockFragment;
    private MarketFragment marketFragment;

    @Override
    public int bindLayout() {
        return R.layout.fragment_main_stock;
    }

    @Override
    public void doBusiness(Context mContext) {

        INTERVAL_TIME = SPTools.getLong("interval_time", 15000);

        BaseActivity.iniRefresh(refreshLayout);

        myStockFragment = new MyStockFragment();
        marketFragment = new MarketFragment();

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
                    dismissMarket();
                }
            }

            public void onTabUnselected(TabLayout.Tab tab) {
            }

            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        initView();//初始化一些View

        refreshAll(AppConst.REFRESH_TYPE_FIRST);
//        handler.postDelayed(runnable, INTERVAL_TIME);//启动定时刷新
    }

    public void initView() {
        //人造弹窗
        rvDialogMarket.setLayoutManager(new GridLayoutManager(getContext(), 3));
        rvDialogMarket.addItemDecoration(new RvDialogMarketDvider(getContext()));
        listDialogMarketDatas = new ArrayList<>();
        myAdaptetDialogMarket = new GridMarketAdapter(listDialogMarketDatas);
        rvDialogMarket.setAdapter(myAdaptetDialogMarket);

        viewDialogMask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissMarket();
            }
        });
    }

    /**
     * 获取大盘数据
     */
    public void getMarketLittle(final int refreshType) {
        final Map<String, String> param = new HashMap<>();
        param.put("fullcode", "sh000001;sz399001;sh000300;csi930715;sz399006");
        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                int code = JSONObject.parseObject(responseInfo).getIntValue("code");
                if (code == 0) {
                    List<MyStockMarketBean.MyStockMarketData> marketDatas =
                            JSONObject.parseObject(responseInfo, MyStockMarketBean.class).getData();
                    listDialogMarketDatas.clear();

                    listDialogMarketDatas.addAll(marketDatas);

                    myAdaptetDialogMarket.notifyDataSetChanged();

                    myStockFragment.changeIitem(
                            marketDatas.get(SPTools.getInt("CHANGE_ITEM", 0)));

                }
            }

            @Override
            public void onFailure(String msg) {
                UIHelper.toastError(getActivity(), msg);
            }
        };
        new GGOKHTTP(param, GGOKHTTP.APP_HQ_GET, ggHttpInterface).startGet();
    }

    /**
     * 显示指数[弹窗]
     */
    public void showMarketDialog() {
        ((MainActivity) getActivity()).showMainMsk();//父Activity显示蒙版
        rvDialogMarket.startAnimation(
                android.view.animation.AnimationUtils.loadAnimation(getContext(), R.anim.top_in));
        rvDialogMarket.setVisibility(View.VISIBLE);
        viewDialogMask.setEnabled(true);
        viewDialogMask.setClickable(true);
        viewDialogMask.setVisibility(View.VISIBLE);
        viewDialogMask.startAnimation(
                android.view.animation.AnimationUtils.loadAnimation(getContext(), R.anim.alpha_in));
    }

    private void refreshAll(int type){
        getMarketLittle(type);
        AppManager.getInstance().sendMessage("stock_market_updata");
    }

    /**
     * 销毁指数[弹窗]
     */
    public void dismissMarket() {
        if (isMaskViewVisiable()) {
            ((MainActivity) getActivity()).hideMainMsk();
            rvDialogMarket.setVisibility(View.GONE);
            rvDialogMarket.startAnimation(
                    android.view.animation.AnimationUtils.loadAnimation(getContext(), R.anim.top_out));

            viewDialogMask.setClickable(false);
            viewDialogMask.setEnabled(false);//防止重复点击反复出现
            viewDialogMask.setVisibility(View.GONE);
            viewDialogMask.startAnimation(
                    android.view.animation.AnimationUtils.loadAnimation(getContext(), R.anim.alpha_out
                    ));
        }
    }

    public boolean isMaskViewVisiable() {
        //弹窗是否可见
        return rvDialogMarket.getVisibility() == View.VISIBLE;
    }

    //定时刷新
    Handler handler = new Handler();

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                handler.postDelayed(this, INTERVAL_TIME);
                //TODO 刷新 1.数据首次载入；2.行情刷新；3.自选股数据刷新；4.自选股中的大盘数据刷新
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

    /**
     * 自选股、行情切换adapter
     */
    private class MainStockTabAdapter extends FragmentPagerAdapter {

        public MainStockTabAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return position == 0 ? myStockFragment : marketFragment;
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

    /**
     * 弹出大盘的recyclerview适配器
     */
    private class GridMarketAdapter extends CommonAdapter<MyStockMarketBean.MyStockMarketData, BaseViewHolder> {

        private GridMarketAdapter(List<MyStockMarketBean.MyStockMarketData> datas) {
            super(R.layout.item_mystock_market_4rv, datas);
        }

        @Override
        protected void convert(BaseViewHolder holder, final MyStockMarketBean.MyStockMarketData data, final int position) {
            View itemView = holder.getView(R.id.item_mystock_market_4rv);

            itemView.setTag(position);

            if (!TextUtils.isEmpty(data.getFullcode())) {

                holder.setText(R.id.tv_mystock_market_name, data.getName());
                holder.setText(R.id.tv_mystock_market_price, StringUtils.saveSignificand(data.getPrice(), 2));

                holder.setText(R.id.tv_mystock_market_price_change$rate,
                        StockUtils.plusMinus(data.getPrice_change(), false) + "  " +
                                StockUtils.plusMinus(data.getPrice_change_rate(), true));

                holder.setTextResColor(R.id.tv_mystock_market_price_change$rate,
                        StockUtils.getStockRateColor(String.valueOf(data.getPrice_change_rate())));

                holder.setTextResColor(R.id.tv_mystock_market_price,
                        StockUtils.getStockRateColor(String.valueOf(data.getPrice_change_rate())));

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismissMarket();
                        SPTools.saveInt("CHANGE_ITEM", position);
                        myStockFragment.changeIitem(data);
                    }
                });
            }
        }
    }

    private class RvDialogMarketDvider extends XDividerItemDecoration {

        private RvDialogMarketDvider(Context context) {
            super(context, 1, ContextCompat.getColor(context, R.color.wx_share_line));
        }

        @Override
        public boolean[] getItemSidesIsHaveOffsets(int itemPosition) {
//            left, top, right, bottom
            boolean[] dividerTog = new boolean[4];
            switch (itemPosition % 3) {
                case 0:
                case 1:
                    dividerTog[2] = true;
                    dividerTog[3] = true;
                    break;
                case 2:
                    dividerTog[3] = true;
                    break;
            }
            return dividerTog;
        }
    }
}
