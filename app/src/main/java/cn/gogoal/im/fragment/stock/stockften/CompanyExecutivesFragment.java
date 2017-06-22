package cn.gogoal.im.fragment.stock.stockften;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hply.alilayout.DelegateAdapter;
import com.hply.alilayout.VirtualLayoutManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.CurrencyTitleAdapter;
import cn.gogoal.im.adapter.ExecutivesListAdapter;
import cn.gogoal.im.adapter.HoldingChangeAdapter;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.bean.ExecutivesData;
import cn.gogoal.im.bean.HoldingData;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.UIHelper;

/**
 * Created by dave.
 * Date: 2017/6/7.
 * Desc: 公司高管
 */
public class CompanyExecutivesFragment extends BaseFragment {

    @BindView(R.id.rv_executives)
    RecyclerView rv_executives;

    private String stockCode;
    private String stockName;
    //缓存池
    private RecyclerView.RecycledViewPool viewPool;
    private DelegateAdapter delegateAdapter;
    private LinkedList<DelegateAdapter.Adapter> adapters;

    public static CompanyExecutivesFragment getInstance(String stockCode, String stockName) {
        CompanyExecutivesFragment fragment = new CompanyExecutivesFragment();
        Bundle b = new Bundle();
        b.putString("stockCode", stockCode);
        b.putString("stockName", stockName);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_company_executives;
    }

    @Override
    public void doBusiness(Context mContext) {

        stockCode = getArguments().getString("stockCode");
        stockName = getArguments().getString("stockName");

        final VirtualLayoutManager layoutManager = new VirtualLayoutManager(getActivity());
        rv_executives.setLayoutManager(layoutManager);
        layoutManager.setAutoMeasureEnabled(true);

        viewPool = new RecyclerView.RecycledViewPool();
        rv_executives.setRecycledViewPool(viewPool);
        viewPool.setMaxRecycledViews(0, 20);

        delegateAdapter = new DelegateAdapter(layoutManager, false);
        adapters = new LinkedList<>();

        rv_executives.setAdapter(delegateAdapter);
        rv_executives.setHasFixedSize(true);
        rv_executives.setNestedScrollingEnabled(false);

        getExecutivesData();
    }

    private void getExecutivesData() {

        final Map<String, String> param = new HashMap<>();
        param.put("stock_code", stockCode);
        //param.put("report_date", "2017-03-31");

        final GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                //FileUtil.writeRequestResponse(responseInfo, "F10DATA.TXT");
                JSONObject object = JSONObject.parseObject(responseInfo);
                if (object.getIntValue("code") == 0) {
                    JSONObject data = object.getJSONObject("data");
                    setListData(data);
                }
            }

            @Override
            public void onFailure(String msg) {
                UIHelper.toast(getContext(), R.string.net_erro_hint);
            }
        };
        new GGOKHTTP(param, GGOKHTTP.COMPANY_SENIOR, ggHttpInterface).startGet();
    }

    private void setListData(JSONObject data) {
        //悬浮头1
        adapters.add(new CurrencyTitleAdapter(getActivity(), "高管列表", true));

        JSONArray senior_info_list = data.getJSONArray("senior_info_list");
        List<ExecutivesData> executivesList = new ArrayList<>();
        executivesList.add(new ExecutivesData("姓名", "学历", "职务"));
        for (int i = 0; i < senior_info_list.size(); i++) {
            executivesList.add(new ExecutivesData(senior_info_list.getJSONObject(i).getString("name"),
                    senior_info_list.getJSONObject(i).getString("degree"),
                    senior_info_list.getJSONObject(i).getString("duty")));
        }
        adapters.add(new ExecutivesListAdapter(getActivity(), executivesList));

        //悬浮头2
        adapters.add(new CurrencyTitleAdapter(getActivity(), "高管持股变动", true));

        JSONArray senior_stock_list = data.getJSONArray("senior_stock_list");
        List<HoldingData> HoldingList = new ArrayList<>();
        HoldingList.add(new HoldingData("日期", "变动人", "变动数量(股)"));
        for (int i = 0; i < senior_stock_list.size(); i++) {
            HoldingList.add(new HoldingData(senior_stock_list.getJSONObject(i).getString("date"),
                    senior_stock_list.getJSONObject(i).getString("name"),
                    senior_stock_list.getJSONObject(i).getString("change_stock")));
        }
        adapters.add(new HoldingChangeAdapter(getActivity(), HoldingList));

        delegateAdapter.setAdapters(adapters);
    }
}
