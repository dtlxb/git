package cn.gogoal.im.activity.stock;

import android.content.Context;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.AnalysisLeftAdapter;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.bean.ChartBean;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.copy.FtenUtils;
import cn.gogoal.im.ui.copy.InnerListView;
import cn.gogoal.im.ui.view.BarView;
import cn.gogoal.im.ui.view.MySyncHorizontalScrollView;

/**
 * Created by dave.
 * Date: 2017/6/7.
 * Desc: 财务分析详情
 */
public class FinancialAnalysisActivity extends BaseActivity {

    @BindView(R.id.textFormName)
    TextView textFormName;
    @BindView(R.id.barAnalysisView)
    BarView barAnalysisView;

    @BindView(R.id.lsv_left)
    InnerListView lsvLeft;
    @BindView(R.id.lsv_right)
    InnerListView lsvRight;
    @BindView(R.id.rightHorscrollView)
    MySyncHorizontalScrollView rightHorscrollView;

    private String stockCode;
    private String stockName;
    private String stype;
    private String type;

    private AnalysisLeftAdapter leftAdapter;

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
                    JSONObject data = object.getJSONObject("data");
                    setBarViewData(data);
                }
            }

            @Override
            public void onFailure(String msg) {
                UIHelper.toast(getContext(), R.string.net_erro_hint);
            }
        };
        new GGOKHTTP(param, GGOKHTTP.FINANCIAL_ANALYSIS, ggHttpInterface).startGet();
    }

    /**
     * 设置柱状图数据
     */
    private void setBarViewData(JSONObject data) {
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

        List<ChartBean> chartBeanList = new ArrayList<>();
        for (int i = 0; i < title.size(); i++) {
            chartBeanList.add(new ChartBean(values.get(i), dates.get(i)));
        }

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

    private FinancialAnalysisActivity getContext() {
        return FinancialAnalysisActivity.this;
    }
}
