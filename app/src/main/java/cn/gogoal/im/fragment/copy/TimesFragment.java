package cn.gogoal.im.fragment.copy;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;

import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.activity.copy.StockDetailChartsActivity;
import cn.gogoal.im.base.AppManager;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.bean.stock.StockDetail;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.SPTools;
import cn.gogoal.im.common.StockUtils;
import cn.gogoal.im.common.UIHelper;
import hply.com.niugu.ConstantUtils;
import hply.com.niugu.DeviceUtil;
import hply.com.niugu.LandScapeChartView;
import hply.com.niugu.MessageHandlerList;
import hply.com.niugu.StringUtils;
import hply.com.niugu.adapter.MingXiAdapter;
import hply.com.niugu.bean.TimeDetialBean;
import hply.com.niugu.bean.TimeDetialData;
import hply.com.niugu.stock.StockMinuteBean;
import hply.com.niugu.stock.StockMinuteData;
import hply.com.niugu.stock.TimesFivesBitmap;

/**
 * Created by Lizn on 2015/9/25.
 */
public class TimesFragment extends BaseFragment {
    @BindView(R.id.times_view)
    LandScapeChartView mTimesView;
    private TimesFivesBitmap timesBitmap;
    @BindView(R.id.layout_price)
    View layout_price;

    @BindView(R.id.rdb_detail_wudang)
    RadioButton rdbTabWudang;
    @BindView(R.id.rdb_detail_mingxi)
    RadioButton rdbTabMingxi;
    @BindView(R.id.linear_treat)
    LinearLayout layoutWudang;
    @BindView(R.id.tvLsvNoData)
    LinearLayout tvLsvNoData;

    private int totalHeight;

    @BindView(R.id.lsvDetial)
    ListView lsvDetial;

    //买5卖5
    private int[] priceVolumeIds = new int[]{
            R.id.time_charts_sell5_price, R.id.time_charts_sell5_volume, R.id.time_charts_sell4_price, R.id.time_charts_sell4_volume,
            R.id.time_charts_sell3_price, R.id.time_charts_sell3_volume, R.id.time_charts_sell2_price, R.id.time_charts_sell2_volume,
            R.id.time_charts_sell1_price, R.id.time_charts_sell1_volume, R.id.time_charts_buy1_price,
            R.id.time_charts_buy1_volume, R.id.time_charts_buy2_price, R.id.time_charts_buy2_volume, R.id.time_charts_buy3_price,
            R.id.time_charts_buy3_volume, R.id.time_charts_buy4_price, R.id.time_charts_buy4_volume, R.id.time_charts_buy5_price,
            R.id.time_charts_buy5_volume};

    private TextView[] priceVolumes=new TextView[priceVolumeIds.length];

    private String stockCode;
    private List<StockMinuteData> stockMinuteDatas;
    private Context mContext;
    private int stockType;
    private ArrayList<String> priceVolumDatas;
    //定时刷新
    private Timer timer;
    private int refreshtime;
    private int stock_charge_type;
    private double closePrice;
    private int width;
    private int height;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        stockCode = getActivity().getIntent().getStringExtra("stockCode");
        Bundle bundle = getArguments();
        stockType = bundle.getInt("type");
        totalHeight = bundle.getInt("totalHeight", 0);
        priceVolumDatas = bundle.getStringArrayList("priceVolumDatas");

        stock_charge_type = bundle.getInt("stock_charge_type");
        refreshtime = SPTools.getInt("refreshtime", 15000);
        width = bundle.getInt("width", 0);
        height = bundle.getInt("height", 0);
        closePrice = bundle.getDouble("closePrice");
    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_times;
    }

    @Override
    public void doBusiness(Context mContext) {
        rdbTabWudang.setOnCheckedChangeListener(rdbTablistener);
        rdbTabMingxi.setOnCheckedChangeListener(rdbTablistener);

        getStockTimeDetial(stockCode);
    }

    //分时数据交易明细 -- 主动
    private void getStockTimeDetial(final String stockCode) {
        HashMap<String, String> param = new HashMap<>();
        param.put("stock_code", stockCode);
        param.put("limit", "3");

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                if (responseInfo!=null && JSONObject.parseObject(responseInfo).getIntValue("code")==0) {
                    setListview(JSONObject.parseObject(responseInfo, TimeDetialBean.class).getData());
                }
            }

            public void onFailure(String msg) {
            }
        };
        new GGOKHTTP(param, GGOKHTTP.GET_STOCK_TIME_DETIAL, ggHttpInterface).startGet();
    }

    private void setListview(List<TimeDetialData> detiallLists) {
        MingXiAdapter adapter = new MingXiAdapter(detiallLists);
        try {
            lsvDetial.setAdapter(adapter);
            tvLsvNoData.setVisibility(View.GONE);
        } catch (Exception e) {
            tvLsvNoData.setVisibility(View.VISIBLE);
        }
    }

    private void showLayoutWudang() {
        lsvDetial.setVisibility(View.GONE);
        layoutWudang.setVisibility(View.VISIBLE);
        tvLsvNoData.setVisibility(View.GONE);
    }

    private void showLayoutMingxi() {
        if (lsvDetial.getCount() != 0) {
            lsvDetial.setVisibility(View.VISIBLE);
            layoutWudang.setVisibility(View.GONE);
        } else {
            tvLsvNoData.setVisibility(View.VISIBLE);
            lsvDetial.setVisibility(View.GONE);
            layoutWudang.setVisibility(View.GONE);
        }
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        if (stockType == StockDetailChartsActivity.STOCK_MARKE_INDEX) {
            layout_price.setVisibility(View.GONE);
        } else if (stockType == StockDetailChartsActivity.STOCK_COMMON) {
            for (int i = 0; i < priceVolumeIds.length; i++) {
                priceVolumes[i] = (TextView) view.findViewById(priceVolumeIds[i]);
            }
            initValue();
        }
        GetMinLineData();
    }

    private void GetMinLineData() {
        ((StockDetailChartsActivity) getActivity()).setLoading(true);
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
                    timesBitmap = new TimesFivesBitmap((int) (0.8 * width), height);
                }
                StockMinuteBean bean = JSONObject.parseObject(responseInfo, StockMinuteBean.class);
                if (bean.getCode() == 0) {
                    stockMinuteDatas = bean.getData();
                    if (stockMinuteDatas.size() > 0) {
                        if (totalHeight <= DeviceUtil.DPI480P) {
                            timesBitmap.setIsSw480P(true);
                        } else if (totalHeight <= DeviceUtil.DPI720P) {
                            timesBitmap.setIsSw720P(true);
                        } else if (totalHeight <= DeviceUtil.DPI1080P) {
                            timesBitmap.setIsSw1080P(true);
                        }
                        timesBitmap.setShowDetail(true);
                        Bitmap bitmap = timesBitmap.setTimesList(bean, true, stock_charge_type);
                        mTimesView.setBitmap(bitmap);
                        mTimesView.setData(timesBitmap, stock_charge_type);
                    }
                    MessageHandlerList.sendMessage(StockDetailChartsActivity.class, ConstantUtils.DISS_PROGRESSBAR, 0);
                    ((StockDetailChartsActivity) getActivity()).setLoading(false);
                } else {

                }
            }

            @Override
            public void onFailure(String msg) {
                UIHelper.toast(mContext, "网络连接异常，请检查后重试！");
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
        AutoRefresh(refreshtime);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (timer != null) {
            timer.cancel();
        }
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
                AppManager.getInstance().sendMessage("TimesFragment");
                // TODO: 2016/9/26 0026
                getStockTimeDetial(stockCode);
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
            GetMinLineData();
            if (stockType == StockDetailChartsActivity.STOCK_COMMON)
                InitList(stockCode);

        } else {
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
        }
    }

    private void InitList(final String stockCode) {
        final Map<String, String> param = new HashMap<String, String>();
        param.put("stock_code", stockCode);

        GGOKHTTP.GGHttpInterface httpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                StockDetail bean = JSONObject.parseObject(responseInfo, StockDetail.class);
                if (bean.getCode()==0) {
                    StockDetail.TreatData info = bean.getData();
                    closePrice = StringUtils.getDouble(info.getClose_price());
                    priceVolumDatas.clear();
                    //卖
                    priceVolumDatas.add(StringUtils.save2Significand(info.getSell5_price()));
                    priceVolumDatas.add(info.getSell5_volume() + "");
                    priceVolumDatas.add(StringUtils.save2Significand(info.getSell4_price()));
                    priceVolumDatas.add(info.getSell4_volume() + "");
                    priceVolumDatas.add(StringUtils.save2Significand(info.getSell3_price()));
                    priceVolumDatas.add(info.getSell3_volume() + "");
                    priceVolumDatas.add(StringUtils.save2Significand(info.getSell2_price()));
                    priceVolumDatas.add(info.getSell2_volume() + "");
                    priceVolumDatas.add(StringUtils.save2Significand(info.getSell1_price()));
                    priceVolumDatas.add(info.getSell1_volume() + "");
                    //买
                    priceVolumDatas.add(StringUtils.save2Significand(info.getBuy1_price()));
                    priceVolumDatas.add(info.getBuy1_volume() + "");
                    priceVolumDatas.add(StringUtils.save2Significand(info.getBuy2_price()));
                    priceVolumDatas.add(info.getBuy2_volume() + "");
                    priceVolumDatas.add(StringUtils.save2Significand(info.getBuy3_price()));
                    priceVolumDatas.add(info.getBuy3_volume() + "");
                    priceVolumDatas.add(StringUtils.save2Significand(info.getBuy4_price()));
                    priceVolumDatas.add(info.getBuy4_volume() + "");
                    priceVolumDatas.add(StringUtils.save2Significand(info.getBuy5_price()));
                    priceVolumDatas.add(info.getBuy5_volume() + "");
                    //买卖数值
                    initValue();
                }
            }

            @Override
            public void onFailure(String msg) {
            }
        };
        new GGOKHTTP(param, GGOKHTTP.ONE_STOCK_DETAIL, httpInterface).startGet();
    }

    private void initValue() {
        if (priceVolumDatas != null && priceVolumDatas.size() > 0) {
            for (int i = 0; i < priceVolumeIds.length; i++) {
                if (!priceVolumDatas.get(i).equals("null") && !priceVolumDatas.get(i).equals("")) {
                    if (i % 2 == 0) {
                        priceVolumes[i].setText(priceVolumDatas.get(i));
                        if (StringUtils.getDouble(priceVolumDatas.get(i)) > closePrice) {
                            priceVolumes[i].setTextColor(getResColor(R.color.stock_red));
                        } else if (StringUtils.getDouble(priceVolumDatas.get(i)) == closePrice || StringUtils.getDouble(priceVolumDatas.get(i)) == 0) {
                            priceVolumes[i].setTextColor(getResColor(R.color.gray_light));
                        } else {
                            priceVolumes[i].setTextColor(getResColor(R.color.stock_green));
                        }
                    } else {
                        priceVolumes[i].setText(Integer.parseInt(priceVolumDatas.get(i)) / 100 + "");
                    }
                }
            }
        }
    }

    private CompoundButton.OnCheckedChangeListener rdbTablistener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            lsvDetial.setFocusable(false);
            if (isChecked)
                switch (buttonView.getId()) {
                    case R.id.rdb_detail_wudang:
                       showLayoutWudang();
                        break;
                    case R.id.rdb_detail_mingxi:
                        showLayoutMingxi();
                        break;
                }
        }
    };

}