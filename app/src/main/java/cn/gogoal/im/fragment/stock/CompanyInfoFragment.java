package cn.gogoal.im.fragment.stock;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hply.alilayout.VirtualLayoutManager;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.stockften.CompanySummaryAdapter;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.bean.f10.CompanyInforData;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.copy.FtenUtils;
import cn.gogoal.im.ui.view.FullyLinearLayoutManager;

/**
 * author wangjd on 2017/6/16 0016.
 * Staff_id 1375
 * phone 18930640263
 * description :股票公司资料（个股Tab）
 */
public class CompanyInfoFragment extends BaseFragment {

    @BindView(R.id.rv_company_summary)
    RecyclerView rvComSummary;

    private FullyLinearLayoutManager mLayoutManager;

    private String stockCode;
    private String stockName;

    private ArrayList<CompanyInforData> infoList = new ArrayList<>();
    private CompanySummaryAdapter adapter;

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

        BaseActivity.initRecycleView(rvComSummary, null);

        mLayoutManager = new FullyLinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(VirtualLayoutManager.VERTICAL);
        mLayoutManager.setAutoMeasureEnabled(true);
        rvComSummary.setLayoutManager(mLayoutManager);
        rvComSummary.setHasFixedSize(false);
        rvComSummary.setNestedScrollingEnabled(false);

        getCompanySummary();
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
                KLog.e(responseInfo);
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

                    adapter = new CompanySummaryAdapter(getActivity(), infoList, stockCode, stockName);
                    rvComSummary.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(String msg) {
            }
        };
        new GGOKHTTP(param, GGOKHTTP.GET_STOCK_MAIN_BUSINESS, ggHttpInterface).startGet();
    }
}
