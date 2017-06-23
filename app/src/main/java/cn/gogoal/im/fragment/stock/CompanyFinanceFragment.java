package cn.gogoal.im.fragment.stock;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hply.alilayout.DelegateAdapter;
import com.hply.alilayout.VirtualLayoutManager;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.CurrencyTitle2Adapter;
import cn.gogoal.im.adapter.KeyIndexAdapter;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.bean.ProfileData;
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

    //缓存池
    private RecyclerView.RecycledViewPool viewPool;
    private DelegateAdapter delegateAdapter;
    private LinkedList<DelegateAdapter.Adapter> adapters;

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

        final VirtualLayoutManager layoutManager = new VirtualLayoutManager(getActivity());
        rv_finance.setLayoutManager(layoutManager);
        layoutManager.setAutoMeasureEnabled(true);

        viewPool = new RecyclerView.RecycledViewPool();
        rv_finance.setRecycledViewPool(viewPool);
        viewPool.setMaxRecycledViews(0, 20);

        delegateAdapter = new DelegateAdapter(layoutManager, false);
        adapters = new LinkedList<>();

        rv_finance.setAdapter(delegateAdapter);
        rv_finance.setHasFixedSize(true);
        rv_finance.setNestedScrollingEnabled(false);

        getParamData();
    }

    private void getParamData() {

        final Map<String, String> param = new HashMap<>();
        param.put("stock_code", stockCode);

        final GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                KLog.e(responseInfo);
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
                KLog.e(responseInfo);
                JSONObject object = JSONObject.parseObject(responseInfo);
                if (object.getIntValue("code") == 0) {
                    JSONObject data = object.getJSONObject("data");
                    String title = data.getJSONArray("title").getString(0);
                    //悬浮头1
                    adapters.add(new CurrencyTitle2Adapter(getActivity(), new String[]{"关键指标", title}, false));

                    ArrayList<ProfileData> keyList = new ArrayList<>();
                    for (int i = 0; i < FtenUtils.keyIndex1.length; i++) {
                        JSONArray content = null;
                        if (stype.equals("1")) {
                            content = data.getJSONArray(FtenUtils.keyIndex1_1[i]);
                        } else {
                            content = data.getJSONArray(FtenUtils.keyIndex1_2[i]);
                        }

                        if (content.size() != 0) {
                            keyList.add(new ProfileData(FtenUtils.keyIndex1[i], content.getString(0)));
                        } else {
                            keyList.add(new ProfileData(FtenUtils.keyIndex1[i], "--"));
                        }
                    }

                    adapters.add(new KeyIndexAdapter(getActivity(), keyList, KeyIndexAdapter.GENRE_KEY_INDEX));

                    getProfitStateData(stype);
                }
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

        final GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                KLog.e(responseInfo);
                JSONObject object = JSONObject.parseObject(responseInfo);
                if (object.getIntValue("code") == 0) {
                    JSONObject data = object.getJSONObject("data");
                    String title = data.getJSONObject("title").getJSONArray("title").getString(0);

                    //悬浮头2
                    adapters.add(new CurrencyTitle2Adapter(getActivity(), new String[]{"利润表", title}, false));

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

                    ArrayList<ProfileData> profitList = new ArrayList<>();
                    for (int i = 0; i < FtenUtils.profitState1.length; i++) {
                        JSONArray profit = data.getJSONObject(content[i]).getJSONArray("original_data");
                        if (profit.size() != 0) {
                            profitList.add(new ProfileData(FtenUtils.profitState1[i], profit.getString(0)));
                        } else {
                            profitList.add(new ProfileData(FtenUtils.profitState1[i], "--"));
                        }
                    }

                    adapters.add(new KeyIndexAdapter(getActivity(), profitList, KeyIndexAdapter.GENRE_PROFIT_STATE));

                    getBalanceSheetData(stype);
                }
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

        final GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                KLog.e(responseInfo);
                JSONObject object = JSONObject.parseObject(responseInfo);
                if (object.getIntValue("code") == 0) {
                    JSONObject data = object.getJSONObject("data");
                    String title = data.getJSONObject("title").getJSONArray("title").getString(0);

                    //悬浮头3
                    adapters.add(new CurrencyTitle2Adapter(getActivity(), new String[]{"资产负债表", title}, false));

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

                    ArrayList<ProfileData> balanceList = new ArrayList<>();
                    for (int i = 0; i < FtenUtils.assetsLiabili1.length; i++) {
                        JSONArray balance = data.getJSONObject(content[i]).getJSONArray("original_data");
                        if (balance.size() != 0) {
                            balanceList.add(new ProfileData(FtenUtils.assetsLiabili1[i], balance.getString(0)));
                        } else {
                            balanceList.add(new ProfileData(FtenUtils.assetsLiabili1[i], "--"));
                        }
                    }

                    adapters.add(new KeyIndexAdapter(getActivity(), balanceList, KeyIndexAdapter.GENRE_BALANCE_SHEET));

                    delegateAdapter.setAdapters(adapters);
                }
            }

            @Override
            public void onFailure(String msg) {
            }
        };
        new GGOKHTTP(param, GGOKHTTP.FIREPORT_BALANCE_SHEET, ggHttpInterface).startGet();
    }
}
