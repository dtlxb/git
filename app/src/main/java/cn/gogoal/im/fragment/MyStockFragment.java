package cn.gogoal.im.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.percent.PercentRelativeLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.gogoal.im.R;
import cn.gogoal.im.activity.MainActivity;
import cn.gogoal.im.activity.copy.StockSearchActivity;
import cn.gogoal.im.activity.stock.EditMyStockActivity;
import cn.gogoal.im.activity.stock.MyStockNewsActivity;
import cn.gogoal.im.adapter.MyStockPagerAdapter;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.bean.stock.MyStockBean;
import cn.gogoal.im.bean.stock.MyStockData;
import cn.gogoal.im.bean.stock.MyStockMarketBean;
import cn.gogoal.im.common.AnimationUtils;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.MyStockSortInteface;
import cn.gogoal.im.common.SPTools;
import cn.gogoal.im.common.StockUtils;
import cn.gogoal.im.common.StringUtils;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.ui.NormalItemDecoration;
import cn.gogoal.im.ui.XDividerItemDecoration;
import cn.gogoal.im.ui.view.DrawableCenterTextView;
import cn.gogoal.im.ui.view.SortView;

/**
 * author wangjd on 2017/4/7 0007.
 * Staff_id 1375
 * phone 18930640263
 * description :我的自选股.
 */
public class MyStockFragment extends BaseFragment implements MyStockSortInteface {

    @BindView(R.id.swiperefreshlayout_my_stock)
    SwipeRefreshLayout refreshLayout;

    //左上角编辑按钮
    @BindView(R.id.tv_mystock_edit)
    TextView tvMystockEdit;

    @BindView(R.id.img_mystock_refresh)
    ImageView imgMystockRefresh;

    //搜索框视图按钮
    @BindView(R.id.search_market)
    DrawableCenterTextView searchMarket;

    //自选股列表
    @BindView(R.id.rv_mystock)
    RecyclerView rvMystock;

    @BindView(R.id.tv_mystock_price)
    SortView tvMystockPrice;

    @BindView(R.id.tv_mystock_rate)
    SortView tvMystockRate;

    @BindView(R.id.item_mystock)
    PercentRelativeLayout itemMystock;

    @BindView(R.id.tv_mystock_stockcode)
    View flagLayout;

    @BindView(R.id.tv_mystock_stockname)
    TextView sortTitleName;

    @BindView(R.id.flipper_mystock_banner)
    ViewPager mystockBanner;

    @BindView(R.id.btn_my_stock_zixuan)
    RadioButton btnMyStockZixuan;

    @BindView(R.id.btn_my_stock_flag)
    RadioButton btnMyStockFlag;

    //大盘缩略viewpager集合
    private ArrayList<MyStockMarketBean.MyStockMarketData> bannerDatas = new ArrayList<>();
    //大盘缩略viewpager适配器
    private MyStockPagerAdapter bannerAdapter;

    @BindView(R.id.layout_title)
    ViewGroup layoutTitle;

    //自选股集合
    private ArrayList<MyStockData> myStockDatas = new ArrayList<>();
    private MyStockAdapter myStockAdapter;

    //标题右上角旋转动画
    private RotateAnimation rotateAnimation;

    //自动刷新默认时间
    private long INTERVAL_TIME;

    //弹窗的recyclerView
    @BindView(R.id.rv_mystock_market)
    RecyclerView rvMystockMarket;

    private GridMarketAdapter myStockMarketAdapter;
    //蒙版
    @BindView(R.id.view_dialog_mask)
    View viewDialogMask;

    @Override
    public int bindLayout() {
        return R.layout.fragment_my_stock;
    }

    @Override
    public void doBusiness(final Context mContext) {
        BaseActivity.iniRefresh(refreshLayout);

        INTERVAL_TIME=SPTools.getLong("INTERVAL_TIME",15000);

        initRecyclerView(mContext);
        initMarketBanner();
        initSortTitle(mContext);

        refreshAll(AppConst.REFRESH_TYPE_FIRST);//请求

        handler.postDelayed(runnable, INTERVAL_TIME);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshAll(AppConst.REFRESH_TYPE_SWIPEREFRESH);
            }
        });

        imgMystockRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshAll(AppConst.REFRESH_TYPE_PARENT_BUTTON);
            }
        });

        searchMarket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), StockSearchActivity.class));
            }
        });
    }

    private void refreshAll(int refreshType) {
        startAnimation();//刷新按钮动画
        getMyStockData(refreshType);
        getMarketLittle();
    }

    public boolean isMaskViewVisiable() {
        return viewDialogMask.getVisibility() == View.VISIBLE;
    }

    public void dismissMarket() {
        ((MainActivity)getActivity()).hideMainMsk();
        rvMystockMarket.setVisibility(View.GONE);
        rvMystockMarket.startAnimation(
                android.view.animation.AnimationUtils.loadAnimation(getContext(), R.anim.top_out));

        viewDialogMask.setClickable(false);
        viewDialogMask.setEnabled(false);//防止重复点击反复出现
        viewDialogMask.setVisibility(View.GONE);
        viewDialogMask.startAnimation(
                android.view.animation.AnimationUtils.loadAnimation(getContext(),R.anim.alpha_out
        ));

    }

    public void showMarketDialog() {
//        new MyStockTopDialog().show(getChildFragmentManager());
        ((MainActivity)getActivity()).showMainMsk();
        rvMystockMarket.startAnimation(
                android.view.animation.AnimationUtils.loadAnimation(getContext(), R.anim.top_in));
        rvMystockMarket.setVisibility(View.VISIBLE);

        viewDialogMask.setEnabled(true);
        viewDialogMask.setClickable(true);
        viewDialogMask.setVisibility(View.VISIBLE);
        viewDialogMask.startAnimation(
                android.view.animation.AnimationUtils.loadAnimation(getContext(), R.anim.alpha_in));
    }

    //init
    private void initMarketBanner() {
        bannerAdapter = new MyStockPagerAdapter(bannerDatas);
        mystockBanner.setOffscreenPageLimit(4);
        mystockBanner.setAdapter(bannerAdapter);
    }

    public void changeIitem(int pos) {
        mystockBanner.setCurrentItem(pos);
    }


    private void initSortTitle(Context ctx) {
        itemMystock.setPadding(0, AppDevice.dp2px(ctx, 5), 0, AppDevice.dp2px(ctx, 5));
        flagLayout.setVisibility(View.GONE);
        sortTitleName.setText("全部");
        AppDevice.setViewWidth$Height(itemMystock, -1, AppDevice.dp2px(ctx, 40));

        iniSortBar();

        tvMystockPrice.seOntSortListener(this);
        tvMystockRate.seOntSortListener(this);

    }

    private void iniSortBar() {
        tvMystockPrice.setDefaultText("最新价");
        tvMystockRate.setDefaultText("涨跌幅");
        tvMystockPrice.setViewStateNormal();
        tvMystockRate.setViewStateNormal();
    }

    private void initRecyclerView(Context mContext) {
        //自选股列表
        myStockAdapter = new MyStockAdapter(myStockDatas);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        layoutManager.setSmoothScrollbarEnabled(true);
        layoutManager.setAutoMeasureEnabled(true);

        rvMystock.setLayoutManager(layoutManager);
        rvMystock.setHasFixedSize(true);
        rvMystock.setNestedScrollingEnabled(false);

        rvMystock.setLayoutManager(layoutManager);
        rvMystock.addItemDecoration(new NormalItemDecoration(mContext));
        rvMystock.setAdapter(myStockAdapter);

        //大盘列表
        myStockMarketAdapter = new GridMarketAdapter(bannerDatas);
        rvMystockMarket.setLayoutManager(new GridLayoutManager(mContext, 3));
        rvMystockMarket.setAdapter(myStockMarketAdapter);
        rvMystockMarket.addItemDecoration(new RvMarketDvider(mContext));

        viewDialogMask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissMarket();
            }
        });
    }

    @Override
    public void doSort(final View view, final int sortType) {

        KLog.e(sortType);
        Collections.sort(myStockDatas, new Comparator<MyStockData>() {
            @Override
            public int compare(MyStockData o1, MyStockData o2) {
                if (view.getId() == R.id.tv_mystock_price) {
                    tvMystockRate.setViewStateNormal();
                    if (sortType == -1) {
                        return StringUtils.getStockDouble(o2.getPrice()).compareTo(StringUtils.getStockDouble(o1.getPrice()));
                    } else if (sortType == 1) {
                        return StringUtils.getStockDouble(o1.getPrice()).compareTo(StringUtils.getStockDouble(o2.getPrice()));
                    } else {
                        getMyStockData(AppConst.REFRESH_TYPE_PARENT_BUTTON);
                        return 0;
                    }
                } else if (view.getId() == R.id.tv_mystock_rate) {
                    tvMystockPrice.setViewStateNormal();
                    if (sortType == -1) {
                        return StringUtils.getStockDouble(o2.getChange_rate()).compareTo(StringUtils.getStockDouble(o1.getChange_rate()));
                    } else if (sortType == 1) {
                        return StringUtils.getStockDouble(o1.getChange_rate()).compareTo(StringUtils.getStockDouble(o2.getChange_rate()));
                    } else {
                        getMyStockData(AppConst.REFRESH_TYPE_PARENT_BUTTON);
                        return 0;
                    }
                }
                return 0;
            }
        });

        myStockAdapter.notifyDataSetChanged();
    }

    /**
     * 获取自选股数据
     */
    private void getMyStockData(final int loadType) {
        iniSortBar();
        Map<String, String> params = new HashMap<>();
        params.put("page", "1");
        params.put("rows", "200");
        params.put("token", UserUtils.getToken());

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                int code = JSONObject.parseObject(responseInfo).getIntValue("code");
                if (code == 0) {
                    myStockDatas.clear();
                    myStockDatas.addAll(JSONObject.parseObject(responseInfo, MyStockBean.class).getData());
                    myStockAdapter.notifyDataSetChanged();

                    //缓存自选股
                    StockUtils.saveMyStock(JSONObject.parseObject(responseInfo).getJSONArray("data"));

                    if (loadType == AppConst.REFRESH_TYPE_SWIPEREFRESH || loadType == AppConst.REFRESH_TYPE_PARENT_BUTTON) {
                        UIHelper.toast(getActivity(), getString(R.string.str_refresh_ok));
                    }
                    new android.os.Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            refreshLayout.setRefreshing(false);
                            stopAnimation();
                        }
                    }, 1000);

                } else if (code == 1001) {
                } else {
                    refreshLayout.setRefreshing(false);
                    UIHelper.toastResponseError(getActivity(), responseInfo);
                }
            }

            @Override
            public void onFailure(String msg) {
                refreshLayout.setRefreshing(false);
                UIHelper.toastError(getActivity(), msg);
            }
        };
        new GGOKHTTP(params, GGOKHTTP.GET_MYSTOCKS, ggHttpInterface).startGet();
    }

    /**
     * 获取大盘数据
     */
    public void getMarketLittle() {
        final Map<String, String> param = new HashMap<>();
        param.put("fullcode", "sh000001;sz399001;sh000300;csi930715;sz399006");
        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                int code = JSONObject.parseObject(responseInfo).getIntValue("code");
                if (code == 0) {
                    bannerDatas.clear();
                    List<MyStockMarketBean.MyStockMarketData> marketDatas =
                            JSONObject.parseObject(responseInfo, MyStockMarketBean.class).getData();
                    bannerDatas.addAll(marketDatas);

                    bannerAdapter.notifyDataSetChanged();
                    myStockMarketAdapter.notifyDataSetChanged();

                    try {
                        mystockBanner.setCurrentItem(SPTools.getInt("choose_banner_item", 0));
                    }catch (Exception e){
                        e.getMessage();
                    }
                }
            }

            @Override
            public void onFailure(String msg) {
                UIHelper.toastError(getActivity(), msg);
            }
        };
        new GGOKHTTP(param, GGOKHTTP.APP_HQ_GET, ggHttpInterface).startGet();
    }

    @OnClick({R.id.tv_mystock_edit, R.id.img_mystock_more, R.id.tv_mystock_news, R.id.tv_mystock_gonggao,
            R.id.tv_mystock_yanbao, R.id.btn_my_stock_flag})
    void click(View view) {
        switch (view.getId()) {
            case R.id.tv_mystock_edit:
                Intent intent = new Intent(view.getContext(), EditMyStockActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("my_stock_edit", myStockDatas);
                intent.putExtras(bundle);
                startActivityForResult(intent, 7);
                break;

            case R.id.img_mystock_more:
                showMarketDialog();
                break;

            case R.id.tv_mystock_news:
                intentNews(0);
                break;
            case R.id.tv_mystock_gonggao:
                intentNews(1);
                break;
            case R.id.tv_mystock_yanbao:
                intentNews(2);
                break;
            case R.id.btn_my_stock_flag:
                UIHelper.toast(getContext(), "努力开发中...");
                btnMyStockFlag.setChecked(false);
                btnMyStockZixuan.setChecked(true);
                break;
        }
    }

    private void intentNews(int index) {
        Intent intent = new Intent(getActivity(), MyStockNewsActivity.class);
        intent.putExtra("news_title", getString(R.string.title_mtstock_news));
        intent.putExtra("showTabIndex", index);
        startActivity(intent);
    }

    private void stopAnimation() {
        if (rotateAnimation != null) {
            AnimationUtils.getInstance().cancleLoadingAnime(rotateAnimation, imgMystockRefresh, R.mipmap.img_refresh);
        } else {
            imgMystockRefresh.clearAnimation();
            imgMystockRefresh.setImageResource(R.mipmap.img_refresh);
        }
    }

    private void startAnimation() {
        rotateAnimation = AnimationUtils.getInstance().setLoadingAnime(imgMystockRefresh, R.mipmap.img_loading_refresh);
        rotateAnimation.startNow();
    }

    /**
     * 自选股适配器
     */
    private class MyStockAdapter extends CommonAdapter<MyStockData, BaseViewHolder> {

        private MyStockAdapter(List<MyStockData> datas) {
            super(R.layout.item_my_stock, datas);
        }

        @Override
        protected void convert(BaseViewHolder holder, final MyStockData data, int position) {
            holder.setText(R.id.tv_mystock_stockname, data.getStock_name());
            holder.setText(R.id.tv_mystock_stockcode, data.getStock_code());

            TextView rateView = holder.getView(R.id.tv_mystock_rate);
            TextView priceView = holder.getView(R.id.tv_mystock_price);

            rateView.setClickable(false);
            priceView.setClickable(false);

            priceView.setText(StringUtils.saveSignificand(data.getPrice(), 2));

            if (data.getStock_type() == 1) {
                rateView.setText(StockUtils.plusMinus(data.getChange_rate(), true));
                rateView.setTextColor(ContextCompat.getColor(getActivity(),
                        StockUtils.getStockRateColor(data.getChange_rate())));
            } else {
                rateView.setText(StockUtils.getStockStatus(data.getStock_type()));
                rateView.setTextColor(getResColor(R.color.stock_gray));
            }

            UIHelper.setRippBg(holder.itemView);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StockUtils.go2StockDetail(v.getContext(),
                            data.getStock_code(), data.getStock_name());
                }
            });
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
                        SPTools.saveInt("choose_banner_item", position);
                        changeIitem(position);
                    }
                });
            }
        }
    }

    private class RvMarketDvider extends XDividerItemDecoration {

        private RvMarketDvider(Context context) {
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
//        handler.postDelayed(runnable,INTERVAL_TIME);
    };

    @Override
    public void onStop() {
        super.onStop();
        handler.removeCallbacks(runnable);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 7 && data != null) {
            List<MyStockData> myStockdata = (List<MyStockData>) data.getSerializableExtra("my_stock_edit");
            myStockDatas.clear();
            myStockDatas.addAll(myStockdata);
            myStockAdapter.notifyDataSetChanged();
        }
    }

}

