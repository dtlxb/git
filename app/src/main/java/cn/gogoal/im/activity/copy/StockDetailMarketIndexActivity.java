package cn.gogoal.im.activity.copy;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.ColorRes;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

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
import cn.gogoal.im.adapter.copy.ChangeListAdapter;
import cn.gogoal.im.adapter.copy.MarketTitleAdapter;
import cn.gogoal.im.base.AppManager;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.bean.BaseMessage;
import cn.gogoal.im.common.AnimationUtils;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.SPTools;
import cn.gogoal.im.common.StockUtils;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.fragment.copy.TimesFragment;
import cn.gogoal.im.ui.copy.InnerListView;
import cn.gogoal.im.ui.stockviews.BitmapChartView;
import cn.gogoal.im.ui.stockviews.KChartsBitmap;
import cn.gogoal.im.ui.stockviews.TimesFivesBitmap;
import hply.com.niugu.DeviceUtil;
import hply.com.niugu.StringUtils;
import hply.com.niugu.bean.StockData;
import hply.com.niugu.bean.StockDetailMarketIndexBean;
import hply.com.niugu.bean.StockDetailMarketIndexData;
import hply.com.niugu.bean.StockRankBean;
import hply.com.niugu.stock.StockMinuteBean;


/*
* 大盘股票详情
* */
public class StockDetailMarketIndexActivity extends BaseActivity {

    TimesFivesBitmap fiveDayBitmap;
    TimesFivesBitmap timesBitmap;
    StockMinuteBean timesBean;
    //返回
    @BindView(R.id.btnBack)
    LinearLayout btnBack;
    //标题栏
    @BindView(R.id.title_header)
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
    private RotateAnimation rotateAnimation;
    //    protected RotateAnimation animation;
    //数据栏
    @BindView(R.id.linear_header)
    LinearLayout linear_header;
    //下拉刷新
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    //下拉刷新rootView
    @BindView(R.id.fragment_rotate_header_with_view_group_frame)
    SwipeRefreshLayout ptrFrame;
    //    //下拉刷新头部控件
//    private HeaderView headerView;
    //股票价格
    @BindView(R.id.stock_price)
    TextView stock_price;
    //涨跌
    @BindView(R.id.stock_detail_tv1)
    TextView stock_detail_tv1;
    @BindView(R.id.stock_detail_tv2)
    TextView stock_detail_tv2;
    //今开
    @BindView(R.id.textView1)
    TextView textView1;
    @BindView(R.id.stock_start)
    TextView stock_start;
    //成交量
    @BindView(R.id.textView3)
    TextView textView3;
    @BindView(R.id.stock_volume)
    TextView stock_volume;
    //昨收
    @BindView(R.id.textView2)
    TextView textView2;

    @BindView(R.id.stock_close)
    TextView stock_close;
    //振幅
    @BindView(R.id.textView4)
    TextView textView4;

    @BindView(R.id.stock_amplitude)
    TextView stock_amplitude;
    //最高
    @BindView(R.id.stock_detail_tv3)
    TextView stock_detail_tv3;
    //涨家数
    @BindView(R.id.stock_detail_tv4)
    TextView stock_detail_tv4;
    //最低
    @BindView(R.id.stock_detail_tv5)
    TextView stock_detail_tv5;
    //平家数
    @BindView(R.id.stock_detail_tv6)
    TextView stock_detail_tv6;
    //成交额
    @BindView(R.id.stock_detail_tv7)
    TextView stock_detail_tv7;
    //跌家数
    @BindView(R.id.stock_detail_tv8)
    TextView stock_detail_tv8;
    //涨幅榜
    @BindView(R.id.tv_increase)
    TextView tv_increase;
    @BindView(R.id.tv_line_increase)
    TextView tv_line_increase;
    //跌幅榜
    @BindView(R.id.tv_drop)
    TextView tv_drop;
    @BindView(R.id.tv_line_drop)
    TextView tv_line_drop;
    //换手率
    @BindView(R.id.tv_turnover)
    TextView tv_turnover;
    @BindView(R.id.tv_line_turnover)
    TextView tv_line_turnover;
    //列表
    @BindView(R.id.stock_lv)
    InnerListView stock_lv;
    private String getStockCode;
    private List<StockData> StockDatas = new ArrayList<>();
    //适配器
    private MarketTitleAdapter adapter;
    private ChangeListAdapter changeAdapter;
    //中部图形布局
    @BindView(R.id.stock_detail_fragment_time_line)
    BitmapChartView mBitmapChartView;
    @BindView(R.id.stockdetail_textview)
    LinearLayout textLayout;

    //图表表头
    @BindView(R.id.charts_line_tab_up_ll)
    TabLayout tabChartsTitles;
    @BindArray(R.array.stock_detail_chart_titles)
    String[] stockDetailChartTitles;

    //页面加载动画
    @BindView(R.id.load_animation_change)
    RelativeLayout load_animation_change;
    @BindView(R.id.load_animation)
    RelativeLayout load_animation;
    //无数据处理
    @BindView(R.id.stock_no_data)
    LinearLayout stock_no_data;

    private int pixels = -85;
    private String stockName;
    private String stockCode;
    //定时刷新
    private Timer timer;
    private int refreshtime;

    private HashMap<String, Bitmap> map = new HashMap<>();
    //数据集合
    private List<Map<String, Object>> mOHLCData = new ArrayList<>();
    //定时刷新
    private int seat;
    private int showItem;
    private int width;
    private int height;

    @BindView(R.id.flag_layout_treat)
    LinearLayout layoutTreat;

    //k线数据设置
    private int dayk1;
    private int dayk2;
    private int dayk3;
    private int dayk4;

    private ArrayList<StockDetailMarketIndexData> info;

    //=====================20170614===================
    @BindView(R.id.layout_dialog)
    TableLayout layoutDialog;

    @BindView(R.id.view_dialog_mask)
    View viewMask;

    @BindView(R.id.iv_show_info_dialog)
    ImageView imageViewShoeDialog;
    private String stockSource;

    @Override
    public int bindLayout() {
        return R.layout.activity_stock_detail_market_index;
    }

    @Override
    public void doBusiness(Context mContext) {

        iniRefresh(ptrFrame);

        layoutTreat.setVisibility(View.GONE);

        rotateAnimation = AnimationUtils.getInstance().setLoadingAnime(btnRefresh, R.mipmap.loading_white);
        //获取股票数据
        stockName = getIntent().getStringExtra("stockName");
        stockCode = getIntent().getStringExtra("stockCode");

//        stockSource=getIntent().getStringExtra("stockSource");

        textHeadTitle.setText(stockName + "(" + stockCode + ")");
        init();
        InitList(stockCode);
        seat = 0;
        getStockCode = stockCode.substring(0, 2) + "." + stockCode.substring(2);
        getMarketInformation(seat, getStockCode);
        onShow(showItem);
    }

    private void init() {
        dayk1 = SPTools.getInt("tv_ln1", 5);
        dayk2 = SPTools.getInt("tv_ln2", 10);
        dayk3 = SPTools.getInt("tv_ln3", 20);
        dayk4 = SPTools.getInt("tv_ln4", 0);

//        headerView = (HeaderView) LayoutInflater.from(this).inflate(R.layout.header_layout, null).findViewById(R.id.header_view);

        textView1.setAlpha((float) 0.7);
        textView2.setAlpha((float) 0.7);
        textView3.setAlpha((float) 0.7);
        textView4.setAlpha((float) 0.7);
        //获取自动刷新时间
        showItem = SPTools.getInt("showItem", 0);

        for (int i = 0; i < 5; i++) {
            tabChartsTitles.addTab(tabChartsTitles.newTab().setText(stockDetailChartTitles[i]));
        }

        TabLayout.Tab tabAt = tabChartsTitles.getTabAt(showItem);
        if (tabAt != null)
            tabAt.select();

        tabChartsTitles.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (textLayout.getVisibility() == View.VISIBLE) {
                    textLayout.setVisibility(View.GONE);
                }
                switch (tab.getPosition()) {
                    case 0:
                        if (map.containsKey("0")) {
                            setMinLineStatu(1);
                            if (StockUtils.isTradeTime()) {
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
                            if (StockUtils.isTradeTime()) {
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

            public void onTabUnselected(TabLayout.Tab tab) {
            }

            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        refreshtime = SPTools.getInt("refreshtime", 15000);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //点击刷新
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAnimation();
                InitList(stockCode);
                refreshChart(showItem);
                getMarketInformation(seat, getStockCode);
            }
        });
        mBitmapChartView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), StockDetailChartsActivity.class);
                intent.putExtra("position", showItem);
                intent.putExtra("fromtype", TimesFragment.TREAT_FROM_MARKET_INFO);//是指数详情
                intent.putExtra("stockCode", stockCode);
                intent.putExtra("stockName", stockName);
                intent.putExtra("price", info.get(0).getPrice());
                intent.putExtra("volume", info.get(0).getVolume());
                intent.putExtra("time", info.get(0).getUpdate_time());
                intent.putExtra("stockType", StockDetailChartsActivity.STOCK_MARKE_INDEX);
                startActivity(intent);
            }
        });

        // 修改状态栏颜色
        setStatusColor(getResColor(R.color.header_gray));

        relative_header.setBackgroundColor(getResColor(R.color.header_gray));
        linear_header.setBackgroundColor(getResColor(R.color.header_gray));
        initRefreshStyle(R.color.header_gray);
        //股票状态和当前时间
        StockState();

        adapter = new MarketTitleAdapter((ArrayList<StockData>) StockDatas);
        changeAdapter = new ChangeListAdapter((ArrayList<StockData>) StockDatas);

//        //设置下拉头部属性
//        headerView.setFontColor(getResColor(R.color.white));
//        headerView.setPullImage(R.mipmap.arrows_white);
//        headerView.setLoadingImage(R.mipmap.loading_white);

        ptrFrame.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                StockState();
                startAnimation();
                InitList(stockCode);
                refreshChart(showItem);
                ptrFrame.setRefreshing(false);
            }
        });
//        //设置下拉刷新属性
//        ptrFrame.setPtrHandler(new PtrHandler() {
//            @Override
//            public void onRefreshBegin(PtrFrameLayout frame) {
//                //刷新
//                headerView.loading();
//
//                if (canRefreshLine) {
//                    StockState();
//                    startAnimation();
//                    InitList(stockCode);
//                    refreshChart(showItem);
//                } else {
//                    ptrFrame.refreshComplete();
//                }
//            }
//
//            @Override
//            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
//                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
//            }
//        });

//        ptrFrame.setHeaderView(headerView);
//        ptrFrame.addPtrUIHandler(headerView);
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

    /**
     * 初始化下拉刷新样式
     */
    private void initRefreshStyle(@ColorRes int color) {
//        headerView.setBackgroundColor(getResColor(color));
        ptrFrame.setBackgroundColor(getResColor(color));
    }


    private void onShow(int item) {
        switch (item) {
            case 0:
                setMinLineStatu(0);
                GetMinLineData();
                break;
            case 1:
                setFiveStatu(0);
                getFiveData();
                break;
            case 2:
                setDayKStatue(0);
                getKLineData(0);
                break;
            case 3:
                setWeekLineStatu(0);
                getKLineData(1);
                break;
            case 4:
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

    private void setMinLineStatu(int type) {
        if (type == 0) {
            load_animation.setVisibility(View.VISIBLE);
        } else if (type == 1) {
            load_animation.setVisibility(View.GONE);
        }
    }

    private void setFiveStatu(int type) {
        if (type == 0) {
            load_animation.setVisibility(View.VISIBLE);
        } else if (type == 1) {
            load_animation.setVisibility(View.GONE);
        }
    }

    private void setDayKStatue(int type) {
        if (type == 0) {
            load_animation.setVisibility(View.VISIBLE);
        } else if (type == 1) {
            load_animation.setVisibility(View.GONE);
        }
    }

    private void setWeekLineStatu(int type) {
        if (type == 0) {
            load_animation.setVisibility(View.VISIBLE);
        } else if (type == 1) {
            load_animation.setVisibility(View.GONE);
        }
    }

    private void setMonthLineStatu(int type) {
        if (type == 0) {
            load_animation.setVisibility(View.VISIBLE);
        } else if (type == 1) {
            load_animation.setVisibility(View.GONE);
        }
    }

    private void GetMinLineData() {
        HashMap<String, String> param = new HashMap<>();
        param.put("fullcode", stockCode);
        param.put("avg_line_type", "150");

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                if (textLayout.getVisibility() == View.VISIBLE) {
                    textLayout.setVisibility(View.GONE);
                }
                timesBean = JSONObject.parseObject(responseInfo, StockMinuteBean.class);
                if (timesBean.getCode() == 0) {
                    new BitmapTask().execute("0");
                } else {
                    if (load_animation.getVisibility() == View.VISIBLE) {
                        load_animation.setVisibility(View.GONE);
                    }
                    if (map.get("0") == null && textLayout.getVisibility() == View.GONE) {
                        textLayout.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(String msg) {
                try {
                    if (load_animation.getVisibility() == View.VISIBLE) {
                        load_animation.setVisibility(View.GONE);
                    }
                    if (map.get("0") == null && textLayout.getVisibility() == View.GONE) {
                        textLayout.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    e.getMessage();
                }
            }
        };

        new GGOKHTTP(param, GGOKHTTP.GET_HQ_MINUTE, ggHttpInterface).startGet();
    }

    private void getFiveData() {
        final HashMap<String, String> param = new HashMap<>();
        param.put("fullcode", stockCode);
        param.put("day", "5");
        param.put("avg_line_type", "150");

        if (!DeviceUtil.isNetworkConnected(StockDetailMarketIndexActivity.this)) {
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
                Integer code = result.getInteger("code");
                if (code == 0) {
                    new BitmapTask().execute("1", responseInfo);
                } else {
                    AppManager.getInstance().sendMessage("StockDetailMarketIndexActivity", new BaseMessage("indexRefresh2"));
                }
            }

            @Override
            public void onFailure(String msg) {
                AppManager.getInstance().sendMessage("StockDetailMarketIndexActivity", new BaseMessage("indexRefresh2"));
            }
        };
        new GGOKHTTP(param, GGOKHTTP.GET_HQ_MINUTE, ggHttpInterface).startGet();
    }

    private void getKLineData(final int displayIndex) {
        HashMap<String, String> param = new HashMap<>();
        param.put("kline_type", displayIndex + "");
        param.put("fullcode", stockCode);
        param.put("avg_line_type", dayk1 + ";" + dayk2 + ";" + dayk3);
        param.put("page", "1");
        param.put("rows", "200");

        if (!DeviceUtil.isNetworkConnected(StockDetailMarketIndexActivity.this)) {
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
                Integer code = result.getInteger("code");
                if (code == 0) {
                    new BitmapTask().execute(String.valueOf(displayIndex + 2), responseInfo);
                } else {
                    AppManager.getInstance().sendMessage("StockDetailMarketIndexActivity", new BaseMessage("indexRefresh3", displayIndex + ""));
                }
            }

            @Override
            public void onFailure(String msg) {
                AppManager.getInstance().sendMessage("StockDetailMarketIndexActivity", new BaseMessage("indexRefresh3", displayIndex + ""));
            }
        };
        new GGOKHTTP(param, GGOKHTTP.GET_HQ_KLINE, ggHttpInterface).startGet();
    }

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
                AppManager.getInstance().sendMessage("StockDetailMarketIndexActivity", new BaseMessage("indexRefresh1"));
            }
        };
        if (timer == null) {
            timer = new Timer();
        }
        timer.scheduleAtFixedRate(timerTask, 0, num);
    }

    @Subscriber(tag = "StockDetailMarketIndexActivity")
    private void refresh(BaseMessage s) {
        switch (s.getCode()) {
            case "indexRefresh1":
                if (StockUtils.isTradeTime()) {
                    startAnimation();
                    InitList(stockCode);
                    refreshChart(showItem);
                } else {
                    if (timer != null) {
                        timer.cancel();
                        timer = null;
                    }
                }
                break;
            case "indexRefresh2":
                if (load_animation.getVisibility() == View.VISIBLE) {
                    load_animation.setVisibility(View.GONE);
                }
                if (map.get("1") == null && textLayout.getVisibility() == View.GONE) {
                    textLayout.setVisibility(View.VISIBLE);
                }
                break;
            case "indexRefresh3":
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
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        float density = metric.density;
        width = metric.widthPixels - (int) (20 * density + 0.5f);
        height = (int) (190 * density + 0.5f);
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

    private void InitList(final String stockCode) {
        final Map<String, String> param = new HashMap<>();
        param.put("fullcode", stockCode);

        GGOKHTTP.GGHttpInterface httpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {

                StockDetailMarketIndexBean bean = JSONObject.parseObject(responseInfo, StockDetailMarketIndexBean.class);

                if (JSONObject.parseObject(responseInfo).getIntValue("code") == 0) {
                    info = bean.getData();
                    //涨跌
                    if (info.get(0).getPrice_change() > 0) {
                        setStatusColorId(R.color.header_red);
                        relative_header.setBackgroundColor(getResColor(R.color.header_red));
                        linear_header.setBackgroundColor(getResColor(R.color.header_red));
                        initRefreshStyle(R.color.header_red);
                        stock_price.setText(StringUtils.save2Significand(info.get(0).getPrice()));//股票价格
                        stock_start.setText(StringUtils.save2Significand(info.get(0).getOpen_price()));//开盘价
                        stock_detail_tv1.setText(StockUtils.plusMinus(info.get(0).getPrice_change(), false));
                        stock_detail_tv2.setText(StockUtils.plusMinus(info.get(0).getPrice_change_rate(), true));

                    } else if (info.get(0).getPrice_change() < 0) {
                        setStatusColorId(R.color.header_green);
                        relative_header.setBackgroundColor(getResColor(R.color.header_green));
                        linear_header.setBackgroundColor(getResColor(R.color.header_green));
                        initRefreshStyle(R.color.header_green);

                        stock_price.setText(StringUtils.save2Significand(info.get(0).getPrice()));//股票价格
                        stock_start.setText(StringUtils.save2Significand(info.get(0).getOpen_price()));//开盘价
                        stock_detail_tv1.setText(StringUtils.save2Significand(info.get(0).getPrice_change()));
                        stock_detail_tv2.setText(StockUtils.plusMinus(info.get(0).getPrice_change_rate(), true));

                    } else {
                        setStatusColorId(R.color.header_gray);
                        relative_header.setBackgroundColor(getResColor(R.color.header_gray));
                        linear_header.setBackgroundColor(getResColor(R.color.header_gray));
                        initRefreshStyle(R.color.header_gray);
                        stock_price.setText(StringUtils.save2Significand(info.get(0).getPrice()));//股票价格
                        stock_start.setText(StringUtils.save2Significand(info.get(0).getOpen_price()));//开盘价
                        stock_detail_tv1.setText(StringUtils.save2Significand(info.get(0).getPrice_change()));
                        stock_detail_tv2.setText(StockUtils.plusMinus(info.get(0).getPrice_change_rate(), true));
                    }

                    stock_volume.setText(GetVolume(info.get(0).getVolume()));//成交量
                    stock_close.setText(StringUtils.save2Significand(info.get(0).getClose_price()));//收盘价
                    stock_amplitude.setText(StockUtils.plusMinus(info.get(0).getAmplitude(), true));//振幅
                    stock_detail_tv3.setText(StringUtils.save2Significand(info.get(0).getHigh_price()));//最高
                    stock_detail_tv3.setTextColor(getResColor(StockUtils.getStockRateColor(
                            cn.gogoal.im.common.StringUtils.parseStringDouble(info.get(0).getHigh_price()) -
                                    info.get(0).getClose_price())));

                    stock_detail_tv5.setText(StringUtils.save2Significand(info.get(0).getLow_price()));//最低
                    stock_detail_tv5.setTextColor(getResColor(StockUtils.getStockRateColor(
                            cn.gogoal.im.common.StringUtils.parseStringDouble(info.get(0).getLow_price()) -
                                    info.get(0).getClose_price())));

                    String turnover = String.valueOf(Math.ceil(info.get(0).getTurnover() / 10000));

                    stock_detail_tv7.setText(
                            String.format(Locale.getDefault(), getString(R.string.thousand_million),
                                    cn.gogoal.im.common.StringUtils.saveSignificand(turnover, 0)));//成交额

                    //监听当前滚动位置，动态改变状态
                    scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
                        @Override
                        public void onScrollChanged() {
                            int[] location = new int[2];
                            linear_header.getLocationInWindow(location);
//                            int x = location[0];
                            int y = location[1];

                            if (y > pixels) {
                                StockState();
                            } else {
                                String result = StringUtils.save2Significand(info.get(0).getPrice()) + "  "
                                        + StringUtils.save2Significand(info.get(0).getPrice_change()) + "  "
                                        + StringUtils.save2Significand(info.get(0).getPrice_change_rate()) + "%";

                                text_state.setText(result);
                            }
                        }
                    });
                }
                stopAnimation();
                refreshCompalte();
            }

            @Override
            public void onFailure(String msg) {
                refreshCompalte();
                stopAnimation();
                UIHelper.toast(getApplicationContext(), "请检查网络");
            }
        };
        new GGOKHTTP(param, GGOKHTTP.GET_HQ, httpInterface).startGet();
    }

    private void refreshCompalte() {
        //显示刷新完毕提示
//        headerView.over();
        //延时1s收回下拉头部
//        ptrFrame.postDelayed(new Runnable() {
//            @Override
//            public void run() {
////                ptrFrame.refreshComplete();
//                ptrFrame.setRefreshing(false);
//            }
//        }, 1000);
    }

    //股票成交量
    private String GetVolume(String volume) {
        if (volume.length() > 10) {
            Double num = Double.parseDouble(volume);
            volume = String.valueOf(StringUtils.save2Significand(num / 10000000000d)) + "亿手";
        } else {
            String num = StringUtils.save2Significand((Long.parseLong(volume) / 1000000));
            volume = StringUtils.getIntegerData(num) + "万手";
        }
        return volume;
    }

    //股票状态
    private void StockState() {
        SimpleDateFormat df = new SimpleDateFormat("MM-dd HH:mm", Locale.getDefault());
        text_state.setText(StockUtils.getTreatState() + " " + df.format(new Date()));
        text_state.setTextColor(Color.WHITE);
        text_state.setAlpha((float) 0.7);
    }

    @OnClick({R.id.tv_increase_layout,
            R.id.tv_drop_layout,
            R.id.tv_turnover_layout,
            R.id.iv_show_info_dialog})
    public void allClick(View v) {
        switch (v.getId()) {
            case R.id.tv_increase_layout:
                if (tv_line_increase.getVisibility() != View.VISIBLE) {
                    tv_increase.setTextColor(getResColor(R.color.red));
                    tv_line_increase.setVisibility(View.VISIBLE);
                    tv_drop.setTextColor(getResColor(R.color.text_color_tab));
                    tv_line_drop.setVisibility(View.GONE);
                    tv_turnover.setTextColor(getResColor(R.color.text_color_tab));
                    tv_line_turnover.setVisibility(View.GONE);
                    load_animation_change.setVisibility(View.VISIBLE);
                    stock_lv.setVisibility(View.GONE);
                    seat = 0;
                    StockDatas.clear();
                    getMarketInformation(seat, getStockCode);
                }
                break;
            case R.id.tv_drop_layout:
                if (tv_line_drop.getVisibility() != View.VISIBLE) {
                    tv_increase.setTextColor(getResColor(R.color.text_color_tab));
                    tv_line_increase.setVisibility(View.GONE);
                    tv_drop.setTextColor(getResColor(R.color.red));
                    tv_line_drop.setVisibility(View.VISIBLE);
                    tv_turnover.setTextColor(getResColor(R.color.text_color_tab));
                    tv_line_turnover.setVisibility(View.GONE);
                    load_animation_change.setVisibility(View.VISIBLE);
                    stock_lv.setVisibility(View.GONE);
                    seat = 1;
                    StockDatas.clear();
                    getMarketInformation(seat, getStockCode);
                }
                break;
            case R.id.tv_turnover_layout:
                if (tv_line_turnover.getVisibility() != View.VISIBLE) {
                    tv_increase.setTextColor(getResColor(R.color.text_color_tab));
                    tv_line_increase.setVisibility(View.GONE);
                    tv_drop.setTextColor(getResColor(R.color.text_color_tab));
                    tv_line_drop.setVisibility(View.GONE);
                    tv_turnover.setTextColor(getResColor(R.color.red));
                    tv_line_turnover.setVisibility(View.VISIBLE);
                    load_animation_change.setVisibility(View.VISIBLE);
                    stock_lv.setVisibility(View.GONE);
                    seat = 2;
                    StockDatas.clear();
                    getMarketInformation(seat, getStockCode);
                }
                break;
            case R.id.iv_show_info_dialog:
                if (isMaskViewVisiable()) {
                    dismissMarket();
                } else {
                    showStockInfoDialog();
                }
                break;
        }
    }

    private void getMarketInformation(final int seat, String getStockCode) {
        //type=0;1&channel=sh.000001
        final Map<String, String> param = new HashMap<>();
        param.put("type", String.valueOf(seat));
        param.put("channel", getStockCode);

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {

                StockDatas.clear();
                StockRankBean bean = JSONObject.parseObject(responseInfo, StockRankBean.class);

                if (bean.getCode() == 0) {
                    stock_detail_tv4.setText(bean.getData().getRed_counts());//涨家数
                    stock_detail_tv6.setText(bean.getData().getGray_counts());//平家数
                    stock_detail_tv8.setText(bean.getData().getGreen_counts());//跌家数

                    switch (seat) {
                        case 0:
                            StockDatas.addAll(bean.getData().getIncrease_list());
                            stock_lv.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            break;
                        case 1:
                            StockDatas.addAll(bean.getData().getDown_list());
                            stock_lv.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            break;
                        case 2:
                            StockDatas.addAll(bean.getData().getChange_list());
                            stock_lv.setAdapter(changeAdapter);
                            changeAdapter.notifyDataSetChanged();
                            break;
                    }
                    stock_lv.setVisibility(View.VISIBLE);
                    stock_no_data.setVisibility(View.GONE);
                } else if (bean.getCode() == 1001) {
                    stock_lv.setVisibility(View.GONE);
                    stock_no_data.setVisibility(View.VISIBLE);
                }
                load_animation_change.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(String msg) {
                try {
                    load_animation_change.setVisibility(View.GONE);
                    stock_lv.setVisibility(View.GONE);
                    stock_no_data.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    e.getMessage();
                }
            }
        };
        new GGOKHTTP(param, GGOKHTTP.STOCK_RANK_LIST, ggHttpInterface).startGet();
    }

    private class BitmapTask extends AsyncTask<String, Void, Bitmap> {
        private String item_index;

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bitmap;
            int dpi = DeviceUtil.getWidth(StockDetailMarketIndexActivity.this);
            item_index = params[0];
            switch (item_index) {
                case "0":
                    timesBitmap = new TimesFivesBitmap(width, height);
                    timesBitmap.setShowDetail(false);
                    timesBitmap.setLongitudeNum(0);
                    timesBitmap.setmSize(AppDevice.dp2px(StockDetailMarketIndexActivity.this, 1));
                    timesBitmap.setmSpaceSize(AppDevice.dp2px(StockDetailMarketIndexActivity.this, 3));
                    timesBitmap.setmAxisTitleSize(AppDevice.dp2px(StockDetailMarketIndexActivity.this, 10));

                    bitmap = timesBitmap.setTimesList(timesBean, true, 1);
                    map.put(item_index, bitmap);
                    break;
                case "1":
                    if (params.length > 1) {
                        StockMinuteBean bean = JSONObject.parseObject(params[1], StockMinuteBean.class);
                        fiveDayBitmap = new TimesFivesBitmap(width, height);
                        fiveDayBitmap.setShowDetail(false);
                        fiveDayBitmap.setLongitudeNum(4);
                        fiveDayBitmap.setmSize(AppDevice.dp2px(StockDetailMarketIndexActivity.this, 1));
                        fiveDayBitmap.setmSpaceSize(AppDevice.dp2px(StockDetailMarketIndexActivity.this, 3));
                        fiveDayBitmap.setmAxisTitleSize(AppDevice.dp2px(StockDetailMarketIndexActivity.this, 10));

                        bitmap = fiveDayBitmap.setTimesList(bean, false, 1);
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
                        kChartsBitmap.setmAxisTitleSize(AppDevice.dp2px(StockDetailMarketIndexActivity.this, 10));
                        kChartsBitmap.setmSize(AppDevice.dp2px(StockDetailMarketIndexActivity.this, 1));
                        kChartsBitmap.setmSpaceSize(AppDevice.dp2px(StockDetailMarketIndexActivity.this, 3));

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
                        kChartsBitmap1.setUperLatitudeNum(1);
                        kChartsBitmap1.setLongitudeNum(1);
                        kChartsBitmap1.setmSpaceSize(AppDevice.dp2px(StockDetailMarketIndexActivity.this, 3));
                        kChartsBitmap1.setmAxisTitleSize(AppDevice.dp2px(StockDetailMarketIndexActivity.this, 10));
                        kChartsBitmap1.setmSize(AppDevice.dp2px(StockDetailMarketIndexActivity.this, 1));

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
                        kChartsBitmap2.setUperLatitudeNum(1);
                        kChartsBitmap2.setLongitudeNum(1);
                        kChartsBitmap2.setmSpaceSize(AppDevice.dp2px(StockDetailMarketIndexActivity.this, 3));
                        kChartsBitmap2.setmAxisTitleSize(AppDevice.dp2px(StockDetailMarketIndexActivity.this, 10));
                        kChartsBitmap2.setmSize(AppDevice.dp2px(StockDetailMarketIndexActivity.this, 1));
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
            if (load_animation.getVisibility() == View.VISIBLE) {
                load_animation.setVisibility(View.GONE);
            }
            switch (String.valueOf(showItem)) {
                case "0":
                    if (StockUtils.isTradeTime()) {
                        mBitmapChartView.setBitmap(map.get(String.valueOf("0")), true, timesBitmap);
                    } else {
                        mBitmapChartView.setBitmap(map.get(String.valueOf("0")), false, timesBitmap);
                    }
                    break;
                case "1":
                    if (StockUtils.isTradeTime()) {
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
            JSONObject singleData = (JSONObject) data.get(i);
            Map<String, Object> itemData = new HashMap<>();
            itemData.put("amplitude", singleData.getFloat("amplitude"));
            itemData.put("avg_price_" + dayk1, ParseNum(singleData.getString("avg_price_" + dayk1)));
            itemData.put("avg_price_" + dayk2, ParseNum(singleData.getString("avg_price_" + dayk2)));
            itemData.put("avg_price_" + dayk3, ParseNum(singleData.getString("avg_price_" + dayk3)));
            itemData.put("avg_price_" + dayk4, ParseNum(singleData.getString("avg_price_" + dayk4)));
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
        float avg_price;
        if (s == null) {
            avg_price = 0.0f;
        } else {
            avg_price = Float.parseFloat(s);
        }
        return avg_price;
    }

    //=======================================20170614========================================
    private boolean isMaskViewVisiable() {
        return layoutDialog.getVisibility() == View.VISIBLE;
    }

    private void dismissMarket() {
        if (isMaskViewVisiable()) {
            layoutDialog.setVisibility(View.GONE);

//            layoutDialog.startAnimation(
//                    android.view.animation.AnimationUtils.loadAnimation(getActivity(), R.anim.slide_out_from_top));

            viewMask.setClickable(false);
            viewMask.setEnabled(false);//防止重复点击反复出现
            viewMask.setVisibility(View.GONE);
            viewMask.startAnimation(
                    android.view.animation.AnimationUtils.loadAnimation(getActivity(), R.anim.alpha_out
                    ));

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

    //显示弹窗
    private void showStockInfoDialog() {
//        layoutDialog.startAnimation(
//                android.view.animation.AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_from_top));
        layoutDialog.setVisibility(View.VISIBLE);

        viewMask.setEnabled(true);
        viewMask.setClickable(true);
        viewMask.setVisibility(View.VISIBLE);
        viewMask.startAnimation(
                android.view.animation.AnimationUtils.loadAnimation(getActivity(), R.anim.alpha_in));

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

}
