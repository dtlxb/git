package cn.gogoal.im.activity.stock;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.RotateAnimation;
import android.widget.AdapterViewFlipper;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSONObject;
import com.hply.imagepicker.view.StatusBarUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.recycleviewAdapterHelper.wrapper.HeaderAndFooterWrapper;
import cn.gogoal.im.adapter.stock.AdViewAdapter;
import cn.gogoal.im.adapter.stock.HotIndustryAdapter;
import cn.gogoal.im.adapter.stock.MarketAdapter;
import cn.gogoal.im.adapter.stock.StockRankAdapter;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.bean.market.RankList;
import cn.gogoal.im.bean.market.StockMarketBanner;
import cn.gogoal.im.bean.market.StockMarketBean;
import cn.gogoal.im.common.AnimationUtils;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.ClickUtils;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.SPTools;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.ui.view.CustomNestedScrollView;
import cn.gogoal.im.ui.view.XTitle;

import static cn.gogoal.im.base.MyApp.getAppContext;

/**
 * author wangjd on 2017/3/13 0013.
 * Staff_id 1375
 * phone 18930640263
 * description:行情
 */

public class MarketActivity extends BaseActivity {

    // 大盘
    @BindView(R.id.rv_market)
    RecyclerView rvMarket;

    //广告
    @BindView(R.id.flipper_ad)
    AdapterViewFlipper flipperAd;

    //热门行业
    @BindView(R.id.rv_hot)
    RecyclerView rvHotIndustry;

    // 涨、跌、振、换列表
    @BindView(R.id.rv_sort_list)
    RecyclerView rvSortList;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.nestedScrollView)
    CustomNestedScrollView scrollView;

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        public void run() {
            getMarketInformation();
            getMarketAd();
//                KLog.e(StockUtils.getCurrentTime("HH:mm:ss"));
            handler.postDelayed(this, 5000);
        }
    };
    private RotateAnimation rotateAnimation;

    private ImageView refreshButton;

    @Override
    public int bindLayout() {
        return R.layout.activity_market;
    }

    @Override
    public void doBusiness(Context mContext) {
        iniTitle();
        iniRefresh(swipeRefreshLayout);
        setRecycle(new GridLayoutManager(getAppContext(), 2), rvMarket);
        setRecycle(new GridLayoutManager(getAppContext(), 3), rvHotIndustry);
        setRecycle(new LinearLayoutManager(getAppContext(), LinearLayoutManager.VERTICAL, false), rvSortList);
        getMarketInformation();
        getMarketAd();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getMarketInformation();
                        getMarketAd();
                        if (rotateAnimation != null) {
                            AnimationUtils.getInstance().setLoadingAnime(refreshButton, R.mipmap.loading_fresh);
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 2000);
            }
        });

        startRefresh();
        scrollView.setOnScrollingStateChangeListener(new CustomNestedScrollView.onScrolliingStatesChangeListener() {
            public void onScrolling() {
                stopRefresh();
            }

            public void stopScrolling() {
                startRefresh();
            }
        });

    }

    @Override
    public void setStatusBar() {
    }

    //初始化标题
    private void iniTitle() {
        StatusBarUtil.with(MarketActivity.this).setColor(getResColor(R.color.colorAccent));
        XTitle title = setMyTitle(R.string.title_stock_market, true)
                .setTitleColor(Color.WHITE)
                .setLeftTextColor(Color.WHITE)
                .setLeftImageResource(R.mipmap.image_title_back_255);
        title.setBackgroundColor(ContextCompat.getColor(getAppContext(), R.color.colorAccent));

        //添加action

        XTitle.ImageAction action = new XTitle.ImageAction(ContextCompat.getDrawable(getAppContext(), R.mipmap.refresh_white)) {
            @Override
            public void actionClick(View view) {
                rotateAnimation = AnimationUtils.getInstance().setLoadingAnime((ImageView) view, R.mipmap.loading_fresh);
                rotateAnimation.startNow();
                swipeRefreshLayout.setRefreshing(true);
            }
        };

        refreshButton = (ImageView) title.addAction(action, 0);

        title.getViewByTitle().setOnClickListener(new ClickUtils(new ClickUtils.OnSuperClickListener() {
            @Override
            public void onClick(View v) {
                UIHelper.toast(getAppContext(), "single click");
            }

            @Override
            public void onDoubleClick(View v) {
                UIHelper.toast(getAppContext(), "double click");
            }
        }));
    }

    private void startRefresh() {
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, 1000);
    }

    private void stopRefresh() {
        handler.removeCallbacks(runnable);
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

    /**
     * 获取[大盘]、[热门行业]、[涨跌振换]列表
     */
    private void getMarketInformation() {
        final Map<String, String> param = new HashMap<>();
        param.put("fullcode", "sh000001;sz399001;sh000300;sz399006");
        param.put("category_type", "1");
        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
//                KLog.e(responseInfo);
                if (JSONObject.parseObject(responseInfo).getIntValue("code") == 0) {
                    SPTools.saveString("market_data", responseInfo);//缓存大盘数据
                    parseMarketData(responseInfo);
                } else {
                    UIHelper.toast(getAppContext(), JSONObject.parseObject(responseInfo).getString("message"));
                }
            }

            @Override
            public void onFailure(String msg) {
                UIHelper.toastError(getAppContext(), msg);
            }
        };
        new GGOKHTTP(param, GGOKHTTP.APP_HQ_INFORMATION, ggHttpInterface).startGet();
    }

    /**
     * 跑马灯数据解析
     */
    private void getMarketAd() {
        Map<String, String> param = new HashMap<String, String>();
        param.put("product", "4");
        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
//                KLog.e(responseInfo);
                if (JSONObject.parseObject(responseInfo).getIntValue("code") == 0) {
                    SPTools.saveJsonArray("marketAdData", JSONObject.parseObject(responseInfo).getJSONArray("data"));
                    parseBannerData(responseInfo);
                } else if (JSONObject.parseObject(responseInfo).getIntValue("code")==1001){

                }else {
                    UIHelper.toastResponseError(getAppContext(), responseInfo);
                }
            }

            @Override
            public void onFailure(String msg) {
                UIHelper.toastError(getAppContext(), msg);
            }
        };
        new GGOKHTTP(param, GGOKHTTP.BANNER_LIST, ggHttpInterface).startGet();
    }

    /**
     * 大盘数据解析
     */
    private void parseMarketData(String responseInfo) {
        StockMarketBean stockMarketBean = JSONObject.parseObject(responseInfo, StockMarketBean.class);
        StockMarketBean.DataBean marketData = stockMarketBean.getData();

        //设置大盘
        MarketAdapter adapter = new MarketAdapter(marketData.getHangqing());
        rvMarket.setAdapter(adapter);

        // 设置热门行业
        HotIndustryAdapter hotIndustryAdapter = new HotIndustryAdapter(marketData.getHostIndustrylist());
        HeaderAndFooterWrapper headWraper = new HeaderAndFooterWrapper(hotIndustryAdapter);

        View hotHeadView = LayoutInflater.from(getAppContext()).inflate(R.layout.header_view_market, new LinearLayout(getAppContext()), false);
        headWraper.addHeaderView(hotHeadView);
        rvHotIndustry.setAdapter(headWraper);

        //设置[涨跌振换]列表
        String[] rankTitles = getResources().getStringArray(R.array.rank_list_arr);
        List<RankList> rankLists = new ArrayList<>();

        rankLists.add(new RankList(rankTitles[0], marketData.getStockRanklist().getIncrease_list()));
        rankLists.add(new RankList(rankTitles[1], marketData.getStockRanklist().getDown_list()));
        rankLists.add(new RankList(rankTitles[2], marketData.getStockRanklist().getChange_list()));
        rankLists.add(new RankList(rankTitles[3], marketData.getStockRanklist().getAmplitude_list()));

        StockRankAdapter rankAdapter = new StockRankAdapter(getAppContext(), rankLists);
        rvSortList.setAdapter(rankAdapter);

        swipeRefreshLayout.setRefreshing(false);

        if (refreshButton != null) {
            AnimationUtils.getInstance().cancleLoadingAnime(rotateAnimation, refreshButton, R.mipmap.refresh_white);
        }
    }

    /**
     * 跑马灯广告数据解析
     */
    private void parseBannerData(String responseInfo) {
        List<StockMarketBanner.DataBeanX> bannerData = JSONObject.parseObject(responseInfo, StockMarketBanner.class).getData();
        flipperAd.setAdapter(new AdViewAdapter(bannerData));
        ObjectAnimator inAnimator = ObjectAnimator.ofFloat(flipperAd, "translationY", AppDevice.dp2px(getAppContext(), 30), 0).setDuration(800);
        ObjectAnimator outAnimator = ObjectAnimator.ofFloat(flipperAd, "translationY", 0, -AppDevice.dp2px(getAppContext(), 30)).setDuration(800);
        flipperAd.setInAnimation(inAnimator);
        flipperAd.setOutAnimation(outAnimator);
        flipperAd.startFlipping();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopRefresh();
    }
}
