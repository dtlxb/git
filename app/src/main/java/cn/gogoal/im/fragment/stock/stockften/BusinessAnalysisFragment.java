package cn.gogoal.im.fragment.stock.stockften;

import android.content.Context;
import android.os.Bundle;

import com.alibaba.fastjson.JSONObject;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.UIHelper;

/**
 * Created by dave.
 * Date: 2017/6/7.
 * Desc: 经营分析
 */
public class BusinessAnalysisFragment extends BaseFragment {

    private String stockCode;
    private String stockName;

    public static BusinessAnalysisFragment getInstance(String stockCode, String stockName) {
        BusinessAnalysisFragment fragment = new BusinessAnalysisFragment();
        Bundle b = new Bundle();
        b.putString("stockCode", stockCode);
        b.putString("stockName", stockName);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_business_analysis;
    }

    @Override
    public void doBusiness(Context mContext) {

        stockCode = getArguments().getString("stockCode");
        stockName = getArguments().getString("stockName");

        getAnalysisData();
    }

    private void getAnalysisData() {

        Map<String, String> param = new HashMap<>();
        param.put("stock_code", stockCode);

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                KLog.e(responseInfo);
                JSONObject object = JSONObject.parseObject(responseInfo);

                if (object.getIntValue("code") == 0) {
                    JSONObject data = object.getJSONObject("data");
                    Map<String, JSONObject> map = JSONObject.parseObject(String.valueOf(data), Map.class);
                    map.remove("main");
                    KLog.e(map);
                    Set<String> entries = map.keySet();
                    List<String> mapKey = new ArrayList<>();
                    for (String entry : entries) {
                        mapKey.add(entry);
                    }
                    KLog.e(mapKey);
                }
            }

            @Override
            public void onFailure(String msg) {
                UIHelper.toast(getContext(), R.string.net_erro_hint);
            }
        };
        new GGOKHTTP(param, GGOKHTTP.COMPANY_BUSINESS_ANALYSIS, ggHttpInterface).startGet();
    }
}
