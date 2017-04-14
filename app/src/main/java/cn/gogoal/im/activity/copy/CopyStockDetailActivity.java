package cn.gogoal.im.activity.copy;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hply.imagepicker.view.StatusBarUtil;

import org.simple.eventbus.Subscriber;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
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
import cn.gogoal.im.base.AppManager;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.bean.BaseMessage;
import cn.gogoal.im.bean.stock.ChartImageBean;
import cn.gogoal.im.bean.stock.StockDetail;
import cn.gogoal.im.common.AnimationUtils;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.SPTools;
import cn.gogoal.im.common.StockUtils;
import cn.gogoal.im.common.StringUtils;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.fragment.copy.StockDetailNewsFragment;
import cn.gogoal.im.fragment.stock.ImageChartFragment;
import hply.com.niugu.HeaderView;
import hply.com.niugu.autofixtext.AutofitTextView;
import hply.com.niugu.bean.TimeDetialBean;
import hply.com.niugu.bean.TimeDetialData;
import hply.com.niugu.stock.BitmapChartView;
import hply.com.niugu.stock.StockMinuteBean;
import hply.com.niugu.stock.TimesFivesBitmap;
import hply.com.niugu.view.KChartsBitmap;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;


/*
* 普通股票详情
* */
public class CopyStockDetailActivity extends BaseActivity implements OnClickListener {

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
    ScrollView scrollView;
    //股票价格
    @BindView(R.id.stock_price)
    AutofitTextView stock_price;
    //涨跌
    @BindView(R.id.stock_detail_tv1)
    AutofitTextView stock_detail_tv1;
    @BindView(R.id.stock_detail_tv2)
    AutofitTextView stock_detail_tv2;
    //今开
    @BindView(R.id.textView1)
    TextView textView1;
    @BindView(R.id.stock_start)
    AutofitTextView stock_start;
    //成交量
    @BindView(R.id.textView3)
    TextView textView3;
    @BindView(R.id.stock_volume)
    AutofitTextView stock_volume;
    //昨收
    @BindView(R.id.textView2)
    TextView textView2;
    @BindView(R.id.stock_close)
    AutofitTextView stock_close;
    //换手率
    @BindView(R.id.textView4)
    TextView textView4;
    @BindView(R.id.stock_turnover_rate)
    TextView stock_turnover_rate;
    //最高
    @BindView(R.id.stock_detail_tv3)
    TextView stock_detail_tv3;
    //内盘
    @BindView(R.id.stock_detail_tv4)
    TextView stock_detail_tv4;
    //市盈率
    @BindView(R.id.stock_detail_tv5)
    TextView stock_detail_tv5;
    //最低
    @BindView(R.id.stock_detail_tv6)
    AutofitTextView stock_detail_tv6;
    //外盘
    @BindView(R.id.stock_detail_tv7)
    TextView stock_detail_tv7;
    //振幅
    @BindView(R.id.stock_detail_tv8)
    AutofitTextView stock_detail_tv8;
    //成交额
    @BindView(R.id.stock_detail_tv9)
    AutofitTextView stock_detail_tv9;
    //总市值
    @BindView(R.id.stock_detail_tv10)
    TextView stock_detail_tv10;
    //流通市值
    @BindView(R.id.stock_detail_tv11)
    AutofitTextView stock_detail_tv11;
    //停牌
    @BindView(R.id.text_delist)
    TextView text_delist;
    @BindView(R.id.stock_detail_delist)
    TextView stock_detail_delist;

    //下拉刷新rootView
    @BindView(R.id.fragment_rotate_header_with_view_group_frame)
    PtrClassicFrameLayout ptrFrame;
    //下拉刷新头部控件
    private HeaderView headerView;
    private int stock_charge_type = 1;
    private double change_value;
    private double change_rate;

    //五个图的布局
    @BindView(R.id.charts_min_line_layout)
    RelativeLayout tiemsLayout;
    @BindView(R.id.charts_fiveday_k_layout)
    RelativeLayout fivedayLayout;
    @BindView(R.id.charts_day_k_layout)
    RelativeLayout daykLayout;
    @BindView(R.id.charts_week_k_layout)
    RelativeLayout weekkLayout;
    @BindView(R.id.charts_mouth_k_layout)
    RelativeLayout monthkLayout;

    //每个图表item
    @BindView(R.id.charts_min_line_tv)
    TextView minLineTv;
    @BindView(R.id.charts_day_k_tv)
    TextView dayKTv;
    @BindView(R.id.charts_fiveday_k_tv)
    TextView fiveKTv;
    @BindView(R.id.charts_mouth_k_tv)
    TextView monthKTv;
    @BindView(R.id.charts_week_k_tv)
    TextView WeekKTv;

    @BindView(R.id.tv_line_min)
    TextView minLine;
    @BindView(R.id.tv_line_dayk)
    TextView daykLine;
    @BindView(R.id.tv_line_fiveday)
    TextView fivedayLine;
    @BindView(R.id.tv_line_monthk)
    TextView monthkLine;
    @BindView(R.id.tv_line_weekk)
    TextView weekkLine;
    //页面加载动画
    @BindView(R.id.load_animation)
    RelativeLayout load_animation;

    @BindView(R.id.stockdetail_textview)
    LinearLayout textLayout;//无内容时布局

    @BindView(R.id.stock_detail_fragment_time_line)
    BitmapChartView mBitmapChartView;//图表控件

    //诊断
    @BindView(R.id.stock_detail_diagnose)
    RelativeLayout stock_detail_diagnose;
    //加自选
    @BindView(R.id.stock_detail_choose)
    RelativeLayout stock_detail_choose;

    @BindView(R.id.stock_detail_choose_iv)
    ImageView stock_detail_choose_iv;
    @BindView(R.id.stock_detail_choose_tv)
    TextView stock_detail_choose_tv;

    private int pixels = -85;
    private String stockName;
    private String stockCode;
    private List<String> priceVolumDatas = new ArrayList<String>();
    //定时刷新
    private Timer timer;
    private static final int refreshtime = 15000;
    //数据集合
    private List<Map<String, Object>> mOHLCData = new ArrayList<Map<String, Object>>();
    private double closePrice;
    private HashMap<String, Bitmap> map = new HashMap<>();
    //定时刷新
    private int position;
    private int showItem;
    private int width;
    private int height;
    private boolean canRefreshLine = true;

    private StockDetail.TreatData info;
    private boolean isChoose = true;
    private int dpi;

    private View view;
    private PopupWindow mPopupWindow;

    //k线数据设置
    private int dayk1;
    private int dayk2;
    private int dayk3;
    private int dayk4;

    private Fragment[] listDataFragment = null;
    private int currentIndex = 0;

    //图片表格
    @BindView(R.id.stock_no_data)
    LinearLayout layoutNoData;

    @BindView(R.id.tablayout_chatImg)
    TabLayout tabImageChart;
    @BindView(R.id.layout_imgchart_and_nodata)
    FrameLayout layoutImageChart;
    @BindView(R.id.smartImageView_chat)
    ViewPager vpImageChart;
    @BindArray(R.array.srock_chart_image)
    String[] arrStockChartImage;

    @BindView(R.id.rv_treat)
    RecyclerView rvTreat;//交易，买1~5、卖1~5

    //    @BindView(R.id.layout_imgchart_and_nodata)
//    FrameLayout layoutImgChart_Nodata;
    //修改的中间新闻模块
    @BindView(R.id.tablayout_news_)
    TabLayout tabLayoutNews;
    @BindView(R.id.vp_news_)
    ViewPager viewPagerNews;
    private String[] newTitles = {"新闻", "公告", "研报", "看点"};
    private List<TimeDetialData> timeDetialLists;
    private RotateAnimation rotateAnimation;

    @Override
    public int bindLayout() {
        return R.layout.copy_stock_detail;
    }

    @Override
    public void doBusiness(Context mContext) {
        //找控件
        findView();
//        //初始化
        init();

        setNewsTab();

        initList(stockCode);

        getImageChart();

        getStockTimeDetial(stockCode);

        onShow(showItem);


    }

    private void getImageChart() {
        Map<String, String> param = new HashMap<>();
        param.put("stock_code", stockCode);
        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {

                int code = JSONObject.parseObject(responseInfo).getIntValue("code");

                if (code == 0) {
                    layoutNoData.setVisibility(View.GONE);
                    final ChartImageBean.ChartImage chartImage = JSONObject.parseObject(responseInfo, ChartImageBean.class).getData();

                    vpImageChart.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
                        @Override
                        public Fragment getItem(int position) {
                            return ImageChartFragment.getInstance(position == 0 ? chartImage.getEps_img() : (
                                    position == 1 ? chartImage.getProfit_img() : chartImage.getPe_img()
                            ));
                        }

                        @Override
                        public int getCount() {
                            return 3;
                        }

                        @Override
                        public CharSequence getPageTitle(int position) {
                            return arrStockChartImage[position];
                        }
                    });
                    tabImageChart.setupWithViewPager(vpImageChart);

                } else {
                    layoutNoData.setVisibility(View.VISIBLE);
                }

            }

            public void onFailure(String msg) {

                UIHelper.toastError(getActivity(), msg);
                layoutNoData.setVisibility(View.VISIBLE);
            }
        };
        new GGOKHTTP(param, GGOKHTTP.DM_GET_IMG, ggHttpInterface).startGet();

    }

    @Override
    public void setStatusBar() {
    }

    /***/
    private void setNewsTab() {

        viewPagerNews.setOffscreenPageLimit(3);

        viewPagerNews.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            public Fragment getItem(int position) {
                return StockDetailNewsFragment.newInstance(position, newTitles[position]);
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

        setTabLayoutWidth(tabLayoutNews);
    }

    /**
     * 反射修改TabLayout的默认宽度
     */
    public void setTabLayoutWidth(TabLayout t) {
        try {
            Class<?> tablayout = t.getClass();
            Field tabStrip = tablayout.getDeclaredField("mTabStrip");
            tabStrip.setAccessible(true);
            LinearLayout ll_tab = (LinearLayout) tabStrip.get(t);
            for (int i = 0; i < ll_tab.getChildCount(); i++) {
                View child = ll_tab.getChildAt(i);
                child.setPadding(0, 0, 0, 0);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
                params.setMargins(AppDevice.dp2px(getActivity(), 10), 0, AppDevice.dp2px(getActivity(), 10), 0);
                child.setLayoutParams(params);
                child.invalidate();
            }
        } catch (Exception e) {
        }
    }

    private void findView() {
        scrollView.smoothScrollTo(0, 20);
        headerView = (HeaderView) LayoutInflater.from(this).inflate(R.layout.header_layout, null);

    }

    //初始化
    private void init() {
        AppDevice.setViewWidth$Height(layoutImageChart, -1, 350 * AppDevice.getWidth(getActivity()) / 560);

        dayk1 = SPTools.getInt("tv_ln1", 5);
        dayk2 = SPTools.getInt("tv_ln2", 10);
        dayk3 = SPTools.getInt("tv_ln3", 20);
        dayk4 = SPTools.getInt("tv_ln4", 0);
        stockCode = getIntent().getStringExtra("stock_code");
        stockName = getIntent().getStringExtra("stock_name");
        setStockCode(stockCode);
        setStockName(stockName);
        rvTreat.setVisibility(View.GONE);
        textHeadTitle.setText(stockName + "(" + stockCode + ")");
        dpi = AppDevice.getWidth(CopyStockDetailActivity.this);
        if (StockUtils.getMyStockSet() != null) {
            if (StockUtils.isMyStock(stockCode)) {
                stock_detail_choose_iv.setBackgroundResource(R.drawable.not_choose_stock);
                stock_detail_choose_tv.setText("已自选");
                isChoose = true;
            } else {
                stock_detail_choose_iv.setBackgroundResource(R.drawable.choose_stock);
                stock_detail_choose_tv.setText("加自选");
                isChoose = false;
            }
        }

        textView1.setAlpha((float) 0.7);
        textView2.setAlpha((float) 0.7);
        textView3.setAlpha((float) 0.7);
        textView4.setAlpha((float) 0.7);

        showItem = SPTools.getInt("showItem", 0);
        tiemsLayout.setOnClickListener(this);
        daykLayout.setOnClickListener(this);
        monthkLayout.setOnClickListener(this);
        weekkLayout.setOnClickListener(this);
        fivedayLayout.setOnClickListener(this);

        mBitmapChartView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (stockCode != null && stockName != null && info != null) {
                    Intent intent = new Intent(CopyStockDetailActivity.this, StockDetailChartsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("detialList", (Serializable) timeDetialLists);
                    intent.putExtras(bundle);
                    intent.putExtra("position", showItem);
                    intent.putExtra("closePrice", closePrice);
                    intent.putStringArrayListExtra("priceVolumDatas", (ArrayList<String>) priceVolumDatas);
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
                //通知嵌套的Fragment更新
                AppManager.getInstance().sendMessage("child_fragment_Refresh");
//                AppManager.sendMessage("StockDetailNewsFragment_Refresh");
            }
        });


        // 修改状态栏颜色
        StatusBarUtil.with(getActivity()).setColor(getResColor(R.color.header_gray));
        relative_header.setBackgroundResource(R.color.header_gray);
        linear_header.setBackgroundResource(R.color.header_gray);
        initRefreshStyle(R.color.header_gray);
        //股票状态和当前时间
        StockState();

        //设置下拉头部属性
        headerView.setFontColor(getResColor(R.color.white));
        headerView.setPullImage(R.mipmap.arrows_white);
        headerView.setLoadingImage(R.mipmap.loading_white);

        //设置下拉刷新属性
        ptrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                //刷新
                headerView.loading();

                if (canRefreshLine) {
                    StockState();
                    startAnimation();
                    initList(stockCode);
                    refreshChart(showItem);
                } else {
                    ptrFrame.refreshComplete();

                }
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }
        });

        ptrFrame.setHeaderView(headerView);
        ptrFrame.addPtrUIHandler(headerView);

        ptrFrame.setLastUpdateTimeRelateObject(this);

        // the following are default settings
        ptrFrame.setResistance(1.7f);
        ptrFrame.setRatioOfHeaderHeightToRefresh(1.5f);
        ptrFrame.setDurationToClose(200);
        ptrFrame.setDurationToCloseHeader(1000);
        // default is false
        ptrFrame.setPullToRefresh(false);
        // default is true
        ptrFrame.setKeepHeaderWhenRefresh(true);


    }

//    @Subscriber(tag = "check_net_work")
//    private void checkNet(String isnetwork) {
//        if ("yes".equals(isnetwork)) {
//            refreshAll();
//        }
//    }

    private void refreshAll() {
        startAnimation();
        initList(stockCode);
        refreshChart(showItem);
        new Thread(new Runnable() {
            @Override
            public void run() {
                getStockTimeDetial(stockCode);
            }
        }).start();
        setNewsTab();
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

    //分时数据交易明细
    private void getStockTimeDetial(final String stockCode) {
        HashMap<String, String> param = new HashMap<>();
        param.put("stock_code", stockCode);
        param.put("limit", "3");

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                parseTimeDetial(responseInfo);
            }

            public void onFailure(String msg) {
            }
        };
        new GGOKHTTP(param, GGOKHTTP.GET_STOCK_TIME_DETIAL, ggHttpInterface).startGet();
    }

    private void parseTimeDetial(String responseInfo) {
        timeDetialLists = JSONObject.parseObject(responseInfo, TimeDetialBean.class).getData();
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

//    private void initChartsData(String stockCode) {
//        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
//            @Override
//            public void onSuccess(String responseInfo) {
//                JSONObject result = JSONObject.parseObject(responseInfo);
//                Integer code = result.getInteger("code");
//                if (code == 0) {
//                    JSONObject data = result.getJSONObject("data");
//                    handleChartData(data);
//                } else {
//                    chars_progressbar.setVisibility(View.GONE);
//                    charts_title.setVisibility(View.GONE);
//                }
//            }
//
//            @Override
//            public void onFailure(String msg) {
//                chars_progressbar.setVisibility(View.GONE);
//                charts_title.setVisibility(View.GONE);
//            }
//        };
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("stock_code", stockCode);
//        new GGOKHTTP(params, GGOKHTTP.GET_CHARTS_DATA, ggHttpInterface).startGet();
//    }

//    private void handleChartData(JSONObject data) {
//
//        pie_data.clear();
//        bar_eps_list.clear();
//        bar_peg_list.clear();
//        bar_roe_list.clear();
//        if (data.getJSONArray("pie") != null) {
//            pie_data = data.getJSONArray("pie");
//        }
//        JSONArray bar_data = data.getJSONArray("bar");
//
//        if (bar_data != null) {
//            for (int i = 0; i < bar_data.size(); i++) {
//                JSONObject one = bar_data.getJSONObject(i);
//                Map<String, String> map_eps = new HashMap<String, String>();
//                map_eps.put("stock_name", one.getString("stock_name"));
//                map_eps.put("stock_code", one.getString("stock_code"));
//                map_eps.put("data", one.getString("eps"));
//                map_eps.put("year", one.getString("year"));
//                map_eps.put("type", "元");
//                map_eps.put("profit_type", one.getString("profit_type"));
//                bar_eps_list.add(map_eps);
//
//                Map<String, String> map_peg = new HashMap<String, String>();
//                map_peg.put("stock_name", one.getString("stock_name"));
//                map_peg.put("stock_code", one.getString("stock_code"));
//                map_peg.put("data", one.getString("pe"));
//                map_peg.put("year", one.getString("year"));
//                map_peg.put("type", "倍");
//                map_peg.put("profit_type", one.getString("profit_type"));
//                bar_peg_list.add(map_peg);
//
//                Map<String, String> map_roe = new HashMap<String, String>();
//                map_roe.put("stock_name", one.getString("stock_name"));
//                map_roe.put("stock_code", one.getString("stock_code"));
//                map_roe.put("data", one.getString("roe"));
//                map_roe.put("year", one.getString("year"));
//                map_roe.put("type", "%");
//                map_roe.put("profit_type", one.getString("profit_type"));
//                bar_roe_list.add(map_roe);
//            }
//        } else {
//            chars_progressbar.setVisibility(View.GONE);
//            charts_title.setVisibility(View.GONE);
//        }
//
//        if (bar_eps_list.size() > 0 && bar_eps_list != null) {
//            chars_progressbar.setVisibility(View.GONE);
//            mBarChart.setVisibility(View.VISIBLE);
//
//            charts_title.setVisibility(View.GONE);
//            stock_nodata.setVisibility(View.GONE);
//            charts_title.setVisibility(View.VISIBLE);
//
//            tv_eps.setTextColor(getResColor(R.color.red));
//            tv_line_eps.setVisibility(View.VISIBLE);
//            tv_pe.setTextColor(getResColor(R.color.text_color_tab));
//            tv_line_pe.setVisibility(View.GONE);
//            tv_return_equity.setTextColor(getResColor(R.color.text_color_tab));
//            tv_line_return_equity.setVisibility(View.GONE);
//            tv_rating_agencies.setTextColor(getResColor(R.color.text_color_tab));
//            tv_line_rating_agencies.setVisibility(View.GONE);
//
//            if (dpi <= AppDevice.DPI480P) {
//                mBarChart.setIsSw480P(true);
//            } else if (dpi <= AppDevice.DPI720P) {
//                mBarChart.setIsSw720P(true);
//            } else if (dpi <= AppDevice.DPI1080P) {
//                mBarChart.setIsSw1080P(true);
//            }
//            mPieView.setVisibility(View.GONE);
//            mBarChart.setData(bar_eps_list, false);
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//            String date = sdf.format(new Date());
//            charts_date.setText("统计截止" + date);
//        }
//    }

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
        canRefreshLine = false;
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
                canRefreshLine = true;
            }
        };
        new GGOKHTTP(param, GGOKHTTP.STOCK_MINUTE, ggHttpInterface).startGet();
    }

    private void getFiveData() {
        canRefreshLine = false;
        final HashMap<String, String> param = new HashMap<>();
        param.put("stock_code", stockCode);
        param.put("day", "5");
        if (!AppDevice.isNetworkConnected(CopyStockDetailActivity.this)) {
            if (load_animation.getVisibility() == View.VISIBLE) {
                load_animation.setVisibility(View.GONE);
            }
            if (map.get("1") == null && textLayout.getVisibility() == View.GONE) {
                textLayout.setVisibility(View.VISIBLE);
            }
            canRefreshLine = true;
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
        param.put("avg_line_type", dayk1 + ";" + dayk2 + ";" + dayk3);
        param.put("page", "1");
        param.put("rows", "100");

        if (!AppDevice.isNetworkConnected(CopyStockDetailActivity.this)) {
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
            canRefreshLine = true;
        }
        canRefreshLine = false;

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
                canRefreshLine = true;

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
        minLineTv.setTextColor(getResColor(R.color.red));
        dayKTv.setTextColor(getResColor(R.color.text_color_tab));
        fiveKTv.setTextColor(getResColor(R.color.text_color_tab));
        monthKTv.setTextColor(getResColor(R.color.text_color_tab));
        WeekKTv.setTextColor(getResColor(R.color.text_color_tab));
        minLine.setVisibility(View.VISIBLE);
        fivedayLine.setVisibility(View.GONE);
        daykLine.setVisibility(View.GONE);
        weekkLine.setVisibility(View.GONE);
        monthkLine.setVisibility(View.GONE);
    }

    private void setFiveStatu(int type) {
        if (type == 0) {
            load_animation.setVisibility(View.VISIBLE);
        } else if (type == 1) {
            load_animation.setVisibility(View.GONE);
        }
        minLineTv.setTextColor(getResColor(R.color.text_color_tab));
        dayKTv.setTextColor(getResColor(R.color.text_color_tab));
        fiveKTv.setTextColor(getResColor(R.color.red));
        monthKTv.setTextColor(getResColor(R.color.text_color_tab));
        WeekKTv.setTextColor(getResColor(R.color.text_color_tab));
        minLine.setVisibility(View.GONE);
        fivedayLine.setVisibility(View.VISIBLE);
        daykLine.setVisibility(View.GONE);
        weekkLine.setVisibility(View.GONE);
        monthkLine.setVisibility(View.GONE);
    }

    private void setDayKStatue(int type) {
        if (type == 0) {
            load_animation.setVisibility(View.VISIBLE);
        } else if (type == 1) {
            load_animation.setVisibility(View.GONE);
        }
        minLineTv.setTextColor(getResColor(R.color.text_color_tab));
        dayKTv.setTextColor(getResColor(R.color.red));
        fiveKTv.setTextColor(getResColor(R.color.text_color_tab));
        monthKTv.setTextColor(getResColor(R.color.text_color_tab));
        WeekKTv.setTextColor(getResColor(R.color.text_color_tab));
        minLine.setVisibility(View.GONE);
        fivedayLine.setVisibility(View.GONE);
        daykLine.setVisibility(View.VISIBLE);
        weekkLine.setVisibility(View.GONE);
        monthkLine.setVisibility(View.GONE);
    }

    private void setWeekLineStatu(int type) {
        if (type == 0) {
            load_animation.setVisibility(View.VISIBLE);
        } else if (type == 1) {
            load_animation.setVisibility(View.GONE);
        }
        minLineTv.setTextColor(getResColor(R.color.text_color_tab));
        dayKTv.setTextColor(getResColor(R.color.text_color_tab));
        fiveKTv.setTextColor(getResColor(R.color.text_color_tab));
        monthKTv.setTextColor(getResColor(R.color.text_color_tab));
        WeekKTv.setTextColor(getResColor(R.color.red));
        minLine.setVisibility(View.GONE);
        fivedayLine.setVisibility(View.GONE);
        daykLine.setVisibility(View.GONE);
        weekkLine.setVisibility(View.VISIBLE);
        monthkLine.setVisibility(View.GONE);
    }

    private void setMonthLineStatu(int type) {
        if (type == 0) {
            load_animation.setVisibility(View.VISIBLE);
        } else if (type == 1) {
            load_animation.setVisibility(View.GONE);
        }
        minLineTv.setTextColor(getResColor(R.color.text_color_tab));
        dayKTv.setTextColor(getResColor(R.color.text_color_tab));
        fiveKTv.setTextColor(getResColor(R.color.text_color_tab));
        monthKTv.setTextColor(getResColor(R.color.red));
        WeekKTv.setTextColor(getResColor(R.color.text_color_tab));
        minLine.setVisibility(View.GONE);
        fivedayLine.setVisibility(View.GONE);
        daykLine.setVisibility(View.GONE);
        weekkLine.setVisibility(View.GONE);
        monthkLine.setVisibility(View.VISIBLE);
    }

    //初始化下拉刷新样式
    private void initRefreshStyle(int color) {
        int c = getResColor(color);
        headerView.setBackgroundColor(c);
        ptrFrame.setBackgroundColor(c);
    }

    //定时自动刷新
    private void AutoRefresh(int time) {
        if (StockUtils.isTradeTime()) {
            setTime(time);
        } else {
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
        }
    }

    public void setTime(int num) {
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

                    initList(stockCode);
                    refreshChart(showItem);
                    getStockTimeDetial(stockCode);
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
                canRefreshLine = true;
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
//        if (showItem == 0) {
//            if (sailLineLayout.getVisibility() == View.GONE) {
//                sailLineLayout.setVisibility(View.VISIBLE);
//            }
//        }
        width = AppDevice.getWidth(CopyStockDetailActivity.this) - AppDevice.dp2px(CopyStockDetailActivity.this, 22);
        height = AppDevice.dp2px(CopyStockDetailActivity.this, 190);
        timer = new Timer();
        AutoRefresh(refreshtime);
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
     * @param stockCode
     */
    private void initList(final String stockCode) {
        final Map<String, String> param = new HashMap<String, String>();
        param.put("stock_code", stockCode);
        GGOKHTTP.GGHttpInterface httpInterface = new GGOKHTTP.GGHttpInterface() {

            @Override
            public void onSuccess(String responseInfo) {

                StockDetail bean = JSONObject.parseObject(responseInfo, StockDetail.class);

//                KLog.e(JSONObject.toJSON(bean.getData()).toString());

                if (bean.getCode() == 0) {

                    info = bean.getData();

                    setNow(info.getUpdate_time());
                    //保存收盘价
                    stock_charge_type = info.getStock_type();
                    change_value = hply.com.niugu.StringUtils.getDouble(info.getChange_value());
                    closePrice = hply.com.niugu.StringUtils.getDouble(String.valueOf(info.getClose_price()));
                    change_rate = hply.com.niugu.StringUtils.getDouble(info.getChange_rate());

                    StockUtils.savaColseprice((float) closePrice);
                    priceVolumDatas.clear();
                    //卖
                    priceVolumDatas.add(StringUtils.getStockDouble(info.getSell5_price(), 2));
                    priceVolumDatas.add(info.getSell5_volume() + "");
                    priceVolumDatas.add(StringUtils.getStockDouble(info.getSell4_price(), 2));
                    priceVolumDatas.add(info.getSell4_volume() + "");
                    priceVolumDatas.add(StringUtils.getStockDouble(info.getSell3_price(), 2));
                    priceVolumDatas.add(info.getSell3_volume() + "");
                    priceVolumDatas.add(StringUtils.getStockDouble(info.getSell2_price(), 2));
                    priceVolumDatas.add(info.getSell2_volume() + "");
                    priceVolumDatas.add(StringUtils.getStockDouble(info.getSell1_price(), 2));
                    priceVolumDatas.add(info.getSell1_volume() + "");
                    //买
                    priceVolumDatas.add(StringUtils.getStockDouble(info.getBuy1_price(), 2));
                    priceVolumDatas.add(info.getBuy1_volume() + "");
                    priceVolumDatas.add(StringUtils.getStockDouble(info.getBuy2_price(), 2));
                    priceVolumDatas.add(info.getBuy2_volume() + "");
                    priceVolumDatas.add(StringUtils.getStockDouble(info.getBuy3_price(), 2));
                    priceVolumDatas.add(info.getBuy3_volume() + "");
                    priceVolumDatas.add(StringUtils.getStockDouble(info.getBuy4_price(), 2));
                    priceVolumDatas.add(info.getBuy4_volume() + "");
                    priceVolumDatas.add(StringUtils.getStockDouble(info.getBuy5_price(), 2));
                    priceVolumDatas.add(info.getBuy5_volume() + "");
                    stock_price.setText(StringUtils.getStockDouble(info.getPrice(), 2));//股票价格
                    stock_start.setText(StringUtils.getStockDouble(info.getOpen_price(), 2));//开盘价

                    //涨跌
                    if (StringUtils.getStockDouble(info.getChange_value()) > 0) {
                        setStatusColor(R.color.header_red);
                        relative_header.setBackgroundColor(getResColor(R.color.header_red));
                        linear_header.setBackgroundColor(getResColor(R.color.header_red));
                        initRefreshStyle(R.color.header_red);
                        stock_detail_tv1.setText("+" + StringUtils.getStockDouble(info.getChange_value(), 2));
                        stock_detail_tv2.setText("+" + StringUtils.getStockDouble(info.getChange_rate(), 2) + "%");
                    } else if (StringUtils.getStockDouble(info.getChange_value()) < 0) {
                        setStatusColor(R.color.header_green);
                        relative_header.setBackgroundColor(getResColor(R.color.header_green));
                        linear_header.setBackgroundColor(getResColor(R.color.header_green));
                        initRefreshStyle(R.color.header_green);
                        stock_detail_tv1.setText(StringUtils.getStockDouble(info.getChange_value(), 2));
                        stock_detail_tv2.setText(StringUtils.getStockDouble(info.getChange_rate(), 2) + "%");
                    } else {
                        setStatusColor(R.color.header_gray);
                        relative_header.setBackgroundColor(getResColor(R.color.header_gray));
                        linear_header.setBackgroundColor(getResColor(R.color.header_gray));
                        initRefreshStyle(R.color.header_gray);
                        stock_detail_tv1.setText(StringUtils.getStockDouble(info.getChange_value(), 2));
                        stock_detail_tv2.setText(StringUtils.getStockDouble(info.getChange_rate(), 2) + "%");
                    }

                    if (info.getVolume() == null) {
                        stock_volume.setText("0手");//成交量
                    } else {
                        if (info.getVolume().length() > 6) {

                            stock_volume.setText(StringUtils.save2Significand(StringUtils.getStockDouble(info.getVolume()) / 1000000) + "万手");//成交量
                        } else {
                            String volume = String.valueOf((Double.parseDouble(info.getVolume()) / 100));
                            stock_volume.setText(StringUtils.getIntegerData(volume) + "手");
                        }
                    }
                    stock_turnover_rate.setText(StringUtils.save2Significand(
                            StringUtils.getStockDouble(info.getTurnover_rate()) * 100) + "%");//换手率
                    if (info.getHigh_price() == null) {
                        stock_detail_tv3.setText("0.00");//最高
                    } else {
                        stock_detail_tv3.setText(StringUtils.save2Significand(Double.parseDouble(info.getHigh_price())));
                    }
                    if (info.getVolume_inner() == null || Integer.parseInt(info.getVolume_inner()) == 0 || info.getVolume_inner().length() < 3) {
                        stock_detail_tv4.setText("0");//内盘
                    } else {
                        if (info.getVolume_inner().length() > 6) {
                            stock_detail_tv4.setText(StringUtils.save2Significand((Double.parseDouble(info.getVolume_inner()) / 1000000)) + "万");
                        } else {
                            String inner = String.valueOf((Double.parseDouble(info.getVolume_inner()) / 100));
                            stock_detail_tv4.setText(StringUtils.getIntegerData(inner) + "手");
                        }
                    }
                    if (info.getLow_price() == null) {
                        stock_detail_tv6.setText("0.00");//最低
                    } else {
                        stock_detail_tv6.setText(StringUtils.save2Significand(Double.parseDouble(info.getLow_price())));
                    }
                    if ($(info.getVolume_outer()) == 0 || info.getVolume_outer().length() < 3) {
                        stock_detail_tv7.setText("0");//外盘
                    } else {
                        if (info.getVolume_outer().length() > 6) {
                            stock_detail_tv7.setText(
                                    StringUtils.save2Significand(
                                            ($(info.getVolume_outer()) / 1000000)) + "万");
                        } else {
                            double outer = $(info.getVolume_outer()) / 100;
                            stock_detail_tv7.setText(StringUtils.getIntegerData(Double.toString(outer)) + "手");
                        }
                    }
                    if ($(info.getTurnover()) == 0) {
                        stock_detail_tv9.setText("0");
                    } else {
                        if (StringUtils.save2Significand(Double.valueOf(info.getTurnover())).length() < 7) {
                            stock_detail_tv9.setText(StringUtils.save2Significand(Double.valueOf(info.getTurnover())) + "万");//成交额
                        } else {
                            stock_detail_tv9.setText(StringUtils.save2Significand((Double.valueOf(info.getTurnover()) / 10000)) + "亿");
                        }
                    }
                    if (stock_charge_type == 1) {
                        text_state.setVisibility(View.VISIBLE);
                        text_delist.setVisibility(View.GONE);
                        stock_detail_tv1.setVisibility(View.VISIBLE);
                        stock_detail_tv2.setVisibility(View.VISIBLE);
                        stock_detail_delist.setVisibility(View.GONE);
                        stock_detail_tv8.setText(StringUtils.save2Significand(info.getAmplitude()) + "%");//振幅
                    } else {
                        //停牌处理
                        text_state.setVisibility(View.GONE);
                        text_delist.setVisibility(View.VISIBLE);
                        text_delist.setText(StringUtils.save2Significand(info.getClose_price()) + " " + StockUtils.getStockStatus(stock_charge_type));
                        stock_detail_tv1.setVisibility(View.GONE);
                        stock_detail_tv2.setVisibility(View.GONE);
                        stock_detail_delist.setVisibility(View.VISIBLE);
                        stock_detail_delist.setText(StockUtils.getStockStatus(stock_charge_type));
                        stock_detail_tv6.setText("--");//最低
                        stock_detail_tv8.setText("--");//振幅
                    }
                    stock_close.setText(StringUtils.save2Significand(info.getClose_price()));//收盘价
                    if (info.getPe_y1() == null) {
                        stock_detail_tv5.setText("0.00");
                    } else {
                        stock_detail_tv5.setText(StringUtils.save2Significand(Double.parseDouble(info.getPe_y1())));//市盈率
                    }
                    DecimalFormat df = new DecimalFormat("0.0");
                    if (StringUtils.save2Significand(info.getTcap()).length() > 10) {
                        stock_detail_tv10.setText(df.format(StringUtils.getStockDouble(info.getTcap()) / 100000000) + "万亿");//市值
                    } else {
                        try {
                            stock_detail_tv10.setText(df.format(StringUtils.getStockDouble(info.getTcap()) / 10000) + "亿");//市值
                        } catch (Exception e) {
                        }
                    }
                    if (StringUtils.save2Significand(info.getMcap()).length() > 10) {
                        try {
                            stock_detail_tv11.setText(df.format(StringUtils.getStockDouble(info.getMcap()) / 100000000) + "万亿");//流通市值
                        } catch (Exception e) {
                        }
                    } else {
                        try {
                            stock_detail_tv11.setText(df.format(StringUtils.getStockDouble(info.getMcap()) / 10000) + "亿");//流通市值
                        } catch (Exception e) {
                        }
                    }
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
                        }
                    });

                    //退市数据处理 停牌 停市 未上市
                    if (stock_charge_type == 0 || stock_charge_type == -1 || stock_charge_type == -2) {//退市/未上市
                        setZero2Line(stock_price);
                        setZero2Line(stock_start);
                        setZero2Line(stock_close);
                        setZero2Line(stock_volume);
                        setZero2Line(stock_turnover_rate);

                        setZero2Line(stock_detail_tv3);
                        setZero2Line(stock_detail_tv4);
                        setZero2Line(stock_detail_tv5);
                        setZero2Line(stock_detail_tv6);
                        setZero2Line(stock_detail_tv7);
                        setZero2Line(stock_detail_tv9);
                        setZero2Line(stock_detail_tv8);
                        setZero2Line(stock_detail_tv10);
                        setZero2Line(stock_detail_tv11);
                    }
                }
                stopAnimation();

                //显示刷新完毕提示
                headerView.over();
                //延时1s收回下拉头部
                ptrFrame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ptrFrame.refreshComplete();
                    }
                }, 1000);

                stopAnimation();
            }

            @Override
            public void onFailure(String msg) {
                //显示刷新完毕提示
                headerView.over();
                //延时1s收回下拉头部
                ptrFrame.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ptrFrame.refreshComplete();
                    }
                }, 1000);
                stopAnimation();
                UIHelper.toast(getApplicationContext(), "请检查网络");
            }
        };
        new GGOKHTTP(param, GGOKHTTP.ONE_STOCK_DETAIL, httpInterface).startGet();
    }

    private double $(String volume_outer) {
        return StringUtils.getStockDouble(volume_outer);
    }

    private void setStatusColor(int color) {
        StatusBarUtil util = StatusBarUtil.with(getActivity());
        util.setColor(getResColor(color));
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
        ;
    }

    //股票状态
    private void StockState() {
        SimpleDateFormat df = new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA);
        text_state.setText(StockUtils.getTreatState() + " " + df.format(new Date()));
        text_state.setTextColor(Color.WHITE);
        text_state.setAlpha((float) 0.7);
    }

    //图表点击事件
    @Override
    public void onClick(View view) {
        if (textLayout.getVisibility() == View.VISIBLE) {
            textLayout.setVisibility(View.GONE);
        }
        switch (view.getId()) {
            case R.id.charts_min_line_layout:
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
                position = 0;
                break;
            case R.id.charts_fiveday_k_layout:
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
                position = 1;
                break;
            case R.id.charts_day_k_layout:
                if (map.containsKey("2")) {
                    setDayKStatue(1);
                    mBitmapChartView.setBitmap(map.get("2"));
                } else {
                    setDayKStatue(0);
                    getKLineData(0);
                }
                position = 2;
                break;
            case R.id.charts_week_k_layout:
                if (map.containsKey("3")) {
                    setWeekLineStatu(1);
                    mBitmapChartView.setBitmap(map.get("3"));
                } else {
                    setWeekLineStatu(0);
                    getKLineData(1);
                }
                position = 3;
                break;
            case R.id.charts_mouth_k_layout:
                if (map.containsKey("4")) {
                    setMonthLineStatu(1);
                    mBitmapChartView.setBitmap(map.get("4"));
                } else {
                    setMonthLineStatu(0);
                    getKLineData(2);
                }
                position = 4;
                break;
            default:

                break;
        }
        showItem = position;
        SPTools.saveInt("showItem", position);
    }


    //异步画图
    class BitmapTask extends AsyncTask<String, Void, Bitmap> {
        private String item_index;

        @Override
        protected Bitmap doInBackground(String... params) {
            canRefreshLine = false;
            Bitmap bitmap = null;
            item_index = params[0];
            switch (item_index) {
                case "0":
                    timesBitmap = new TimesFivesBitmap(width, height);
                    if (dpi <= AppDevice.DPI480P) {
                        timesBitmap.setIsSw480P(true);
                    } else if (dpi <= AppDevice.DPI720P) {
                        timesBitmap.setIsSw720P(true);
                    } else if (dpi <= AppDevice.DPI1080P) {
                        timesBitmap.setIsSw1080P(true);
                    }
                    timesBitmap.setShowDetail(false);
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
                        if (dpi <= AppDevice.DPI480P) {
                            fiveDayBitmap.setIsSw480P(true);
                        } else if (dpi <= AppDevice.DPI720P) {
                            fiveDayBitmap.setIsSw720P(true);
                        } else if (dpi <= AppDevice.DPI1080P) {
                            fiveDayBitmap.setIsSw1080P(true);
                        }
                        fiveDayBitmap.setShowDetail(false);
                        bitmap = fiveDayBitmap.setTimesList(bean, false, stock_charge_type);
                        map.put(item_index, bitmap);
                    }
                    break;
                case "2":
                    if (params.length > 1) {
                        mOHLCData.clear();
                        parseObjects(params[1]);
                        KChartsBitmap kChartsBitmap = new KChartsBitmap(getActivity(), width, height);
                        if (dpi <= AppDevice.DPI480P) {
                            kChartsBitmap.setIsSw480P(true);
                        } else if (dpi <= AppDevice.DPI720P) {
                            kChartsBitmap.setIsSw720P(true);
                        } else if (dpi <= AppDevice.DPI1080P) {
                            kChartsBitmap.setIsSw1080P(true);
                        }
                        bitmap = kChartsBitmap.setOHLCData(mOHLCData);
                        map.put(item_index, bitmap);
                    }
                    break;
                case "3":
                    if (params.length > 1) {
                        mOHLCData.clear();
                        parseObjects(params[1]);
                        KChartsBitmap kChartsBitmap1 = new KChartsBitmap(getActivity(), width, height);
                        if (dpi <= AppDevice.DPI480P) {
                            kChartsBitmap1.setIsSw480P(true);
                        } else if (dpi <= AppDevice.DPI720P) {
                            kChartsBitmap1.setIsSw720P(true);
                        } else if (dpi <= AppDevice.DPI1080P) {
                            kChartsBitmap1.setIsSw1080P(true);
                        }
                        bitmap = kChartsBitmap1.setOHLCData(mOHLCData);
                        map.put(item_index, bitmap);
                    }
                    break;
                case "4":
                    if (params.length > 1) {
                        mOHLCData.clear();
                        parseObjects(params[1]);
                        KChartsBitmap kChartsBitmap2 = new KChartsBitmap(getActivity(), width, height);
                        if (dpi <= AppDevice.DPI480P) {
                            kChartsBitmap2.setIsSw480P(true);
                        } else if (dpi <= AppDevice.DPI720P) {
                            kChartsBitmap2.setIsSw720P(true);
                        } else if (dpi <= AppDevice.DPI1080P) {
                            kChartsBitmap2.setIsSw1080P(true);
                        }
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
            canRefreshLine = true;
        }
    }

    //评论, 诊断, 分享, 加自选
    @OnClick({R.id.stock_detail_diagnose, R.id.stock_detail_choose})
    public void allBtnClick(View v) {
        switch (v.getId()) {
            case R.id.stock_detail_diagnose:
                showPopupWindow();
                break;
            case R.id.stock_detail_choose:
                addOptionalShare();
                break;
        }
    }

    //诊断弹窗
    private void showPopupWindow() {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.diagnose_view, null);
        LinearLayout words_minute = (LinearLayout) view.findViewById(R.id.diagnose_words_minute);
        LinearLayout data_minute = (LinearLayout) view.findViewById(R.id.diagnose_data_minute);
        LinearLayout profession_compare = (LinearLayout) view.findViewById(R.id.diagnose_profession_compare);
        LinearLayout interactive_investor = (LinearLayout) view.findViewById(R.id.diagnose_interactive_investor);
        TextView btn_cancel = (TextView) view.findViewById(R.id.btn_cancel);
        //取消按钮
        btn_cancel.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                //销毁弹出框
                mPopupWindow.dismiss();
            }
        });

        //设置按钮监听
        words_minute.setOnClickListener(PopupItemsOnClick);
        data_minute.setOnClickListener(PopupItemsOnClick);
        profession_compare.setOnClickListener(PopupItemsOnClick);
        interactive_investor.setOnClickListener(PopupItemsOnClick);

        mPopupWindow = new PopupWindow(view);
        //设置SelectPicPopupWindow的View
        mPopupWindow.setContentView(view);
        //设置SelectPicPopupWindow弹出窗体的宽
        mPopupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        mPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        mPopupWindow.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        mPopupWindow.setAnimationStyle(R.style.popwin_anim_style);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        mPopupWindow.setBackgroundDrawable(dw);

        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.4f;
        getWindow().setAttributes(lp);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        view.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = view.findViewById(R.id.diagnose_view).getTop();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y < height) {
                        mPopupWindow.dismiss();
                    }
                }
                return true;
            }
        });

        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            //在dismiss中恢复透明度
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });

        mPopupWindow.showAtLocation(this.findViewById(R.id.stock_detail_linear),
                Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    //为弹出窗口实现监听类
    private OnClickListener PopupItemsOnClick = new OnClickListener() {

        public void onClick(View v) {
            mPopupWindow.dismiss();
            switch (v.getId()) {
                case R.id.diagnose_words_minute:
                    break;
                case R.id.diagnose_data_minute:
                    break;
                case R.id.diagnose_profession_compare:
                    break;
                case R.id.diagnose_interactive_investor:
                    break;
            }
        }
    };

    //添加自选股
    private void addOptionalShare() {
        if (isChoose) {
            if (!UserUtils.isLogin()) {
                StockUtils.removeStock(stockCode);
                isChoose = false;
                stock_detail_choose_iv.setBackgroundResource(R.drawable.choose_stock);
                stock_detail_choose_tv.setText("加自选");
                UIHelper.toast(CopyStockDetailActivity.this, "删除自选成功");
            } else {
                //登录时
                final Map<String, String> param = new HashMap<String, String>();
                param.put("token", UserUtils.getToken());
                param.put("stock_code", stockCode);

                GGOKHTTP.GGHttpInterface httpInterface = new GGOKHTTP.GGHttpInterface() {
                    @Override
                    public void onSuccess(String responseInfo) {

                        JSONObject result = JSONObject.parseObject(responseInfo);
                        int data = (int) result.get("code");
                        if (data == 0) {
                            isChoose = false;
                            stock_detail_choose_iv.setBackgroundResource(R.drawable.choose_stock);
                            stock_detail_choose_tv.setText("加自选");
                            UIHelper.toast(CopyStockDetailActivity.this, "删除自选成功");
                        }
                    }

                    @Override
                    public void onFailure(String msg) {
                        UIHelper.toast(CopyStockDetailActivity.this, "请检查网络");
                    }
                };
                new GGOKHTTP(param, GGOKHTTP.MYSTOCK_DELETE, httpInterface).startGet();
            }
        } else {
            if (!UserUtils.isLogin()) {
                JSONObject singlestock = new JSONObject();
                singlestock.put("stock_name", stockName);
                singlestock.put("stock_code", stockCode);
                singlestock.put("stock_type", 1);
                singlestock.put("price", 0);
                singlestock.put("change_rate", 0);
                StockUtils.addStock2MyStock(singlestock);
                isChoose = true;
                stock_detail_choose_iv.setBackgroundResource(R.drawable.not_choose_stock);
                stock_detail_choose_tv.setText("已自选");
                UIHelper.toast(CopyStockDetailActivity.this, "添加自选成功");
            } else {
                //登录时
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
                            JSONObject singlestock = new JSONObject();
                            singlestock.put("stock_name", stockName);
                            singlestock.put("stock_code", stockCode);
                            singlestock.put("stock_type", 1);
                            singlestock.put("price", 0);
                            singlestock.put("change_rate", 0);
                            StockUtils.addStock2MyStock(singlestock);
                            isChoose = true;
                            stock_detail_choose_iv.setBackgroundResource(R.drawable.not_choose_stock);
                            stock_detail_choose_tv.setText("已自选");
                            UIHelper.toast(CopyStockDetailActivity.this, "添加自选成功");
                        }
                    }

                    @Override
                    public void onFailure(String msg) {
                        UIHelper.toast(CopyStockDetailActivity.this, "请检查网络");
                    }
                };
                new GGOKHTTP(param, GGOKHTTP.MYSTOCK_ADD, httpInterface).startGet();
            }
        }
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
            com.alibaba.fastjson.JSONObject singledata = (com.alibaba.fastjson.JSONObject) data.get(i);
            Map<String, Object> itemData = new HashMap<String, Object>();
            itemData.put("amplitude", singledata.getFloat("amplitude"));
            itemData.put("avg_price_" + dayk1, ParseNum(singledata.getString("avg_price_" + dayk1)));
            itemData.put("avg_price_" + dayk2, ParseNum(singledata.getString("avg_price_" + dayk2)));
            itemData.put("avg_price_" + dayk3, ParseNum(singledata.getString("avg_price_" + dayk3)));
            itemData.put("avg_price_" + dayk4, ParseNum(singledata.getString("avg_price_" + dayk4)));
            itemData.put("close_price", ParseNum(singledata.getString("close_price")));
            itemData.put("date", singledata.getString("date"));
            itemData.put("high_price", ParseNum(singledata.getString("high_price")));
            itemData.put("low_price", ParseNum(singledata.getString("low_price")));
            itemData.put("open_price", ParseNum(singledata.getString("open_price")));
            itemData.put("price_change", ParseNum(singledata.getString("price_change")));
            itemData.put("price_change_rate", ParseNum(singledata.getString("price_change_rate")));
            itemData.put("rightValue", ParseNum(singledata.getString("rightValue")));
            itemData.put("turnover", ParseNum(singledata.getString("turnover")));
            itemData.put("turnover_rate", ParseNum(singledata.getString("turnover_rate")));
            itemData.put("volume", ParseNum(singledata.getString("volume")));
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

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public String getStockCode() {
        return stockCode;
    }

    public void setStockCode(String stockCode) {
        this.stockCode = stockCode;
    }

    String now;

    public String getNow() {
        return now;
    }

    public void setNow(String now) {
        this.now = now;
    }

}