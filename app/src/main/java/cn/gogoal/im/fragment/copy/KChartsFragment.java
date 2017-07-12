package cn.gogoal.im.fragment.copy;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;


import com.alibaba.fastjson.JSONArray;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.activity.copy.StockDetailChartsActivity;
import cn.gogoal.im.base.AppManager;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.bean.BaseMessage;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.FileUtil;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.JsonUtils;
import cn.gogoal.im.common.SPTools;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.ui.stockviews.KChartView;
import cn.gogoal.im.ui.stockviews.KLineGridChart;
import hply.com.niugu.bean.OHLCBean;


public class KChartsFragment extends BaseFragment {
    @BindView(R.id.k_charts_view)
    KChartView mKChartView;
    KLineGridChart mGridChart;
    private int type;
    private String stockCode;
    private OHLCBean bean;
    private List<Map<String, Object>> mOHLCData = new ArrayList<Map<String, Object>>();
    private Context mContext;
    private int totalHeight;
    private int stockType;

    private int dayK1;
    private int dayK2;
    private int dayK3;
    private int dayK4;

    private int width;
    private int height;

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
        dayK1 = SPTools.getInt("tv_ln1", 5);
        dayK2 = SPTools.getInt("tv_ln2", 10);
        dayK3 = SPTools.getInt("tv_ln3", 20);
        dayK4 = SPTools.getInt("tv_ln4", 0);
        if (stockType == StockDetailChartsActivity.STOCK_MARKE_INDEX) {
            width = bundle.getInt("width", 0);
        } else {
            width = bundle.getInt("width", 0) - AppDevice.dp2px(getActivity(), 52);
        }
        height = bundle.getInt("height", 0);
        GetKLineDataPage(true, false, true);
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
    private void refresh(BaseMessage message) {
        switch (message.getCode()) {
            case "auhority":
                page = 0;
                if (!isHidden()) GetKLineDataPage(false, true, true);
                break;
        }
    }

    public void GetKLineDataPage(boolean needShowProgress, final Boolean is_authroity, final boolean is_need_animation) {
        if (needShowProgress) {
            ((StockDetailChartsActivity) getActivity()).showProgressbar(true);
        }
        page++;

        HashMap<String, String> param = new HashMap<>();
        if (StockDetailChartsActivity.STOCK_COMMON == stockType) {
            param.put("stock_code", stockCode);
            param.put("right_type", SPTools.getInt("authority_type", 0) + "");
        } else if (StockDetailChartsActivity.STOCK_MARKE_INDEX == stockType) {
            param.put("fullcode", stockCode);
        }
        param.put("kline_type", type + "");
        param.put("avg_line_type", dayK1 + ";" + dayK2 + ";" + dayK3);
        param.put("page", String.valueOf(page));
        param.put("rows", "200");

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                //FileUtil.writeRequestResponse(responseInfo, "GetKLineDataPage");
                if (KChartsFragment.this.isVisible()) {
                    if (is_authroity) mOHLCData.clear();
                    JsonObject result = JsonUtils.toJsonObject(responseInfo);
                    if (result != null) {
                        JsonArray data_array = JsonUtils.getJsonArray(result, "data");
                        if (result.get("code").getAsInt() == 0) {
                            handleData(data_array);
                            if (page == 1) {
                                mGridChart = new KLineGridChart(width, height);
                                if (stockType == StockDetailChartsActivity.STOCK_MARKE_INDEX) {
                                    mGridChart.setLeftMargin(AppDevice.dp2px(getActivity(), 50));
                                } else {
                                    mGridChart.setLeftMargin(AppDevice.dp2px(getActivity(), 45));
                                }
                                mGridChart.setRightMargin(AppDevice.dp2px(getActivity(), 0));
                                mGridChart.setShowDetail(true);
                                mGridChart.setUperLatitudeNum(1);
                                mGridChart.setLongitudeNum(1);
                                mGridChart.setmAxisTitleSize(AppDevice.dp2px(getActivity(), 10));
                                mGridChart.setmSize(AppDevice.dp2px(getActivity(), 1));
                                mGridChart.setBorderColor(KChartsFragment.this.getResColor(R.color.weixin_text));
                                mGridChart.setmLongtitudeColor(KChartsFragment.this.getResColor(R.color.weixin_text));
                                mGridChart.setmLatitudeColor(KChartsFragment.this.getResColor(R.color.weixin_text));
                                Bitmap bitmap = mGridChart.drawBitMap();
                                mKChartView.setBitmapData(mGridChart);
                                mKChartView.setBitmap(bitmap);
                                mKChartView.setChartsType(type);
                            }
                            mKChartView.setOHLCData(mOHLCData, is_need_animation);
                            mKChartView.setIsRefresh(true);
                            ((StockDetailChartsActivity) getActivity()).showProgressbar(false);
                            //MessageHandlerList.sendMessage(StockDetailChartsActivity.class, AppConst.DISS_PROGRESSBAR, 0);
                            AppManager.getInstance().sendMessage("Diss_Progressbar");
                        }
                    }
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(String msg) {
                UIHelper.toast(mContext, "网络连接异常，请检查后重试！");
                if (mKChartView != null) {
                    mKChartView.setIsRefresh(true);
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

    private void handleData(JsonArray data) {
        for (int i = 0; i < data.size(); i++) {
            JsonObject singleData = (JsonObject) data.get(i);
            Map<String, Object> itemData = new HashMap<>();
            itemData.put("amplitude", singleData.get("amplitude").getAsFloat());
            itemData.put("avg_price_" + dayK1, ParseNum(singleData.get("avg_price_" + dayK1)));
            itemData.put("avg_price_" + dayK2, ParseNum(singleData.get("avg_price_" + dayK2)));
            itemData.put("avg_price_" + dayK3, ParseNum(singleData.get("avg_price_" + dayK3)));
            itemData.put("avg_price_" + dayK4, ParseNum(singleData.get("avg_price_" + dayK4)));
            itemData.put("close_price", ParseNum(singleData.get("close_price")));
            itemData.put("date", singleData.get("date").getAsString());
            itemData.put("high_price", ParseNum(singleData.get("high_price")));
            itemData.put("low_price", ParseNum(singleData.get("low_price")));
            itemData.put("open_price", ParseNum(singleData.get("open_price")));
            itemData.put("price_change", ParseNum(singleData.get("price_change")));
            itemData.put("price_change_rate", ParseNum(singleData.get("price_change_rate")));
            itemData.put("rightValue", ParseNum(singleData.get("rightValue")));
            itemData.put("turnover", ParseNum(singleData.get("turnover")));
            itemData.put("turnover_rate", ParseNum(singleData.get("turnover_rate")));
            itemData.put("volume", ParseNum(singleData.get("volume")));
            mOHLCData.add(itemData);
        }
    }

    private float ParseNum(JsonElement element) {
        float avg_price;
        if (element == null || element.isJsonNull()) {
            avg_price = 0.0f;
        } else {
            avg_price = Float.parseFloat(element.getAsString());
        }
        return avg_price;
    }

    public KChartView getMyChartsView() {
        return mKChartView;
    }

    public void showLoadingDialog() {
        progressDialog.show();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            if (mOHLCData.size() == 0) {
                page = 0;
                GetKLineDataPage(true, false, false);
            }
        }
        super.onHiddenChanged(hidden);
    }


}
