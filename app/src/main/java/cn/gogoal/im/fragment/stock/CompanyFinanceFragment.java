package cn.gogoal.im.fragment.stock;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.stockften.StockFinanceAdapter;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.bean.f10.FinanceData;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.copy.FtenUtils;

/**
 * author wangjd on 2017/6/16 0016.
 * Staff_id 1375
 * phone 18930640263
 * description :公司财务（个股Tab）
 */
public class CompanyFinanceFragment extends BaseFragment {

    @BindView(R.id.rv_finance)
    RecyclerView rv_finance;

    private String stockCode;
    private String stockName;
    private String stype;

    private ArrayList<FinanceData> financeList = new ArrayList<>();
    private StockFinanceAdapter adapter;

    public static CompanyFinanceFragment getInstance(String stockCode, String stockName) {
        CompanyFinanceFragment fragment = new CompanyFinanceFragment();
        Bundle b = new Bundle();
        b.putString("stockCode", stockCode);
        b.putString("stockName", stockName);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_company_finance;
    }

    @Override
    public void doBusiness(Context mContext) {
        stockCode = getArguments().getString("stockCode");
        stockName = getArguments().getString("stockName");

        BaseActivity.initRecycleView(rv_finance, null);
        rv_finance.setNestedScrollingEnabled(false);

        getParamData();
    }

    private void getParamData() {

        final Map<String, String> param = new HashMap<>();
        param.put("stock_code", stockCode);

        final GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                //KLog.e(responseInfo);
                JSONObject object = JSONObject.parseObject(responseInfo);
                if (object.getIntValue("code") == 0) {
                    JSONObject data = object.getJSONObject("data");
                    stype = data.getString("stype");
                    getKeyIndexData(stype);
                }
            }

            @Override
            public void onFailure(String msg) {
                UIHelper.toast(getContext(), R.string.net_erro_hint);
            }
        };
        new GGOKHTTP(param, GGOKHTTP.GET_STOCK_FINANCIAL_TYPE, ggHttpInterface).startGet();
    }

    /**
     * 关键指标
     */
    private void getKeyIndexData(final String stype) {

        final Map<String, String> param = new HashMap<>();
        param.put("stock_code", stockCode);
        param.put("stock_finance_type", stype);
        param.put("season", "0");
        param.put("type", "1");
        param.put("page", "1");

        final GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                //KLog.e(responseInfo);
                JSONObject object = JSONObject.parseObject(responseInfo);
                if (object.getIntValue("code") == 0) {
                    JSONObject data = object.getJSONObject("data");
                    String title = data.getJSONArray("title").getString(0);

                    ArrayList<FinanceData> keyList = new ArrayList<>();
                    keyList.add(new FinanceData("关键指标", title, 1));

                    for (int i = 0; i < FtenUtils.keyIndex1.length; i++) {
                        JSONArray content = null;
                        if (stype.equals("1")) {
                            content = data.getJSONArray(FtenUtils.keyIndex1_1[i]);
                        } else {
                            content = data.getJSONArray(FtenUtils.keyIndex1_2[i]);
                        }

                        if (content.size() != 0) {
                            if (i == 1) {
                                keyList.add(new FinanceData(FtenUtils.keyIndex1[i], content.getString(0), 3));
                            } else if (i == 3) {
                                keyList.add(new FinanceData(FtenUtils.keyIndex1[i], content.getString(0), 4));
                            } else {
                                keyList.add(new FinanceData(FtenUtils.keyIndex1[i], content.getString(0), 2));
                            }
                        } else {
                            if (i == 1) {
                                keyList.add(new FinanceData(FtenUtils.keyIndex1[i], "--", 3));
                            } else if (i == 3) {
                                keyList.add(new FinanceData(FtenUtils.keyIndex1[i], "--", 4));
                            } else {
                                keyList.add(new FinanceData(FtenUtils.keyIndex1[i], "--", 2));
                            }
                        }
                    }

                    financeList.addAll(keyList);
                }

                getProfitStateData(stype);
            }

            @Override
            public void onFailure(String msg) {
            }
        };
        new GGOKHTTP(param, GGOKHTTP.FINANCIAL_ANALYSIS, ggHttpInterface).startGet();
    }

    /**
     * 利润表
     */
    private void getProfitStateData(final String stype) {

        final Map<String, String> param = new HashMap<>();
        param.put("stock_code", stockCode);
        param.put("report_stype", "1");
        param.put("season", "0");
        param.put("stock_finance_type", stype);
        param.put("page", "1");
        param.put("stype", "1");

        final GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                //KLog.e(responseInfo);
                JSONObject object = JSONObject.parseObject(responseInfo);
                if (object.getIntValue("code") == 0) {
                    JSONObject data = object.getJSONObject("data");
                    String title = data.getJSONObject("title").getJSONArray("title").getString(0);

                    String[] content = null;
                    if (stype.equals("1")) {
                        content = FtenUtils.profitState1_1;
                    } else if (stype.equals("2")) {
                        content = FtenUtils.profitState1_2;
                    } else if (stype.equals("3")) {
                        content = FtenUtils.profitState1_3;
                    } else if (stype.equals("4")) {
                        content = FtenUtils.profitState1_4;
                    } else if (stype.equals("5")) {
                        content = FtenUtils.profitState1_5;
                    }

                    ArrayList<FinanceData> profitList = new ArrayList<>();
                    profitList.add(new FinanceData("利润表", title, 1));

                    for (int i = 0; i < FtenUtils.profitState1.length; i++) {
                        JSONArray profit = data.getJSONObject(content[i]).getJSONArray("original_data");
                        if (profit.size() != 0) {
                            if (i == 1) {
                                profitList.add(new FinanceData(FtenUtils.profitState1[i], profit.getString(0), 6));
                            } else {
                                profitList.add(new FinanceData(FtenUtils.profitState1[i], profit.getString(0), 5));
                            }
                        } else {
                            if (i == 1) {
                                profitList.add(new FinanceData(FtenUtils.profitState1[i], "--", 6));
                            } else {
                                profitList.add(new FinanceData(FtenUtils.profitState1[i], "--", 5));
                            }
                        }
                    }

                    financeList.addAll(profitList);
                }

                getBalanceSheetData(stype);
            }

            @Override
            public void onFailure(String msg) {
            }
        };
        new GGOKHTTP(param, GGOKHTTP.FIREPORT_PROFIT_DIST, ggHttpInterface).startGet();
    }

    /**
     * 资产负债表
     */
    private void getBalanceSheetData(final String stype) {

        final Map<String, String> param = new HashMap<>();
        param.put("stock_code", stockCode);
        param.put("season", "0");
        param.put("stock_finance_type", stype);
        param.put("page", "1");
        param.put("stype", "1");

        final GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                //KLog.e(responseInfo);
                JSONObject object = JSONObject.parseObject(responseInfo);
                if (object.getIntValue("code") == 0) {
                    JSONObject data = object.getJSONObject("data");
                    String title = data.getJSONObject("title").getJSONArray("title").getString(0);

                    String[] content = null;
                    if (stype.equals("1")) {
                        content = FtenUtils.assetsLiabili1_1;
                    } else if (stype.equals("2")) {
                        content = FtenUtils.assetsLiabili1_2;
                    } else if (stype.equals("3")) {
                        content = FtenUtils.assetsLiabili1_3;
                    } else if (stype.equals("4")) {
                        content = FtenUtils.assetsLiabili1_4;
                    } else if (stype.equals("5")) {
                        content = FtenUtils.assetsLiabili1_5;
                    }

                    ArrayList<FinanceData> balanceList = new ArrayList<>();
                    balanceList.add(new FinanceData("资产负债表", title, 1));

                    for (int i = 0; i < FtenUtils.assetsLiabili1.length; i++) {
                        JSONArray balance = data.getJSONObject(content[i]).getJSONArray("original_data");
                        if (balance.size() != 0) {
                            if (i == 1) {
                                balanceList.add(new FinanceData(FtenUtils.assetsLiabili1[i], balance.getString(0), 8));
                            } else {
                                balanceList.add(new FinanceData(FtenUtils.assetsLiabili1[i], balance.getString(0), 7));
                            }
                        } else {
                            if (i == 1) {
                                balanceList.add(new FinanceData(FtenUtils.assetsLiabili1[i], "--", 8));
                            } else {
                                balanceList.add(new FinanceData(FtenUtils.assetsLiabili1[i], "--", 7));
                            }
                        }
                    }

                    financeList.addAll(balanceList);
                }

                getCashFlowData(stype);
            }

            @Override
            public void onFailure(String msg) {
            }
        };
        new GGOKHTTP(param, GGOKHTTP.FIREPORT_BALANCE_SHEET, ggHttpInterface).startGet();
    }

    /**
     * 现金流量表
     */
    private void getCashFlowData(final String stype) {

        final Map<String, String> param = new HashMap<>();
        param.put("stock_code", stockCode);
        param.put("report_stype", "1");
        param.put("season", "0");
        param.put("stock_finance_type", stype);
        param.put("page", "1");
        param.put("stype", "1");

        final GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                //KLog.e(responseInfo);
                JSONObject object = JSONObject.parseObject(responseInfo);
                if (object.getIntValue("code") == 0) {
                    JSONObject data = object.getJSONObject("data");
                    String title = data.getJSONObject("title").getJSONArray("title").getString(0);

                    String[] content = null;
                    if (stype.equals("1")) {
                        content = FtenUtils.cashFlow1_1;
                    } else if (stype.equals("2")) {
                        content = FtenUtils.cashFlow1_2;
                    } else if (stype.equals("3")) {
                        content = FtenUtils.cashFlow1_3;
                    } else if (stype.equals("4")) {
                        content = FtenUtils.cashFlow1_4;
                    } else if (stype.equals("5")) {
                        content = FtenUtils.cashFlow1_5;
                    }

                    ArrayList<FinanceData> cashList = new ArrayList<>();
                    cashList.add(new FinanceData("现金流量表", title, 1));

                    for (int i = 0; i < FtenUtils.cashFlow1.length; i++) {
                        JSONArray profit = data.getJSONObject(content[i]).getJSONArray("original_data");
                        if (profit.size() != 0) {
                            if (i == 1) {
                                cashList.add(new FinanceData(FtenUtils.cashFlow1[i], profit.getString(0), 10));
                            } else {
                                cashList.add(new FinanceData(FtenUtils.cashFlow1[i], profit.getString(0), 9));
                            }
                        } else {
                            if (i == 1) {
                                cashList.add(new FinanceData(FtenUtils.cashFlow1[i], "--", 10));
                            } else {
                                cashList.add(new FinanceData(FtenUtils.cashFlow1[i], "--", 9));
                            }
                        }
                    }

                    financeList.addAll(cashList);
                }

                adapter = new StockFinanceAdapter(getActivity(), financeList, stockCode,
                        stockName, stype);
                rv_finance.setAdapter(adapter);
            }

            @Override
            public void onFailure(String msg) {
            }
        };
        new GGOKHTTP(param, GGOKHTTP.FIREPORT_FLOW_CASH, ggHttpInterface).startGet();
    }
}
