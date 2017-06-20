package cn.gogoal.im.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindArray;
import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.TreatAdapter;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.SPTools;
import cn.gogoal.im.common.StockUtils;
import cn.gogoal.im.ui.stockviews.BitmapChartView;
import cn.gogoal.im.ui.stockviews.KChartsBitmap;
import cn.gogoal.im.ui.stockviews.TimesFivesBitmap;
import cn.gogoal.im.ui.widget.UnSlidingViewPager;
import hply.com.niugu.stock.StockMinuteBean;

/**
 * Created by huangxx on 2017/6/19.
 */

public class StockMapsFragment extends BaseFragment {


    @BindView(R.id.charts_line_tab_up_ll)
    TabLayout tabChartsTitles;//图表表头

    @BindArray(R.array.stock_detail_chart_titles)
    String[] stockDetailChartTitles;//图表数据

    @BindView(R.id.flag_layout_treat)
    LinearLayout layoutTreat;

    @BindView(R.id.load_animation)
    RelativeLayout load_animation;//页面加载动画

    @BindView(R.id.stockdetail_textview)
    LinearLayout textLayout;//无内容时布局

    @BindView(R.id.stock_detail_fragment_time_line)
    BitmapChartView mBitmapChartView;//图表控件

    @BindView(R.id.tablayout_treat)
    TabLayout tabLayoutTreat;//交易五档、明细表头

    @BindView(R.id.vp_treat)
    UnSlidingViewPager vpTreat;
    private String change_value;//交易五档、明细

    private int showItem;//当前显示
    private int width;
    private int height;

    //当前股票
    private String stockName;
    private String stockCode;

    //k线数据设置
    private int dayK1;
    private int dayK2;
    private int dayK3;
    private int dayK4;

    //数据集合
    private int stock_charge_type;
    private HashMap<String, Bitmap> map = new HashMap<>();
    private List<Map<String, Object>> mOHLCData = new ArrayList<>();
    private StockMinuteBean timesBean;
    private TimesFivesBitmap fiveDayBitmap;
    private TimesFivesBitmap timesBitmap;

    public static StockMapsFragment newInstance(int stock_charge_type, String stockName, String stockCode) {
        StockMapsFragment fragment = new StockMapsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("stock_name", stockName);
        bundle.putString("stock_code", stockCode);
        bundle.putInt("stock_charge_type", stock_charge_type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_stock_maps;
    }

    @Override
    public void doBusiness(Context mContext) {
        initMaps();
    }

    private void initMaps() {

        //图形宽高
        width = AppDevice.getWidth(getActivity()) - AppDevice.dp2px(getActivity(), 22);
        height = AppDevice.dp2px(getActivity(), 190);

        //K线初始化
        dayK1 = SPTools.getInt("tv_ln1", 5);
        dayK2 = SPTools.getInt("tv_ln2", 10);
        dayK3 = SPTools.getInt("tv_ln3", 20);
        dayK4 = SPTools.getInt("tv_ln4", 0);

        //获取页面传来的值
        Bundle bundle = getArguments();
        stockCode = bundle.getString("stock_code");
        stockName = bundle.getString("stock_name");
        stock_charge_type = bundle.getInt("stock_charge_type");

        //五档明细
        TreatAdapter treatAdapter = new TreatAdapter(getFragmentManager(), getActivity(), stockCode, true);
        vpTreat.setAdapter(treatAdapter);
        tabLayoutTreat.setupWithViewPager(vpTreat);

        //初始化图表
        showItem = SPTools.getInt("showItem", 0);
        onShow(showItem);
        //添加图表头部
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
                            needShowLoading(false);
                            if (stock_charge_type == 1 && StockUtils.isTradeTime()) {
                                mBitmapChartView.setBitmap(map.get(String.valueOf("0")), true, timesBitmap);
                            } else {
                                mBitmapChartView.setBitmap(map.get(String.valueOf("0")), false, timesBitmap);
                            }
                        } else {
                            needShowLoading(true);
                            GetMinLineData();
                        }
                        break;
                    case 1:
                        if (map.containsKey("1")) {
                            needShowLoading(false);
                            if (stock_charge_type == 1 && StockUtils.isTradeTime()) {
                                mBitmapChartView.setBitmap(map.get(String.valueOf("1")), true, fiveDayBitmap);
                            } else {
                                mBitmapChartView.setBitmap(map.get(String.valueOf("1")), false, fiveDayBitmap);
                            }
                        } else {
                            needShowLoading(true);
                            getFiveData();
                        }
                        break;
                    case 2:
                        if (map.containsKey("2")) {
                            needShowLoading(false);
                            mBitmapChartView.setBitmap(map.get("2"));
                        } else {
                            needShowLoading(true);
                            getKLineData(0);
                        }
                        break;
                    case 3:
                        if (map.containsKey("3")) {
                            needShowLoading(false);
                            mBitmapChartView.setBitmap(map.get("3"));
                        } else {
                            needShowLoading(true);
                            getKLineData(1);
                        }
                        break;
                    case 4:
                        if (map.containsKey("4")) {
                            needShowLoading(false);
                            mBitmapChartView.setBitmap(map.get("4"));
                        } else {
                            needShowLoading(true);
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
    }

    //图表展示
    private void onShow(int item) {
        needShowLoading(true);
        switch (item) {
            case 0://分时
                GetMinLineData();
                break;
            case 1://五日
                getFiveData();
                break;
            case 2://日K
                getKLineData(0);
                break;
            case 3://周K
                getKLineData(1);
                break;
            case 4://月K
                getKLineData(2);
                break;
        }

    }

    /*五档、明细切换*/
    public void toggleTreatMode() {
        if (tabLayoutTreat.getTabAt(0).isSelected()) {
            tabLayoutTreat.getTabAt(1).select();
        } else {
            tabLayoutTreat.getTabAt(0).select();
        }
    }

    private void needShowLoading(boolean needLoading) {
        if (needLoading) {
            load_animation.setVisibility(View.VISIBLE);
        } else {
            load_animation.setVisibility(View.GONE);
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
                new BitmapTask().execute("0");
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
        if (!AppDevice.isNetworkConnected(getActivity())) {
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
                    //失败操作
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (load_animation.getVisibility() == View.VISIBLE) {
                                load_animation.setVisibility(View.GONE);
                            }
                            if (map.get("1") == null && textLayout.getVisibility() == View.GONE) {
                                textLayout.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                }
            }

            @Override
            public void onFailure(String msg) {
                //失败操作
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (load_animation.getVisibility() == View.VISIBLE) {
                            load_animation.setVisibility(View.GONE);
                        }
                        if (map.get("1") == null && textLayout.getVisibility() == View.GONE) {
                            textLayout.setVisibility(View.VISIBLE);
                        }
                    }
                });
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

        if (!AppDevice.isNetworkConnected(getActivity())) {
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
                    //失败操作
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
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
                    });
                }
            }

            @Override
            public void onFailure(String msg) {
            }
        };
        new GGOKHTTP(param, GGOKHTTP.STOCK_K_LINE, ggHttpInterface).startGet();

    }

    //异步画图
    private class BitmapTask extends AsyncTask<String, Void, Bitmap> {
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
                    timesBitmap.setmSize(AppDevice.dp2px(getActivity(), 1));
                    timesBitmap.setmSpaceSize(AppDevice.dp2px(getActivity(), 3));
                    timesBitmap.setmAxisTitleSize(AppDevice.dp2px(getActivity(), 10));
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
                        fiveDayBitmap.setmSpaceSize(AppDevice.dp2px(getActivity(), 3));
                        fiveDayBitmap.setmSize(AppDevice.dp2px(getActivity(), 1));
                        fiveDayBitmap.setmAxisTitleSize(AppDevice.dp2px(getActivity(), 10));

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
                        kChartsBitmap.setmSpaceSize(AppDevice.dp2px(getActivity(), 3));
                        kChartsBitmap.setmAxisTitleSize(AppDevice.dp2px(getActivity(), 10));
                        kChartsBitmap.setmSize(AppDevice.dp2px(getActivity(), 1));

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
                        kChartsBitmap1.setmSpaceSize(AppDevice.dp2px(getActivity(), 3));
                        kChartsBitmap1.setmAxisTitleSize(AppDevice.dp2px(getActivity(), 10));
                        kChartsBitmap1.setmSize(AppDevice.dp2px(getActivity(), 1));

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
                        kChartsBitmap2.setmSpaceSize(AppDevice.dp2px(getActivity(), 3));
                        kChartsBitmap2.setmAxisTitleSize(AppDevice.dp2px(getActivity(), 10));
                        kChartsBitmap2.setmSize(AppDevice.dp2px(getActivity(), 1));

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
        float avg_price;
        if (s == null) {
            avg_price = 0.0f;
        } else {
            avg_price = Float.parseFloat(s);
        }
        return avg_price;
    }

}