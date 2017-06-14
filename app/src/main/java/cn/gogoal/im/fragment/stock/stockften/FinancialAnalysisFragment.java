package cn.gogoal.im.fragment.stock.stockften;

import android.content.Context;
import android.os.Bundle;

import com.alibaba.fastjson.JSONObject;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.ui.view.BarView;

/**
 * Created by dave.
 * Date: 2017/6/7.
 * Desc: 财务分析
 */
public class FinancialAnalysisFragment extends BaseFragment {

    @BindView(R.id.barView)
    BarView barView;

    private String stockCode;
    private String stockName;

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

        List<Float> values = new ArrayList<>();
        values.add(20.0f);
        values.add(-40.0f);
        values.add(60.0f);
        values.add(20.0f);
        values.add(-80.0f);
        List<String> dates = new ArrayList<>();
        dates.add("17Q1");
        dates.add("16Q4");
        dates.add("16Q3");
        dates.add("16Q2");
        dates.add("16Q1");
        Map<String, Object> map = new HashMap<>();
        map.put("dates", dates);
        map.put("values", values);
        //barView.setChartData(map);

        getFinacialData("0", "1");
    }

    private void getFinacialData(String season, String type) {

        Map<String, String> param = new HashMap<>();
        param.put("stock_code", stockCode);
        param.put("season", season);
        param.put("type", type);
        param.put("page", "1");

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                KLog.e(responseInfo);
                JSONObject object = JSONObject.parseObject(responseInfo);
                if (object.getIntValue("code") == 0) {

                }
            }

            @Override
            public void onFailure(String msg) {
                UIHelper.toast(getContext(), R.string.net_erro_hint);
            }
        };
        new GGOKHTTP(param, GGOKHTTP.FINANCIAL_ANALYSIS, ggHttpInterface).startGet();
    }
}
