package cn.gogoal.im.activity.stock.stockften;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.stockften.AnalysisLeftAdapter;
import cn.gogoal.im.adapter.stockften.CapitalStructureAdapter;
import cn.gogoal.im.adapter.stockften.EquityRightAdapter;
import cn.gogoal.im.adapter.stockften.OverYearEquityLeftAdapter;
import cn.gogoal.im.adapter.stockften.OverYearEquityRightAdapter;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.bean.ChartBean;
import cn.gogoal.im.bean.f10.EquityRightData;
import cn.gogoal.im.bean.f10.ProfileData;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.copy.FtenUtils;
import cn.gogoal.im.ui.copy.InnerListView;
import cn.gogoal.im.ui.view.MultiBarView;
import cn.gogoal.im.ui.view.MySyncHorizontalScrollView;

/**
 * Created by dave.
 * Date: 2017/6/7.
 * Desc: 股本结构
 */
public class CapitalStructureActivity extends BaseActivity {

    @BindView(R.id.rv_equity)
    RecyclerView rvEquity;
    //股本变动明细
    @BindView(R.id.equity_change_left)
    InnerListView equityChangeLeft;
    @BindView(R.id.equity_change_horscroll)
    MySyncHorizontalScrollView equityChangeHorscroll;
    @BindView(R.id.equity_change_right)
    InnerListView equityChangeRight;
    //历年股本变动
    @BindView(R.id.multiBarView)
    MultiBarView multiBarView;
    @BindView(R.id.over_year_equity_change_left)
    InnerListView overYearEquityChangeLeft;
    @BindView(R.id.over_year_equity_change_horscroll)
    MySyncHorizontalScrollView overYearEquityChangeHorscroll;
    @BindView(R.id.over_year_equity_change_right)
    InnerListView overYearEquityChangeRight;

    private String stockCode;
    private String stockName;

    private ArrayList<ProfileData> capitalData = new ArrayList<>();
    private CapitalStructureAdapter capitalAdapter;
    //股本变动明细
    private AnalysisLeftAdapter equityLeftAdapter;
    private EquityRightAdapter equityRightAdapter;
    //历年股本变动
    private OverYearEquityLeftAdapter overYearEquityLeftAdapter;
    private OverYearEquityRightAdapter overYearEquityRightAdapter;

    @Override
    public int bindLayout() {
        return R.layout.activity_capital_structure;
    }

    @Override
    public void doBusiness(Context mContext) {

        stockCode = getIntent().getStringExtra("stockCode");
        stockName = getIntent().getStringExtra("stockName");

        setMyTitle(stockName + "-股本", true);

        BaseActivity.initRecycleView(rvEquity, null);
        rvEquity.setHasFixedSize(true);
        rvEquity.setNestedScrollingEnabled(false);

        getRestrictSale();

        getEquityChange();

        getOverYearEquityChange();
    }

    /**
     * 限售解禁
     */
    private void getRestrictSale() {
        final Map<String, String> param = new HashMap<>();
        param.put("stock_code", stockCode);

        final GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                JSONObject object = JSONObject.parseObject(responseInfo);
                if (object.getIntValue("code") == 0) {
                    JSONObject data = object.getJSONArray("data").getJSONObject(0);
                    ArrayList<ProfileData> saleData = new ArrayList<>();
                    saleData.add(new ProfileData("限售解禁", null));
                    for (int i = 0; i < FtenUtils.RestrictSale1.length; i++) {
                        saleData.add(new ProfileData(FtenUtils.RestrictSale1[i],
                                data.getString(FtenUtils.RestrictSale1_1[i])));
                    }

                    capitalData.addAll(saleData);

                    getCapitalStructure();
                }
            }

            @Override
            public void onFailure(String msg) {
                UIHelper.toast(CapitalStructureActivity.this, R.string.net_erro_hint);
            }
        };
        new GGOKHTTP(param, GGOKHTTP.RESTRICT_SALE, ggHttpInterface).startGet();
    }

    /**
     * 股本结构
     */
    private void getCapitalStructure() {
        final Map<String, String> param = new HashMap<>();
        param.put("stock_code", stockCode);

        final GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                JSONObject object = JSONObject.parseObject(responseInfo);
                if (object.getIntValue("code") == 0) {
                    JSONObject data = object.getJSONObject("data");
                    ArrayList<ProfileData> strucData = new ArrayList<>();
                    strucData.add(new ProfileData("股本结构", null));
                    for (int i = 0; i < FtenUtils.capital1.length; i++) {
                        strucData.add(new ProfileData(FtenUtils.capital1[i],
                                data.getString(FtenUtils.capital1_1[i])));
                    }

                    capitalData.addAll(strucData);

                    capitalAdapter = new CapitalStructureAdapter(CapitalStructureActivity.this, capitalData);
                    rvEquity.setAdapter(capitalAdapter);
                }
            }

            @Override
            public void onFailure(String msg) {
            }
        };
        new GGOKHTTP(param, GGOKHTTP.STOCK_STRUCTURE, ggHttpInterface).startGet();
    }

    /**
     * 股本变动明细
     */
    private void getEquityChange() {
        final Map<String, String> param = new HashMap<>();
        param.put("stock_code", stockCode);

        final GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                JSONObject object = JSONObject.parseObject(responseInfo);
                if (object.getIntValue("code") == 0) {
                    JSONArray data = object.getJSONArray("data");

                    //设置左边数据
                    ArrayList<String> leftList = new ArrayList<>();
                    leftList.add("日期");
                    for (int i = 0; i < data.size(); i++) {
                        leftList.add(data.getJSONObject(i).getString("date"));
                    }
                    equityLeftAdapter = new AnalysisLeftAdapter(CapitalStructureActivity.this, leftList);
                    equityChangeLeft.setAdapter(equityLeftAdapter);

                    //设置右边数据
                    ArrayList<EquityRightData> rightList = new ArrayList<>();
                    rightList.add(new EquityRightData("总股本(万股)", "流通股本(万股)", "流通A股(万股)", "变动原因"));
                    for (int i = 0; i < data.size(); i++) {
                        rightList.add(new EquityRightData(data.getJSONObject(i).getString("stock_total"),
                                data.getJSONObject(i).getString("tradable_stock"),
                                data.getJSONObject(i).getString("tradable_A_stock"),
                                data.getJSONObject(i).getString("change_reason")));
                    }
                    equityRightAdapter = new EquityRightAdapter(CapitalStructureActivity.this, rightList);
                    equityChangeRight.setAdapter(equityRightAdapter);
                }
            }

            @Override
            public void onFailure(String msg) {
                UIHelper.toast(CapitalStructureActivity.this, R.string.net_erro_hint);
            }
        };
        new GGOKHTTP(param, GGOKHTTP.STOCK_CHANGE_DETAIL, ggHttpInterface).startGet();
    }

    /**
     * 历年股本变动
     */
    private void getOverYearEquityChange() {
        final Map<String, String> param = new HashMap<>();
        param.put("stock_code", stockCode);

        final GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                JSONObject object = JSONObject.parseObject(responseInfo);
                if (object.getIntValue("code") == 0) {
                    JSONArray data = object.getJSONArray("data");
                    initMultiBarView(data);
                }
            }

            @Override
            public void onFailure(String msg) {
                UIHelper.toast(CapitalStructureActivity.this, R.string.net_erro_hint);
            }
        };
        new GGOKHTTP(param, GGOKHTTP.OVER_YEAR_STOCK_CHANGE, ggHttpInterface).startGet();
    }

    /**
     * 设置柱状图数据、列表数据
     */
    private void initMultiBarView(JSONArray data) {
        //柱状图数据
        List<ChartBean> chartBeanList = new ArrayList<>();
        for (int i = 0; i < (data.size() <= 5 ? data.size() : 5); i++) {
            chartBeanList.add(new ChartBean(data.getJSONObject(i).getFloatValue("stock_total"),
                    data.getJSONObject(i).getFloatValue("tradable_A_stock"),
                    data.getJSONObject(i).getFloatValue("tradable_limit_stock"),
                    data.getJSONObject(i).getString("date")));
        }
        multiBarView.setMarginBottom(AppDevice.dp2px(getActivity(), 30));
        multiBarView.setMarginLeft(AppDevice.dp2px(getActivity(), 60));
        multiBarView.setMarginRight(AppDevice.dp2px(getActivity(), 10));
        multiBarView.setTextSize(AppDevice.dp2px(getActivity(), 10));
        multiBarView.setChartData(chartBeanList);
        //设置左边数据
        ArrayList<String> leftList = new ArrayList<>();
        for (int i = 0; i < FtenUtils.EquityChange1.length; i++) {
            leftList.add(FtenUtils.EquityChange1[i]);
        }
        overYearEquityLeftAdapter = new OverYearEquityLeftAdapter(CapitalStructureActivity.this, leftList);
        overYearEquityChangeLeft.setAdapter(overYearEquityLeftAdapter);

        //设置右边数据
        ArrayList<JSONArray> rightList = new ArrayList<>();
        for (int i = 0; i < FtenUtils.EquityChange1.length; i++) {
            JSONArray jsonArray = new JSONArray();
            for (int j = 0; j < (data.size() <= 5 ? data.size() : 5); j++) {
                jsonArray.add(data.getJSONObject(j).getString(FtenUtils.EquityChange1_1[i]));
            }
            rightList.add(jsonArray);
        }
        overYearEquityRightAdapter = new OverYearEquityRightAdapter(CapitalStructureActivity.this, rightList);
        overYearEquityChangeRight.setAdapter(overYearEquityRightAdapter);
    }
}
