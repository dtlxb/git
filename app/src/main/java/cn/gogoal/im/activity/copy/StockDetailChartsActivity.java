package cn.gogoal.im.activity.copy;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;

import org.simple.eventbus.EventBus;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.gogoal.im.base.AppManager;
import cn.gogoal.im.bean.BaseMessage;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.SPTools;
import cn.gogoal.im.common.StringUtils;
import cn.gogoal.im.fragment.copy.FiveDayFragment;
import cn.gogoal.im.fragment.copy.KChartsFragment;
import cn.gogoal.im.fragment.copy.TimesFragment;
import hply.com.niugu.ConstantUtils;
import hply.com.niugu.DeviceUtil;
import hply.com.niugu.MessageHandlerList;
import hply.com.niugu.ProgressWheel;
import hply.com.niugu.bean.TimeDetialBean;
import hply.com.niugu.bean.TimeDetialData;
import hply.com.niugu.stock.StockMinuteData;
import hply.com.niugu.view.KChartsView;


/**
 * Created by Lizn on 2015/9/24.
 */
public class StockDetailChartsActivity extends FragmentActivity implements View.OnClickListener {

    private static final String TAG = "StockDetailChartsActivity";

    public static final int STOCK_MARKE_INDEX = 0;
    public static final int STOCK_COMMON = 1;

    @BindView(hply.com.niugu.R.id.tab_stock_detail)
    TabLayout tabLayout;

    @BindView(hply.com.niugu.R.id.progress_wheel)
    ProgressWheel progressWheel;

    @BindView(hply.com.niugu.R.id.tv_cantGetData)
    TextView tvCannotGetData;

    @BindView(hply.com.niugu.R.id.fragment_stock_detail)
    View fragment;

    @BindView(hply.com.niugu.R.id.kline_detail)
    View kline_detail;
    @BindView(hply.com.niugu.R.id.times_detail)
    View times_detail;
    @BindView(hply.com.niugu.R.id.head_stock_detail_charts)
    View fragment_stock_detail;
    //页面加载动画
    @BindView(hply.com.niugu.R.id.load_animation)
    RelativeLayout load_animation;

    //最新数据显示
    @BindView(hply.com.niugu.R.id.tv_stock_name)
    TextView tv_stock_name;
    @BindView(hply.com.niugu.R.id.tv_stock_price)
    TextView tv_stock_price;
    @BindView(hply.com.niugu.R.id.tv_volume)
    TextView tv_volume;
    @BindView(hply.com.niugu.R.id.tv_time)
    TextView tv_time;
    @BindView(hply.com.niugu.R.id.btn_close)
    View btn_close;

    //时分数据显示
    @BindView(hply.com.niugu.R.id.tv_times_date)
    TextView tv_times_date;
    @BindView(hply.com.niugu.R.id.tv_times_price)
    TextView tv_times_price;
    @BindView(hply.com.niugu.R.id.tv_times_price_rate)
    TextView tv_times_price_rate;
    @BindView(hply.com.niugu.R.id.tv_times_volume)
    TextView tv_times_volume;
    @BindView(hply.com.niugu.R.id.tv_times_avg_price)
    TextView tv_times_avg_price;

    @BindView(hply.com.niugu.R.id.no_authority)
    TextView no_authority;
    @BindView(hply.com.niugu.R.id.before_authority)
    TextView before_authority;
    @BindView(hply.com.niugu.R.id.after_authority)
    TextView after_authority;

    //K线数据显示
    @BindView(hply.com.niugu.R.id.tv_kline_date)
    TextView tv_kline_date;
    @BindView(hply.com.niugu.R.id.tv_kline_open)
    TextView tv_kline_open;
    @BindView(hply.com.niugu.R.id.tv_kline_high)
    TextView tv_kline_high;
    @BindView(hply.com.niugu.R.id.tv_kline_low)
    TextView tv_kline_low;
    @BindView(hply.com.niugu.R.id.tv_kline_close)
    TextView tv_kline_close;
    @BindView(hply.com.niugu.R.id.tv_kline_amplitude)
    TextView tv_kline_amplitude;

    @BindView(hply.com.niugu.R.id.authority_blog)
    LinearLayout authority_blog;

    //最新的时分数据
    private double closePrice;

    private FragmentManager fragmentManager;
    private List<Fragment> fragmentList;

    private List<TextView> authority_tv;

    //个股信息
    private String stockName;
    private String stockCode;
    private int stockType;
    private double price;
    private String volume;
    private int width;
    private int height;
    private int totalHeight;
    private int showItem = -1;
    private int flagIndex = -1;

    private TimesFragment timesFragment;
    private FiveDayFragment fiveDayFragment;
    private KChartsFragment dayK, weekK, monthK;

    private Handler myHandler;

    //复权类型 0 不复权  1 前复权 2 后复权
    private Integer authority_type;

    private String[] tabTitles = {"分 时", "五 日", "日 K", "周 K", "月 K"};
    //加载中
    private boolean isLoading = true;
    private int stock_charge_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(hply.com.niugu.R.layout.activity_stock_detail_charts);
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        initView();
        setBottomTab();

        totalHeight = DeviceUtil.getHeight(this);
        width = DeviceUtil.getWidth(this) - DeviceUtil.dp2px(this, 18);
        height = DeviceUtil.getHeight(this) - DeviceUtil.dp2px(this, 90);

        if (!DeviceUtil.isNetworkConnected(this)) {
            progressWheel.setVisibility(View.GONE);
            tvCannotGetData.setVisibility(View.VISIBLE);
            handler.removeCallbacks(runnable);

        } else {
            progressWheel.setVisibility(View.VISIBLE);
            tvCannotGetData.setVisibility(View.GONE);
            handler.removeCallbacks(runnable);
            handler.postDelayed(runnable, 5000);
        }
        setEnvents();

        //获取个股信息
        initGetData();

        fragmentManager = getSupportFragmentManager();
        fragmentList = new ArrayList<>();

        //将碎片加入碎片集合
        timesFragment = new TimesFragment();
        fiveDayFragment = new FiveDayFragment();

        if (stockType == STOCK_MARKE_INDEX) {
            authority_blog.setVisibility(View.GONE);
        }
        initFragment();

        addFragments();

    }

    private void initView() {

    }

    private void setBottomTab() {
        for (int i = 0; i < tabTitles.length; i++) {
            tabLayout.addTab(tabLayout.newTab().setText(tabTitles[i]),i==getIntent().getIntExtra("position",0));
        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                showProgressbar(true);
                selectItem(tab.getPosition());
            }

            public void onTabUnselected(TabLayout.Tab tab) {
            }

            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        setTabLayoutWidth(tabLayout);//
    }

    private void setEnvents() {
        btn_close.setOnClickListener(this);
        no_authority.setOnClickListener(this);
        before_authority.setOnClickListener(this);
        after_authority.setOnClickListener(this);
    }

    private void initGetData() {
        stockName = getIntent().getStringExtra("stockName");
        stockCode = getIntent().getStringExtra("stockCode");
        price = getIntent().getDoubleExtra("price", 0);
        volume = getIntent().getStringExtra("volume");
        tv_time.setText(getIntent().getStringExtra("time").substring(10, getIntent().getStringExtra("time").lastIndexOf(":")));
        showItem = SPTools.getInt("showItem", 0);
        closePrice = getIntent().getDoubleExtra("closePrice", 0);
        authority_type = SPTools.getInt("authority_type", 0);
        authority_tv = Arrays.asList(no_authority, before_authority, after_authority);

        stockType = getIntent().getIntExtra("stockType", 0);
        stock_charge_type = getIntent().getIntExtra("stock_charge_type", 1);
    }

    private void addFragments() {
        fragmentList.add(timesFragment);
        fragmentList.add(fiveDayFragment);
        fragmentList.add(dayK);
        fragmentList.add(weekK);
        fragmentList.add(monthK);
    }

    private void initFragment() {
        Bundle timesBundle = new Bundle();
        Bundle fiveDayBundle = new Bundle();
        Bundle dayKBundle = new Bundle();
        Bundle weekKBundle = new Bundle();
        Bundle monthKBundle = new Bundle();
        if (stockType == STOCK_MARKE_INDEX) {
            timesBundle.putInt("type", STOCK_MARKE_INDEX);
            timesBundle.putParcelableArrayList("priceVolumDatas", null);

            fiveDayBundle.putInt("type", STOCK_MARKE_INDEX);
            dayKBundle.putInt("stockType", STOCK_MARKE_INDEX);
            weekKBundle.putInt("stockType", STOCK_MARKE_INDEX);
            monthKBundle.putInt("stockType", STOCK_MARKE_INDEX);
        } else if (stockType == STOCK_COMMON) {
            ArrayList arrayList = getIntent().getParcelableArrayListExtra("priceVolumDatas");
            timesBundle.putInt("type", STOCK_COMMON);
            timesBundle.putStringArrayList("priceVolumDatas", arrayList);

            fiveDayBundle.putInt("type", STOCK_COMMON);
            dayKBundle.putInt("stockType", STOCK_COMMON);
            weekKBundle.putInt("stockType", STOCK_COMMON);
            monthKBundle.putInt("stockType", STOCK_COMMON);
        }
        timesBundle.putInt("stock_charge_type", stock_charge_type);
        fiveDayBundle.putInt("stock_charge_type", stock_charge_type);
        timesBundle.putDouble("closePrice", closePrice);
        putInBundle(timesBundle);
        putInBundle(fiveDayBundle);
        putInBundle(dayKBundle);
        putInBundle(weekKBundle);
        putInBundle(monthKBundle);

        timesFragment.setArguments(timesBundle);
        fiveDayFragment.setArguments(fiveDayBundle);

        dayK = new KChartsFragment();
        dayKBundle.putInt("type", KChartsView.KLINE_TYPE_DAY);
        dayKBundle.putInt("authority_type", authority_type);
        dayK.setArguments(dayKBundle);

        weekK = new KChartsFragment();
        weekKBundle.putInt("type", KChartsView.KLINE_TYPE_WEEK);
        weekKBundle.putInt("authority_type", authority_type);
        weekK.setArguments(weekKBundle);

        monthK = new KChartsFragment();
        monthKBundle.putInt("type", KChartsView.KLINE_TYPE_MONTH);
        monthKBundle.putInt("authority_type", authority_type);
        monthK.setArguments(monthKBundle);
    }

    @Override
    protected void onStart() {
        super.onStart();
        myHandler = new MyHandler();
        MessageHandlerList.addHandler(StockDetailChartsActivity.class.getName(), myHandler);
        showProgressbar(true);
        initData();
        selectItem(showItem);
    }

    @Override
    protected void onStop() {
        myHandler = null;
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        fragmentList.clear();
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void putInBundle(Bundle bundle) {
        bundle.putInt("width", width);
        bundle.putInt("height", height);
        bundle.putInt("totalHeight", totalHeight);
    }

    private void addFragmentToStack(int cur) {
        Bundle args = new Bundle();
        args.putString("stock", stockName + "&" + stockCode);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = fragmentList.get(cur);
        if (!fragment.isAdded()) {
            fragmentTransaction.add(hply.com.niugu.R.id.fragment_stock_detail, fragment);
        }
        for (int i = 0; i < fragmentList.size(); i++) {
            Fragment f = fragmentList.get(i);
            if (i == cur && f.isAdded()) {
                showProgressbar(false);
                fragmentTransaction.show(f);
            } else if (f != null && f.isAdded() && f.isVisible()) {
                fragmentTransaction.hide(f);
            }
        }
        fragmentTransaction.commitAllowingStateLoss();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case hply.com.niugu.R.id.btn_close:
                finish();
                break;
            case hply.com.niugu.R.id.no_authority:
                if (SPTools.getInt("authority_type", 0) != 0) {
                    authority_type = 0;
                    SPTools.saveInt("authority_type", authority_type);
                    no_authority.setTextColor(ContextCompat.getColor(getContext(),hply.com.niugu.R.color.piechart_bule));
                    before_authority.setTextColor(ContextCompat.getColor(getContext(),hply.com.niugu.R.color.gray));
                    after_authority.setTextColor(ContextCompat.getColor(getContext(),hply.com.niugu.R.color.gray));
                    showProgressbar(true);
                    AppManager.getInstance().sendMessage("KChartsFragment", new BaseMessage("auhority", authority_type.toString()));
                }
                break;
            case hply.com.niugu.R.id.before_authority:
                if (SPTools.getInt("authority_type", 0) != 1) {
                    authority_type = 1;
                    SPTools.saveInt("authority_type", authority_type);
                    no_authority.setTextColor(ContextCompat.getColor(getContext(),hply.com.niugu.R.color.gray));
                    before_authority.setTextColor(ContextCompat.getColor(getContext(),hply.com.niugu.R.color.piechart_bule));
                    after_authority.setTextColor(ContextCompat.getColor(getContext(),hply.com.niugu.R.color.gray));
                    showProgressbar(true);
                    AppManager.getInstance().sendMessage("KChartsFragment", new BaseMessage("auhority", authority_type.toString()));
                }
                break;
            case hply.com.niugu.R.id.after_authority:
                if (SPTools.getInt("authority_type", 0) != 2) {
                    authority_type = 2;
                    SPTools.saveInt("authority_type", authority_type);
                    no_authority.setTextColor(ContextCompat.getColor(getContext(),hply.com.niugu.R.color.gray));
                    before_authority.setTextColor(ContextCompat.getColor(getContext(),hply.com.niugu.R.color.gray));
                    after_authority.setTextColor(ContextCompat.getColor(getContext(),hply.com.niugu.R.color.piechart_bule));
                    showProgressbar(true);
                    AppManager.getInstance().sendMessage("KChartsFragment", new BaseMessage("auhority", authority_type.toString()));
                }
                break;
        }
    }

    public void initData() {
        if (stockCode.length() > 6) {
            tv_stock_name.setText(stockName + "(" + stockCode.substring(2, stockCode.length()) + ")");
        } else {
            tv_stock_name.setText(stockName + "(" + stockCode + ")");
        }
        tv_stock_price.setText(StringUtils.save2Significand(this.price));
        tv_stock_price.setTextColor(Color.GRAY);
        if (closePrice < price) {
            tv_stock_price.setTextColor(Color.RED);
        } else {
            tv_stock_price.setTextColor(Color.GREEN);
        }
        Long volumeNum = Long.parseLong(volume);
        volumeNum = volumeNum / 100;
        if (volumeNum >= 10000) {
            tv_volume.setText(StringUtils.save2Significand(volumeNum / 10000) + "万手");
        } else {
            tv_volume.setText(volumeNum + "手");
        }
    }

    private void getStockTimeDetial(final String stockCode) {
        HashMap<String, String> param = new HashMap<>();
        param.put("stock_code", stockCode);
        param.put("limit", "3");

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                if (responseInfo!=null && JSONObject.parseObject(responseInfo).getIntValue("code")==0) {
//                    setListview(JSONObject.parseObject(responseInfo, TimeDetialBean.class).getData());
                    List<TimeDetialData> data = JSONObject.parseObject(responseInfo, TimeDetialBean.class).getData();
                    String time=data.get(0).getUpdate_time();
                    tv_time.setText(time.substring(10, time.lastIndexOf(":")));
                    AppManager.getInstance().sendMessage("updata_list",responseInfo);
                }
            }

            public void onFailure(String msg) {
            }
        };
        new GGOKHTTP(param, GGOKHTTP.GET_STOCK_TIME_DETIAL, ggHttpInterface).startGet();
    }

    private void showProgressbar(boolean flag) {
        if (flag) {
            load_animation.setVisibility(View.VISIBLE);
            fragment.setVisibility(View.INVISIBLE);
        } else {
            load_animation.setVisibility(View.INVISIBLE);
            fragment.setVisibility(View.VISIBLE);
        }
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
                params.setMargins(DeviceUtil.dp2px(getContext(), 30f), 0, DeviceUtil.dp2px(getContext(), 30f), 0);
                child.setLayoutParams(params);
                child.invalidate();
            }
        } catch (Exception e) {
        }
    }

    private StockDetailChartsActivity getContext() {
        return this;
    }

    private void selectItem(int position) {
        if (position > 1 && stockType != STOCK_MARKE_INDEX) {
            authority_blog.setVisibility(View.VISIBLE);
            authority_tv.get(authority_type).setTextColor(ContextCompat.getColor(getContext(),hply.com.niugu.R.color.piechart_bule));
        } else {
            authority_blog.setVisibility(View.GONE);
        }

        if (position == flagIndex) {
            if (!isLoading)
                showProgressbar(false);
            return;
        }

        this.flagIndex = position;
        this.showItem = position;

        addFragmentToStack(position);
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            int red = Color.rgb(0xf2, 0x49, 0x57);
            int green = Color.rgb(0x1d, 0xbf, 0x60);
            int gray = Color.rgb(0x54, 0x69, 0x80);
            switch (msg.what) {
                case ConstantUtils.DISPLAY_XCHART_TIME_DATA:
                    times_detail.setVisibility(View.VISIBLE);
                    fragment_stock_detail.setVisibility(View.INVISIBLE);

                    StockMinuteData stockMinuteData = (StockMinuteData) msg.obj;

                    String mData = stockMinuteData.getDate();
                    tv_times_date.setText(mData.substring(10, mData.lastIndexOf(":")));

                    double price = StringUtils.getStockDouble(stockMinuteData.getPrice());
                    double tRate = StringUtils.getStockDouble(stockMinuteData.getPrice_change_rate());
                    tv_times_price.setText(StringUtils.save2Significand(price));
                    tv_times_price_rate.setText(StringUtils.save2Significand(tRate) + "%");
                    tv_times_price.setTextColor(gray);
                    tv_times_price_rate.setTextColor(gray);
                    if (tRate > 0) {
                        tv_times_price.setTextColor(red);
                        tv_times_price_rate.setTextColor(red);
                    } else if (tRate < 0) {
                        tv_times_price.setTextColor(green);
                        tv_times_price_rate.setTextColor(green);
                    } else {
                        tv_times_price.setTextColor(gray);
                        tv_times_price_rate.setTextColor(gray);
                    }

                    float volume = stockMinuteData.getVolume();
                    volume = volume / 100;
                    String s;
                    if (volume >= 10000) {
                        s = StringUtils.save2Significand(volume / 10000);
                        tv_times_volume.setText(s + "万手");
                    } else {
                        s = String.format("%.0f", volume);
                        tv_times_volume.setText(s + "手");
                    }

                    double avg_price = StringUtils.getStockDouble(stockMinuteData.getAvg_price());
                    tv_times_avg_price.setText(StringUtils.save2Significand(avg_price));
                    tv_times_avg_price.setTextColor(gray);
                    if (closePrice < price) {
                        tv_times_avg_price.setTextColor(red);
                    } else {
                        tv_times_avg_price.setTextColor(green);
                    }
                    break;

                case ConstantUtils.DISPLAY_XCHART_K_DATA:
                    kline_detail.setVisibility(View.VISIBLE);
                    fragment_stock_detail.setVisibility(View.INVISIBLE);
                    List<Map<String, Object>> datas = (List<Map<String, Object>>) msg.obj;
                    int index = msg.arg1;
                    if (datas.size() > index) {

                        String kData = (String) datas.get(index).get("date");
                        tv_kline_date.setText(kData.substring(5, kData.lastIndexOf(" ")));

                        float open = (float) datas.get(index).get("open_price");
                        float high = (float) datas.get(index).get("high_price");
                        float low = (float) datas.get(index).get("low_price");
                        float close = (float) datas.get(index).get("close_price");
                        float kRate = (float) datas.get(index).get("price_change_rate");

                        tv_kline_open.setText(StringUtils.save2Significand(open));
                        tv_kline_high.setText(StringUtils.save2Significand(high));
                        tv_kline_low.setText(StringUtils.save2Significand(low));
                        tv_kline_close.setText(StringUtils.save2Significand(close));
                        tv_kline_amplitude.setText(StringUtils.save2Significand(kRate) + "%");

                        tv_kline_open.setTextColor(gray);
                        tv_kline_high.setTextColor(gray);
                        tv_kline_low.setTextColor(gray);
                        tv_kline_close.setTextColor(gray);
                        tv_kline_amplitude.setTextColor(gray);

                        if (index + 1 < datas.size()) {
                            float yestoday_close = (float) datas.get(index + 1).get("close_price");

                            if (close > open) tv_kline_close.setTextColor(red);
                            else if (close < open) tv_kline_close.setTextColor(green);

                            if (yestoday_close < open) tv_kline_open.setTextColor(red);
                            else if (yestoday_close > open) tv_kline_open.setTextColor(green);

                            if (open < high) tv_kline_high.setTextColor(red);
                            else if (open > high) tv_kline_high.setTextColor(green);

                            if (open < low) tv_kline_low.setTextColor(red);
                            else if (open > low) tv_kline_low.setTextColor(green);

                        } else {
                            if (close > open) {
                                tv_kline_close.setTextColor(red);
                                tv_kline_open.setTextColor(red);
                                tv_kline_high.setTextColor(red);
                                tv_kline_low.setTextColor(red);
                            } else if (close < open) {
                                tv_kline_close.setTextColor(green);
                                tv_kline_open.setTextColor(green);
                                tv_kline_high.setTextColor(green);
                                tv_kline_low.setTextColor(green);
                            }
                        }

                        if (kRate > 0) tv_kline_amplitude.setTextColor(red);
                        else if (kRate < 0) tv_kline_amplitude.setTextColor(green);
                    }

                    break;

                case ConstantUtils.DISS_XCHART_DATA:
                    kline_detail.setVisibility(View.INVISIBLE);
                    times_detail.setVisibility(View.INVISIBLE);
                    fragment_stock_detail.setVisibility(View.VISIBLE);
                    break;
                case ConstantUtils.DISS_PROGRESSBAR:
                    showProgressbar(false);
                    break;
                case ConstantUtils.XCHART_FLING:
                    switch (msg.arg1) {
                        case KChartsView.KLINE_TYPE_DAY:
                            dayK.getMyChartsView().displayChartTouchUp((Float) msg.obj);
                            break;
                        case KChartsView.KLINE_TYPE_WEEK:
                            weekK.getMyChartsView().displayChartTouchUp((Float) msg.obj);
                            break;
                        case KChartsView.KLINE_TYPE_MONTH:
                            monthK.getMyChartsView().displayChartTouchUp((Float) msg.obj);
                            break;
                    }
                    break;
                case ConstantUtils.REFRESH_KLINE_DATA:
                    switch (msg.arg1) {
                        case KChartsView.KLINE_TYPE_DAY:
                            dayK.showLoadingDialog();
                            dayK.GetKLineDataPage(false, false);
                            break;
                        case KChartsView.KLINE_TYPE_WEEK:
                            weekK.showLoadingDialog();
                            weekK.GetKLineDataPage(false, false);
                            break;
                        case KChartsView.KLINE_TYPE_MONTH:
                            monthK.showLoadingDialog();
                            monthK.GetKLineDataPage(false, false);
                            break;
                    }
                    break;
            }
            super.handleMessage(msg);
        }
    }

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        public void run() {
            update();
            handler.postDelayed(this, 5000);
        }
    };

    //定时任务
    private void update() {
//        Log.e("TAG","===定时任务开始===StockDetailChartActivity===");
//        initGetData();
//        initFragment();
        initData();
        getStockTimeDetial(stockCode);
        AppManager.getInstance().sendMessage("stockinfo_updata");
    }

    @Override
    protected void onResume() {
        super.onResume();
//        开始计时
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable, 5000);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        停止计时
        handler.removeCallbacks(runnable);
//        Log.e("TAG","===定时任务结束===StockDetailChartActivity===");
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public void setLoading(boolean loading) {
        this.isLoading = loading;
    }

}
