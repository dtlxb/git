package cn.gogoal.im.fragment.stock;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.gogoal.im.R;
import cn.gogoal.im.activity.stock.stockften.ShareholderResearchActivity;
import cn.gogoal.im.adapter.stockften.CompanySummaryAdapter;
import cn.gogoal.im.adapter.stockften.DividendTransAdapter;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.bean.ChartBean;
import cn.gogoal.im.bean.f10.CompanyInforData;
import cn.gogoal.im.bean.f10.DividendTransData;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.StringUtils;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.copy.FtenUtils;
import cn.gogoal.im.ui.view.BarView;

/**
 * author wangjd on 2017/6/16 0016.
 * Staff_id 1375
 * phone 18930640263
 * description :股票公司资料（个股Tab）
 */
public class CompanyInfoFragment extends BaseFragment {

    @BindView(R.id.rv_company_summary)
    RecyclerView rvComSummary;

    @BindView(R.id.chart_tab_one)
    TextView chart_tab_one;
    @BindView(R.id.chart_tab_two)
    TextView chart_tab_two;
    @BindView(R.id.chart_tab_three)
    TextView chart_tab_three;
    @BindView(R.id.barPerforView)
    BarView barPerforView;
    @BindView(R.id.textPerfor)
    TextView textPerfor;

    @BindView(R.id.rv_dividend_transfer)
    RecyclerView rvDivTransfer;

    private String stockCode;
    private String stockName;

    private ArrayList<CompanyInforData> infoList = new ArrayList<>();
    private CompanySummaryAdapter summaryAdapter;

    private String stype;
    private JSONObject finacialData;
    private int chartTab = 0;
    List<ChartBean> chartBeanList;

    private ArrayList<DividendTransData> transDatas = new ArrayList<>();
    private DividendTransAdapter transAdapter;

    public static CompanyInfoFragment newInstance(String stockCode, String stockName) {
        CompanyInfoFragment infoFragment = new CompanyInfoFragment();
        Bundle bundle = new Bundle();
        bundle.putString("stockCode", stockCode);
        bundle.putString("stockName", stockName);
        infoFragment.setArguments(bundle);
        return infoFragment;
    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_company_info;
    }

    @Override
    public void doBusiness(Context mContext) {
        stockCode = getArguments().getString("stockCode");
        stockName = getArguments().getString("stockName");
        chartBeanList = new ArrayList<>();

        BaseActivity.initRecycleView(rvComSummary, null);
        BaseActivity.initRecycleView(rvDivTransfer, null);

        rvComSummary.setHasFixedSize(true);
        rvComSummary.setNestedScrollingEnabled(false);

        rvDivTransfer.setHasFixedSize(true);
        rvDivTransfer.setNestedScrollingEnabled(false);

        getCompanySummary();

        getParamData();

        getDividendTransfer();
    }

    /**
     * 公司概况
     */
    private void getCompanySummary() {
        Map<String, String> param = new HashMap<>();
        param.put("stock_code", stockCode);

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                JSONObject object = JSONObject.parseObject(responseInfo);
                if (object.getIntValue("code") == 0) {
                    JSONObject data = object.getJSONObject("data");
                    JSONObject basic_data = data.getJSONObject("basic_data");

                    ArrayList<CompanyInforData> basicList = new ArrayList<>();
                    for (int i = 0; i < FtenUtils.comSummary1.length; i++) {
                        basicList.add(new CompanyInforData("公司概况", FtenUtils.comSummary1[i],
                                basic_data.getString(FtenUtils.comSummary1_1[i]), null));
                    }
                    infoList.addAll(basicList);

                    getEquityData();
                }
            }

            @Override
            public void onFailure(String msg) {
                UIHelper.toast(getActivity(), R.string.net_erro_hint);
            }
        };
        new GGOKHTTP(param, GGOKHTTP.COMPANY_SUMMARY, ggHttpInterface).startGet();
    }

    /**
     * 股本
     */
    private void getEquityData() {
        Map<String, String> param = new HashMap<>();
        param.put("stock_code", stockCode);

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                JSONObject object = JSONObject.parseObject(responseInfo);
                if (object.getIntValue("code") == 0) {
                    JSONObject data = object.getJSONObject("data");

                    ArrayList<CompanyInforData> equityList = new ArrayList<>();
                    for (int i = 0; i < FtenUtils.equity1.length; i++) {
                        equityList.add(new CompanyInforData("股本", FtenUtils.equity1[i],
                                data.getString(FtenUtils.equity1_1[i]), null));
                    }
                    infoList.addAll(equityList);

                    getExecutivesData();
                }
            }

            @Override
            public void onFailure(String msg) {
            }
        };
        new GGOKHTTP(param, GGOKHTTP.STOCK_STRUCTURE, ggHttpInterface).startGet();
    }

    /**
     * 高管
     */
    private void getExecutivesData() {
        final Map<String, String> param = new HashMap<>();
        param.put("stock_code", stockCode);

        final GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                JSONObject object = JSONObject.parseObject(responseInfo);
                if (object.getIntValue("code") == 0) {
                    JSONObject data = object.getJSONObject("data");
                    JSONArray senior_info_list = data.getJSONArray("senior_info_list");

                    ArrayList<CompanyInforData> executivesList = new ArrayList<>();
                    for (int i = 0; i < 3; i++) {
                        executivesList.add(new CompanyInforData("高管",
                                senior_info_list.getJSONObject(i).getString("duty"),
                                senior_info_list.getJSONObject(i).getString("name"), null));
                    }
                    infoList.addAll(executivesList);

                    getPrincipalRevenue();
                }
            }

            @Override
            public void onFailure(String msg) {
            }
        };
        new GGOKHTTP(param, GGOKHTTP.COMPANY_SENIOR, ggHttpInterface).startGet();
    }

    /**
     * 主营收入
     */
    private void getPrincipalRevenue() {
        final Map<String, String> param = new HashMap<>();
        param.put("stock_code", stockCode);

        final GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                JSONObject object = JSONObject.parseObject(responseInfo);
                if (object.getIntValue("code") == 0) {
                    JSONObject data = object.getJSONObject("data");
                    JSONArray main_business_income = data.getJSONArray("main_business_income");

                    ArrayList<CompanyInforData> revenueList = new ArrayList<>();
                    revenueList.add(new CompanyInforData("主营收入", "主营收入构成", "收入(万元)", "占比"));
                    for (int i = 0; i < 3; i++) {
                        revenueList.add(new CompanyInforData(null,
                                main_business_income.getJSONObject(i).getString("name"),
                                main_business_income.getJSONObject(i).getString("product_value"),
                                main_business_income.getJSONObject(i).getString("product_proportion")));
                    }
                    infoList.addAll(revenueList);

                    summaryAdapter = new CompanySummaryAdapter(getActivity(), infoList, stockCode, stockName);
                    rvComSummary.setAdapter(summaryAdapter);
                }
            }

            @Override
            public void onFailure(String msg) {
            }
        };
        new GGOKHTTP(param, GGOKHTTP.GET_STOCK_MAIN_BUSINESS, ggHttpInterface).startGet();
    }

    /**
     * 获取股票类型
     */
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

                    getFinacialData(stype);
                }
            }

            @Override
            public void onFailure(String msg) {
            }
        };
        new GGOKHTTP(param, GGOKHTTP.GET_STOCK_FINANCIAL_TYPE, ggHttpInterface).startGet();
    }

    /**
     * 业绩表现
     */
    private void getFinacialData(final String stype) {

        final Map<String, String> param = new HashMap<>();
        param.put("stock_code", stockCode);
        param.put("stock_finance_type", stype);
        param.put("season", "4");
        param.put("type", "0");
        param.put("page", "1");

        final GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                JSONObject object = JSONObject.parseObject(responseInfo);
                if (object.getIntValue("code") == 0) {
                    finacialData = object.getJSONObject("data");

                    setBarViewData(finacialData, stype, chartTab);
                }
            }

            @Override
            public void onFailure(String msg) {
            }
        };
        new GGOKHTTP(param, GGOKHTTP.FINANCIAL_ANALYSIS, ggHttpInterface).startGet();
    }

    /**
     * 设置柱状图数据
     */
    private void setBarViewData(JSONObject data, String stype, int chartTab) {
        String profits = null;
        if (chartTab == 0) {
            if (stype.equals("1")) {
                profits = "business_income04";
            } else {
                profits = "ffajr_04";
            }
        } else if (chartTab == 1) {
            if (stype.equals("1")) {
                profits = "retained_profits10";
            } else {
                profits = "ffajr_09";
            }
        } else {
            if (stype.equals("1")) {
                profits = "perbasic_eps20";
            } else {
                profits = "ffajr_19";
            }
        }

        JSONArray retained_profits = data.getJSONArray(profits);
        List<Float> values = new ArrayList<>();
        if (chartTab == 2) {
            for (int i = 0; i < retained_profits.size(); i++) {
                values.add(retained_profits.getFloatValue(i));
            }
        } else {
            for (int i = 0; i < retained_profits.size(); i++) {
                values.add(Float.valueOf(StringUtils.save2Significand(retained_profits.getDoubleValue(i) / 10000)));
            }
        }

        JSONArray title = data.getJSONArray("title");
        List<String> dates = new ArrayList<>();
        for (int i = 0; i < title.size(); i++) {
            dates.add(FtenUtils.getReportType(title.getString(i)));
        }

        chartBeanList.clear();
        for (int i = 0; i < title.size(); i++) {
            chartBeanList.add(new ChartBean(values.get(i), dates.get(i)));
        }
        barPerforView.setTextSize(AppDevice.dp2px(getActivity(), 10));
        barPerforView.setChartData(chartBeanList);
    }

    /**
     * 分红转送
     */
    private void getDividendTransfer() {
        final Map<String, String> param = new HashMap<>();
        param.put("stock_code", stockCode);

        final GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                KLog.e(responseInfo);
                JSONObject object = JSONObject.parseObject(responseInfo);
                if (object.getIntValue("code") == 0) {
                    JSONArray data = object.getJSONArray("data");

                    for (int i = 0; i < (data.size() > 3 ? 3 : data.size()); i++) {
                        transDatas.add(new DividendTransData(
                                data.getJSONObject(i).getString("dividend_program"),
                                data.getJSONObject(i).getString("date")));
                    }

                    transAdapter = new DividendTransAdapter(getActivity(), transDatas);
                    rvDivTransfer.setAdapter(transAdapter);
                }
            }

            @Override
            public void onFailure(String msg) {
            }
        };
        new GGOKHTTP(param, GGOKHTTP.DIVIDEND_FINANCING, ggHttpInterface).startGet();
    }

    @OnClick({R.id.chart_tab_one, R.id.chart_tab_two, R.id.chart_tab_three, R.id.relative_share_holder,
            R.id.relative_circula_holder, R.id.relative_institu_investor})
    public void ChartTabClick(View v) {
        switch (v.getId()) {
            case R.id.chart_tab_one:
                chart_tab_one.setEnabled(false);
                chart_tab_two.setEnabled(true);
                chart_tab_three.setEnabled(true);
                chart_tab_one.setTextColor(getResColor(R.color.white));
                chart_tab_one.setBackgroundResource(R.drawable.chart_tab_shape_select_left);
                chart_tab_two.setTextColor(getResColor(R.color.colorPrimary));
                chart_tab_two.setBackgroundResource(R.drawable.chart_tab_center_shape);
                chart_tab_three.setTextColor(getResColor(R.color.colorPrimary));
                chart_tab_three.setBackgroundResource(0);

                textPerfor.setText("单位:亿元");

                chartTab = 0;
                setBarViewData(finacialData, stype, chartTab);
                break;
            case R.id.chart_tab_two:
                chart_tab_one.setEnabled(true);
                chart_tab_two.setEnabled(false);
                chart_tab_three.setEnabled(true);
                chart_tab_one.setTextColor(getResColor(R.color.colorPrimary));
                chart_tab_one.setBackgroundResource(0);
                chart_tab_two.setTextColor(getResColor(R.color.white));
                chart_tab_two.setBackgroundResource(R.drawable.chart_tab_shape_select_center);
                chart_tab_three.setTextColor(getResColor(R.color.colorPrimary));
                chart_tab_three.setBackgroundResource(0);

                textPerfor.setText("单位:亿元");

                chartTab = 1;
                setBarViewData(finacialData, stype, chartTab);
                break;
            case R.id.chart_tab_three:
                chart_tab_one.setEnabled(true);
                chart_tab_two.setEnabled(true);
                chart_tab_three.setEnabled(false);
                chart_tab_one.setTextColor(getResColor(R.color.colorPrimary));
                chart_tab_one.setBackgroundResource(0);
                chart_tab_two.setTextColor(getResColor(R.color.colorPrimary));
                chart_tab_two.setBackgroundResource(R.drawable.chart_tab_center_shape);
                chart_tab_three.setTextColor(getResColor(R.color.white));
                chart_tab_three.setBackgroundResource(R.drawable.chart_tab_shape_select_right);

                textPerfor.setText("单位:元");

                chartTab = 2;
                setBarViewData(finacialData, stype, chartTab);
                break;
            case R.id.relative_share_holder:
                JumpShareholderResearch(ShareholderResearchActivity.TEN_HOLDER);
                break;
            case R.id.relative_circula_holder:
                JumpShareholderResearch(ShareholderResearchActivity.TEN_TRADABLE_HOLDER);
                break;
            case R.id.relative_institu_investor:
                JumpShareholderResearch(ShareholderResearchActivity.FUND_HOLDER);
                break;
        }
    }

    private void JumpShareholderResearch(int genre) {
        Intent intent = new Intent(getContext(), ShareholderResearchActivity.class);
        intent.putExtra("stockCode", stockCode);
        intent.putExtra("stockName", stockName);
        intent.putExtra("genre", genre);
        startActivity(intent);
    }
}
