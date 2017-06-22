package cn.gogoal.im.fragment.stock.stockften;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

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
import cn.gogoal.im.adapter.CompanyProfileAdapter;
import cn.gogoal.im.adapter.CurrencyTitleAdapter;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.bean.ProfileData;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.copy.FtenUtils;

/**
 * Created by dave.
 * Date: 2017/6/7.
 * Desc: 公司概况
 */
public class CompanyProfileFragment extends BaseFragment {

    @BindView(R.id.sticky_recyView)
    RecyclerView recySticky;

    private String stockCode;
    private String stockName;

    //缓存池
    private RecyclerView.RecycledViewPool viewPool;
    private DelegateAdapter delegateAdapter;
    private LinkedList<DelegateAdapter.Adapter> adapters;

    public static CompanyProfileFragment getInstance(String stockCode, String stockName) {
        CompanyProfileFragment fragment = new CompanyProfileFragment();
        Bundle b = new Bundle();
        b.putString("stockCode", stockCode);
        b.putString("stockName", stockName);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_company_profile;
    }

    @Override
    public void doBusiness(Context mContext) {

        stockCode = getArguments().getString("stockCode");
        stockName = getArguments().getString("stockName");

        initRecyView();
        getProfileData();
    }

    private void initRecyView() {
        final VirtualLayoutManager layoutManager = new VirtualLayoutManager(getActivity());
        recySticky.setLayoutManager(layoutManager);
        layoutManager.setAutoMeasureEnabled(true);

        viewPool = new RecyclerView.RecycledViewPool();
        recySticky.setRecycledViewPool(viewPool);
        viewPool.setMaxRecycledViews(0, 20);

        delegateAdapter = new DelegateAdapter(layoutManager, false);
        adapters = new LinkedList<>();

        recySticky.setAdapter(delegateAdapter);
        recySticky.setHasFixedSize(true);
        recySticky.setNestedScrollingEnabled(false);
    }

    private void getProfileData() {

        Map<String, String> param = new HashMap<>();
        param.put("stock_code", stockCode);

        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
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
        new GGOKHTTP(param, GGOKHTTP.COMPANY_SUMMARY, ggHttpInterface).startGet();
    }

    private void setListData(JSONObject data) {
        //悬浮头1
        adapters.add(new CurrencyTitleAdapter(getActivity(), "基本资料", true));

        JSONObject basic_data = data.getJSONObject("basic_data");
        List<ProfileData> basicList = new ArrayList<>();
        for (int i = 0; i < FtenUtils.basicName.length; i++) {
            basicList.add(new ProfileData(FtenUtils.basicName[i],
                    basic_data.getString(FtenUtils.basicContent[i])));
        }
        adapters.add(new CompanyProfileAdapter(getActivity(), basicList));

        //悬浮头2
        adapters.add(new CurrencyTitleAdapter(getActivity(), "发行相关", true));

        JSONObject issue_data = data.getJSONObject("issue_data");
        List<ProfileData> issueList = new ArrayList<>();
        for (int i = 0; i < FtenUtils.issueName.length; i++) {
            issueList.add(new ProfileData(FtenUtils.issueName[i],
                    issue_data.getString(FtenUtils.issueContent[i])));
        }
        adapters.add(new CompanyProfileAdapter(getActivity(), issueList));

        delegateAdapter.setAdapters(adapters);
    }
}
