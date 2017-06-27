//package cn.gogoal.im.activity.copy;
//
//import android.content.Context;
//import android.content.Intent;
//import android.graphics.Color;
//import android.graphics.drawable.Drawable;
//<<<<<<< HEAD
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.os.Handler;
//=======
//import android.support.annotation.ColorInt;
//>>>>>>> ff5672b932aea7ea1b8a3abf96258ddc5dc05fef
//import android.support.design.widget.TabLayout;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentPagerAdapter;
//import android.support.v4.content.ContextCompat;
//import android.support.v4.view.ViewPager;
//import android.support.v4.widget.NestedScrollView;
//import android.support.v4.widget.SwipeRefreshLayout;
//import android.support.v7.widget.GridLayoutManager;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.Gravity;
//import android.view.KeyEvent;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewTreeObserver;
//import android.view.animation.RotateAnimation;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import com.socks.library.KLog;
//
//import org.simple.eventbus.Subscriber;
//
//import java.io.File;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Locale;
//import java.util.Map;
//import java.util.Timer;
//import java.util.TimerTask;
//
//import butterknife.BindArray;
//import butterknife.BindView;
//import butterknife.OnClick;
//import cn.gogoal.im.R;
//import cn.gogoal.im.activity.MessageHolderActivity;
//import cn.gogoal.im.activity.ShareMessageActivity;
//import cn.gogoal.im.activity.SquareChatRoomActivity;
//import cn.gogoal.im.activity.ToolsSettingActivity;
//import cn.gogoal.im.activity.stock.InteractiveInvestorActivity;
//import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
//import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
//import cn.gogoal.im.base.AppManager;
//import cn.gogoal.im.base.BaseActivity;
//import cn.gogoal.im.bean.BaseMessage;
//import cn.gogoal.im.bean.ToolData;
//import cn.gogoal.im.bean.stock.Stock;
//import cn.gogoal.im.bean.stock.StockDetail;
//import cn.gogoal.im.bean.stock.StockDetail2Text;
//import cn.gogoal.im.bean.stock.TreatData;
//import cn.gogoal.im.common.AnimationUtils;
//import cn.gogoal.im.common.AppConst;
//import cn.gogoal.im.common.AppDevice;
//import cn.gogoal.im.common.ArrayUtils;
//import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
//import cn.gogoal.im.common.IMHelpers.ChatGroupHelper;
//import cn.gogoal.im.common.IMHelpers.MessageListUtils;
//import cn.gogoal.im.common.ImageUtils.ImageUtils;
//import cn.gogoal.im.common.Impl;
//import cn.gogoal.im.common.SPTools;
//import cn.gogoal.im.common.StockUtils;
//import cn.gogoal.im.common.StringUtils;
//import cn.gogoal.im.common.UIHelper;
//import cn.gogoal.im.common.UserUtils;
//import cn.gogoal.im.fragment.stock.CompanyFinanceFragment;
//import cn.gogoal.im.fragment.stock.CompanyInfoFragment;
//import cn.gogoal.im.fragment.stock.StockNewsMinFragment;
//import cn.gogoal.im.ui.Badge.BadgeView;
//import cn.gogoal.im.ui.dialog.NormalAlertDialog;
//import cn.gogoal.im.ui.dialog.StockDetailPopuDialog;
//import cn.gogoal.im.ui.dialog.WaitDialog;
//import cn.gogoal.im.ui.stock.DialogRecyclerView;
//import cn.gogoal.im.ui.widget.WrapContentHeightViewPager;
//import hply.com.niugu.autofixtext.AutofitTextView;
//
//
///*
//* 普通股票详情
//* */
//public class CopyStockDetailActivity extends BaseActivity {
//
//    //返回
//    @BindView(R.id.btnBack)
//    LinearLayout btnBack;
//
//    //标题栏
//    @BindView(R.id.title_header)
//    RelativeLayout title_header;
//
//    //标题
//    @BindView(R.id.textHeadTitle)
//    TextView textHeadTitle;
//
//    //交易状态及时间
//    @BindView(R.id.text_state)
//    TextView text_state;
//
//    //刷新
//    @BindView(R.id.btnRefresh)
//    ImageView btnRefresh;
//
//    //数据栏
//    @BindView(R.id.linear_header)
//    LinearLayout linear_header;
//
//    //下拉刷新
//    @BindView(R.id.scrollView)
//    NestedScrollView scrollView;
//
//    //股票价格
//    @BindView(R.id.stock_price)
//    AutofitTextView stock_price;
//
//    //涨跌
//    @BindView(R.id.stock_detail_tv1)
//    AutofitTextView tvFluctuationValue;
//
//    @BindView(R.id.stock_detail_tv2)
//    AutofitTextView tvFluctuationRate;
//
//    //今开
//    @BindView(R.id.stock_start)
//    AutofitTextView stock_start;
//
//    //成交量
//    @BindView(R.id.stock_volume)
//    AutofitTextView stock_volume;
//
//    //昨收
//    @BindView(R.id.stock_close)
//    AutofitTextView stock_close;
//
//    //换手率
//    @BindView(R.id.stock_turnover_rate)
//    TextView stock_turnover_rate;
//
//    //停牌
//    @BindView(R.id.text_delist)
//    TextView text_delist;
//
//    @BindView(R.id.stock_detail_delist)
//    TextView stock_detail_delist;
//
//    //下拉刷新rootView
//    @BindView(R.id.fragment_rotate_header_with_view_group_frame)
//    SwipeRefreshLayout ptrFrame;
//
//    //下拉刷新头部控件
//    //private HeaderView headerView;
//    private int stock_type = 1;
//
//    @BindArray(R.array.stock_detail_chart_titles)
//    String[] stockDetailChartTitles;
//
//    //加自选
//    @BindView(R.id.tv_stockDetail_toggle_mystock)
//    TextView stock_detail_choose;
//
//    private int pixels = -85;
//    private String stockName;
//    private String stockCode;
//    private ArrayList<String> priceVolumDatas = new ArrayList<>();
//    //定时刷新
//    private Timer timer;
//    private double closePrice;
//
//    private boolean isChoose = true;
//
//    //修改的中间新闻模块
//    @BindView(R.id.tablayout_news_)
//    TabLayout tabLayoutNews;
//
//    @BindView(R.id.vp_news_)
//    WrapContentHeightViewPager viewPagerNews;
//
//    private String[] newTitles = {"新闻", "公告", "研报", "资料", "财务"};
//
//    private RotateAnimation rotateAnimation;
//
//    private String change_value;
//
//    //=====================20170613===================
//    @BindView(R.id.rv_stock_info)
//    DialogRecyclerView rvStockInfo;
//
//    @BindView(R.id.view_dialog_mask)
//    View viewMask;
//
//    @BindView(R.id.view_dialog_mask_bottom)
//    View viewMaskBottom;
//
//    @BindView(R.id.iv_show_info_dialog)
//    ImageView imageViewShoeDialog;
//
//    @BindView(R.id.iv_message_tag)
//    ImageView ivMessageTag;
//
//    @BindArray(R.array.stock_detail_info)
//    String[] stockDetailInfos;
//
//    //消息
//    private BadgeView badge;
//    private int unReadCount;
//
//    //是否分享消息
//    private int num;
//    private WaitDialog waitDialog;
//
//    private StockInfoDialogAdapter infoDialogAdapter;
//
//    private List<StockDetail2Text> stockDialogInfoList;
//
//    private ArrayList<ToolData.Tool> diagnoseStockTools;//诊断工具
//
//    @Override
//    public int bindLayout() {
//        return R.layout.copy_stock_detail;
//    }
//
//    @Override
//    public void doBusiness(Context mContext) {
//
//        //找控件
//        findView();
//        //初始化
//        init();
//
//        setNewsTab();
//
//        getStockInfoDialog();
//
//        initList(stockCode);
//
//        initChatMessage();
//
//        diagnoseStockTools = new ArrayList<>();
//    }
//
//    private void initChatMessage() {
//        badge = new BadgeView(getActivity());
//        initBadge(unReadCount, badge);
//        ivMessageTag.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(CopyStockDetailActivity.this, MessageHolderActivity.class));
//            }
//        });
//    }
//
//    /***/
//    private void setNewsTab() {
//        final List<Fragment> fragments = new ArrayList<>();
//        /*
//         * stockCode 股票代码
//         * stockName 股票代码
//         * position 第几个tab
//         * */
//        fragments.add(StockNewsMinFragment.getInstance(stockCode, stockName, 0));
//        fragments.add(StockNewsMinFragment.getInstance(stockCode, stockName, 1));
//        fragments.add(StockNewsMinFragment.getInstance(stockCode, stockName, 2));
//        fragments.add(CompanyInfoFragment.newInstance(stockCode));
//        fragments.add(CompanyFinanceFragment.getInstance(stockCode, stockName));
//
//        viewPagerNews.setOffscreenPageLimit(5);
//        viewPagerNews.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
//            public Fragment getItem(int position) {
//                return fragments.get(position);
//            }
//
//            public int getCount() {
//                return newTitles.length;
//            }
//
//            public CharSequence getPageTitle(int position) {
//                return newTitles[position];
//            }
//        });
//        tabLayoutNews.setupWithViewPager(viewPagerNews);
//
//        viewPagerNews.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                AppManager.getInstance().sendMessage("StockDetailNewsFragment_TAB", new BaseMessage(String.valueOf(position)));
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });
//
//        AppDevice.setTabLayoutWidth(tabLayoutNews, 15);
//    }
//
//    private void findView() {
//        BaseActivity.iniRefresh(ptrFrame);
//        scrollView.smoothScrollTo(0, 20);
//    }
//
//    //初始化
//    private void init() {
//        stockCode = getIntent().getStringExtra("stock_code");
//        stockName = getIntent().getStringExtra("stock_name");
//        num = getIntent().getIntExtra("num", 0);
//        waitDialog = WaitDialog.getInstance("加载中", R.mipmap.login_loading, true);
//
//        if (num == AppConst.TYPE_GET_STOCK) {
//            waitDialog.show(getSupportFragmentManager());
//            waitDialog.setCancelable(false);
//        }
//
//        textHeadTitle.setText(stockName + "(" + stockCode + ")");
//
//        if (StockUtils.isMyStock(stockCode)) {
//            toggleIsMyStock(true, false);
//        } else {
//            toggleIsMyStock(false, false);
//        }
//
//        //返回
//        btnBack.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//        //点击刷新
//        btnRefresh.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                refreshAll();
//            }
//        });
//
//
//        // 修改状态栏颜色
//        setStatusColorId(R.color.header_gray);
//
//        title_header.setBackgroundResource(R.color.header_gray);
//        linear_header.setBackgroundResource(R.color.header_gray);
//
//        //股票状态和当前时间
//        StockState();
//
//        //设置下拉刷新属性
//        ptrFrame.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                refreshAll();
//                ptrFrame.setRefreshing(false);
//            }
//        });
//
//        linear_header.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                imageViewShoeDialog.performClick();
//            }
//        });
//    }
//
//    private void refreshAll() {
//        StockState();
//        startAnimation();
//        initList(stockCode);
//        AppManager.getInstance().sendMessage("updata_treat_data");
//    }
//
//    void stopAnimation() {
//        if (rotateAnimation != null) {
//            AnimationUtils.getInstance().cancleLoadingAnime(rotateAnimation, btnRefresh, R.mipmap.refresh_white);
//        } else {
//            btnRefresh.clearAnimation();
//            btnRefresh.setImageResource(R.mipmap.refresh_white);
//        }
//    }
//
//    void startAnimation() {
//        rotateAnimation = AnimationUtils.getInstance().setLoadingAnime(btnRefresh, R.mipmap.loading_white);
//        rotateAnimation.startNow();
//    }
//
//    //定时自动刷新
//    private void AutoRefresh(long time) {
//        if (StockUtils.isTradeTime()) {
//            setTime(time);
//        } else {
//            if (timer != null) {
//                timer.cancel();
//                timer = null;
//            }
//        }
//    }
//
//    public void setTime(long num) {
//        TimerTask timerTask = new TimerTask() {
//            @Override
//            public void run() {
//                AppManager.getInstance().sendMessage("CopyStockDetailActivity", new BaseMessage("autoRefresh1"));
//            }
//        };
//        if (timer == null) {
//            timer = new Timer();
//        }
//        timer.scheduleAtFixedRate(timerTask, num, num);
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        long refreshtime = SPTools.getLong("interval_time", 15000);
//        timer = new Timer();
//        AutoRefresh(refreshtime);
//
//        unReadCount = MessageListUtils.getAllMessageUnreadCount();
//        badge.setBadgeNumber(unReadCount);
//
//        getMyTools();
//
//        if (StockUtils.isMyStock(stockCode)) {
//            toggleIsMyStock(true, false);
//        } else {
//            toggleIsMyStock(false, false);
//        }
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        if (timer != null) {
//            timer.cancel();
//        }
//    }
//
//    /**
//     * 初始化交易数据
//     *
//     * @param stockCode;
//     */
//    private void initList(final String stockCode) {
//        final Map<String, String> param = new HashMap<>();
//        param.put("stock_code", stockCode);
//        GGOKHTTP.GGHttpInterface httpInterface = new GGOKHTTP.GGHttpInterface() {
//
//            @Override
//            public void onSuccess(String responseInfo) {
//
//                StockDetail bean = JSONObject.parseObject(responseInfo, StockDetail.class);
//
//                if (bean.getCode() == 0) {
//
//                    final TreatData info = bean.getData();
//
//                    //20170613
//                    infoDialogAdapter.setClosePrice(info.getClose_price());
//                    infoDialogAdapter.setStockType(info.getStock_type());
//                    setDialogInfoData(info);
//
//                    //保存收盘价
//                    stock_type = info.getStock_type();
//
//                    closePrice = hply.com.niugu.StringUtils.getDouble(String.valueOf(info.getClose_price()));
//                    change_value = info.getChange_value();
//                    StockUtils.savaColseprice((float) closePrice);
//
//                    priceVolumDatas.clear();
//                    stock_price.setText(StringUtils.parseStringDouble(info.getPrice(), 2));//股票价格
//                    stock_start.setText(StringUtils.parseStringDouble(info.getOpen_price(), 2));//开盘价
//
//                    //根据Change_value设置颜色
//                    @ColorInt int stockColor = ContextCompat.getColor(
//                            getActivity(),
//                            StockUtils.getStockRateColor(info.getChange_value()));
//
//                    setStatusColor(stockColor);
//                    title_header.setBackgroundColor(stockColor);
//                    linear_header.setBackgroundColor(stockColor);
//                    tvFluctuationValue.setText(StockUtils.plusMinus(info.getChange_value(),false));
//                    tvFluctuationRate.setText(StockUtils.plusMinus(info.getChange_rate(), true));
//
//                    if (info.getVolume() == null) {
//                        stock_volume.setText("0手");//成交量
//                    } else {
//                        if (info.getVolume().length() > 6) {
//
//                            stock_volume.setText(StringUtils.save2Significand(StringUtils.parseStringDouble(info.getVolume()) / 1000000) + "万手");//成交量
//                        } else {
//                            String volume = String.valueOf((Double.parseDouble(info.getVolume()) / 100));
//                            stock_volume.setText(StringUtils.getIntegerData(volume) + "手");
//                        }
//                    }
//                    stock_turnover_rate.setText(StringUtils.save2Significand(
//                            StringUtils.parseStringDouble(info.getTurnover_rate()) * 100) + "%");//换手率
//                    if (stock_type == 1) {
//                        text_state.setVisibility(View.VISIBLE);
//                        text_delist.setVisibility(View.GONE);
//                        tvFluctuationValue.setVisibility(View.VISIBLE);
//                        tvFluctuationRate.setVisibility(View.VISIBLE);
//                        stock_detail_delist.setVisibility(View.GONE);
//                    } else {
//                        //停牌处理
//                        text_state.setVisibility(View.GONE);
//                        text_delist.setVisibility(View.VISIBLE);
//                        text_delist.setText(StringUtils.save2Significand(info.getClose_price()) + " " + StockUtils.getStockStatus(stock_type));
//                        tvFluctuationValue.setVisibility(View.GONE);
//                        tvFluctuationRate.setVisibility(View.GONE);
//                        stock_detail_delist.setVisibility(View.VISIBLE);
//                        stock_detail_delist.setText(StockUtils.getStockStatus(stock_type));
//                    }
//                    stock_close.setText(StringUtils.save2Significand(info.getClose_price()));//收盘价
//                    //监听当前滚动位置，动态改变状态
//
//                    scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
//                        @Override
//                        public void onScrollChanged() {
//                            int[] location = new int[2];
//                            linear_header.getLocationInWindow(location);
//                            int x = location[0];
//                            int y = location[1];
//                            if (y > pixels) {
//                                StockState();
//                            } else {
//                                String result = StringUtils.save2Significand(info.getPrice()) + "  "
//                                        + StringUtils.save2Significand(info.getChange_value()) + "  "
//                                        + StringUtils.save2Significand(info.getChange_rate()) + "%";
//
//                                text_state.setText(result);
//                            }
//                            scrollView.getViewTreeObserver().removeOnScrollChangedListener(this);
//                        }
//                    });
//
//                    //退市数据处理 停牌 停市 未上市
//                    if (stock_type == 0 || stock_type == -1 || stock_type == -2) {//退市/未上市
//                        setZero2Line(stock_price);
//                        setZero2Line(stock_start);
//                        setZero2Line(stock_close);
//                        setZero2Line(stock_volume);
//                        setZero2Line(stock_turnover_rate);
//                    }
//                }
//                stopAnimation();
//            }
//
//            @Override
//            public void onFailure(String msg) {
//                stopAnimation();
//                UIHelper.toast(getApplicationContext(), "请检查网络");
//                KLog.e(msg);
//            }
//        };
//        new GGOKHTTP(param, GGOKHTTP.ONE_STOCK_DETAIL, httpInterface).startGet();
//    }
//
//    private double $(String volume_outer) {
//        return StringUtils.parseStringDouble(volume_outer);
//    }
//
//    private void setZero2Line(TextView tv) {
//        try {
//            String text = tv.getText().toString();
//            if (!TextUtils.isEmpty(text)) {
//                if (Double.parseDouble(text) == 0 || text.equals("0手") || text.equals("0.00%")) {
//                    tv.setText("--");
//                }
//            }
//        } catch (Exception e) {
//            tv.setText("--");
//        }
//    }
//
//    //股票状态
//    private void StockState() {
//        SimpleDateFormat df = new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA);
//        text_state.setText(StockUtils.getTreatState() + " " + df.format(new Date()));
//        text_state.setTextColor(Color.WHITE);
//        text_state.setAlpha((float) 0.7);
//    }
//
//    //异步画图
//    private class BitmapTask extends AsyncTask<String, Void, Bitmap> {
//        private String item_index;
//
//        @Override
//        protected Bitmap doInBackground(String... params) {
//            Bitmap bitmap = null;
//            item_index = params[0];
//            switch (item_index) {
//                case "0":
//                    timesBitmap = new TimesFivesBitmap(13 * width / 20, height);
//                    timesBitmap.setShowDetail(false);
//                    timesBitmap.setLongitudeNum(0);
//                    timesBitmap.setmSize(AppDevice.dp2px(CopyStockDetailActivity.this, 1));
//                    timesBitmap.setmSpaceSize(AppDevice.dp2px(CopyStockDetailActivity.this, 3));
//                    timesBitmap.setmAxisTitleSize(AppDevice.dp2px(CopyStockDetailActivity.this, 10));
//                    try {
//                        bitmap = timesBitmap.setTimesList(timesBean, true, stock_charge_type);
//                    } catch (Exception e) {
//                    }
//                    map.put(item_index, bitmap);
//                    break;
//                case "1":
//                    if (params.length > 1) {
//                        StockMinuteBean bean = JSONObject.parseObject(params[1], StockMinuteBean.class);
//                        fiveDayBitmap = new TimesFivesBitmap(width, height);
//                        fiveDayBitmap.setShowDetail(false);
//                        fiveDayBitmap.setLongitudeNum(4);
//                        fiveDayBitmap.setmSpaceSize(AppDevice.dp2px(CopyStockDetailActivity.this, 3));
//                        fiveDayBitmap.setmSize(AppDevice.dp2px(CopyStockDetailActivity.this, 1));
//                        fiveDayBitmap.setmAxisTitleSize(AppDevice.dp2px(CopyStockDetailActivity.this, 10));
//
//                        bitmap = fiveDayBitmap.setTimesList(bean, false, stock_charge_type);
//                        map.put(item_index, bitmap);
//                    }
//                    break;
//                case "2":
//                    if (params.length > 1) {
//                        mOHLCData.clear();
//                        parseObjects(params[1]);
//                        KChartsBitmap kChartsBitmap = new KChartsBitmap(width, height);
//                        kChartsBitmap.setShowDetail(false);
//                        kChartsBitmap.setLongitudeNum(1);
//                        kChartsBitmap.setmSpaceSize(AppDevice.dp2px(CopyStockDetailActivity.this, 3));
//                        kChartsBitmap.setmAxisTitleSize(AppDevice.dp2px(CopyStockDetailActivity.this, 10));
//                        kChartsBitmap.setmSize(AppDevice.dp2px(CopyStockDetailActivity.this, 1));
//
//                        bitmap = kChartsBitmap.setOHLCData(mOHLCData);
//                        map.put(item_index, bitmap);
//                    }
//                    break;
//                case "3":
//                    if (params.length > 1) {
//                        mOHLCData.clear();
//                        parseObjects(params[1]);
//                        KChartsBitmap kChartsBitmap1 = new KChartsBitmap(width, height);
//                        kChartsBitmap1.setShowDetail(false);
//                        kChartsBitmap1.setLongitudeNum(1);
//                        kChartsBitmap1.setmSpaceSize(AppDevice.dp2px(CopyStockDetailActivity.this, 3));
//                        kChartsBitmap1.setmAxisTitleSize(AppDevice.dp2px(CopyStockDetailActivity.this, 10));
//                        kChartsBitmap1.setmSize(AppDevice.dp2px(CopyStockDetailActivity.this, 1));
//
//                        bitmap = kChartsBitmap1.setOHLCData(mOHLCData);
//                        map.put(item_index, bitmap);
//                    }
//                    break;
//                case "4":
//                    if (params.length > 1) {
//                        mOHLCData.clear();
//                        parseObjects(params[1]);
//                        KChartsBitmap kChartsBitmap2 = new KChartsBitmap(width, height);
//                        kChartsBitmap2.setShowDetail(false);
//                        kChartsBitmap2.setLongitudeNum(1);
//                        kChartsBitmap2.setmSpaceSize(AppDevice.dp2px(CopyStockDetailActivity.this, 3));
//                        kChartsBitmap2.setmAxisTitleSize(AppDevice.dp2px(CopyStockDetailActivity.this, 10));
//                        kChartsBitmap2.setmSize(AppDevice.dp2px(CopyStockDetailActivity.this, 1));
//
//                        bitmap = kChartsBitmap2.setOHLCData(mOHLCData);
//                        map.put(item_index, bitmap);
//                    }
//                    break;
//            }
//            return map.get(item_index);
//        }
//
//        @Override
//        protected void onPostExecute(Bitmap bitmap) {
//            super.onPostExecute(bitmap);
//            switch (String.valueOf(showItem)) {
//                case "0":
//                    if (stock_charge_type == 1 && StockUtils.isTradeTime()) {
//                        mBitmapChartView.setBitmap(map.get(String.valueOf("0")), true, timesBitmap);
//                    } else {
//                        mBitmapChartView.setBitmap(map.get(String.valueOf("0")), false, timesBitmap);
//                    }
//                    break;
//                case "1":
//                    if (stock_charge_type == 1 && StockUtils.isTradeTime()) {
//                        mBitmapChartView.setBitmap(map.get(String.valueOf("1")), true, fiveDayBitmap);
//                    } else {
//                        mBitmapChartView.setBitmap(map.get(String.valueOf("1")), false, fiveDayBitmap);
//                    }
//                    break;
//                case "2":
//                    mBitmapChartView.setBitmap(map.get(String.valueOf("2")));
//                    break;
//                case "3":
//                    mBitmapChartView.setBitmap(map.get(String.valueOf("3")));
//                    break;
//                case "4":
//                    mBitmapChartView.setBitmap(map.get(String.valueOf("4")));
//                    break;
//            }
//            if (load_animation.getVisibility() == View.VISIBLE) {
//                load_animation.setVisibility(View.GONE);
//            }
//            //加载消失,截屏开始
//            if (num == AppConst.TYPE_GET_STOCK) {
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        waitDialog.dismiss();
//                        int screenWidth = AppDevice.getWidth(CopyStockDetailActivity.this);
//                        final Bitmap bitmapMessage = ImageUtils.screenshot(scrollView, 0, 0, screenWidth, screenWidth);
//                        ImageUtils.saveImageToSD(getActivity(), getExternalCacheDir().getAbsolutePath() +
//                                File.separator +
//                                String.valueOf(System.currentTimeMillis()) + ".png", bitmapMessage, new Impl<String>() {
//                            @Override
//                            public void response(int code, String url) {
//                                HashMap<String, Object> map = new HashMap<>();
//                                map.put("stock_bitmap", bitmapMessage);
//                                map.put("stock_bitmapUrl", url);
//                                map.put("stock_name", stockName);
//                                map.put("stock_code", stockCode);
//                                BaseMessage baseMessage = new BaseMessage("stockInfo", map);
//                                AppManager.getInstance().sendMessage("oneStock", baseMessage);
//                                finish();
//                            }
//                        });
//                    }
//                }, 1000);
//
//            }
//        }
//    }
//
//    //popu，工具箱，股票
//    @OnClick({R.id.iv_show_info_dialog,
//            R.id.tv_stockDetail_tools, R.id.tv_stockDetail_stockCircle,
//            R.id.tv_stockDetail_interactiveinvestor, R.id.tv_stockDetail_toggle_mystock})
//    public void allBtnClick(View v) {
//        Intent intent = new Intent();
//        switch (v.getId()) {
//            case R.id.iv_show_info_dialog://交易popu
//                if (isMaskViewVisiable()) {
//                    dismissMarket();
//                } else {
//                    showStockInfoDialog();
//                }
//                break;
//
//            case R.id.tv_stockDetail_tools://工具箱
//
//                if (!ArrayUtils.isEmpty(diagnoseStockTools)) {
//                    StockDetailPopuDialog.newInstance(
//                            diagnoseStockTools,
//                            new Stock(stockCode, stockName))
//                            .show(getSupportFragmentManager());
//                } else {
//                    NormalAlertDialog.newInstance("暂无诊股工具\n现在去我的工具中添加?", new OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            startActivity(new Intent(getThisContext(), ToolsSettingActivity.class));
//                        }
//                    }).show(getSupportFragmentManager());
//                }
//
//                break;
//
//            case R.id.tv_stockDetail_toggle_mystock://tog 加减自选股
//                addOptionalShare();//TODO 更换新的删除自选股接口
//                break;
//            case R.id.tv_stockDetail_stockCircle://股票圈，进入后台群
//                getGroupChartConvsation();
//                break;
//            case R.id.tv_stockDetail_interactiveinvestor://投资者互动
//                intent.setClass(v.getContext(), InteractiveInvestorActivity.class);
//                intent.putExtra("stock_info", getStockBean());
//                startActivity(intent);
//                break;
//        }
//    }
//
//    private void getGroupChartConvsation() {
//        final WaitDialog waitDialog = WaitDialog.getInstance("请稍后...", R.mipmap.login_loading, true);
//        waitDialog.show(getSupportFragmentManager());
//
//        HashMap<String, String> params = UserUtils.getTokenParams();
//        params.put("stock_code", stockCode);
//        new GGOKHTTP(params, GGOKHTTP.GET_STOCK_GROUP_ID, new GGOKHTTP.GGHttpInterface() {
//            @Override
//            public void onSuccess(String responseInfo) {
//                KLog.e(responseInfo);
//                int code = JSONObject.parseObject(responseInfo).getIntValue("code");
//                if (code == 0) {
//                    try {
//                        final String conversationId = JSONObject.parseObject(responseInfo).
//                                getJSONObject("data").getString("conv_id");
//
//                        List<Integer> addIdList = new ArrayList<>();
//                        addIdList.add(Integer.parseInt(UserUtils.getMyAccountId()));
//                        //添加群成员
//                        ChatGroupHelper.addAnyone(addIdList, conversationId, new ChatGroupHelper.ChatGroupManager() {
//                            @Override
//                            public void groupActionSuccess(JSONObject object) {
//                                Intent intent = new Intent(getActivity(), SquareChatRoomActivity.class);
//                                intent.putExtra("conversation_id", conversationId);
//                                intent.putExtra("need_update", false);
//                                intent.putExtra("square_action", AppConst.CREATE_SQUARE_ROOM_BY_STOCK);
//                                startActivity(intent);
//                            }
//
//                            @Override
//                            public void groupActionFail(String error) {
//                                UIHelper.toast(getActivity(), "股票群初始化失败，请稍后重试！");
//                                Log.e("TAG", error);
//                            }
//                        });
//
//                    } catch (Exception e) {
//                        //返回数据异常
//                        UIHelper.toast(getActivity(), "股票群初始化失败，请稍后重试！");
//                        Log.e("TAG", e.getMessage());
//                    }
//                } else {//没有获取到ConvsationId
//                    UIHelper.toast(getActivity(), "股票群初始化失败，请稍后重试！");
//                }
//                waitDialog.dismiss();
//            }
//
//            @Override
//            public void onFailure(String msg) {//获取ConvsationId出错
//                waitDialog.dismiss();
//                UIHelper.toast(getActivity(), "请求异常");
//            }
//        }).startGet();
//    }
//
//    private Stock getStockBean() {
//        Stock stock = new Stock(stockCode, stockName);
//        stock.setChangeValue(StringUtils.save2Significand(change_value));
//        stock.setStock_type(stock_type);
//        stock.setClosePrice(closePrice);
//        return stock;
//    }
//
//    //添加自选股
//    private void addOptionalShare() {
//        final WaitDialog dialog = WaitDialog.getInstance("请稍后", R.mipmap.login_loading, true);
//        dialog.show(getSupportFragmentManager());
//
//        if (isChoose) {
//            final Map<String, String> param = new HashMap<String, String>();
//            param.put("token", UserUtils.getToken());
//            param.put("stock_code", stockCode);
//
//            GGOKHTTP.GGHttpInterface httpInterface = new GGOKHTTP.GGHttpInterface() {
//                @Override
//                public void onSuccess(String responseInfo) {
//                    JSONObject result = JSONObject.parseObject(responseInfo);
//                    int data = (int) result.get("code");
//                    if (data == 0) {
//                        toggleIsMyStock(false, true);
//                        StockUtils.removeStock(stockCode);
//                    }
//                    dialog.dismiss();
//                }
//
//                @Override
//                public void onFailure(String msg) {
//                    dialog.dismiss();
//                    UIHelper.toastError(getThisContext(), msg);
//                }
//            };
//            new GGOKHTTP(param, GGOKHTTP.MYSTOCK_DELETE, httpInterface).startGet();
//        } else {
//            final Map<String, String> param = new HashMap<String, String>();
//            param.put("token", UserUtils.getToken());
//            param.put("group_id", "0");
//            param.put("stock_code", stockCode);
//            param.put("stock_class", "0");
//            param.put("source", "9");
//            param.put("group_class", "1");
//
//            GGOKHTTP.GGHttpInterface httpInterface = new GGOKHTTP.GGHttpInterface() {
//                @Override
//                public void onSuccess(String responseInfo) {
//                    JSONObject result = JSONObject.parseObject(responseInfo);
//
//                    int data = (int) result.get("code");
//                    if (data == 0) {
//                        toggleIsMyStock(true, true);
//                        StockUtils.addStock2MyStock(stockCode);
//                    }
//                    dialog.dismiss();
//                }
//
//                @Override
//                public void onFailure(String msg) {
//                    dialog.dismiss();
//                    UIHelper.toastError(getThisContext(), msg);
//                }
//            };
//            new GGOKHTTP(param, GGOKHTTP.MYSTOCK_ADD, httpInterface).startGet();
//        }
//    }
//
//    public void toggleIsMyStock(boolean addMyStock, boolean needToast) {
//
//        Drawable drawable = ContextCompat.getDrawable(this,
//                addMyStock ? R.mipmap.not_choose_stock : R.mipmap.choose_stock);
//
//        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
//
//        stock_detail_choose.setCompoundDrawables(null, drawable, null, null);
////
//        stock_detail_choose.setText(addMyStock ? "已自选" : "加自选");
//
//        if (needToast) {
//            UIHelper.toast(getThisContext(), addMyStock ? "添加自选成功" : "删除自选成功");
//        }
//
//        isChoose = addMyStock;
//    }
//
//    //==============================================20170613=================================
//
//    //初始化
//    private void getStockInfoDialog() {
//        rvStockInfo.setLayoutManager(new
//                GridLayoutManager(getActivity(), 4, GridLayoutManager.VERTICAL, false));
//        stockDialogInfoList = new ArrayList<>();
//        infoDialogAdapter = new StockInfoDialogAdapter(stockDialogInfoList);
//        rvStockInfo.setAdapter(infoDialogAdapter);
//    }
//
//    /**
//     * 设置-修改个股详情为弹窗
//     */
//    private void setDialogInfoData(TreatData info) {
//        stockDialogInfoList.clear();
//        //最高价
//        stockDialogInfoList.add(new StockDetail2Text(stockDetailInfos[0],
//                StringUtils.parseStringDouble(info.getHigh_price(), 2)));
//
//        //最低价
//        stockDialogInfoList.add(new StockDetail2Text(stockDetailInfos[1],
//                StringUtils.parseStringDouble(info.getLow_price(), 2)));
//
//        //涨停
//        stockDialogInfoList.add(new StockDetail2Text(stockDetailInfos[2],
//                "" + StringUtils.save2Significand(StringUtils.parseStringDouble(info.getClose_price()) * (
//                        (stockName.startsWith("*") ||
//                                stockName.contains("ST") ||
//                                stockName.startsWith("N")) ? 1.05 : 1.10))));
//
//        //跌停
//        stockDialogInfoList.add(new StockDetail2Text(stockDetailInfos[3],
//                "" + StringUtils.save2Significand(StringUtils.parseStringDouble(info.getClose_price()) * (
//                        (stockName.startsWith("*") ||
//                                stockName.contains("ST") ||
//                                stockName.startsWith("N")) ? 0.95 : 0.9))));
//
//        //内盘
//        stockDialogInfoList.add(new StockDetail2Text(stockDetailInfos[4],
//                (StringUtils.isActuallyEmpty(info.getVolume_inner()) || info.getVolume_inner().length() < 3) ? "0手" : (
//                        info.getVolume_inner().length() > 6 ? StringUtils.save2Significand((Double.parseDouble(info.getVolume_inner()) / 1000000)) + "万手" :
//                                StringUtils.getIntegerData(String.valueOf((Double.parseDouble(info.getVolume_inner()) / 100))) + "手")));
//
//        //外盘
//        stockDialogInfoList.add(new StockDetail2Text(stockDetailInfos[5],
//                (StringUtils.isActuallyEmpty(info.getVolume_outer()) || info.getVolume_outer().length() < 3) ? "0手" : (
//                        info.getVolume_outer().length() > 6 ? StringUtils.save2Significand((Double.parseDouble(info.getVolume_outer()) / 1000000)) + "万手" :
//                                StringUtils.getIntegerData(String.valueOf((Double.parseDouble(info.getVolume_outer()) / 100))) + "手")));
//
//        //成交额
//        String turnover = StringUtils.parseStringDouble(info.getTurnover(), 2);
//        stockDialogInfoList.add(new StockDetail2Text(stockDetailInfos[6],
//                StringUtils.parseStringDouble(info.getTurnover()) == 0 ? "0" :
//                        turnover.length() <= 7 ? StringUtils.parseStringDouble(info.getTurnover(), 2) + "万" :
//                                StringUtils.save2Significand(StringUtils.parseStringDouble(info.getTurnover()) / 10000) + "亿"));
//        //振幅
//        stockDialogInfoList.add(new StockDetail2Text(stockDetailInfos[7],
//                StringUtils.parseStringDouble(info.getAmplitude(), 2) + "%"));
//
//        //委比
//        stockDialogInfoList.add(new StockDetail2Text(stockDetailInfos[8],
//                StringUtils.parseStringDouble(info.getCommission_rate(), 2) + "%"));
//
//        //量比
//        stockDialogInfoList.add(new StockDetail2Text(stockDetailInfos[9],
//                StringUtils.parseStringDouble(info.getQuantity_ratio(), 2)));
//
//        //流通市值
//        String mCapString = StringUtils.parseStringDouble(info.getMcap(), 2);
//        stockDialogInfoList.add(new StockDetail2Text(stockDetailInfos[10],
//                mCapString.length() > 11 ?
//                        StringUtils.saveSignificand(StringUtils.parseStringDouble(info.getMcap()) / 100000000d, 2) + "万亿" :
//                        mCapString.length() > 8 ? StringUtils.saveSignificand(StringUtils.parseStringDouble(info.getMcap()) / 10000d, 2) + "亿" :
//                                mCapString + "万"));
//
//        //总市值
//        String mTcapString = StringUtils.parseStringDouble(info.getTcap(), 2);
//        stockDialogInfoList.add(new StockDetail2Text(stockDetailInfos[11],
//                mTcapString.length() > 11 ?
//                        StringUtils.saveSignificand(StringUtils.parseStringDouble(info.getTcap()) / 100000000d, 2) + "万亿" :
//                        mTcapString.length() > 8 ? StringUtils.saveSignificand(StringUtils.parseStringDouble(info.getTcap()) / 10000d, 2) + "亿" :
//                                mTcapString + "万"));
//
//        //市盈率
//        stockDialogInfoList.add(new StockDetail2Text(stockDetailInfos[12],
//                StringUtils.parseStringDouble(info.getPe_y1(), 2)));
//
//        //市净率
//        stockDialogInfoList.add(new StockDetail2Text(stockDetailInfos[13],
//                StringUtils.parseStringDouble(info.getPb_y1(), 2)));
//
//        //每股收益
//        stockDialogInfoList.add(new StockDetail2Text(stockDetailInfos[14],
//                StringUtils.parseStringDouble(info.getEps_y1(), 2)));
//
//        //总股本
//        String capital = StringUtils.parseStringDouble(info.getCapital(), 2);
//        stockDialogInfoList.add(new StockDetail2Text(stockDetailInfos[15],
//                StringUtils.parseStringDouble(info.getCapital()) == 0 ? "0.00" :
//                        (capital.length() > 7 ? StringUtils.save2Significand(StringUtils.parseStringDouble(info.getCapital()) / 10000) + "亿" :
//                                StringUtils.parseStringDouble(info.getCapital(), 2) + "万")));
//
//        //流通股
//        stockDialogInfoList.add(new StockDetail2Text(stockDetailInfos[16],
//                StringUtils.parseStringDouble(info.getNegotiable_capital()) == 0 ? "0.00" :
//                        (StringUtils.parseStringDouble(info.getNegotiable_capital(), 2).length() > 7 ? StringUtils.save2Significand(StringUtils.parseStringDouble(info.getNegotiable_capital()) / 10000) + "亿" :
//                                StringUtils.parseStringDouble(info.getNegotiable_capital(), 2) + "万")));
//
//        infoDialogAdapter.notifyDataSetChanged();
//    }
//
//    //显示弹窗
//    private void showStockInfoDialog() {
//        rvStockInfo.startAnimation(
//                android.view.animation.AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_from_top));
//        rvStockInfo.setVisibility(View.VISIBLE);
//
//        functionViewMask(viewMask, true);
//        functionViewMask(viewMaskBottom, true);
//
//        //禁止滑动
//        scrollView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return true;
//            }
//        });
//        //禁止下拉刷新
//        ptrFrame.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                return true;
//            }
//        });
//        //禁止点击
//        imageViewShoeDialog.setImageResource(R.mipmap.img_drop_up);
//    }
//
//    /**
//     * 销毁指数[弹窗]
//     */
//    public void dismissMarket() {
//        if (isMaskViewVisiable()) {
//            rvStockInfo.setVisibility(View.GONE);
//            rvStockInfo.startAnimation(
//                    android.view.animation.AnimationUtils.loadAnimation(getActivity(), R.anim.slide_out_from_top));
//
//            functionViewMask(viewMask, false);
//            functionViewMask(viewMaskBottom, false);
//
//            scrollView.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    return false;
//                }
//            });
//            ptrFrame.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    return false;
//                }
//            });
//
//            //恢复可点击
//            imageViewShoeDialog.setImageResource(R.mipmap.img_drop_down);
//        }
//    }
//
//    private void functionViewMask(final View viewMask, boolean show) {
////        viewMaskBottom
//        viewMask.setEnabled(show);
//        viewMask.setClickable(show);
//        viewMask.setVisibility(show ? View.VISIBLE : View.GONE);
//        viewMask.startAnimation(
//                android.view.animation.AnimationUtils.loadAnimation(getActivity(),
//                        show ? R.anim.alpha_in : R.anim.alpha_out));
//
//        //点击蒙版消失
//        viewMask.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_UP) {
//                    dismissMarket();
//                    viewMask.setEnabled(false);
//                    viewMask.setClickable(false);
//                }
//                return true;
//            }
//        });
//
//    }
//
//    //弹窗是否可见
//    public boolean isMaskViewVisiable() {
//        return rvStockInfo.getVisibility() == View.VISIBLE;
//    }
//
//    private class StockInfoDialogAdapter extends CommonAdapter<StockDetail2Text, BaseViewHolder> {
//
//        private String closePrice;
//
//        /**
//         * @serialField StockUtils.getStockStatus();
//         */
//        private int stockType;
//
//        public void setStockType(int stockType) {
//            this.stockType = stockType;
//        }
//
//        public void setClosePrice(String closePrice) {
//            this.closePrice = closePrice;
//        }
//
//        private StockInfoDialogAdapter(List<StockDetail2Text> data) {
//            super(R.layout.item_stock_detail_dialog_layout, data);
//        }
//
//        @Override
//        protected void convert(BaseViewHolder holder, StockDetail2Text data, int position) {
//            holder.setBackgroundColor(R.id.item_stock_detail_dialog,
//                    (position / 4) % 2 == 0 ? 0xffffffff : 0xfff0f4fa);
//
//            holder.setText(R.id.tv_item_stock_detail_info_key, data.getKey());
//
//            holder.setText(R.id.tv_item_stock_detail_info_value, (position == 0 || position == 1) ? (
//                    realDouble(data.getValue()) == 0 ? "--" : data.getValue()) : data.getValue());
//
//            switch (position) {
//                case 0:
//                case 1:
//                    if (stockType == 1) {
//                        holder.setTextColor(R.id.tv_item_stock_detail_info_value,
//                                realDouble(data.getValue()) == 0 ? Color.BLACK : getResColor(
//                                        StockUtils.getStockRateColor(realDouble(data.getValue()) -
//                                                StringUtils.parseStringDouble(closePrice))));
//                    } else {
//                        holder.setTextColor(R.id.tv_item_stock_detail_info_value, Color.BLACK);
//                    }
//                    break;
//                case 2:
//                    holder.setTextColor(R.id.tv_item_stock_detail_info_value, getResColor(R.color.stock_red));
//                    break;
//                case 3:
//                    holder.setTextColor(R.id.tv_item_stock_detail_info_value, getResColor(R.color.stock_green));
//                    break;
//                case 4:
//                    holder.setTextColor(R.id.tv_item_stock_detail_info_value, getResColor(
//                            realDouble(data.getValue()) == 0 ? R.color.stock_gray : R.color.stock_green));
//                    break;
//                case 5:
//                    holder.setTextColor(R.id.tv_item_stock_detail_info_value, getResColor(
//                            realDouble(data.getValue()) == 0 ? R.color.stock_gray : R.color.stock_red));
//                    break;
//                case 8:
//                    holder.setTextColor(R.id.tv_item_stock_detail_info_value,
//                            getResColor(StockUtils.getStockRateColor(realDouble(data.getValue()))));
//                    break;
//                default:
//                    holder.setTextColor(R.id.tv_item_stock_detail_info_value, Color.BLACK);
//                    break;
//            }
//
//        }
//
//        private double realDouble(String value) {
//            return StringUtils.parseStringDouble(value.replaceAll("[%手亿万]", ""));
//        }
//    }
//
//    private void initBadge(int num, BadgeView badge) {
//        badge.setGravityOffset(2, 5, true);
//        badge.setShowShadow(false);
//        badge.setBadgeGravity(Gravity.TOP | Gravity.END);
//        badge.setBadgeTextSize(8, true);
//        badge.bindTarget(ivMessageTag);
//        badge.setBadgeNumber(num);
//    }
//
//    /**
//     * 消息接收
//     */
//    @Subscriber(tag = "IM_Message")
//    public void handleMessage(BaseMessage baseMessage) {
//        unReadCount++;
//        badge.setBadgeNumber(unReadCount);
//    }
//
//    private CopyStockDetailActivity getThisContext() {
//        return CopyStockDetailActivity.this;
//    }
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            if (isMaskViewVisiable()) {
//                dismissMarket();
//            } else {
//                finish();
//            }
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
//    //========================================20170622============================
//
//    private void getMyTools() {
//        diagnoseStockTools.clear();
//
//        take(new TakeToolsListener() {
//            @Override
//            public void onTakeTools(boolean sueecss, ArrayList<ToolData.Tool> tools) {
//                if (sueecss) {
//                    for (ToolData.Tool tool : tools) {
//                        if (tool.getIsShow() == 1) {
//                            diagnoseStockTools.add(tool);
//                        }
//                    }
//                }
//            }
//        });
//    }
//
//    /**
//     * 获取诊断工具
//     */
//    public void take(final TakeToolsListener listener) {
//        UserUtils.getAllMyTools(new Impl<String>() {
//            @Override
//            public void response(int code, String data) {
//                switch (code) {
//                    case Impl.RESPON_DATA_SUCCESS:
//                        List<ToolData> toolDatas = JSONArray.parseArray(data, ToolData.class);
//                        for (ToolData tool : toolDatas) {
//                            if (tool.getTitle_id() == 3) {
//                                if (listener != null) {
//                                    listener.onTakeTools(true, tool.getDatas());
//                                }
//                                break;
//                            }
//                        }
//                        break;
//                    default:
//                        if (listener != null)
//                            listener.onTakeTools(false, null);
//                        break;
//                }
//            }
//        });
//    }
//
//    /**
//     * 诊断工具回调
//     */
//    public interface TakeToolsListener {
//        void onTakeTools(boolean sueecss, ArrayList<ToolData.Tool> tools);
//    }
//
//}