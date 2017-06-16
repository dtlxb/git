package cn.gogoal.im.fragment.stock.stockften;

import android.content.Context;
import android.os.Bundle;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.AnalysisLeftAdapter;
import cn.gogoal.im.adapter.HolderResearchAdapter;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.bean.stock.HolderResearchData;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.ui.copy.InnerListView;

/**
 * Created by dave.
 * Date: 2017/6/7.
 * Desc: 股东研究
 */
public class ShareholderResearchFragment extends BaseFragment {

    @BindView(R.id.lsv_left)
    InnerListView lsvLeft;
    @BindView(R.id.lsv_right)
    InnerListView lsvRight;

    private String stockCode;
    private String stockName;

    private AnalysisLeftAdapter leftAdapter;
    private HolderResearchAdapter rightAdapter;

    public static ShareholderResearchFragment getInstance(String stockCode, String stockName) {
        ShareholderResearchFragment fragment = new ShareholderResearchFragment();
        Bundle b = new Bundle();
        b.putString("stockCode", stockCode);
        b.putString("stockName", stockName);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_shareholder_research;
    }

    @Override
    public void doBusiness(Context mContext) {

        stockCode = getArguments().getString("stockCode");
        stockName = getArguments().getString("stockName");

        getHolderData();
    }

    private void getHolderData() {

        final Map<String, String> param = new HashMap<>();
        param.put("stock_code", stockCode);
        //param.put("report_date", "2017-03-31");

        final GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                //KLog.e(responseInfo);
                JSONObject object = JSONObject.parseObject(responseInfo);
                if (object.getIntValue("code") == 0) {
                    JSONObject data = object.getJSONObject("data");
                    setLeftListData(data.getJSONArray("data"));
                    setRightListData(data.getJSONArray("data"));
                }
            }

            @Override
            public void onFailure(String msg) {
                UIHelper.toast(getContext(), R.string.net_erro_hint);
            }
        };
        new GGOKHTTP(param, GGOKHTTP.TEN_STOCK_HOLDERS, ggHttpInterface).startGet();
    }

    /**
     * 设置左边列表数据
     */
    private void setLeftListData(JSONArray data) {
        ArrayList<String> titleList = new ArrayList<>();

        titleList.add("十大股东");
        for (int i = 0; i < data.size(); i++) {
            titleList.add(data.getJSONObject(i).getString("stock_holder_name"));
        }

        leftAdapter = new AnalysisLeftAdapter(getActivity(), titleList);
        lsvLeft.setAdapter(leftAdapter);
    }

    /**
     * 设置右边列表数据
     */
    private void setRightListData(JSONArray data) {
        ArrayList<HolderResearchData> contList = new ArrayList<>();
        contList.add(new HolderResearchData("持股数(万股)", "占总股本持股比例"));

        for (int i = 0; i < data.size(); i++) {
            contList.add(new HolderResearchData(data.getJSONObject(i).getString("stock_holding_quantity"),
                    data.getJSONObject(i).getString("stock_holder_ratio")));
        }

        rightAdapter = new HolderResearchAdapter(getContext(), contList);
        lsvRight.setAdapter(rightAdapter);
    }
}
