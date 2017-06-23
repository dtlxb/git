package cn.gogoal.im.fragment.copy;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.View;

import com.alibaba.fastjson.JSONObject;

import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.activity.copy.StockDetailChartsActivity;
import cn.gogoal.im.adapter.TreatAdapter;
import cn.gogoal.im.base.AppManager;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.SPTools;
import cn.gogoal.im.common.StockUtils;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.ui.stockviews.LandScapeChartView;
import cn.gogoal.im.ui.stockviews.TimesFivesBitmap;
import cn.gogoal.im.ui.widget.UnSlidingViewPager;
import hply.com.niugu.stock.StockMinuteBean;
import hply.com.niugu.stock.StockMinuteData;

public class TimesFragment extends BaseFragment {
    @BindView(R.id.times_view)
    LandScapeChartView mTimesView;
    private TimesFivesBitmap timesBitmap;

    private int totalHeight;

    private String stockCode;
    private List<StockMinuteData> stockMinuteDatas;
    private int stockType;
    private ArrayList<String> priceVolumeDatas;
    //定时刷新
    private Timer timer;
    private int stock_charge_type;
    private int width;
    private int height;

    //交易五档、明细
    @BindView(R.id.tablayout_treat)
    TabLayout tabLayoutTreat;

    @BindView(R.id.vp_treat)
    UnSlidingViewPager vpTreat;

    @Override
    public int bindLayout() {
        return R.layout.fragment_times;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stockCode = getActivity().getIntent().getStringExtra("stockCode");
        Bundle bundle = getArguments();
        stockType = bundle.getInt("type");
        totalHeight = bundle.getInt("totalHeight", 0);
        priceVolumeDatas = bundle.getStringArrayList("priceVolumeDatas");

        stock_charge_type = bundle.getInt("stock_charge_type");
        width = bundle.getInt("width", 0);
        height = bundle.getInt("height", 0);
    }

    @Override
    public void doBusiness(Context mContext) {

        TreatAdapter treatAdapter = new TreatAdapter(getChildFragmentManager(), mContext, stockCode, false);
        vpTreat.setAdapter(treatAdapter);
        tabLayoutTreat.setupWithViewPager(vpTreat);

        for (int i = 0; i < 3; i++) {
            TabLayout.Tab tabAt = tabLayoutTreat.getTabAt(i);
            if (tabAt != null) {
                tabAt.setCustomView(treatAdapter.getTabView(i));
            }
        }
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        if (stockType == StockDetailChartsActivity.STOCK_MARKE_INDEX) {

        } else if (stockType == StockDetailChartsActivity.STOCK_COMMON) {
            initValue();
        }
        GetMinLineData(true);
    }

    /*五档、明细切换*/
    public void toggleTreatMode() {
        if (tabLayoutTreat.getTabAt(0).isSelected()) {
            tabLayoutTreat.getTabAt(1).select();
        } else if (tabLayoutTreat.getTabAt(1).isSelected()) {
            tabLayoutTreat.getTabAt(2).select();
        } else if (tabLayoutTreat.getTabAt(2).isSelected()) {
            tabLayoutTreat.getTabAt(0).select();
        }
    }

    private void GetMinLineData(boolean needShowProgress) {
        if (needShowProgress) {
            ((StockDetailChartsActivity) getActivity()).showProgressbar(true);
        }
        HashMap<String, String> param = new HashMap<>();
        if (StockDetailChartsActivity.STOCK_COMMON == stockType) {
            param.put("stock_code", stockCode);
        } else if (StockDetailChartsActivity.STOCK_MARKE_INDEX == stockType) {
            param.put("fullcode", stockCode);
            param.put("avg_line_type", "150");
        }
        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                if (stockType == StockDetailChartsActivity.STOCK_MARKE_INDEX) {
                    timesBitmap = new TimesFivesBitmap(width, height);
                } else if (stockType == StockDetailChartsActivity.STOCK_COMMON) {
                    timesBitmap = new TimesFivesBitmap((int) (0.78 * width), height);
                }
                StockMinuteBean bean = JSONObject.parseObject(responseInfo, StockMinuteBean.class);
                if (bean.getCode() == 0) {
                    stockMinuteDatas = bean.getData();
                    if (stockMinuteDatas.size() > 0) {
                        timesBitmap.setShowDetail(true);
                        timesBitmap.setLongitudeNum(0);
                        timesBitmap.setmSize(AppDevice.dp2px(getActivity(), 1));
                        //画交互点
                        timesBitmap.setmSpaceSize(AppDevice.dp2px(getActivity(), 3));
                        timesBitmap.setmAxisTitleSize(AppDevice.dp2px(getActivity(), 10));
                        if (StockDetailChartsActivity.STOCK_COMMON == stockType) {
                            timesBitmap.setLeftMargin(AppDevice.dp2px(getActivity(), 40));
                            timesBitmap.setRightMargin(AppDevice.dp2px(getActivity(), 40));
                        } else if (StockDetailChartsActivity.STOCK_MARKE_INDEX == stockType) {
                            timesBitmap.setLeftMargin(AppDevice.dp2px(getActivity(), 45));
                            timesBitmap.setRightMargin(AppDevice.dp2px(getActivity(), 45));
                        }
                        Bitmap bitmap = timesBitmap.setTimesList(bean, true, stock_charge_type);
                        mTimesView.setBitmap(bitmap);
                        mTimesView.setData(timesBitmap, stock_charge_type);
                    }
                    ((StockDetailChartsActivity) getActivity()).showProgressbar(false);
                } else {

                }
            }

            @Override
            public void onFailure(String msg) {
                UIHelper.toast(getActivity(), "网络连接异常，请检查后重试！");
            }
        };
        if (StockDetailChartsActivity.STOCK_COMMON == stockType) {
            new GGOKHTTP(param, GGOKHTTP.STOCK_MINUTE, ggHttpInterface).startGet();
        } else if (StockDetailChartsActivity.STOCK_MARKE_INDEX == stockType) {
            new GGOKHTTP(param, GGOKHTTP.GET_HQ_MINUTE, ggHttpInterface).startGet();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        timer = new Timer();
        long refreshtime = SPTools.getLong("interval_time", 15000);
        AutoRefresh(refreshtime);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (timer != null) {
            timer.cancel();
        }
    }

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
                AppManager.getInstance().sendMessage("TimesFragment");
                // TODO: 2016/9/26 0026
//                getStockTimeDetial(stockCode);
            }
        };
        if (timer == null) {
            timer = new Timer();
        }
        timer.scheduleAtFixedRate(timerTask, num, num);
    }

    @Subscriber(tag = "TimesFragment")
    private void refresh(String s) {
        if (StockUtils.isTradeTime()) {
            GetMinLineData(false);
            if (stockType == StockDetailChartsActivity.STOCK_COMMON) ;
//                InitList(stockCode);

        } else {
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
        }
    }

    private void initValue() {
        if (priceVolumeDatas != null && priceVolumeDatas.size() > 0) {
//            for (int i = 0; i < priceVolumeIds.length; i++) {
//                if (!priceVolumeDatas.get(i).equals("null") && !priceVolumeDatas.get(i).equals("")) {
//                    if (i % 2 == 0) {
//                        priceVolumes[i].setText(priceVolumeDatas.get(i));
//                        if (StringUtils.getDouble(priceVolumeDatas.get(i)) > closePrice) {
//                            priceVolumes[i].setTextColor(getResColor(R.color.stock_red));
//                        } else if (StringUtils.getDouble(priceVolumeDatas.get(i)) == closePrice || StringUtils.getDouble(priceVolumeDatas.get(i)) == 0) {
//                            priceVolumes[i].setTextColor(getResColor(R.color.gray_light));
//                        } else {
//                            priceVolumes[i].setTextColor(getResColor(R.color.stock_green));
//                        }
//                    } else {
//                        priceVolumes[i].setText(Integer.parseInt(priceVolumeDatas.get(i)) / 100 + "");
//                    }
//                }
//            }
        }
    }

}