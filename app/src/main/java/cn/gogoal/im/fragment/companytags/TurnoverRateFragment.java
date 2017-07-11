package cn.gogoal.im.fragment.companytags;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.bean.ChartBean;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.JsonUtils;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.ui.view.BarView;

/**
 * Created by huangxx on 2017/6/29.
 */

public class TurnoverRateFragment extends BaseFragment {

    @BindView(R.id.turnOver_layout)
    LinearLayout turnOver_layout;

    @BindView(R.id.barView)
    BarView barView;

    private String stockCode;
    private int fragmentType;
    private List<ChartBean> chartBeanList;

    public static TurnoverRateFragment newInstance(String code, int type) {
        TurnoverRateFragment fragment = new TurnoverRateFragment();
        Bundle bundle = new Bundle();
        bundle.putString("stock_code", code);
        bundle.putInt("fragment_type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_turnover_rate;
    }

    @Override
    public void doBusiness(Context mContext) {
        stockCode = getArguments().getString("stock_code");
        fragmentType = getArguments().getInt("fragment_type");
        chartBeanList = new ArrayList<>();
        turnOver_layout.setVisibility(View.GONE);
        getTurnOverRate();
    }

    private void getTurnOverRate() {
        Map<String, String> param = new HashMap<>();
        param.put("token", UserUtils.getToken());
        param.put("stock_code", stockCode);
        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                JsonObject result = JsonUtils.toJsonObject(responseInfo);
                JsonArray array = result.get("data").getAsJsonArray();

                if (result.get("code").getAsInt() == 0) {
                    chartBeanList.clear();
                    for (int i = 0; i < array.size(); i++) {
                        JsonObject chartObject = array.get(i).getAsJsonObject();
                        chartBeanList.add(new ChartBean(chartObject.get("turnover").getAsFloat(),
                                chartObject.get("date").getAsString().substring(5, 10)));
                    }
                    if (chartBeanList.size() > 0) {
                        turnOver_layout.setVisibility(View.VISIBLE);
                        barView.setTextSize(AppDevice.dp2px(getActivity(), 10));
                        barView.setMarginBottom(AppDevice.dp2px(getActivity(), 25));
                        barView.setMarginTop(AppDevice.dp2px(getActivity(), 20));
                        barView.setBarType(AppConst.TYPE_FRAGMENT_TURNOVER_RATE);
                        barView.setChartData(chartBeanList);
                    }
                }
            }

            @Override
            public void onFailure(String msg) {

            }
        };
        new GGOKHTTP(param, GGOKHTTP.GET_TURNOVER_RATE, ggHttpInterface).startGet();
    }

}
