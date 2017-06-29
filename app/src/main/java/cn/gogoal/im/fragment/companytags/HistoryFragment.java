package cn.gogoal.im.fragment.companytags;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import cn.gogoal.im.bean.companytags.IndustryBean;
import cn.gogoal.im.bean.companytags.IndustryData;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.StringUtils;
import cn.gogoal.im.common.UserUtils;

/**
 * Created by huangxx on 2017/6/28.
 */

public class HistoryFragment extends BaseFragment {

    @BindView(R.id.rv_history)
    RecyclerView recyclerView;

    private String stockCode;
    private int fragmentType;
    private List<HistoryData> historyDatas;
    private HistoryAdapter historyAdapter;

    private List<IndustryData> industryDatas;
    private IndustryAdapter industryAdapter;

    public static HistoryFragment newInstance(String code, int type) {
        HistoryFragment fragment = new HistoryFragment();
        Bundle bundle = new Bundle();
        bundle.putString("stock_code", code);
        bundle.putInt("fragment_type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_history;
    }

    @Override
    public void doBusiness(Context mContext) {
        stockCode = getArguments().getString("stock_code");
        fragmentType = getArguments().getInt("fragment_type");
        init();
    }

    private void init() {
        historyDatas = new ArrayList<>();
        industryDatas = new ArrayList<>();
        historyAdapter = new HistoryAdapter(historyDatas);
        industryAdapter = new IndustryAdapter(industryDatas);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

        if (fragmentType == AppConst.TYPE_FRAGMENT_HISTORY) {
            //设置布局管理器
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerView.setLayoutManager(linearLayoutManager);
            //绑定
            recyclerView.setAdapter(historyAdapter);
            getHistory();
        } else if (fragmentType == AppConst.TYPE_FRAGMENT_INDUSTRY) {
            //设置布局管理器
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(linearLayoutManager);
            //绑定
            recyclerView.setAdapter(industryAdapter);
            getIndustry();

        }
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
                    historyDatas.addAll(JSONObject.parseObject(responseInfo, HistoryBean.class).getData());
                    HistoryData historyData = new HistoryData("财报周期", 0, "财报发布日", 0);
                    historyDatas.add(0, historyData);
                    historyAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(String msg) {

            }
        };
        new GGOKHTTP(param, GGOKHTTP.GET_HISTORY_ACHIEVEMENT_APPRAISE, ggHttpInterface).startGet();
    }

    private void getIndustry() {
        Map<String, String> param = new HashMap<>();
        param.put("token", UserUtils.getToken());
        param.put("stock_code", stockCode);
        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                JSONObject result = JSONObject.parseObject(responseInfo);
                KLog.e(responseInfo);
                if (result.getIntValue("code") == 0) {
                    industryDatas.addAll(JSONObject.parseObject(responseInfo, IndustryBean.class).getData());
                    IndustryData industryData = new IndustryData(0.0f, "所属行业", "", "代码", "名称");
                    industryDatas.add(0, industryData);
                    industryAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(String msg) {

            }
        };
        new GGOKHTTP(param, GGOKHTTP.GET_BUSINESS_STATUS, ggHttpInterface).startGet();
    }

    private class HistoryAdapter extends CommonAdapter<HistoryData, BaseViewHolder> {

        HistoryAdapter(List<HistoryData> datas) {
            super(R.layout.item_history, datas);
        }

        @Override
        protected void convert(BaseViewHolder holder, HistoryData data, int position) {

            View itemView = holder.getView(R.id.layout_item);
            ViewGroup.LayoutParams params = itemView.getLayoutParams();
            params.width = (AppDevice.getWidth(getActivity()) - AppDevice.dp2px(getActivity(), 20)) / 5;
            itemView.setLayoutParams(params);

            if (position == 0) {
                holder.setText(R.id.tv_history_item, "");
            } else {
                holder.setText(R.id.tv_history_item, data.getYear() + "第" + data.getQuarter() + "季");
            }
            holder.setText(R.id.tv_history_week, data.getReport_period());
            holder.setText(R.id.tv_history_day, data.getReport_publish());
        }

    }

    private class IndustryAdapter extends CommonAdapter<IndustryData, BaseViewHolder> {

        IndustryAdapter(List<IndustryData> datas) {
            super(R.layout.item_industry, datas);
        }

        @Override
        protected void convert(BaseViewHolder holder, IndustryData data, int position) {

            TextView tv_NO_one = holder.getView(R.id.tv_NO_one);
            TextView tv_belong = holder.getView(R.id.tv_belong);
            TextView tv_code = holder.getView(R.id.tv_code);
            TextView tv_name = holder.getView(R.id.tv_name);
            TextView tv_worth = holder.getView(R.id.tv_worth);

            int longWidth = (AppDevice.getWidth(getActivity()) - AppDevice.dp2px(getActivity(), 55)) * 5 / 23;
            int shortWidth = (AppDevice.getWidth(getActivity()) - AppDevice.dp2px(getActivity(), 55)) * 4 / 23;
            tv_NO_one.setWidth(longWidth);
            tv_belong.setWidth(longWidth);
            tv_code.setWidth(shortWidth);
            tv_name.setWidth(shortWidth);
            tv_worth.setWidth(longWidth);

            if (position == 0) {
                holder.setText(R.id.tv_order, "序号");
                tv_worth.setText("总市值\n(亿元)");
            } else {
                holder.setText(R.id.tv_order, String.valueOf(position));
                tv_worth.setText(StringUtils.save2Significand(data.getEUR_Mn()));
            }
            tv_NO_one.setText(data.getIs_head());
            tv_belong.setText(data.getIndustry_involved());
            tv_code.setText(data.getStock_code());
            tv_name.setText(data.getStock_name());
        }

    }
}
