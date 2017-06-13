package cn.gogoal.im.fragment.stock.stockften;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.copy.FtenUtils;
import cn.gogoal.im.ui.view.BarView;

/**
 * Created by dave.
 * Date: 2017/6/7.
 * Desc: 财务分析
 */
public class FinancialAnalysisFragment extends BaseFragment {

    @BindView(R.id.textProfit1)
    TextView textProfit1;
    @BindView(R.id.textShareEPS1)
    TextView textShareEPS1;
    @BindView(R.id.textProfitRate1)
    TextView textProfitRate1;
    @BindView(R.id.textDebtRatio1)
    TextView textDebtRatio1;
    @BindView(R.id.textSovency1)
    TextView textSovency1;
    @BindView(R.id.textTurnoverRate1)
    TextView textTurnoverRate1;
    @BindView(R.id.textGrows1)
    TextView textGrows1;

    @BindView(R.id.barProfitView)
    BarView barProfitView;
    @BindView(R.id.barShareEPS)
    BarView barShareEPS;
    @BindView(R.id.barProfitRateView)
    BarView barProfitRateView;
    @BindView(R.id.barDebtRatioView)
    BarView barDebtRatioView;
    @BindView(R.id.barSovencyView)
    BarView barSovencyView;
    @BindView(R.id.barTurnoverRateView)
    BarView barTurnoverRateView;
    @BindView(R.id.barGrowsView)
    BarView barGrowsView;

    private String stockCode;
    private String stockName;

    private String stype;

    public static FinancialAnalysisFragment getInstance(String stockCode, String stockName) {
        FinancialAnalysisFragment fragment = new FinancialAnalysisFragment();
        Bundle b = new Bundle();
        b.putString("stockCode", stockCode);
        b.putString("stockName", stockName);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_financial_analysis;
    }

    @Override
    public void doBusiness(Context mContext) {

        stockCode = getArguments().getString("stockCode");
        stockName = getArguments().getString("stockName");

        getParamData();
    }

    private void getParamData() {

        final Map<String, String> param = new HashMap<>();
        param.put("stock_code", stockCode);

        final GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                JSONObject object = JSONObject.parseObject(responseInfo);
                if (object.getIntValue("code") == 0) {
                    JSONObject data = object.getJSONObject("data");
                    stype = data.getString("stype");

                    getFinacialData("0", "1", stype);
                    getFinacialData("0", "2", stype);
                    getFinacialData("0", "3", stype);
                    getFinacialData("0", "4", stype);
                    getFinacialData("0", "5", stype);
                    getFinacialData("0", "6", stype);
                    getFinacialData("0", "7", stype);

                    setTextTitle(stype);
                }
            }

            @Override
            public void onFailure(String msg) {
            }
        };
        new GGOKHTTP(param, GGOKHTTP.GET_STOCK_FINANCIAL_TYPE, ggHttpInterface).startGet();
    }

    private void setTextTitle(String stype) {
        if (stype.equals("1")) {
            textProfit1.setText(FtenUtils.title1[0]);
            textShareEPS1.setText(FtenUtils.title1[1]);
            textProfitRate1.setText(FtenUtils.title1[2]);
            textDebtRatio1.setText(FtenUtils.title1[3]);
            textSovency1.setText(FtenUtils.title1[4]);
            textTurnoverRate1.setText(FtenUtils.title1[5]);
            textGrows1.setText(FtenUtils.title1[6]);
        } else {
            textProfit1.setText(FtenUtils.title2[0]);
            textShareEPS1.setText(FtenUtils.title2[1]);
            textProfitRate1.setText(FtenUtils.title2[2]);
            textDebtRatio1.setText(FtenUtils.title2[3]);
            textSovency1.setText(FtenUtils.title2[4]);
            textTurnoverRate1.setText(FtenUtils.title2[5]);
            textGrows1.setText(FtenUtils.title2[6]);
        }
    }

    private void getFinacialData(String season, final String type, final String stype) {

        final Map<String, String> param = new HashMap<>();
        param.put("stock_code", stockCode);
        param.put("stock_finance_type", stype);
        param.put("season", season);
        param.put("type", type);
        param.put("page", "1");

        final GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                JSONObject object = JSONObject.parseObject(responseInfo);
                if (object.getIntValue("code") == 0) {
                    JSONObject data = object.getJSONObject("data");
                    JSONArray retained_profits = null;
                    String profits = null;
                    if (stype.equals("1")) {
                        switch (type) {
                            case "1":
                                profits = "retained_profits10";
                                break;
                            case "2":
                                profits = "perbasic_eps20";
                                break;
                            case "3":
                                profits = "epopratio_profit35";
                                break;
                            case "4":
                                profits = "asset_liratio46";
                                break;
                            case "5":
                                profits = "debtflow_ratio53";
                                break;
                            case "6":
                                profits = "service_saveloan72";
                                break;
                            case "7":
                                profits = "business_totalrevenue_growthrate90";
                                break;
                        }
                    } else {
                        switch (type) {
                            case "1":
                                profits = "ffajr_09";
                                break;
                            case "2":
                                profits = "ffajr_19";
                                break;
                            case "3":
                                profits = "ffajr_29";
                                break;
                            case "4":
                                profits = "ffajr_38";
                                break;
                            case "5":
                                profits = "ffajr_45";
                                break;
                            case "6":
                                profits = "ffajr_50";
                                break;
                            case "7":
                                profits = "ffajr_57";
                                break;
                        }
                    }
                    retained_profits = data.getJSONArray(profits);

                    List<Float> values = new ArrayList<>();
                    for (int i = 0; i < retained_profits.size(); i++) {
                        values.add(retained_profits.getFloatValue(i));
                    }

                    JSONArray title = data.getJSONArray("title");
                    List<String> dates = new ArrayList<>();
                    for (int i = 0; i < title.size(); i++) {
                        dates.add(FtenUtils.getReportType(title.getString(i)));
                    }

                    Map<String, Object> map = new HashMap<>();
                    map.put("dates", dates);
                    map.put("values", values);

                    switch (type) {
                        case "1":
                            barProfitView.setChartData(map);
                            break;
                        case "2":
                            barShareEPS.setChartData(map);
                            break;
                        case "3":
                            barProfitRateView.setChartData(map);
                            break;
                        case "4":
                            barDebtRatioView.setChartData(map);
                            break;
                        case "5":
                            barSovencyView.setChartData(map);
                            break;
                        case "6":
                            barTurnoverRateView.setChartData(map);
                            break;
                        case "7":
                            barGrowsView.setChartData(map);
                            break;
                    }
                }
            }

            @Override
            public void onFailure(String msg) {
                UIHelper.toast(getContext(), R.string.net_erro_hint);
            }
        };
        new GGOKHTTP(param, GGOKHTTP.FINANCIAL_ANALYSIS, ggHttpInterface).startGet();
    }

    @OnClick({R.id.textProfit2, R.id.textShareEPS2, R.id.textProfitRate2, R.id.textDebtRatio2,
            R.id.textSovency2, R.id.textTurnoverRate2, R.id.textGrows2,})
    public void AnalysisOnClick(View v) {
        switch (v.getId()) {
            case R.id.textProfit2:
                break;
            case R.id.textShareEPS2:
                break;
            case R.id.textProfitRate2:
                break;
            case R.id.textDebtRatio2:
                break;
            case R.id.textSovency2:
                break;
            case R.id.textTurnoverRate2:
                break;
            case R.id.textGrows2:
                break;
        }
    }
}
