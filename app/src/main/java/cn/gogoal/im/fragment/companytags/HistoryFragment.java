package cn.gogoal.im.fragment.companytags;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
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
import cn.gogoal.im.bean.companytags.HistoryBean;
import cn.gogoal.im.bean.companytags.HistoryData;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.UserUtils;

/**
 * Created by huangxx on 2017/6/28.
 */

public class HistoryFragment extends BaseFragment {

    @BindView(R.id.rv_history)
    RecyclerView recyclerView;

    private String stockCode;
    private List<HistoryData> historyDatas;
    private HistoryAdapter historyAdapter;

    public static HistoryFragment newInstance() {
        HistoryFragment fragment = new HistoryFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_history;
    }

    @Override
    public void doBusiness(Context mContext) {
        stockCode = "000001";
        init();
    }

    private void init() {
        historyDatas = new ArrayList<>();
        historyAdapter = new HistoryAdapter(historyDatas);
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        //绑定
        recyclerView.setAdapter(historyAdapter);
        getHistory();
    }

    private void getHistory() {
        Map<String, String> param = new HashMap<>();
        param.put("token", UserUtils.getToken());
        param.put("stock_code", stockCode);
        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                JSONObject result = JSONObject.parseObject(responseInfo);
                if (result.getIntValue("code") == 0) {
                    historyDatas = JSONObject.parseObject(responseInfo, HistoryBean.class).getData();
                    HistoryData historyData = new HistoryData("财报周期", 0, "财报发布日", 0);
                    historyDatas.add(0, historyData);
                    KLog.e(historyDatas);
                    historyAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(String msg) {

            }
        };
        new GGOKHTTP(param, GGOKHTTP.GET_HISTORY_ACHIEVEMENT_APPRAISE, ggHttpInterface).startGet();
    }

    private class HistoryAdapter extends CommonAdapter<HistoryData, BaseViewHolder> {

        HistoryAdapter(List<HistoryData> data) {
            super(R.layout.item_history, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, HistoryData data, int position) {
            if (position == 0) {
                holder.setText(R.id.tv_history_item, "");
            } else {
                holder.setText(R.id.tv_history_item, data.getYear() + "第" + data.getQuarter() + "季");
            }
            holder.setText(R.id.tv_history_week, data.getReport_period());
            holder.setText(R.id.tv_history_day, data.getReport_publish());
        }

    }
}
