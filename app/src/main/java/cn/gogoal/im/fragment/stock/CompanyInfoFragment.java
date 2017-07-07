package cn.gogoal.im.fragment.stock;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
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
import cn.gogoal.im.activity.stock.stockften.ShareholderResearchActivity;
import cn.gogoal.im.adapter.stockften.CompanySummaryAdapter;
import cn.gogoal.im.adapter.stockften.DividendTransAdapter;
import cn.gogoal.im.adapter.stockften.PeerComparisonAdaprer;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.bean.ChartBean;
import cn.gogoal.im.bean.f10.CompanyInforData;
import cn.gogoal.im.bean.f10.DividendTransData;
import cn.gogoal.im.bean.f10.PeerCompariData;
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
    //业绩表现
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
    //分红转送
    @BindView(R.id.rv_dividend_transfer)
    RecyclerView rvDivTransfer;
    //同业比较
    @BindView(R.id.chart_peer_one)
    TextView chart_peer_one;
    @BindView(R.id.chart_peer_two)
    TextView chart_peer_two;
    @BindView(R.id.chart_peer_three)
    TextView chart_peer_three;
    @BindView(R.id.rv_peer)
    RecyclerView rvPeer;

    private String stockCode;
    private String stockName;

    private ArrayList<CompanyInforData> infoList = new ArrayList<>();
    private CompanySummaryAdapter summaryAdapter;

    private String stype;
    private JSONObject finacialData = null;
    private int chartTab = 0;
    List<ChartBean> chartBeanList;

    private ArrayList<DividendTransData> transDatas = new ArrayList<>();
    private DividendTransAdapter transAdapter;

    private ArrayList<PeerCompariData> peerLists = new ArrayList<>();
    private PeerComparisonAdaprer peerAdapter;
    private JSONObject peerData;
    private int screenWidth;

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
        screenWidth = AppDevice.getWidth(getActivity());

        BaseActivity.initRecycleView(rvComSummary, null);
        BaseActivity.initRecycleView(rvDivTransfer, null);
        BaseActivity.initRecycleView(rvPeer, null);

        rvComSummary.setHasFixedSize(true);
        rvComSummary.setNestedScrollingEnabled(false);

        rvDivTransfer.setHasFixedSize(true);
        rvDivTransfer.setNestedScrollingEnabled(false);

        rvPeer.setHasFixedSize(true);
        rvPeer.setNestedScrollingEnabled(false);

        getCompanySummary();

        getParamData();

        getDividendTransfer();

        getPeerComparison();
    }

    /**
     * 公司概况
     */
    private void getCompanySummary() {
        infoList.add(new CompanyInforData("公司概况", null, null, 1));

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
                        if (i == 3) {
                            basicList.add(new CompanyInforData(FtenUtils.comSummary1[i],
                                    basic_data.getString(FtenUtils.comSummary1_1[i]), null, 6));
                        } else {
                            basicList.add(new CompanyInforData(FtenUtils.comSummary1[i],
                                    basic_data.getString(FtenUtils.comSummary1_1[i]), null, 5));
                        }
                    }
                    infoList.addAll(basicList);
                }

                getEquityData();
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
        infoList.add(new CompanyInforData("股本", null, null, 2));

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
                        equityList.add(new CompanyInforData(FtenUtils.equity1[i],
                                data.getString(FtenUtils.equity1_1[i]), null, 5));
                    }
                    infoList.addAll(equityList);
                }

                getExecutivesData();
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
        infoList.add(new CompanyInforData("高管", null, null, 3));

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
                    for (int i = 0; i < (senior_info_list.size() >= 3 ? 3 : senior_info_list.size()); i++) {
                        executivesList.add(new CompanyInforData(
                                senior_info_list.getJSONObject(i).getString("duty"),
                                senior_info_list.getJSONObject(i).getString("name"), null, 5));
                    }
                    infoList.addAll(executivesList);
                }

                getPrincipalRevenue();
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
        infoList.add(new CompanyInforData("主营收入", null, null, 4));
        infoList.add(new CompanyInforData("主营收入构成", "收入(万元)", "占比", 7));

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
                    for (int i = 0; i < main_business_income.size(); i++) {
                        revenueList.add(new CompanyInforData(
                                main_business_income.getJSONObject(i).getString("name"),
                                main_business_income.getJSONObject(i).getString("product_value"),
                                main_business_income.getJSONObject(i).getString("product_proportion"), 8));
                    }
                    infoList.addAll(revenueList);
                }

                summaryAdapter = new CompanySummaryAdapter(getActivity(), infoList, stockCode, stockName);
                rvComSummary.setAdapter(summaryAdapter);
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

        if (data != null) {
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
        } else {
            chartBeanList = null;
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

    /**
     * 同业比较
     */
    private void getPeerComparison() {
        final Map<String, String> param = new HashMap<>();
        param.put("stock_code", stockCode);

        final GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                JSONObject object = JSONObject.parseObject(responseInfo);
                if (object.getIntValue("code") == 0) {
                    peerData = object.getJSONObject("data");

                    setPeerListData(peerData, PeerComparisonAdaprer.TYPE_MARKET_CAP);
                }
            }

            @Override
            public void onFailure(String msg) {
            }
        };
        new GGOKHTTP(param, GGOKHTTP.GET_PEERCOMPARISON_INFO, ggHttpInterface).startGet();
    }

    /**
     * 设置同业列表数据
     */
    private void setPeerListData(JSONObject data, int listType) {
        peerLists.clear();
        JSONArray jsonArray = null;
        String value = null;
        if (listType == PeerComparisonAdaprer.TYPE_MARKET_CAP) {
            jsonArray = data.getJSONArray("totalValueData");
            value = StringUtils.save2Significand(jsonArray.getJSONObject(jsonArray.size() - 1).getString("top1"));
        } else if (listType == PeerComparisonAdaprer.TYPE_GROSS_INCOME) {
            jsonArray = data.getJSONArray("grossRevenueData");
            value = StringUtils.save2Significand(jsonArray.getJSONObject(jsonArray.size() - 1).getDoubleValue("top1") / 10000);
        } else {
            jsonArray = data.getJSONArray("retainedProfitsData");
            value = StringUtils.save2Significand(jsonArray.getJSONObject(jsonArray.size() - 1).getDoubleValue("top1") / 10000);
        }

        if (listType == PeerComparisonAdaprer.TYPE_MARKET_CAP) {
            for (int i = 0; i < jsonArray.size() - 1; i++) {
                peerLists.add(new PeerCompariData(jsonArray.getJSONObject(i).getString("order"),
                        jsonArray.getJSONObject(i).getString("sname"),
                        StringUtils.save2Significand(jsonArray.getJSONObject(i).getString("tcap"))));
            }

            peerLists.add(1, new PeerCompariData(null, "最高值", value));
            peerLists.add(2, new PeerCompariData(null, "中位值", StringUtils.save2Significand(
                    jsonArray.getJSONObject(jsonArray.size() - 1).getString("top6"))));

        } else if (listType == PeerComparisonAdaprer.TYPE_GROSS_INCOME) {
            for (int i = 0; i < jsonArray.size() - 1; i++) {
                peerLists.add(new PeerCompariData(jsonArray.getJSONObject(i).getString("order"),
                        jsonArray.getJSONObject(i).getString("sname"), StringUtils.save2Significand(
                        jsonArray.getJSONObject(i).getDoubleValue("ffs2_01") / 10000)));
            }

            peerLists.add(1, new PeerCompariData(null, "最高值", value));
            peerLists.add(2, new PeerCompariData(null, "中位值", StringUtils.save2Significand(
                    jsonArray.getJSONObject(jsonArray.size() - 1).getDoubleValue("top6") / 10000)));

        } else {
            for (int i = 0; i < jsonArray.size() - 1; i++) {
                peerLists.add(new PeerCompariData(jsonArray.getJSONObject(i).getString("order"),
                        jsonArray.getJSONObject(i).getString("sname"), StringUtils.save2Significand(
                        jsonArray.getJSONObject(i).getDoubleValue("ffs2_40") / 10000)));
            }

            peerLists.add(1, new PeerCompariData(null, "最高值", value));
            peerLists.add(2, new PeerCompariData(null, "中位值", StringUtils.save2Significand(
                    jsonArray.getJSONObject(jsonArray.size() - 1).getDoubleValue("top6") / 10000)));

        }

        peerAdapter = new PeerComparisonAdaprer(getActivity(), peerLists, value, screenWidth, listType);
        rvPeer.setAdapter(peerAdapter);
    }

    @OnClick({R.id.chart_tab_one, R.id.chart_tab_two, R.id.chart_tab_three, R.id.relative_share_holder,
            R.id.relative_circula_holder, R.id.relative_institu_investor, R.id.chart_peer_one,
            R.id.chart_peer_two, R.id.chart_peer_three})
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
            case R.id.chart_peer_one:
                chart_peer_one.setEnabled(false);
                chart_peer_two.setEnabled(true);
                chart_peer_three.setEnabled(true);
                chart_peer_one.setTextColor(getResColor(R.color.white));
                chart_peer_one.setBackgroundResource(R.drawable.chart_tab_shape_select_left);
                chart_peer_two.setTextColor(getResColor(R.color.colorPrimary));
                chart_peer_two.setBackgroundResource(R.drawable.chart_tab_center_shape);
                chart_peer_three.setTextColor(getResColor(R.color.colorPrimary));
                chart_peer_three.setBackgroundResource(0);

                setPeerListData(peerData, PeerComparisonAdaprer.TYPE_MARKET_CAP);
                break;
            case R.id.chart_peer_two:
                chart_peer_one.setEnabled(true);
                chart_peer_two.setEnabled(false);
                chart_peer_three.setEnabled(true);
                chart_peer_one.setTextColor(getResColor(R.color.colorPrimary));
                chart_peer_one.setBackgroundResource(0);
                chart_peer_two.setTextColor(getResColor(R.color.white));
                chart_peer_two.setBackgroundResource(R.drawable.chart_tab_shape_select_center);
                chart_peer_three.setTextColor(getResColor(R.color.colorPrimary));
                chart_peer_three.setBackgroundResource(0);

                setPeerListData(peerData, PeerComparisonAdaprer.TYPE_GROSS_INCOME);
                break;
            case R.id.chart_peer_three:
                chart_peer_one.setEnabled(true);
                chart_peer_two.setEnabled(true);
                chart_peer_three.setEnabled(false);
                chart_peer_one.setTextColor(getResColor(R.color.colorPrimary));
                chart_peer_one.setBackgroundResource(0);
                chart_peer_two.setTextColor(getResColor(R.color.colorPrimary));
                chart_peer_two.setBackgroundResource(R.drawable.chart_tab_center_shape);
                chart_peer_three.setTextColor(getResColor(R.color.white));
                chart_peer_three.setBackgroundResource(R.drawable.chart_tab_shape_select_right);

                setPeerListData(peerData, PeerComparisonAdaprer.TYPE_NET_PROFIT);
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
