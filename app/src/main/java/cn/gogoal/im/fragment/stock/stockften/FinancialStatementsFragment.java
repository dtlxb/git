package cn.gogoal.im.fragment.stock.stockften;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.AnalysisLeftAdapter;
import cn.gogoal.im.adapter.AnalysisRightAdapter;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.copy.FtenUtils;
import cn.gogoal.im.ui.copy.InnerListView;
import cn.gogoal.im.ui.view.MySyncHorizontalScrollView;

/**
 * Created by dave.
 * Date: 2017/6/7.
 * Desc: 财务报表
 */
public class FinancialStatementsFragment extends BaseFragment {

    @BindView(R.id.rightHorscrollView)
    MySyncHorizontalScrollView rightHorscrollView;

    @BindView(R.id.lsv_left)
    InnerListView lsvLeft;
    @BindView(R.id.lsv_right)
    InnerListView lsvRight;

    private String stockCode;
    private String stockName;

    private String stype;

    private AnalysisLeftAdapter leftAdapter;
    private AnalysisRightAdapter rightAdapter;

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
        } else if (stype.equals("2")) {
            stringList = FtenUtils.profitForm2;
        } else if (stype.equals("3")) {
            stringList = FtenUtils.profitForm3;
        } else if (stype.equals("4")) {
            stringList = FtenUtils.profitForm4;
        } else if (stype.equals("5")) {
            stringList = FtenUtils.profitForm5;
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
        param.put("stock_finance_type", stype);
        param.put("page", "1");

        final GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                //KLog.e(responseInfo);
                JSONObject object = JSONObject.parseObject(responseInfo);
                if (object.getIntValue("code") == 0) {
                    JSONObject data = object.getJSONObject("data");
                    setRightListData(data);
                }
            }

            @Override
            public void onFailure(String msg) {
                UIHelper.toast(getContext(), R.string.net_erro_hint);
            }
        };
        new GGOKHTTP(param, GGOKHTTP.FIREPORT_PROFIT_DIST, ggHttpInterface).startGet();
    }

    /**
     * 设置右边列表数据
     */
    private void setRightListData(JSONObject data) {
        ArrayList<JSONArray> contList = new ArrayList<>();
        contList.add(data.getJSONObject("title").getJSONArray("title"));

        String[] stringList = null;
        if (stype.equals("1")) {
            stringList = FtenUtils.profitForm1_1;
        } else if (stype.equals("2")) {
            stringList = FtenUtils.profitForm2_1;
        } else if (stype.equals("3")) {
            stringList = FtenUtils.profitForm3_1;
        } else if (stype.equals("4")) {
            stringList = FtenUtils.profitForm4_1;
        } else if (stype.equals("5")) {
            stringList = FtenUtils.profitForm5_1;
        }

        for (int i = 0; i < stringList.length; i++) {
            contList.add(data.getJSONObject(stringList[i]).getJSONArray("original_data"));
        }

        rightAdapter = new AnalysisRightAdapter(getContext(), contList);
        lsvRight.setAdapter(rightAdapter);
    }

    @OnClick({R.id.radioBtn0, R.id.radioBtn1, R.id.radioBtn2, R.id.radioBtn3, R.id.radioBtn4})
    public void ChartTabClick(View v) {
        switch (v.getId()) {
            case R.id.radioBtn0:
                getStatementsData("0", stype);
                break;
            case R.id.radioBtn1:
                getStatementsData("4", stype);
                break;
            case R.id.radioBtn2:
                getStatementsData("2", stype);
                break;
            case R.id.radioBtn3:
                getStatementsData("1", stype);
                break;
            case R.id.radioBtn4:
                getStatementsData("3", stype);
                break;
        }
    }
}
