package cn.gogoal.im.activity.stock.stockften;

import android.content.Context;
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
import cn.gogoal.im.adapter.CurrencyTitleAdapter;
import cn.gogoal.im.adapter.FundHolderAdapter;
import cn.gogoal.im.adapter.TenHolderAdapter;
import cn.gogoal.im.adapter.TenTradableHolderAdapter;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.bean.FundHolderData;
import cn.gogoal.im.bean.TenHolderData;
import cn.gogoal.im.bean.TenTradableHolderData;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.UIHelper;

/**
 * Created by dave.
 * Date: 2017/6/7.
 * Desc: 股东研究
 */
public class ShareholderResearchActivity extends BaseActivity {

    @BindView(R.id.rv_research)
    RecyclerView rv_research;

    private String stockCode;
    private String stockName;

    //缓存池
    private RecyclerView.RecycledViewPool viewPool;
    private DelegateAdapter delegateAdapter;
    private LinkedList<DelegateAdapter.Adapter> adapters;

    @Override
    public int bindLayout() {
        return R.layout.activity_shareholder_research;
    }

    @Override
    public void doBusiness(Context mContext) {

        stockCode = getIntent().getStringExtra("stockCode");
        stockName = getIntent().getStringExtra("stockName");

        setMyTitle(stockName + "-股东", true);

        final VirtualLayoutManager layoutManager = new VirtualLayoutManager(getActivity());
        rv_research.setLayoutManager(layoutManager);
        layoutManager.setAutoMeasureEnabled(true);

        viewPool = new RecyclerView.RecycledViewPool();
        rv_research.setRecycledViewPool(viewPool);
        viewPool.setMaxRecycledViews(0, 20);

        delegateAdapter = new DelegateAdapter(layoutManager, false);
        adapters = new LinkedList<>();

        rv_research.setAdapter(delegateAdapter);
        rv_research.setHasFixedSize(true);
        rv_research.setNestedScrollingEnabled(false);

        getTenHolderData();
    }

    /**
     * 十大股东
     */
    private void getTenHolderData() {
        final Map<String, String> param = new HashMap<>();
        param.put("stock_code", stockCode);
        //param.put("report_date", "2017-03-31");

        final GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                KLog.e(responseInfo);
                JSONObject object = JSONObject.parseObject(responseInfo);
                if (object.getIntValue("code") == 0) {
                    JSONArray data = object.getJSONObject("data").getJSONArray("data");
                    //悬浮头1
                    adapters.add(new CurrencyTitleAdapter(getActivity(), "十大股东"));

                    ArrayList<TenHolderData> HoldingList = new ArrayList<>();
                    for (int i = 0; i < data.size(); i++) {
                        HoldingList.add(new TenHolderData(
                                data.getJSONObject(i).getString("stock_holder_ratio"),
                                data.getJSONObject(i).getString("stock_holding_quantity"),
                                data.getJSONObject(i).getString("stock_holder_name")));
                    }
                    adapters.add(new TenHolderAdapter(getActivity(), HoldingList));

                    getTenTradableHolderData();
                }
            }

            @Override
            public void onFailure(String msg) {
                UIHelper.toast(ShareholderResearchActivity.this, R.string.net_erro_hint);
            }
        };
        new GGOKHTTP(param, GGOKHTTP.TEN_STOCK_HOLDERS, ggHttpInterface).startGet();
    }

    /**
     * 十大流通股东
     */
    private void getTenTradableHolderData() {
        final Map<String, String> param = new HashMap<>();
        param.put("stock_code", stockCode);
        //param.put("report_date", "2017-03-31");

        final GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                JSONObject object = JSONObject.parseObject(responseInfo);
                if (object.getIntValue("code") == 0) {
                    JSONArray data = object.getJSONObject("data").getJSONArray("data");
                    //悬浮头2
                    adapters.add(new CurrencyTitleAdapter(getActivity(), "十大流通股东"));

                    ArrayList<TenTradableHolderData> HolderList = new ArrayList<>();
                    for (int i = 0; i < data.size(); i++) {
                        HolderList.add(new TenTradableHolderData(
                                data.getJSONObject(i).getString("stock_holder_name"),
                                data.getJSONObject(i).getString("stock_holding_quantity"),
                                data.getJSONObject(i).getString("stock_holder_ratio")));
                    }
                    adapters.add(new TenTradableHolderAdapter(getActivity(), HolderList));

                    getFundHolderData();
                }
            }

            @Override
            public void onFailure(String msg) {
            }
        };
        new GGOKHTTP(param, GGOKHTTP.TEN_TRADABLE_STOCK_HOLDERS, ggHttpInterface).startGet();
    }


    /**
     * 基金持股
     */
    private void getFundHolderData() {
        final Map<String, String> param = new HashMap<>();
        param.put("stock_code", stockCode);
        //param.put("report_date", "2017-03-31");

        final GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                JSONObject object = JSONObject.parseObject(responseInfo);
                if (object.getIntValue("code") == 0) {
                    JSONArray data = object.getJSONObject("data").getJSONArray("data");
                    //悬浮头3
                    adapters.add(new CurrencyTitleAdapter(getActivity(), "基金持股"));

                    ArrayList<FundHolderData> fundList = new ArrayList<>();
                    fundList.add(new FundHolderData("基金名称", "基金代码", "持股(万股)"));
                    for (int i = data.size() - 1; i >= (data.size() > 20 ? data.size() - 20 : 0); i--) {
                        fundList.add(new FundHolderData(
                                data.getJSONObject(i).getString("fund_name"),
                                data.getJSONObject(i).getString("fund_code"),
                                data.getJSONObject(i).getString("stock_holding_quantity")));
                    }
                    adapters.add(new FundHolderAdapter(getActivity(), fundList));

                    delegateAdapter.setAdapters(adapters);
                }
            }

            @Override
            public void onFailure(String msg) {
            }
        };
        new GGOKHTTP(param, GGOKHTTP.FUND_HOLDINGS, ggHttpInterface).startGet();
    }
}
