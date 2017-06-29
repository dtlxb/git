package cn.gogoal.im.fragment.companytags;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.bean.companytags.IncidentBean;
import cn.gogoal.im.bean.companytags.IncidentData;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.UserUtils;

/**
 * Created by huangxx on 2017/6/29.
 */

public class IncidentFragment extends BaseFragment {

    @BindView(R.id.rv_history)
    RecyclerView recyclerView;

    private String stockCode;
    private int fragmentType;
    private List<IncidentData> incidentDatas;
    private IncidentAdapter incidentAdapter;

    public static IncidentFragment newInstance(String code, int type) {
        IncidentFragment fragment = new IncidentFragment();
        Bundle bundle = new Bundle();
        bundle.putString("stock_code", code);
        bundle.putInt("fragment_type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_incident;
    }

    @Override
    public void doBusiness(Context mContext) {
        stockCode = getArguments().getString("stock_code");
        fragmentType = getArguments().getInt("fragment_type");
        initThis();
    }

    private void initThis() {
        incidentDatas = new ArrayList<>();
        incidentAdapter = new IncidentAdapter(incidentDatas);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        //设置布局管理器
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        //绑定
        recyclerView.setAdapter(incidentAdapter);
        getFuture();
    }

    private void getFuture() {
        Map<String, String> param = new HashMap<>();
        param.put("token", UserUtils.getToken());
        param.put("stock_code", stockCode);
        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                JSONObject result = JSONObject.parseObject(responseInfo);
                KLog.e(responseInfo);
                if (result.getIntValue("code") == 0) {
                    incidentDatas.addAll(JSONObject.parseObject(responseInfo, IncidentBean.class).getData());
                    incidentAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(String msg) {

            }
        };
        new GGOKHTTP(param, GGOKHTTP.GET_FUTURE_EVENT, ggHttpInterface).startGet();
    }

    private class IncidentAdapter extends CommonAdapter<IncidentData, BaseViewHolder> {

        IncidentAdapter(List<IncidentData> datas) {
            super(R.layout.item_incident_future, datas);
        }

        @Override
        protected void convert(BaseViewHolder holder, IncidentData data, int position) {

        }

    }
}
