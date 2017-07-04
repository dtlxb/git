package cn.gogoal.im.fragment.companytags;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import cn.gogoal.im.bean.companytags.HistoryData;
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
                    BaseBeanList<HistoryData> beanList = JSONObject.parseObject(
                            responseInfo,
                            new TypeReference<BaseBeanList<HistoryData>>() {
                            });
                    historyDatas.addAll(beanList.getData());
                    if (historyDatas.size() > 0) {
                        HistoryData historyData = new HistoryData("报表发布日", 0, "报表周期", 0);
                        historyDatas.add(0, historyData);
                        historyAdapter.notifyDataSetChanged();
                    }
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
                    BaseBeanList<IndustryData> beanList = JSONObject.parseObject(
                            responseInfo,
                            new TypeReference<BaseBeanList<IndustryData>>() {
                            });
                    industryDatas.addAll(beanList.getData());
                    if (industryDatas.size() > 0) {
                        IndustryData industryData = new IndustryData(0.0f, "所属行业", "", "代码", "名称");
                        industryDatas.add(0, industryData);
                        industryAdapter.notifyDataSetChanged();
                    }
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
            TextView tvPeriod = holder.getView(R.id.tv_history_week);
            TextView tvPublish = holder.getView(R.id.tv_history_day);
            ViewGroup.LayoutParams params = itemView.getLayoutParams();
            params.width = (AppDevice.getWidth(getActivity()) - AppDevice.dp2px(getActivity(), 20)) / 5;
            itemView.setLayoutParams(params);
            if (data.getYear() == 0) {
                holder.setText(R.id.tv_history_item, "");
            } else {
                holder.setText(R.id.tv_history_item, data.getYear() + stringParse(data.getQuarter()));
            }
            if (TextUtils.isEmpty(data.getReport_period())) {
                tvPeriod.setText("--");
            } else {
                tvPeriod.setText(colorParse(data.getReport_period()));
            }
            if (TextUtils.isEmpty(data.getReport_publish())) {
                tvPublish.setText("--");
            } else {
                tvPublish.setText(colorParse(data.getReport_publish()));
            }
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

    private String stringParse(int num) {
        String season;
        switch (num) {
            case 1:
                season = "第一季";
                break;
            case 2:
                season = "中报";
                break;
            case 3:
                season = "第三季";
                break;
            case 4:
                season = "年报";
                break;
            default:
                season = "";
                break;
        }

        return season;
    }

    private SpannableStringBuilder colorParse(String report) {
        SpannableStringBuilder sb = new SpannableStringBuilder(report); // 包装字体内容
        ForegroundColorSpan fcs; // 设置字体颜色
        if (report.equals("超预期")) {
            fcs = new ForegroundColorSpan(Color.RED);
        } else {
            fcs = new ForegroundColorSpan(getResColor(R.color.textColor_333333));
        }
        sb.setSpan(fcs, 0, report.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        KLog.e(sb);
        return sb;
    }
}
