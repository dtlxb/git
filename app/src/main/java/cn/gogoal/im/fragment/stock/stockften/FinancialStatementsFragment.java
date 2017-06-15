package cn.gogoal.im.fragment.stock.stockften;

import android.content.Context;
import android.os.Bundle;

import com.alibaba.fastjson.JSONObject;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.AnalysisLeftAdapter;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.copy.FtenUtils;
import cn.gogoal.im.ui.copy.InnerListView;

/**
 * Created by dave.
 * Date: 2017/6/7.
 * Desc: 财务报表
 */
public class FinancialStatementsFragment extends BaseFragment {

    @BindView(R.id.lsv_left)
    InnerListView lsvLeft;
    @BindView(R.id.lsv_right)
    InnerListView lsvRight;

    private String stockCode;
    private String stockName;

    private String stype;

    private AnalysisLeftAdapter leftAdapter;

    public static FinancialStatementsFragment getInstance(String stockCode, String stockName) {
        FinancialStatementsFragment fragment = new FinancialStatementsFragment();
        Bundle b = new Bundle();
        b.putString("stockCode", stockCode);
        b.putString("stockName", stockName);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_financial_statements;
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

                    setLeftListData(stype);
                    getStatementsData("0", stype);
                }
            }

            @Override
            public void onFailure(String msg) {
            }
        };
        new GGOKHTTP(param, GGOKHTTP.GET_STOCK_FINANCIAL_TYPE, ggHttpInterface).startGet();
    }

    /**
     * 设置左边列表数据
     */
    private void setLeftListData(String stype) {
        ArrayList<String> titleList = new ArrayList<>();
        String[] stringList = null;
        if (stype.equals("1")) {
            stringList = FtenUtils.profitForm1;
        } else {
            stringList = FtenUtils.profitForm2;
        }

        titleList.add("利润分配表");
        for (int i = 0; i < stringList.length; i++) {
            titleList.add(stringList[i]);
        }

        leftAdapter = new AnalysisLeftAdapter(getActivity(), titleList);
        lsvLeft.setAdapter(leftAdapter);
    }

    private void getStatementsData(String season, final String stype) {

        final Map<String, String> param = new HashMap<>();
        param.put("stock_code", stockCode);
        param.put("report_stype", "1");
        param.put("season", season);
        param.put("stype", stype);
        param.put("page", "1");

        final GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                KLog.e(responseInfo);
                JSONObject object = JSONObject.parseObject(responseInfo);
                if (object.getIntValue("code") == 0) {
                    JSONObject data = object.getJSONObject("data");
                }
            }

            @Override
            public void onFailure(String msg) {
            }
        };
        new GGOKHTTP(param, GGOKHTTP.FIREPORT_PROFIT_DIST, ggHttpInterface).startGet();
    }
}
