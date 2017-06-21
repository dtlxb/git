package cn.gogoal.im.activity.copy;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.socks.library.KLog;

import org.simple.eventbus.Subscriber;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindArray;
import butterknife.BindView;
import butterknife.OnClick;
import cn.gogoal.im.R;
import cn.gogoal.im.activity.MessageHolderActivity;
import cn.gogoal.im.activity.SquareChatRoomActivity;
import cn.gogoal.im.activity.stock.InteractiveInvestorActivity;
import cn.gogoal.im.adapter.TreatAdapter;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.base.AppManager;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.bean.BaseMessage;
import cn.gogoal.im.bean.stock.Stock;
import cn.gogoal.im.bean.stock.StockDetail;
import cn.gogoal.im.bean.stock.StockDialogInfo;
import cn.gogoal.im.bean.stock.TreatData;
import cn.gogoal.im.common.AnimationUtils;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.IMHelpers.ChatGroupHelper;
import cn.gogoal.im.common.IMHelpers.MessageListUtils;
import cn.gogoal.im.common.Impl;
import cn.gogoal.im.common.SPTools;
import cn.gogoal.im.common.StockUtils;
import cn.gogoal.im.common.StringUtils;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.fragment.stock.CompanyFinanceFragment;
import cn.gogoal.im.fragment.stock.CompanyInfoFragment;
import cn.gogoal.im.fragment.stock.StockNewsMinFragment;
import cn.gogoal.im.ui.Badge.BadgeView;
import cn.gogoal.im.ui.dialog.StockPopuDialog;
import cn.gogoal.im.ui.dialog.WaitDialog;
import cn.gogoal.im.ui.stock.DialogRecyclerView;
import cn.gogoal.im.ui.stockviews.BitmapChartView;
import cn.gogoal.im.ui.stockviews.KChartsBitmap;
import cn.gogoal.im.ui.stockviews.TimesFivesBitmap;
import cn.gogoal.im.ui.widget.UnSlidingViewPager;
import cn.gogoal.im.ui.widget.WrapContentHeightViewPager;
import hply.com.niugu.autofixtext.AutofitTextView;
import hply.com.niugu.stock.StockMinuteBean;


/*
* 普通股票详情
* */
public class CopyStockDetailActivity extends BaseActivity {

    public static final String TAG = "CopyStockDetailActivity";

    TimesFivesBitmap fiveDayBitmap;
    TimesFivesBitmap timesBitmap;

    StockMinuteBean timesBean;

    //返回
    @BindView(R.id.btnBack)
    LinearLayout btnBack;

    //标题栏
    @BindView(R.id.relative_header)
    RelativeLayout relative_header;

    //标题
    @BindView(R.id.textHeadTitle)
    TextView textHeadTitle;

    //交易状态及时间
    @BindView(R.id.text_state)
    TextView text_state;

    //刷新
    @BindView(R.id.btnRefresh)
    ImageView btnRefresh;

    //刷新按钮加载菊花动画
    protected static RotateAnimation animation;

    //数据栏
    @BindView(R.id.linear_header)
    LinearLayout linear_header;

    //下拉刷新
    @BindView(R.id.scrollView)
    NestedScrollView scrollView;

    //股票价格
    @BindView(R.id.stock_price)
    AutofitTextView stock_price;

    //涨跌
    @BindView(R.id.stock_detail_tv1)
    AutofitTextView stock_detail_tv1;

    @BindView(R.id.stock_detail_tv2)
    AutofitTextView stock_detail_tv2;

    //今开
    @BindView(R.id.stock_start)
    AutofitTextView stock_start;

    //成交量
    @BindView(R.id.stock_volume)
    AutofitTextView stock_volume;

    //昨收
    @BindView(R.id.stock_close)
    AutofitTextView stock_close;

    //换手率
    @BindView(R.id.stock_turnover_rate)
    TextView stock_turnover_rate;

    //停牌
    @BindView(R.id.text_delist)
    TextView text_delist;

    @BindView(R.id.stock_detail_delist)
    TextView stock_detail_delist;

    //下拉刷新rootView
    @BindView(R.id.fragment_rotate_header_with_view_group_frame)
    SwipeRefreshLayout ptrFrame;

    //下拉刷新头部控件
    //private HeaderView headerView;
    private int stock_charge_type = 1;

    //图表表头
    @BindView(R.id.charts_line_tab_up_ll)
    TabLayout tabChartsTitles;
    @BindArray(R.array.stock_detail_chart_titles)
    String[] stockDetailChartTitles;

    @BindView(R.id.flag_layout_treat)
    LinearLayout layoutTreat;

    //页面加载动画
    @BindView(R.id.load_animation)
    RelativeLayout load_animation;

    @BindView(R.id.stockdetail_textview)
    LinearLayout textLayout;//无内容时布局

    @BindView(R.id.stock_detail_fragment_time_line)
    BitmapChartView mBitmapChartView;//图表控件

    //    //加自选
    @BindView(R.id.tv_stockDetail_toggle_mystock)
    TextView stock_detail_choose;

    private int pixels = -85;
    private String stockName;
    private String stockCode;
    private ArrayList<String> priceVolumDatas = new ArrayList<>();
    //定时刷新
    private Timer timer;
    //数据集合
    private List<Map<String, Object>> mOHLCData = new ArrayList<>();
    private double closePrice;
    private HashMap<String, Bitmap> map = new HashMap<>();
    //定时刷新
    private int showItem;
    private int width;
    private int height;

    private boolean isChoose = true;

    //k线数据设置
    private int dayK1;
    private int dayK2;
    private int dayK3;
    private int dayK4;

    //修改的中间新闻模块
    @BindView(R.id.tablayout_news_)
    TabLayout tabLayoutNews;

    @BindView(R.id.vp_news_)
    WrapContentHeightViewPager viewPagerNews;

    private String[] newTitles = {"新闻", "公告", "研报", "资料", "财务"};

    private RotateAnimation rotateAnimation;

    //交易五档、明细
    @BindView(R.id.tablayout_treat)
    TabLayout tabLayoutTreat;

    @BindView(R.id.vp_treat)
    UnSlidingViewPager vpTreat;

    private String change_value;

    //=====================20170613===================
    @BindView(R.id.rv_stock_info)
    DialogRecyclerView rvStockInfo;

    @BindView(R.id.view_dialog_mask)
    View viewMask;

    @BindView(R.id.view_dialog_mask_bottom)
    View viewMaskBottom;

    @BindView(R.id.iv_show_info_dialog)
    ImageView imageViewShoeDialog;

    @BindView(R.id.iv_message_tag)
    ImageView ivMessageTag;

    @BindArray(R.array.stock_detail_info)
    String[] stockDetailInfos;

    //消息
    private BadgeView badge;
    private int unReadCount;

    private StockInfoDialogAdapter infoDialogAdapter;

    private List<StockDialogInfo> stockDialogInfoList;

    @Override
    public int bindLayout() {
        return R.layout.copy_stock_detail;
    }

    @Override
    public void doBusiness(Context mContext) {

        //找控件
        findView();
        //初始化
        init();

        setNewsTab();

        getStockInfoDialog();

        initList(stockCode);

        onShow(showItem);

        initChatMessage();
    }

    private void initChatMessage() {
        badge = new BadgeView(getActivity());
        initBadge(unReadCount, badge);
        ivMessageTag.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CopyStockDetailActivity.this, MessageHolderActivity.class));
            }
        });
    }

    /***/
    private void setNewsTab() {
        final List<Fragment> fragments = new ArrayList<>();
        fragments.add(StockNewsMinFragment.getInstance(stockCode, stockName, 0));
        fragments.add(StockNewsMinFragment.getInstance(stockCode, stockName, 1));
        fragments.add(StockNewsMinFragment.getInstance(stockCode, stockName, 2));
        fragments.add(CompanyInfoFragment.newInstance(stockCode));
        fragments.add(new CompanyFinanceFragment());

        viewPagerNews.setOffscreenPageLimit(4);
        viewPagerNews.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            public int getCount() {
                return newTitles.length;
            }

            public CharSequence getPageTitle(int position) {
                return newTitles[position];
            }
        });
        tabLayoutNews.setupWithViewPager(viewPagerNews);

        viewPagerNews.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                AppManager.getInstance().sendMessage("StockDetailNewsFragment_TAB", new BaseMessage(String.valueOf(position)));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        AppDevice.setTabLayoutWidth(tabLayoutNews, 15);
    }

    private void findView() {
        BaseActivity.iniRefresh(ptrFrame);
        scrollView.smoothScrollTo(0, 20);
    }

    //初始化
    private void init() {
        dayK1 = SPTools.getInt("tv_ln1", 5);
        dayK2 = SPTools.getInt("tv_ln2", 10);
        dayK3 = SPTools.getInt("tv_ln3", 20);
        dayK4 = SPTools.getInt("tv_ln4", 0);
        stockCode = getIntent().getStringExtra("stock_code");
        stockName = getIntent().getStringExtra("stock_name");

        TreatAdapter treatAdapter = new TreatAdapter(getSupportFragmentManager(), getActivity(), stockCode, true);
        vpTreat.setAdapter(treatAdapter);
        tabLayoutTreat.setupWithViewPager(vpTreat);

        for (int i = 0; i < 2; i++) {
            TabLayout.Tab tabAt = tabLayoutTreat.getTabAt(i);
            if (tabAt != null) {
                tabAt.setCustomView(treatAdapter.getTabView(i));
            }
        }

        textHeadTitle.setText(stockName + "(" + stockCode + ")");

        if (StockUtils.isMyStock(stockCode)) {
            toggleIsMyStock(true, false);
        } else {
            toggleIsMyStock(false, false);
        }

        showItem = SPTools.getInt("showItem", 0);

        layoutTreat.setVisibility(showItem == 0 ? View.VISIBLE : View.GONE);

        for (int i = 0; i < 5; i++) {
            tabChartsTitles.addTab(tabChartsTitles.newTab().setText(stockDetailChartTitles[i]));
        }

        TabLayout.Tab tabAt = tabChartsTitles.getTabAt(showItem);
        if (tabAt != null) tabAt.select();

        //图表头点击事件
        tabChartsTitles.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                layoutTreat.setVisibility(tab.getPosition() == 0 ? View.VISIBLE : View.GONE);
                switch (tab.getPosition()) {
                    case 0:
                        if (map.containsKey("0")) {
                            setMinLineStatu(1);
                            if (stock_charge_type == 1 && StockUtils.isTradeTime()) {
                                mBitmapChartView.setBitmap(map.get(String.valueOf("0")), true, timesBitmap);
                            } else {
                                mBitmapChartView.setBitmap(map.get(String.valueOf("0")), false, timesBitmap);
                            }
                        } else {
                            setMinLineStatu(0);
                            GetMinLineData();
                        }
                        break;
                    case 1:
                        if (map.containsKey("1")) {
                            setFiveStatu(1);
                            if (stock_charge_type == 1 && StockUtils.isTradeTime()) {
                                mBitmapChartView.setBitmap(map.get(String.valueOf("1")), true, fiveDayBitmap);
                            } else {
                                mBitmapChartView.setBitmap(map.get(String.valueOf("1")), false, fiveDayBitmap);
                            }
                        } else {
                            setFiveStatu(0);
                            getFiveData();
                        }
                        break;
                    case 2:
                        if (map.containsKey("2")) {
                            setDayKStatue(1);
                            mBitmapChartView.setBitmap(map.get("2"));
                        } else {
                            setDayKStatue(0);
                            getKLineData(0);
                        }
                        break;
                    case 3:
                        if (map.containsKey("3")) {
                            setWeekLineStatu(1);
                            mBitmapChartView.setBitmap(map.get("3"));
                        } else {
                            setWeekLineStatu(0);
                            getKLineData(1);
                        }
                        break;
                    case 4:
                        if (map.containsKey("4")) {
                            setMonthLineStatu(1);
                            mBitmapChartView.setBitmap(map.get("4"));
                        } else {
                            setMonthLineStatu(0);
                            getKLineData(2);
                        }
                        break;
                    default:
                        break;
                }
                showItem = tab.getPosition();
                SPTools.saveInt("showItem", showItem);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //返回
        btnBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //点击刷新
        btnRefresh.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshAll();
            }
        });


        // 修改状态栏颜色
        setStatusColorId(R.color.header_gray);

        relative_header.setBackgroundResource(R.color.header_gray);
        linear_header.setBackgroundResource(R.color.header_gray);

        initRefreshStyle(R.color.header_gray);

        //股票状态和当前时间
        StockState();

        //设置下拉刷新属性
        ptrFrame.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshAll();
                ptrFrame.setRefreshing(false);
            }
        });

    }

    private void refreshAll() {
        StockState();
        startAnimation();
        initList(stockCode);
        refreshChart(showItem);
        AppManager.getInstance().sendMessage("updata_treat_data");
    }

    void stopAnimation() {
        if (rotateAnimation != null) {
            AnimationUtils.getInstance().cancleLoadingAnime(rotateAnimation, btnRefresh, R.mipmap.refresh_white);
        } else {
            btnRefresh.clearAnimation();
            btnRefresh.setImageResource(R.mipmap.refresh_white);
        }
    }

    void startAnimation() {
        rotateAnimation = AnimationUtils.getInstance().setLoadingAnime(btnRefresh, R.mipmap.loading_white);
        rotateAnimation.startNow();
    }

    //图表展示
    private void onShow(int item) {
        switch (item) {
            case 0://分时
                setMinLineStatu(0);
                GetMinLineData();
                break;
            case 1://五日
                setFiveStatu(0);
                getFiveData();
                break;
            case 2://日K
                setDayKStatue(0);
                getKLineData(0);
                break;
            case 3://周K
                setWeekLineStatu(0);
                getKLineData(1);
                break;
            case 4://月K
                setMonthLineStatu(0);
                getKLineData(2);
                break;
        }

    }

    private void refreshChart(int item) {
        switch (item) {
            case 0:
                GetMinLineData();
                break;
            case 1:
                getFiveData();
                break;
            case 2:
                getKLineData(0);
                break;
            case 3:
                getKLineData(1);
                break;
            case 4:
                getKLineData(2);
                break;
        }
    }

    private void GetMinLineData() {
        HashMap<String, String> param = new HashMap<>();
        param.put("stock_code", stockCode);
        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {

                if (textLayout.getVisibility() == View.VISIBLE) {
                    textLayout.setVisibility(View.GONE);
                }
                timesBean = JSONObject.parseObject(responseInfo, StockMinuteBean.class);
//                if (timesBean.getCode() == 0) {
                new BitmapTask().execute("0");
//                } else {
//                    if (chart_progressbar.getVisibility() == View.VISIBLE) {
//                        chart_progressbar.setVisibility(View.GONE);
//                    }
//                    if (map.get("0") == null && textLayout.getVisibility() == View.GONE) {
//                        textLayout.setVisibility(View.VISIBLE);
//                    }
//                    canRefreshLine = true;
//                }
            }

            @Override
            public void onFailure(String msg) {

                if (load_animation.getVisibility() == View.VISIBLE) {
                    load_animation.setVisibility(View.GONE);
                }
                if (map.get("0") == null && textLayout.getVisibility() == View.GONE) {
                    textLayout.setVisibility(View.VISIBLE);
                }
            }
        };
        new GGOKHTTP(param, GGOKHTTP.STOCK_MINUTE, ggHttpInterface).startGet();
    }

    private void getFiveData() {
        final HashMap<String, String> param = new HashMap<>();
        param.put("stock_code", stockCode);
        param.put("day", "5");
        if (!AppDevice.isNetworkConnected(getThisContext())) {
            if (load_animation.getVisibility() == View.VISIBLE) {
                load_animation.setVisibility(View.GONE);
            }
            if (map.get("1") == null && textLayout.getVisibility() == View.GONE) {
                textLayout.setVisibility(View.VISIBLE);
            }
        }
        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {

                JSONObject result = JSONObject.parseObject(responseInfo);
                int code = result.getInteger("code");
                if (code == 0) {
                    new BitmapTask().execute("1", responseInfo);
                } else {
                    AppManager.getInstance().sendMessage("CopyStockDetailActivity", new BaseMessage("autoRefresh2"));
                }
            }

            @Override
            public void onFailure(String msg) {

                AppManager.getInstance().sendMessage("CopyStockDetailActivity", new BaseMessage("autoRefresh2"));
            }
        };
        new GGOKHTTP(param, GGOKHTTP.STOCK_MINUTE, ggHttpInterface).startGet();
    }

    private void getKLineData(final int displayIndex) {
        HashMap<String, String> param = new HashMap<>();
        param.put("kline_type", displayIndex + "");
        param.put("stock_code", stockCode);
        param.put("avg_line_type", dayK1 + ";" + dayK2 + ";" + dayK3);
        param.put("page", "1");
        param.put("rows", "100");

        if (!AppDevice.isNetworkConnected(getThisContext())) {
            if (load_animation.getVisibility() == View.VISIBLE) {
                load_animation.setVisibility(View.GONE);
            }
            if (displayIndex == 0 && map.get("2") == null && textLayout.getVisibility() == View.GONE) {
                textLayout.setVisibility(View.VISIBLE);
            } else if (displayIndex == 1 && map.get("3") == null && textLayout.getVisibility() == View.GONE) {
                textLayout.setVisibility(View.VISIBLE);
            } else if (displayIndex == 2 && map.get("4") == null && textLayout.getVisibility() == View.GONE) {
                textLayout.setVisibility(View.VISIBLE);
            }
        }

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {

                JSONObject result = JSONObject.parseObject(responseInfo);
                int code = result.getInteger("code");
                if (code == 0) {
                    new BitmapTask().execute(String.valueOf(displayIndex + 2), responseInfo);
                } else {
                    AppManager.getInstance().sendMessage("CopyStockDetailActivity", new BaseMessage("autoRefresh3", displayIndex + ""));
                }
            }

            @Override
            public void onFailure(String msg) {
            }
        };
        new GGOKHTTP(param, GGOKHTTP.STOCK_K_LINE, ggHttpInterface).startGet();

    }

    private void setMinLineStatu(int type) {
        if (type == 0) {
            load_animation.setVisibility(View.VISIBLE);
        } else if (type == 1) {
            load_animation.setVisibility(View.GONE);
        }
//        minLineTv.setTextColor(getResColor(R.color.red));
//        dayKTv.setTextColor(getResColor(R.color.text_color_tab));
//        fiveKTv.setTextColor(getResColor(R.color.text_color_tab));
//        monthKTv.setTextColor(getResColor(R.color.text_color_tab));
//        WeekKTv.setTextColor(getResColor(R.color.text_color_tab));
//        minLine.setVisibility(View.VISIBLE);
//        fivedayLine.setVisibility(View.GONE);
//        daykLine.setVisibility(View.GONE);
//        weekkLine.setVisibility(View.GONE);
//        monthkLine.setVisibility(View.GONE);
    }

    private void setFiveStatu(int type) {
        if (type == 0) {
            load_animation.setVisibility(View.VISIBLE);
        } else if (type == 1) {
            load_animation.setVisibility(View.GONE);
        }
//        minLineTv.setTextColor(getResColor(R.color.text_color_tab));
//        dayKTv.setTextColor(getResColor(R.color.text_color_tab));
//        fiveKTv.setTextColor(getResColor(R.color.red));
//        monthKTv.setTextColor(getResColor(R.color.text_color_tab));
//        WeekKTv.setTextColor(getResColor(R.color.text_color_tab));
//        minLine.setVisibility(View.GONE);
//        fivedayLine.setVisibility(View.VISIBLE);
//        daykLine.setVisibility(View.GONE);
//        weekkLine.setVisibility(View.GONE);
//        monthkLine.setVisibility(View.GONE);
    }

    private void setDayKStatue(int type) {
        if (type == 0) {
            load_animation.setVisibility(View.VISIBLE);
        } else if (type == 1) {
            load_animation.setVisibility(View.GONE);
        }
//        minLineTv.setTextColor(getResColor(R.color.text_color_tab));
//        dayKTv.setTextColor(getResColor(R.color.red));
//        fiveKTv.setTextColor(getResColor(R.color.text_color_tab));
//        monthKTv.setTextColor(getResColor(R.color.text_color_tab));
//        WeekKTv.setTextColor(getResColor(R.color.text_color_tab));
//        minLine.setVisibility(View.GONE);
//        fivedayLine.setVisibility(View.GONE);
//        daykLine.setVisibility(View.VISIBLE);
//        weekkLine.setVisibility(View.GONE);
//        monthkLine.setVisibility(View.GONE);
    }

    private void setWeekLineStatu(int type) {
        if (type == 0) {
            load_animation.setVisibility(View.VISIBLE);
        } else if (type == 1) {
            load_animation.setVisibility(View.GONE);
        }
//        minLineTv.setTextColor(getResColor(R.color.text_color_tab));
//        dayKTv.setTextColor(getResColor(R.color.text_color_tab));
//        fiveKTv.setTextColor(getResColor(R.color.text_color_tab));
//        monthKTv.setTextColor(getResColor(R.color.text_color_tab));
//        WeekKTv.setTextColor(getResColor(R.color.red));
//        minLine.setVisibility(View.GONE);
//        fivedayLine.setVisibility(View.GONE);
//        daykLine.setVisibility(View.GONE);
//        weekkLine.setVisibility(View.VISIBLE);
//        monthkLine.setVisibility(View.GONE);
    }

    private void setMonthLineStatu(int type) {
        if (type == 0) {
            load_animation.setVisibility(View.VISIBLE);
        } else if (type == 1) {
            load_animation.setVisibility(View.GONE);
        }
//        minLineTv.setTextColor(getResColor(R.color.text_color_tab));
//        dayKTv.setTextColor(getResColor(R.color.text_color_tab));
//        fiveKTv.setTextColor(getResColor(R.color.text_color_tab));
//        monthKTv.setTextColor(getResColor(R.color.red));
//        WeekKTv.setTextColor(getResColor(R.color.text_color_tab));
//        minLine.setVisibility(View.GONE);
//        fivedayLine.setVisibility(View.GONE);
//        daykLine.setVisibility(View.GONE);
//        weekkLine.setVisibility(View.GONE);
//        monthkLine.setVisibility(View.VISIBLE);
    }

    //初始化下拉刷新样式
    private void initRefreshStyle(int color) {
        int c = getResColor(color);
//        headerView.setBackgroundColor(c);
        ptrFrame.setBackgroundColor(c);
    }

    //定时自动刷新
    private void AutoRefresh(long time) {
        if (StockUtils.isTradeTime()) {
            setTime(time);
        } else {
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
        }
    }

    public void setTime(long num) {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                AppManager.getInstance().sendMessage("CopyStockDetailActivity", new BaseMessage("autoRefresh1"));
            }
        };
        if (timer == null) {
            timer = new Timer();
        }
        timer.scheduleAtFixedRate(timerTask, num, num);
    }

    @Subscriber(tag = "CopyStockDetailActivity")
    private void refresh(BaseMessage s) {
        switch (s.getCode()) {
            case "autoRefresh1":
                if (StockUtils.isTradeTime()) {
                    refreshAll();
                } else {
                    if (timer != null) {
                        timer.cancel();
                        timer = null;
                    }
                }
                break;
            case "autoRefresh2":
                if (load_animation.getVisibility() == View.VISIBLE) {
                    load_animation.setVisibility(View.GONE);
                }
                if (map.get("1") == null && textLayout.getVisibility() == View.GONE) {
                    textLayout.setVisibility(View.VISIBLE);
                }
                break;
            case "autoRefresh3":
                if (load_animation.getVisibility() == View.VISIBLE) {
                    load_animation.setVisibility(View.GONE);
                }
                if ("0".equals(s.getMsg()) && map.get("2") == null && textLayout.getVisibility() == View.GONE) {
                    textLayout.setVisibility(View.VISIBLE);
                } else if ("1".equals(s.getMsg()) && map.get("3") == null && textLayout.getVisibility() == View.GONE) {
                    textLayout.setVisibility(View.VISIBLE);
                } else if ("2".equals(s.getMsg()) && map.get("4") == null && textLayout.getVisibility() == View.GONE) {
                    textLayout.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        long refreshtime = SPTools.getLong("interval_time", 15000);
        width = AppDevice.getWidth(getThisContext()) - AppDevice.dp2px(getThisContext(), 22);
        height = AppDevice.dp2px(getThisContext(), 190);
        timer = new Timer();
        AutoRefresh(refreshtime);

        unReadCount = MessageListUtils.getAllMessageUnreadCount();
        badge.setBadgeNumber(unReadCount);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (timer != null) {
            timer.cancel();
        }
    }

    /**
     * 初始化数据
     *
     * @param stockCode;
     */
    private void initList(final String stockCode) {
        final Map<String, String> param = new HashMap<String, String>();
        param.put("stock_code", stockCode);
        GGOKHTTP.GGHttpInterface httpInterface = new GGOKHTTP.GGHttpInterface() {

            @Override
            public void onSuccess(String responseInfo) {

                StockDetail bean = JSONObject.parseObject(responseInfo, StockDetail.class);

                if (bean.getCode() == 0) {

                    final TreatData info = bean.getData();

                    //20170613
                    infoDialogAdapter.setClosePrice(info.getClose_price());
                    infoDialogAdapter.setStockType(info.getStock_type());
                    setDialogInfoData(info);

                    //保存收盘价
                    stock_charge_type = info.getStock_type();


                    closePrice = hply.com.niugu.StringUtils.getDouble(String.valueOf(info.getClose_price()));
                    change_value = info.getChange_value();
                    StockUtils.savaColseprice((float) closePrice);
                    priceVolumDatas.clear();
                    //卖
                    priceVolumDatas.add(StringUtils.pareseStringDouble(info.getSell5_price(), 2));
                    priceVolumDatas.add(info.getSell5_volume() + "");
                    priceVolumDatas.add(StringUtils.pareseStringDouble(info.getSell4_price(), 2));
                    priceVolumDatas.add(info.getSell4_volume() + "");
                    priceVolumDatas.add(StringUtils.pareseStringDouble(info.getSell3_price(), 2));
                    priceVolumDatas.add(info.getSell3_volume() + "");
                    priceVolumDatas.add(StringUtils.pareseStringDouble(info.getSell2_price(), 2));
                    priceVolumDatas.add(info.getSell2_volume() + "");
                    priceVolumDatas.add(StringUtils.pareseStringDouble(info.getSell1_price(), 2));
                    priceVolumDatas.add(info.getSell1_volume() + "");
                    //买
                    priceVolumDatas.add(StringUtils.pareseStringDouble(info.getBuy1_price(), 2));
                    priceVolumDatas.add(info.getBuy1_volume() + "");
                    priceVolumDatas.add(StringUtils.pareseStringDouble(info.getBuy2_price(), 2));
                    priceVolumDatas.add(info.getBuy2_volume() + "");
                    priceVolumDatas.add(StringUtils.pareseStringDouble(info.getBuy3_price(), 2));
                    priceVolumDatas.add(info.getBuy3_volume() + "");
                    priceVolumDatas.add(StringUtils.pareseStringDouble(info.getBuy4_price(), 2));
                    priceVolumDatas.add(info.getBuy4_volume() + "");
                    priceVolumDatas.add(StringUtils.pareseStringDouble(info.getBuy5_price(), 2));
                    priceVolumDatas.add(info.getBuy5_volume() + "");
                    stock_price.setText(StringUtils.pareseStringDouble(info.getPrice(), 2));//股票价格
                    stock_start.setText(StringUtils.pareseStringDouble(info.getOpen_price(), 2));//开盘价

                    //涨跌
                    if (StringUtils.pareseStringDouble(info.getChange_value()) > 0) {
                        setStatusColorId(R.color.header_red);
                        relative_header.setBackgroundColor(getResColor(R.color.header_red));
                        linear_header.setBackgroundColor(getResColor(R.color.header_red));
                        initRefreshStyle(R.color.header_red);
                        stock_detail_tv1.setText("+" + StringUtils.pareseStringDouble(info.getChange_value(), 2));
                        stock_detail_tv2.setText("+" + StringUtils.pareseStringDouble(info.getChange_rate(), 2) + "%");
                    } else if (StringUtils.pareseStringDouble(info.getChange_value()) < 0) {
                        setStatusColorId(R.color.header_green);
                        relative_header.setBackgroundColor(getResColor(R.color.header_green));
                        linear_header.setBackgroundColor(getResColor(R.color.header_green));
                        initRefreshStyle(R.color.header_green);
                        stock_detail_tv1.setText(StringUtils.pareseStringDouble(info.getChange_value(), 2));
                        stock_detail_tv2.setText(StringUtils.pareseStringDouble(info.getChange_rate(), 2) + "%");
                    } else {
                        setStatusColorId(R.color.header_gray);
                        relative_header.setBackgroundColor(getResColor(R.color.header_gray));
                        linear_header.setBackgroundColor(getResColor(R.color.header_gray));
                        initRefreshStyle(R.color.header_gray);
                        stock_detail_tv1.setText(StringUtils.pareseStringDouble(info.getChange_value(), 2));
                        stock_detail_tv2.setText(StringUtils.pareseStringDouble(info.getChange_rate(), 2) + "%");
                    }

                    if (info.getVolume() == null) {
                        stock_volume.setText("0手");//成交量
                    } else {
                        if (info.getVolume().length() > 6) {

                            stock_volume.setText(StringUtils.save2Significand(StringUtils.pareseStringDouble(info.getVolume()) / 1000000) + "万手");//成交量
                        } else {
                            String volume = String.valueOf((Double.parseDouble(info.getVolume()) / 100));
                            stock_volume.setText(StringUtils.getIntegerData(volume) + "手");
                        }
                    }
                    stock_turnover_rate.setText(StringUtils.save2Significand(
                            StringUtils.pareseStringDouble(info.getTurnover_rate()) * 100) + "%");//换手率
                    if (stock_charge_type == 1) {
                        text_state.setVisibility(View.VISIBLE);
                        text_delist.setVisibility(View.GONE);
                        stock_detail_tv1.setVisibility(View.VISIBLE);
                        stock_detail_tv2.setVisibility(View.VISIBLE);
                        stock_detail_delist.setVisibility(View.GONE);
                    } else {
                        //停牌处理
                        text_state.setVisibility(View.GONE);
                        text_delist.setVisibility(View.VISIBLE);
                        text_delist.setText(StringUtils.save2Significand(info.getClose_price()) + " " + StockUtils.getStockStatus(stock_charge_type));
                        stock_detail_tv1.setVisibility(View.GONE);
                        stock_detail_tv2.setVisibility(View.GONE);
                        stock_detail_delist.setVisibility(View.VISIBLE);
                        stock_detail_delist.setText(StockUtils.getStockStatus(stock_charge_type));
                    }
                    stock_close.setText(StringUtils.save2Significand(info.getClose_price()));//收盘价
                    //监听当前滚动位置，动态改变状态

                    scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                        @Override
                        public void onScrollChanged() {
                            int[] location = new int[2];
                            linear_header.getLocationInWindow(location);
                            int x = location[0];
                            int y = location[1];
                            if (y > pixels) {
                                StockState();
                            } else {
                                String result = StringUtils.save2Significand(info.getPrice()) + "  "
                                        + StringUtils.save2Significand(info.getChange_value()) + "  "
                                        + StringUtils.save2Significand(info.getChange_rate()) + "%";

                                text_state.setText(result);
                            }
                            scrollView.getViewTreeObserver().removeOnScrollChangedListener(this);
                        }
                    });

                    //图标点击事件
                    mBitmapChartView.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (stockCode != null && stockName != null && info != null) {
                                Intent intent = new Intent(getThisContext(), StockDetailChartsActivity.class);
                                Bundle bundle = new Bundle();
                                intent.putExtras(bundle);
                                intent.putExtra("position", showItem);
                                intent.putExtra("closePrice", closePrice);
                                intent.putStringArrayListExtra("priceVolumDatas", priceVolumDatas);
                                intent.putExtra("stockCode", stockCode);
                                intent.putExtra("stockName", stockName);
                                intent.putExtra("price", info.getPrice());
                                intent.putExtra("volume", info.getVolume());
                                intent.putExtra("time", info.getUpdate_time());
                                intent.putExtra("stockType", StockDetailChartsActivity.STOCK_COMMON);
                                intent.putExtra("stock_charge_type", stock_charge_type);
                                startActivity(intent);
                            }
                        }
                    });

                    //退市数据处理 停牌 停市 未上市
                    if (stock_charge_type == 0 || stock_charge_type == -1 || stock_charge_type == -2) {//退市/未上市
                        setZero2Line(stock_price);
                        setZero2Line(stock_start);
                        setZero2Line(stock_close);
                        setZero2Line(stock_volume);
                        setZero2Line(stock_turnover_rate);
                    }
                }
                stopAnimation();
            }

            @Override
            public void onFailure(String msg) {
                stopAnimation();
                UIHelper.toast(getApplicationContext(), "请检查网络");
                KLog.e(msg);
            }
        };
        new GGOKHTTP(param, GGOKHTTP.ONE_STOCK_DETAIL, httpInterface).startGet();
    }

    private double $(String volume_outer) {
        return StringUtils.pareseStringDouble(volume_outer);
    }

    private void setZero2Line(TextView tv) {
        try {
            String text = tv.getText().toString();
            if (!TextUtils.isEmpty(text)) {
                if (Double.parseDouble(text) == 0 || text.equals("0手") || text.equals("0.00%")) {
                    tv.setText("--");
                }
            }
        } catch (Exception e) {
            tv.setText("--");
        }
    }

    //股票状态
    private void StockState() {
        SimpleDateFormat df = new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA);
        text_state.setText(StockUtils.getTreatState() + " " + df.format(new Date()));
        text_state.setTextColor(Color.WHITE);
        text_state.setAlpha((float) 0.7);
    }

    //异步画图
    class BitmapTask extends AsyncTask<String, Void, Bitmap> {
        private String item_index;

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bitmap = null;
            item_index = params[0];
            switch (item_index) {
                case "0":
                    timesBitmap = new TimesFivesBitmap(13 * width / 20, height);
                    timesBitmap.setShowDetail(false);
                    timesBitmap.setLongitudeNum(0);
                    timesBitmap.setmSize(AppDevice.dp2px(CopyStockDetailActivity.this, 1));
                    timesBitmap.setmSpaceSize(AppDevice.dp2px(CopyStockDetailActivity.this, 3));
                    timesBitmap.setmAxisTitleSize(AppDevice.dp2px(CopyStockDetailActivity.this, 10));
                    try {
                        bitmap = timesBitmap.setTimesList(timesBean, true, stock_charge_type);
                    } catch (Exception e) {
                    }
                    map.put(item_index, bitmap);
                    break;
                case "1":
                    if (params.length > 1) {
                        StockMinuteBean bean = JSONObject.parseObject(params[1], StockMinuteBean.class);
                        fiveDayBitmap = new TimesFivesBitmap(width, height);
                        fiveDayBitmap.setShowDetail(false);
                        fiveDayBitmap.setLongitudeNum(4);
                        fiveDayBitmap.setmSpaceSize(AppDevice.dp2px(CopyStockDetailActivity.this, 3));
                        fiveDayBitmap.setmSize(AppDevice.dp2px(CopyStockDetailActivity.this, 1));
                        fiveDayBitmap.setmAxisTitleSize(AppDevice.dp2px(CopyStockDetailActivity.this, 10));

                        bitmap = fiveDayBitmap.setTimesList(bean, false, stock_charge_type);
                        map.put(item_index, bitmap);
                    }
                    break;
                case "2":
                    if (params.length > 1) {
                        mOHLCData.clear();
                        parseObjects(params[1]);
                        KChartsBitmap kChartsBitmap = new KChartsBitmap(width, height);
                        kChartsBitmap.setShowDetail(false);
                        kChartsBitmap.setLongitudeNum(1);
                        kChartsBitmap.setmSpaceSize(AppDevice.dp2px(CopyStockDetailActivity.this, 3));
                        kChartsBitmap.setmAxisTitleSize(AppDevice.dp2px(CopyStockDetailActivity.this, 10));
                        kChartsBitmap.setmSize(AppDevice.dp2px(CopyStockDetailActivity.this, 1));

                        bitmap = kChartsBitmap.setOHLCData(mOHLCData);
                        map.put(item_index, bitmap);
                    }
                    break;
                case "3":
                    if (params.length > 1) {
                        mOHLCData.clear();
                        parseObjects(params[1]);
                        KChartsBitmap kChartsBitmap1 = new KChartsBitmap(width, height);
                        kChartsBitmap1.setShowDetail(false);
                        kChartsBitmap1.setLongitudeNum(1);
                        kChartsBitmap1.setmSpaceSize(AppDevice.dp2px(CopyStockDetailActivity.this, 3));
                        kChartsBitmap1.setmAxisTitleSize(AppDevice.dp2px(CopyStockDetailActivity.this, 10));
                        kChartsBitmap1.setmSize(AppDevice.dp2px(CopyStockDetailActivity.this, 1));

                        bitmap = kChartsBitmap1.setOHLCData(mOHLCData);
                        map.put(item_index, bitmap);
                    }
                    break;
                case "4":
                    if (params.length > 1) {
                        mOHLCData.clear();
                        parseObjects(params[1]);
                        KChartsBitmap kChartsBitmap2 = new KChartsBitmap(width, height);
                        kChartsBitmap2.setShowDetail(false);
                        kChartsBitmap2.setLongitudeNum(1);
                        kChartsBitmap2.setmSpaceSize(AppDevice.dp2px(CopyStockDetailActivity.this, 3));
                        kChartsBitmap2.setmAxisTitleSize(AppDevice.dp2px(CopyStockDetailActivity.this, 10));
                        kChartsBitmap2.setmSize(AppDevice.dp2px(CopyStockDetailActivity.this, 1));

                        bitmap = kChartsBitmap2.setOHLCData(mOHLCData);
                        map.put(item_index, bitmap);
                    }
                    break;
            }
            return map.get(item_index);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            switch (String.valueOf(showItem)) {
                case "0":
                    if (stock_charge_type == 1 && StockUtils.isTradeTime()) {
                        mBitmapChartView.setBitmap(map.get(String.valueOf("0")), true, timesBitmap);
                    } else {
                        mBitmapChartView.setBitmap(map.get(String.valueOf("0")), false, timesBitmap);
                    }
                    break;
                case "1":
                    if (stock_charge_type == 1 && StockUtils.isTradeTime()) {
                        mBitmapChartView.setBitmap(map.get(String.valueOf("1")), true, fiveDayBitmap);
                    } else {
                        mBitmapChartView.setBitmap(map.get(String.valueOf("1")), false, fiveDayBitmap);
                    }
                    break;
                case "2":
                    mBitmapChartView.setBitmap(map.get(String.valueOf("2")));
                    break;
                case "3":
                    mBitmapChartView.setBitmap(map.get(String.valueOf("3")));
                    break;
                case "4":
                    mBitmapChartView.setBitmap(map.get(String.valueOf("4")));
                    break;
            }
            if (load_animation.getVisibility() == View.VISIBLE) {
                load_animation.setVisibility(View.GONE);
            }
        }
    }

    //popu，工具箱，股票
    @OnClick({R.id.iv_show_info_dialog,
            R.id.tv_stockDetail_tools, R.id.tv_stockDetail_stockCircle,
            R.id.tv_stockDetail_interactiveinvestor, R.id.tv_stockDetail_toggle_mystock})
    public void allBtnClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.iv_show_info_dialog://交易popu
                if (isMaskViewVisiable()) {
                    dismissMarket();
                } else {
                    showStockInfoDialog();
                }
                break;

            case R.id.tv_stockDetail_tools://工具箱
                StockPopuDialog.newInstance(getStockBean()).show(getSupportFragmentManager());
                UserUtils.getAllMyTools(new Impl<JSONArray>() {
                    @Override
                    public void response(int code, JSONArray data) {
                        KLog.e(data);
                    }
                });

                break;

            case R.id.tv_stockDetail_toggle_mystock://tog 加减自选股
                addOptionalShare();//TODO 更换新的删除自选股接口
                break;
            case R.id.tv_stockDetail_stockCircle://股票圈，进入后台群
                getGroupChartConvsation();
                break;
            case R.id.tv_stockDetail_interactiveinvestor://投资者互动
                intent.setClass(v.getContext(), InteractiveInvestorActivity.class);
                intent.putExtra("stock_info", getStockBean());
                startActivity(intent);
                break;
        }
    }

    private void getGroupChartConvsation() {
        final WaitDialog waitDialog = WaitDialog.getInstance("请稍后...", R.mipmap.login_loading, true);
        waitDialog.show(getSupportFragmentManager());

        HashMap<String, String> params = UserUtils.getTokenParams();
        params.put("stock_code", stockCode);
        new GGOKHTTP(params, GGOKHTTP.GET_STOCK_GROUP_ID, new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                KLog.e(responseInfo);
                int code = JSONObject.parseObject(responseInfo).getIntValue("code");
                if (code == 0) {
                    try {
                        final String conversationId = JSONObject.parseObject(responseInfo).
                                getJSONObject("data").getString("conv_id");

                        List<Integer> addIdList = new ArrayList<>();
                        addIdList.add(Integer.parseInt(UserUtils.getMyAccountId()));
                        //添加群成员
                        ChatGroupHelper.addAnyone(addIdList, conversationId, new ChatGroupHelper.ChatGroupManager() {
                            @Override
                            public void groupActionSuccess(JSONObject object) {
                                Intent intent = new Intent(getActivity(), SquareChatRoomActivity.class);
                                intent.putExtra("conversation_id", conversationId);
                                intent.putExtra("need_update", false);
                                startActivity(intent);
                            }

                            @Override
                            public void groupActionFail(String error) {
                                UIHelper.toast(getActivity(), "股票群初始化失败，请稍后重试！");
                                Log.e("TAG", error);
                            }
                        });

                    } catch (Exception e) {
                        //返回数据异常
                        UIHelper.toast(getActivity(), "股票群初始化失败，请稍后重试！");
                        Log.e("TAG", e.getMessage());
                    }
                } else {//没有获取到ConvsationId
                    UIHelper.toast(getActivity(), "股票群初始化失败，请稍后重试！");
                }
                waitDialog.dismiss();
            }

            @Override
            public void onFailure(String msg) {//获取ConvsationId出错
                waitDialog.dismiss();
                UIHelper.toast(getActivity(), "请求异常");
            }
        }).startGet();
    }

    private Stock getStockBean() {
        Stock stock = new Stock(stockCode, stockName);
        stock.setChangeValue(StringUtils.save2Significand(change_value));
        stock.setStock_charge_type(stock_charge_type);
        stock.setClosePrice(closePrice);
        return stock;
    }

    //添加自选股
    private void addOptionalShare() {
        final WaitDialog dialog = WaitDialog.getInstance("请稍后", R.mipmap.login_loading, true);
        dialog.show(getSupportFragmentManager());

        if (isChoose) {
            final Map<String, String> param = new HashMap<String, String>();
            param.put("token", UserUtils.getToken());
            param.put("stock_code", stockCode);

            GGOKHTTP.GGHttpInterface httpInterface = new GGOKHTTP.GGHttpInterface() {
                @Override
                public void onSuccess(String responseInfo) {
                    JSONObject result = JSONObject.parseObject(responseInfo);
                    int data = (int) result.get("code");
                    if (data == 0) {
                        toggleIsMyStock(false, true);
                        StockUtils.removeStock(stockCode);
                    }
                    dialog.dismiss();
                }

                @Override
                public void onFailure(String msg) {
                    dialog.dismiss();
                    UIHelper.toastError(getThisContext(), msg);
                }
            };
            new GGOKHTTP(param, GGOKHTTP.MYSTOCK_DELETE, httpInterface).startGet();
        } else {
            final Map<String, String> param = new HashMap<String, String>();
            param.put("token", UserUtils.getToken());
            param.put("group_id", "0");
            param.put("stock_code", stockCode);
            param.put("stock_class", "0");
            param.put("source", "9");
            param.put("group_class", "1");

            GGOKHTTP.GGHttpInterface httpInterface = new GGOKHTTP.GGHttpInterface() {
                @Override
                public void onSuccess(String responseInfo) {
                    JSONObject result = JSONObject.parseObject(responseInfo);

                    int data = (int) result.get("code");
                    if (data == 0) {
                        toggleIsMyStock(true, true);
                        StockUtils.addStock2MyStock(stockCode);
                    }
                    dialog.dismiss();
                }

                @Override
                public void onFailure(String msg) {
                    dialog.dismiss();
                    UIHelper.toastError(getThisContext(), msg);
                }
            };
            new GGOKHTTP(param, GGOKHTTP.MYSTOCK_ADD, httpInterface).startGet();
        }
    }

    public void toggleIsMyStock(boolean addMyStock, boolean needToast) {

        Drawable drawable = ContextCompat.getDrawable(this,
                addMyStock ? R.mipmap.not_choose_stock : R.mipmap.choose_stock);

        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());

        stock_detail_choose.setCompoundDrawables(null, drawable, null, null);
//
        stock_detail_choose.setText(addMyStock ? "已自选" : "加自选");

        if (needToast) {
            UIHelper.toast(getThisContext(), addMyStock ? "添加自选成功" : "删除自选成功");
        }

        isChoose = addMyStock;
    }


    private void parseObjects(String params) {
        JSONObject result = JSONObject.parseObject(params);
        if (result != null) {
            JSONArray data_array = (JSONArray) result.get("data");
            if ((int) result.get("code") == 0) {
                handleData(data_array);
            }
        }
    }

    private void handleData(JSONArray data) {
        for (int i = 0; i < data.size(); i++) {
            com.alibaba.fastjson.JSONObject singleData = (com.alibaba.fastjson.JSONObject) data.get(i);
            Map<String, Object> itemData = new HashMap<String, Object>();
            itemData.put("amplitude", singleData.getFloat("amplitude"));
            itemData.put("avg_price_" + dayK1, ParseNum(singleData.getString("avg_price_" + dayK1)));
            itemData.put("avg_price_" + dayK2, ParseNum(singleData.getString("avg_price_" + dayK2)));
            itemData.put("avg_price_" + dayK3, ParseNum(singleData.getString("avg_price_" + dayK3)));
            itemData.put("avg_price_" + dayK4, ParseNum(singleData.getString("avg_price_" + dayK4)));
            itemData.put("close_price", ParseNum(singleData.getString("close_price")));
            itemData.put("date", singleData.getString("date"));
            itemData.put("high_price", ParseNum(singleData.getString("high_price")));
            itemData.put("low_price", ParseNum(singleData.getString("low_price")));
            itemData.put("open_price", ParseNum(singleData.getString("open_price")));
            itemData.put("price_change", ParseNum(singleData.getString("price_change")));
            itemData.put("price_change_rate", ParseNum(singleData.getString("price_change_rate")));
            itemData.put("rightValue", ParseNum(singleData.getString("rightValue")));
            itemData.put("turnover", ParseNum(singleData.getString("turnover")));
            itemData.put("turnover_rate", ParseNum(singleData.getString("turnover_rate")));
            itemData.put("volume", ParseNum(singleData.getString("volume")));
            mOHLCData.add(itemData);
        }
    }

    private float ParseNum(String s) {
        float avg_price = 0.0f;
        if (s == null) {
            avg_price = 0.0f;
        } else {
            avg_price = Float.parseFloat(s);
        }
        return avg_price;
    }

    /*五档、明细切换*/
    public void toggleTreatMode() {
        if (tabLayoutTreat.getTabAt(0).isSelected()) {
            tabLayoutTreat.getTabAt(1).select();
        } else {
            tabLayoutTreat.getTabAt(0).select();
        }
    }

    //==============================================20170613=================================

    //初始化
    private void getStockInfoDialog() {
        rvStockInfo.setLayoutManager(new
                GridLayoutManager(getActivity(), 4, GridLayoutManager.VERTICAL, false));
        stockDialogInfoList = new ArrayList<>();
        infoDialogAdapter = new StockInfoDialogAdapter(stockDialogInfoList);
        rvStockInfo.setAdapter(infoDialogAdapter);
    }

    /**
     * 设置-修改个股详情为弹窗
     */
    private void setDialogInfoData(TreatData info) {
        stockDialogInfoList.clear();
        //最高价
        stockDialogInfoList.add(new StockDialogInfo(stockDetailInfos[0],
                StringUtils.pareseStringDouble(info.getHigh_price(), 2)));

        //最低价
        stockDialogInfoList.add(new StockDialogInfo(stockDetailInfos[1],
                StringUtils.pareseStringDouble(info.getLow_price(), 2)));

        //涨停
        stockDialogInfoList.add(new StockDialogInfo(stockDetailInfos[2],
                "" + StringUtils.save2Significand(StringUtils.pareseStringDouble(info.getClose_price()) * (
                        (stockName.startsWith("*") ||
                                stockName.contains("ST") ||
                                stockName.startsWith("N")) ? 1.05 : 1.10))));

        //跌停
        stockDialogInfoList.add(new StockDialogInfo(stockDetailInfos[3],
                "" + StringUtils.save2Significand(StringUtils.pareseStringDouble(info.getClose_price()) * (
                        (stockName.startsWith("*") ||
                                stockName.contains("ST") ||
                                stockName.startsWith("N")) ? 0.95 : 0.9))));

        //内盘
        stockDialogInfoList.add(new StockDialogInfo(stockDetailInfos[4],
                (StringUtils.isActuallyEmpty(info.getVolume_inner()) || info.getVolume_inner().length() < 3) ? "0手" : (
                        info.getVolume_inner().length() > 6 ? StringUtils.save2Significand((Double.parseDouble(info.getVolume_inner()) / 1000000)) + "万手" :
                                StringUtils.getIntegerData(String.valueOf((Double.parseDouble(info.getVolume_inner()) / 100))) + "手")));

        //外盘
        stockDialogInfoList.add(new StockDialogInfo(stockDetailInfos[5],
                (StringUtils.isActuallyEmpty(info.getVolume_outer()) || info.getVolume_outer().length() < 3) ? "0手" : (
                        info.getVolume_outer().length() > 6 ? StringUtils.save2Significand((Double.parseDouble(info.getVolume_outer()) / 1000000)) + "万手" :
                                StringUtils.getIntegerData(String.valueOf((Double.parseDouble(info.getVolume_outer()) / 100))) + "手")));

        //成交额
        String turnover = StringUtils.pareseStringDouble(info.getTurnover(), 2);
        stockDialogInfoList.add(new StockDialogInfo(stockDetailInfos[6],
                StringUtils.pareseStringDouble(info.getTurnover()) == 0 ? "0" :
                        turnover.length() <= 7 ? StringUtils.pareseStringDouble(info.getTurnover(), 2) + "万" :
                                StringUtils.save2Significand(StringUtils.pareseStringDouble(info.getTurnover()) / 10000) + "亿"));
        //振幅
        stockDialogInfoList.add(new StockDialogInfo(stockDetailInfos[7],
                StringUtils.pareseStringDouble(info.getAmplitude(), 2) + "%"));

        //委比
        stockDialogInfoList.add(new StockDialogInfo(stockDetailInfos[8],
                StringUtils.pareseStringDouble(info.getCommission_rate(), 2) + "%"));

        //量比
        stockDialogInfoList.add(new StockDialogInfo(stockDetailInfos[9],
                StringUtils.pareseStringDouble(info.getQuantity_ratio(), 2)));

        //流通市值
        String mCapString = StringUtils.pareseStringDouble(info.getMcap(), 2);
        stockDialogInfoList.add(new StockDialogInfo(stockDetailInfos[10],
                mCapString.length() > 11 ?
                        StringUtils.saveSignificand(StringUtils.pareseStringDouble(info.getMcap()) / 100000000d, 2) + "万亿" :
                        mCapString.length() > 8 ? StringUtils.saveSignificand(StringUtils.pareseStringDouble(info.getMcap()) / 10000d, 2) + "亿" :
                                mCapString + "万"));

        //总市值
        String mTcapString = StringUtils.pareseStringDouble(info.getTcap(), 2);
        stockDialogInfoList.add(new StockDialogInfo(stockDetailInfos[11],
                mTcapString.length() > 11 ?
                        StringUtils.saveSignificand(StringUtils.pareseStringDouble(info.getTcap()) / 100000000d, 2) + "万亿" :
                        mTcapString.length() > 8 ? StringUtils.saveSignificand(StringUtils.pareseStringDouble(info.getTcap()) / 10000d, 2) + "亿" :
                                mTcapString + "万"));

        //市盈率
        stockDialogInfoList.add(new StockDialogInfo(stockDetailInfos[12],
                StringUtils.pareseStringDouble(info.getPe_y1(), 2)));

        //市净率
        stockDialogInfoList.add(new StockDialogInfo(stockDetailInfos[13],
                StringUtils.pareseStringDouble(info.getPb_y1(), 2)));

        //每股收益
        stockDialogInfoList.add(new StockDialogInfo(stockDetailInfos[14],
                StringUtils.pareseStringDouble(info.getEps_y1(), 2)));

        //总股本
        String capital = StringUtils.pareseStringDouble(info.getCapital(), 2);
        stockDialogInfoList.add(new StockDialogInfo(stockDetailInfos[15],
                StringUtils.pareseStringDouble(info.getCapital()) == 0 ? "0.00" :
                        (capital.length() > 7 ? StringUtils.save2Significand(StringUtils.pareseStringDouble(info.getCapital()) / 10000) + "亿" :
                                StringUtils.pareseStringDouble(info.getCapital(), 2) + "万")));

        //流通股
        stockDialogInfoList.add(new StockDialogInfo(stockDetailInfos[16],
                StringUtils.pareseStringDouble(info.getNegotiable_capital()) == 0 ? "0.00" :
                        (StringUtils.pareseStringDouble(info.getNegotiable_capital(), 2).length() > 7 ? StringUtils.save2Significand(StringUtils.pareseStringDouble(info.getNegotiable_capital()) / 10000) + "亿" :
                                StringUtils.pareseStringDouble(info.getNegotiable_capital(), 2) + "万")));

        infoDialogAdapter.notifyDataSetChanged();
    }

    //显示弹窗
    private void showStockInfoDialog() {
        rvStockInfo.startAnimation(
                android.view.animation.AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_from_top));
        rvStockInfo.setVisibility(View.VISIBLE);

        functionViewMask(viewMask,true);
        functionViewMask(viewMaskBottom,true);

        //禁止滑动
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        //禁止下拉刷新
        ptrFrame.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        //禁止点击
        imageViewShoeDialog.setImageResource(R.mipmap.img_drop_up);
    }

    /**
     * 销毁指数[弹窗]
     */
    public void dismissMarket() {
        if (isMaskViewVisiable()) {
            rvStockInfo.setVisibility(View.GONE);
            rvStockInfo.startAnimation(
                    android.view.animation.AnimationUtils.loadAnimation(getActivity(), R.anim.slide_out_from_top));

            functionViewMask(viewMask,false);
            functionViewMask(viewMaskBottom,false);

            scrollView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return false;
                }
            });
            ptrFrame.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return false;
                }
            });

            //恢复可点击
            imageViewShoeDialog.setImageResource(R.mipmap.img_drop_down);
        }
    }

    private void functionViewMask(final View viewMask,boolean show) {
//        viewMaskBottom
        viewMask.setEnabled(show);
        viewMask.setClickable(show);
        viewMask.setVisibility(show ? View.VISIBLE : View.GONE);
        viewMask.startAnimation(
                android.view.animation.AnimationUtils.loadAnimation(getActivity(),
                        show?R.anim.alpha_in:R.anim.alpha_out));

        //点击蒙版消失
        viewMask.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    dismissMarket();
                    viewMask.setEnabled(false);
                    viewMask.setClickable(false);
                }
                return true;
            }
        });

    }

    //弹窗是否可见
    public boolean isMaskViewVisiable() {
        return rvStockInfo.getVisibility() == View.VISIBLE;
    }

    private class StockInfoDialogAdapter extends CommonAdapter<StockDialogInfo, BaseViewHolder> {

        private String closePrice;

        /**
         * @serialField StockUtils.getStockStatus();
         */
        private int stockType;

        public void setStockType(int stockType) {
            this.stockType = stockType;
        }

        public void setClosePrice(String closePrice) {
            this.closePrice = closePrice;
        }

        private StockInfoDialogAdapter(List<StockDialogInfo> data) {
            super(R.layout.item_stock_detail_dialog_layout, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, StockDialogInfo data, int position) {
            holder.setBackgroundColor(R.id.item_stock_detail_dialog,
                    (position / 4) % 2 == 0 ? 0xffffffff : 0xfff0f4fa);

            holder.setText(R.id.tv_item_stock_detail_info_key, data.getKey());

            holder.setText(R.id.tv_item_stock_detail_info_value, (position == 0 || position == 1) ? (
                    realDouble(data.getValue()) == 0 ? "--" : data.getValue()) : data.getValue());

            switch (position) {
                case 0:
                case 1:
                    if (stockType == 1) {
                        holder.setTextColor(R.id.tv_item_stock_detail_info_value,
                                realDouble(data.getValue()) == 0 ? Color.BLACK : getResColor(
                                        StockUtils.getStockRateColor(realDouble(data.getValue()) -
                                                StringUtils.pareseStringDouble(closePrice))));
                    } else {
                        holder.setTextColor(R.id.tv_item_stock_detail_info_value, Color.BLACK);
                    }
                    break;
                case 2:
                    holder.setTextColor(R.id.tv_item_stock_detail_info_value, getResColor(R.color.stock_red));
                    break;
                case 3:
                    holder.setTextColor(R.id.tv_item_stock_detail_info_value, getResColor(R.color.stock_green));
                    break;
                case 4:
                    holder.setTextColor(R.id.tv_item_stock_detail_info_value, getResColor(
                            realDouble(data.getValue()) == 0 ? R.color.stock_gray : R.color.stock_green));
                    break;
                case 5:
                    holder.setTextColor(R.id.tv_item_stock_detail_info_value, getResColor(
                            realDouble(data.getValue()) == 0 ? R.color.stock_gray : R.color.stock_red));
                    break;
                case 8:
                    holder.setTextColor(R.id.tv_item_stock_detail_info_value,
                            getResColor(StockUtils.getStockRateColor(realDouble(data.getValue()))));
                    break;
                default:
                    holder.setTextColor(R.id.tv_item_stock_detail_info_value, Color.BLACK);
                    break;
            }

        }

        private double realDouble(String value) {
            return StringUtils.pareseStringDouble(value.replaceAll("[%手亿万]", ""));
        }
    }

    private void initBadge(int num, BadgeView badge) {
        badge.setGravityOffset(2, 5, true);
        badge.setShowShadow(false);
        badge.setBadgeGravity(Gravity.TOP | Gravity.END);
        badge.setBadgeTextSize(8, true);
        badge.bindTarget(ivMessageTag);
        badge.setBadgeNumber(num);
    }

    /**
     * 消息接收
     */
    @Subscriber(tag = "IM_Message")
    public void handleMessage(BaseMessage baseMessage) {
        unReadCount++;
        badge.setBadgeNumber(unReadCount);
    }

    private CopyStockDetailActivity getThisContext() {
        return CopyStockDetailActivity.this;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isMaskViewVisiable()) {
                dismissMarket();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}