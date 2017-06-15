package cn.gogoal.im.activity.stock;

import android.content.Context;
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
import cn.gogoal.im.adapter.AnalysisLeftAdapter;
import cn.gogoal.im.adapter.AnalysisRightAdapter;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.bean.ChartBean;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.copy.FtenUtils;
import cn.gogoal.im.ui.copy.InnerListView;
import cn.gogoal.im.ui.view.BarView;

/**
 * Created by dave.
 * Date: 2017/6/7.
 * Desc: 财务分析详情
 */
public class FinancialAnalysisActivity extends BaseActivity {

    @BindView(R.id.textFormName)
    TextView textFormName;
    @BindView(R.id.chart_tab_one)
    TextView chart_tab_one;
    @BindView(R.id.chart_tab_two)
    TextView chart_tab_two;
    @BindView(R.id.barAnalysisView)
    BarView barAnalysisView;

    @BindView(R.id.lsv_left)
    InnerListView lsvLeft;
    @BindView(R.id.lsv_right)
    InnerListView lsvRight;

    private String stockCode;
    private String stockName;
    private String stype;
    private String type;

    private JSONObject data;
    private int chartTab = 0;

    List<ChartBean> chartBeanList;

    private AnalysisLeftAdapter leftAdapter;
    private AnalysisRightAdapter rightAdapter;

    @Override
    public int bindLayout() {
        return R.layout.activity_financial_analysis;
    }

    @Override
    public void doBusiness(Context mContext) {

        stockCode = getIntent().getStringExtra("stockCode");
        stockName = getIntent().getStringExtra("stockName");
        stype = getIntent().getStringExtra("stype");
        type = getIntent().getStringExtra("type");
        chartBeanList = new ArrayList<>();

        setMyTitle(stockName + "-资料F10", true);

        if (stype.equals("1")) {
            textFormName.setText(FtenUtils.title1[Integer.parseInt(type) - 1]);
        } else {
            textFormName.setText(FtenUtils.title2[Integer.parseInt(type) - 1]);
        }

        setLeftListData();

        getFinacialData("0");
    }

    private void getFinacialData(String season) {

        final Map<String, String> param = new HashMap<>();
        param.put("stock_code", stockCode);
        param.put("stock_finance_type", stype);
        param.put("season", season);
        param.put("type", type);
        param.put("page", "1");

        final GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                KLog.e(responseInfo);
                JSONObject object = JSONObject.parseObject(responseInfo);
                if (object.getIntValue("code") == 0) {
                    data = object.getJSONObject("data");
                    setBarViewData(data, chartTab);
                    setRightListData(data);
                }
            }

            @Override
            public void onFailure(String msg) {
                UIHelper.toast(getContext(), R.string.net_erro_hint);
            }
        };
        new GGOKHTTP(param, GGOKHTTP.FINANCIAL_ANALYSIS, ggHttpInterface).startGet();
    }

    @OnClick({R.id.chart_tab_one, R.id.chart_tab_two, R.id.radioBtn0, R.id.radioBtn1,
            R.id.radioBtn2, R.id.radioBtn3, R.id.radioBtn4})
    public void ChartTabClick(View v) {
        switch (v.getId()) {
            case R.id.chart_tab_one:
                chart_tab_one.setEnabled(false);
                chart_tab_two.setEnabled(true);
                chart_tab_one.setBackgroundResource(R.drawable.chart_tab_shape_select_left);
                chart_tab_one.setTextColor(getResColor(R.color.white));
                chart_tab_two.setTextColor(getResColor(R.color.colorPrimary));
                chart_tab_two.setBackgroundResource(0);

                if (stype.equals("1")) {
                    textFormName.setText(FtenUtils.title1[Integer.parseInt(type) - 1]);
                } else {
                    textFormName.setText(FtenUtils.title2[Integer.parseInt(type) - 1]);
                }

                chartTab = 0;
                setBarViewData(data, chartTab);
                break;
            case R.id.chart_tab_two:
                chart_tab_one.setEnabled(true);
                chart_tab_two.setEnabled(false);
                chart_tab_two.setBackgroundResource(R.drawable.chart_tab_shape_select_right);
                chart_tab_two.setTextColor(getResColor(R.color.white));
                chart_tab_one.setTextColor(getResColor(R.color.colorPrimary));
                chart_tab_one.setBackgroundResource(0);

                if (stype.equals("1")) {
                    textFormName.setText(FtenUtils.title1_1[Integer.parseInt(type) - 1]);
                } else {
                    textFormName.setText(FtenUtils.title2_1[Integer.parseInt(type) - 1]);
                }

                chartTab = 1;
                setBarViewData(data, chartTab);
                break;
            case R.id.radioBtn0:
                getFinacialData("0");
                break;
            case R.id.radioBtn1:
                getFinacialData("4");
                break;
            case R.id.radioBtn2:
                getFinacialData("2");
                break;
            case R.id.radioBtn3:
                getFinacialData("1");
                break;
            case R.id.radioBtn4:
                getFinacialData("3");
                break;
        }
    }

    /**
     * 设置柱状图数据
     */
    private void setBarViewData(JSONObject data, int chartTab) {
        JSONArray retained_profits = null;
        String profits = null;
        if (chartTab == 0) {
            if (stype.equals("1")) {
                profits = FtenUtils.cotent1[Integer.parseInt(type) - 1];
            } else {
                profits = FtenUtils.cotent2[Integer.parseInt(type) - 1];
            }
        } else {
            if (stype.equals("1")) {
                profits = FtenUtils.cotent1_1[Integer.parseInt(type) - 1];
            } else {
                profits = FtenUtils.cotent2_1[Integer.parseInt(type) - 1];
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

        chartBeanList.clear();
        for (int i = 0; i < title.size(); i++) {
            chartBeanList.add(new ChartBean(values.get(i), dates.get(i)));
        }
        barAnalysisView.setTextSize(AppDevice.dp2px(FinancialAnalysisActivity.this, 10));
        barAnalysisView.setChartData(chartBeanList);
    }

    /**
     * 设置左边列表数据
     */
    private void setLeftListData() {
        ArrayList<String> titleList = new ArrayList<>();
        titleList.add(stockName);
        String[] stringList = null;
        if (stype.equals("1")) {
            switch (type) {
                case "1":
                    stringList = FtenUtils.profit1;
                    break;
                case "2":
                    stringList = FtenUtils.share_eps1;
                    break;
                case "3":
                    stringList = FtenUtils.ProfitRate1;
                    break;
                case "4":
                    stringList = FtenUtils.debtratio1;
                    break;
                case "5":
                    stringList = FtenUtils.sovency1;
                    break;
                case "6":
                    stringList = FtenUtils.turnoverrate1;
                    break;
                case "7":
                    stringList = FtenUtils.grows1;
                    break;
            }
        } else {
            switch (type) {
                case "1":
                    stringList = FtenUtils.profit2;
                    break;
                case "2":
                    stringList = FtenUtils.share_eps2;
                    break;
                case "3":
                    stringList = FtenUtils.ProfitRate2;
                    break;
                case "4":
                    stringList = FtenUtils.debtratio2;
                    break;
                case "5":
                    stringList = FtenUtils.sovency2;
                    break;
                case "6":
                    stringList = FtenUtils.turnoverrate2;
                    break;
                case "7":
                    stringList = FtenUtils.grows2;
                    break;
            }
        }

        for (int i = 0; i < stringList.length; i++) {
            titleList.add(stringList[i]);
        }

        leftAdapter = new AnalysisLeftAdapter(getActivity(), titleList);
        lsvLeft.setAdapter(leftAdapter);
    }

    /**
     * 设置右边列表数据
     */
    private void setRightListData(JSONObject data) {
        ArrayList<JSONArray> contList = new ArrayList<>();
        contList.add(data.getJSONArray("title"));

        String[] stringList = null;
        if (stype.equals("1")) {
            switch (type) {
                case "1":
                    stringList = FtenUtils.profit1_1;
                    break;
                case "2":
                    stringList = FtenUtils.share_eps1_1;
                    break;
                case "3":
                    stringList = FtenUtils.ProfitRate1_1;
                    break;
                case "4":
                    stringList = FtenUtils.debtratio1_1;
                    break;
                case "5":
                    stringList = FtenUtils.sovency1_1;
                    break;
                case "6":
                    stringList = FtenUtils.turnoverrate1_1;
                    break;
                case "7":
                    stringList = FtenUtils.grows1_1;
                    break;
            }
        } else {
            switch (type) {
                case "1":
                    stringList = FtenUtils.profit2_1;
                    break;
                case "2":
                    stringList = FtenUtils.share_eps2_1;
                    break;
                case "3":
                    stringList = FtenUtils.ProfitRate2_1;
                    break;
                case "4":
                    stringList = FtenUtils.debtratio2_1;
                    break;
                case "5":
                    stringList = FtenUtils.sovency2_1;
                    break;
                case "6":
                    stringList = FtenUtils.turnoverrate2_1;
                    break;
                case "7":
                    stringList = FtenUtils.grows2;
                    break;
            }
        }

        for (int i = 0; i < stringList.length; i++) {
            contList.add(data.getJSONArray(stringList[i]));
        }

        rightAdapter = new AnalysisRightAdapter(getContext(), contList);
        lsvRight.setAdapter(rightAdapter);
    }

    private FinancialAnalysisActivity getContext() {
        return FinancialAnalysisActivity.this;
    }
}
