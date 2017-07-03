package cn.gogoal.im.fragment.companytags;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.bean.BaseBeanList;
import cn.gogoal.im.bean.PieBean;
import cn.gogoal.im.bean.companytags.PieData;
import cn.gogoal.im.bean.companytags.RevenueData;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.ui.view.PieView;

/**
 * Created by huangxx on 2017/6/30.
 */

public class RevenueRateFragment extends BaseFragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private String stockCode;
    private int fragmentType;
    private List<RevenueData> revenueDatas;
    private List<PieData> pieDatas;
    private RevenueAdapter revenueAdapter;

    public static RevenueRateFragment newInstance(String code, int type) {
        RevenueRateFragment fragment = new RevenueRateFragment();
        Bundle bundle = new Bundle();
        bundle.putString("stock_code", code);
        bundle.putInt("fragment_type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_revenue_rate;
    }

    @Override
    public void doBusiness(Context mContext) {
        stockCode = getArguments().getString("stock_code");
        fragmentType = getArguments().getInt("fragment_type");
        initThis();
    }

    private void initThis() {
        revenueDatas = new ArrayList<>();
        pieDatas = new ArrayList<>();
        revenueAdapter = new RevenueAdapter(pieDatas);
        //设置布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        //绑定
        recyclerView.setAdapter(revenueAdapter);
        getRevenue();
    }

    private void getRevenue() {
        Map<String, String> param = new HashMap<>();
        param.put("token", UserUtils.getToken());
        param.put("stock_code", stockCode);
        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                JSONObject result = JSONObject.parseObject(responseInfo);
                if (result.getIntValue("code") == 0) {

                    BaseBeanList<RevenueData> beanList = JSONObject.parseObject(
                            responseInfo,
                            new TypeReference<BaseBeanList<RevenueData>>() {
                            });
                    revenueDatas.addAll(beanList.getData());

                    Map<Integer, List<RevenueData>> map = new HashMap<>();
                    for (int i = 0; i < revenueDatas.size(); i++) {
                        RevenueData data = revenueDatas.get(i);
                        List<RevenueData> listRevenue = map.get(data.getReport_year());
                        if (listRevenue == null) {
                            listRevenue = new ArrayList<>();
                            map.put(data.getReport_year(), listRevenue);
                        }
                        listRevenue.add(data);
                    }
                    Set<Map.Entry<Integer, List<RevenueData>>> entries = map.entrySet();
                    for (Map.Entry<Integer, List<RevenueData>> entry : entries) {
                        pieDatas.add(new PieData(entry.getKey(), entry.getValue()));
                    }
                    //排序
                    Collections.sort(pieDatas);
                    KLog.e(pieDatas);
                    revenueAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(String msg) {

            }
        };
        new GGOKHTTP(param, GGOKHTTP.GET_REVENUE_RATE, ggHttpInterface).startGet();
    }

    private class RevenueAdapter extends CommonAdapter<PieData, BaseViewHolder> {

        RevenueAdapter(List<PieData> datas) {
            super(R.layout.item_revenue_rate, datas);
        }

        @Override
        protected void convert(BaseViewHolder holder, PieData data, int position) {
            PieView pieView = holder.getView(R.id.pieView);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) pieView.getLayoutParams();
            params.width = AppDevice.getWidth(getActivity()) / pieDatas.size();
            pieView.setLayoutParams(params);
            List<PieBean> pieBeanList = new ArrayList<>();
            for (int i = 0; i < data.getData().size(); i++) {
                PieBean pieBean = new PieBean((float) data.getData().get(i).getIncomezb(), getColor(data.getData().get(i).getSession_name()));
                pieBeanList.add(pieBean);
            }
            pieView.setPieType(2);
            pieView.setNeedInnerTitle(true);
            pieView.setPieData(pieBeanList);
            pieView.setMarginLeft(AppDevice.dp2px(getActivity(), 20));
            pieView.setMarginRight(AppDevice.dp2px(getActivity(), 20));
            pieView.setMarginBottom(AppDevice.dp2px(getActivity(), 20));
            pieView.setMarginTop(AppDevice.dp2px(getActivity(), 20));
            pieView.setTextSize(AppDevice.dp2px(getActivity(), 10));
            holder.setText(R.id.tv_year, data.getReport_year() + "年");
        }

    }

    private int getColor(String session) {
        switch (session) {
            case "一季度":
                return R.color.stock_green;
            case "中期":
                return R.color.card_color_orange;
            case "三季度":
                return R.color.card_color_purple;
            case "年度":
                return R.color.blue_light;
            default:
                return R.color.blue_light;
        }
    }
}
