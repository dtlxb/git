package cn.gogoal.im.fragment.companytags;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.bean.BaseBeanList;
import cn.gogoal.im.bean.companytags.EventFeetData;
import cn.gogoal.im.bean.companytags.IncidentData;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.UserUtils;

/**
 * Created by huangxx on 2017/6/29.
 */

public class IncidentFragment extends BaseFragment {

    @BindView(R.id.layout_incident)
    LinearLayout layout_incident;

    @BindView(R.id.rv_history)
    RecyclerView recyclerView;

    private String stockCode;
    private int fragmentType;
    private List<IncidentData> incidentDatas;
    private IncidentAdapter incidentAdapter;

    private EventFeetAdapter eventFeetAdapter;
    private List<EventFeetData> eventFeetDatas;

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
        layout_incident.setVisibility(View.GONE);
        initThis();
    }

    private void initThis() {
        incidentDatas = new ArrayList<>();
        eventFeetDatas = new ArrayList<>();
        incidentAdapter = new IncidentAdapter(incidentDatas);
        eventFeetAdapter = new EventFeetAdapter(eventFeetDatas);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        //设置布局管理器
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        if (fragmentType == AppConst.TYPE_FRAGMENT_INCIDENT_FUTURE) {
            //绑定
            recyclerView.setAdapter(incidentAdapter);
            getFuture();
        } else if (fragmentType == AppConst.TYPE_FRAGMENT_INCIDENT_FEET) {
            //绑定
            recyclerView.setAdapter(eventFeetAdapter);
            getEventFeet();
        }
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
                    BaseBeanList<IncidentData> beanList = JSONObject.parseObject(
                            responseInfo,
                            new TypeReference<BaseBeanList<IncidentData>>() {
                            });
                    incidentDatas.addAll(beanList.getData());
                    if (incidentDatas.size() > 0) {
                        layout_incident.setVisibility(View.VISIBLE);
                    }
                    incidentAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(String msg) {

            }
        };
        new GGOKHTTP(param, GGOKHTTP.GET_FUTURE_EVENT, ggHttpInterface).startGet();
    }

    private void getEventFeet() {
        Map<String, String> param = new HashMap<>();
        param.put("token", UserUtils.getToken());
        param.put("stock_code", stockCode);
        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                JSONObject result = JSONObject.parseObject(responseInfo);
                KLog.e(responseInfo);
                if (result.getIntValue("code") == 0) {
                    BaseBeanList<EventFeetData> beanList = JSONObject.parseObject(
                            responseInfo,
                            new TypeReference<BaseBeanList<EventFeetData>>() {
                            });
                    eventFeetDatas.addAll(beanList.getData());
                    eventFeetAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(String msg) {

            }
        };
        new GGOKHTTP(param, GGOKHTTP.GET_LARGE_EVENT, ggHttpInterface).startGet();
    }

    private class IncidentAdapter extends CommonAdapter<IncidentData, BaseViewHolder> {

        IncidentAdapter(List<IncidentData> datas) {
            super(R.layout.item_incident_future, datas);
        }

        @Override
        protected void convert(BaseViewHolder holder, IncidentData data, int position) {
            holder.setText(R.id.tv_time, data.getEvent_date().substring(0, 10));
            holder.setText(R.id.tv_type, data.getEvent_type());
            holder.setText(R.id.tv_title, data.getEvent_title());
        }

    }

    private class EventFeetAdapter extends CommonAdapter<EventFeetData, BaseViewHolder> {

        EventFeetAdapter(List<EventFeetData> datas) {
            super(R.layout.item_incident_future, datas);
        }

        @Override
        protected void convert(BaseViewHolder holder, EventFeetData data, int position) {
            holder.setText(R.id.tv_time, data.getEvent_date().substring(0, 10));
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < data.getEvent_type().size(); i++) {
                stringBuilder.append(data.getEvent_type().get(i));
            }
            holder.setText(R.id.tv_type, stringBuilder);
            holder.setText(R.id.tv_title, data.getEvent_title());
        }

    }
}
