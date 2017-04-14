package cn.gogoal.im.fragment.copy;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.activity.copy.StockDetailChartsActivity;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.bean.BaseMessage;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.SPTools;
import cn.gogoal.im.common.UIHelper;
import hply.com.niugu.MessageHandlerList;
import hply.com.niugu.bean.OHLCBean;
import hply.com.niugu.view.KChartsView;


public class KChartsFragment extends BaseFragment {
    @BindView(R.id.k_charts_view)
    KChartsView mMyChartsView;

    private int type;
    private String stockCode;
    private OHLCBean bean;
    private List<Map<String, Object>> mOHLCData = new ArrayList<Map<String, Object>>();
    private Context mContext;
    private int totalHeight;
    private int stockType;

    private int dayk1;
    private int dayk2;
    private int dayk3;
    private int dayk4;

    //页数
    private int page;
    //loading dialog
    private Dialog progressDialog;

//    @Override
    public int bindLayout() {
        return R.layout.fragment_kcharts;
    }
//
    @Override
    public void doBusiness(Context mContext) {
        Bundle bundle = getArguments();
        totalHeight = bundle.getInt("totalHeight", 0);
        dayk1 = SPTools.getInt("tv_ln1", 5);
        dayk2 = SPTools.getInt("tv_ln2", 10);
        dayk3 = SPTools.getInt("tv_ln3", 20);
        dayk4 = SPTools.getInt("tv_ln4", 0);
        GetKLineDataPage(false, true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        stockCode = getActivity().getIntent().getStringExtra("stockCode");

        type = getArguments().getInt("type");

        progressDialog = new Dialog(getActivity(), R.style.progress);
        progressDialog.setContentView(R.layout.progress);
        progressDialog.setCanceledOnTouchOutside(false);

        stockType = getArguments().getInt("stockType");
    }

    @Subscriber(tag = "KChartsFragment")
    private void refresh(BaseMessage messge) {
        switch (messge.getCode()) {
            case "auhority":
                page = 0;
                if (!isHidden()) GetKLineDataPage(true, true);
                break;
        }
    }

    public void GetKLineDataPage(final Boolean is_authroity, final boolean is_need_animation) {
        page++;
        ((StockDetailChartsActivity) getActivity()).setLoading(true);

        HashMap<String, String> param = new HashMap<>();
        if (StockDetailChartsActivity.STOCK_COMMON == stockType) {
            param.put("stock_code", stockCode);
            param.put("right_type", SPTools.getInt("authority_type", 0) + "");
        } else if (StockDetailChartsActivity.STOCK_MARKE_INDEX == stockType) {
            param.put("fullcode", stockCode);
        }
        param.put("kline_type", type + "");
        param.put("avg_line_type", dayk1 + ";" + dayk2 + ";" + dayk3);
        param.put("page", String.valueOf(page));
        param.put("rows", "200");

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                if (KChartsFragment.this.isVisible()) {
                    if (is_authroity) mOHLCData.clear();
                    JSONObject result = JSONObject.parseObject(responseInfo);
                    if (result != null) {
                        JSONArray data_array = (JSONArray) result.get("data");
                        if ((int) result.get("code") == 0) {
                            handleData(data_array);
                            if (page == 1) {
                                if (totalHeight <= AppDevice.DPI480P) {
                                    mMyChartsView.setIsSw480P(true);
                                } else if (totalHeight <= AppDevice.DPI720P) {
                                    mMyChartsView.setIsSw720P(true);
                                } else if (totalHeight <= AppDevice.DPI1080P) {
                                    mMyChartsView.setIsSw1080P(true);
                                }
                                mMyChartsView.setLowerChartTabTitles(new String[]{"Volume", "MACD", "KDJ", "RSI"});
                                mMyChartsView.setLongitudeNum(1);
                                mMyChartsView.setChartsType(type);
                            }
                            mMyChartsView.setOHLCData(mOHLCData, is_need_animation);
                            mMyChartsView.setIsRefresh(true);
                            ((StockDetailChartsActivity) getActivity()).setLoading(false);
                            MessageHandlerList.sendMessage(StockDetailChartsActivity.class, AppConst.DISS_PROGRESSBAR, 0);
                        }
                    }
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(String msg) {
                UIHelper.toast(mContext, "网络连接异常，请检查后重试！");
                if (mMyChartsView != null) {
                    mMyChartsView.setIsRefresh(true);
                }
                progressDialog.dismiss();
                page--;
            }
        };

        if (StockDetailChartsActivity.STOCK_COMMON == stockType) {
            new GGOKHTTP(param, GGOKHTTP.STOCK_K_LINE, ggHttpInterface).startGet();
        } else if (StockDetailChartsActivity.STOCK_MARKE_INDEX == stockType) {
            new GGOKHTTP(param, GGOKHTTP.GET_HQ_KLINE, ggHttpInterface).startGet();
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public KChartsView getMyChartsView() {
        return mMyChartsView;
    }

    public void showLoadingDialog() {
        progressDialog.show();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            if (mOHLCData.size() == 0) {
                page = 0;
                GetKLineDataPage(false, false);
            }
        }
        super.onHiddenChanged(hidden);
    }


}
