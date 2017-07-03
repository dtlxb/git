package cn.gogoal.im.activity.stock.stockften;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.stockften.CapitalStructureAdapter;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.bean.f10.ProfileData;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.copy.FtenUtils;

/**
 * Created by dave.
 * Date: 2017/6/7.
 * Desc: 股本结构
 */
public class CapitalStructureActivity extends BaseActivity {

    @BindView(R.id.rv_equity)
    RecyclerView rvEquity;

    private String stockCode;
    private String stockName;

    private ArrayList<ProfileData> capitalData = new ArrayList<>();
    private CapitalStructureAdapter capitalAdapter;

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
                KLog.e(responseInfo);
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
}
