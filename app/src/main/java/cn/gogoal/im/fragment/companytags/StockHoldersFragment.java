package cn.gogoal.im.fragment.companytags;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.bean.BaseBeanList;
import cn.gogoal.im.bean.companytags.IndustryData;
import cn.gogoal.im.bean.companytags.StockHolderBean;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.ui.view.LineView;

/**
 * Created by huangxx on 2017/6/30.
 */

public class StockHoldersFragment extends BaseFragment {

    @BindView(R.id.stockHolder_layout)
    LinearLayout stockHolder_layout;

    @BindView(R.id.stock_lineView)
    LineView stockLineView;

    private String stockCode;
    private int fragmentType;
    private List<StockHolderBean> stockHolderBeans;

    public static StockHoldersFragment newInstance(String code, int type) {
        StockHoldersFragment fragment = new StockHoldersFragment();
        Bundle bundle = new Bundle();
        bundle.putString("stock_code", code);
        bundle.putInt("fragment_type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_stock_holders;
    }

    @Override
    public void doBusiness(Context mContext) {
        stockCode = getArguments().getString("stock_code");
        fragmentType = getArguments().getInt("fragment_type");
        stockHolderBeans = new ArrayList<>();
        stockHolder_layout.setVisibility(View.GONE);
        getStockHolders();
    }

    private void getStockHolders() {
        Map<String, String> param = new HashMap<>();
        param.put("token", UserUtils.getToken());
        param.put("stock_code", stockCode);
        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                JSONObject result = JSONObject.parseObject(responseInfo);
                KLog.e(responseInfo);
                if (result.getIntValue("code") == 0) {
                    BaseBeanList<StockHolderBean> beanList = JSONObject.parseObject(
                            responseInfo,
                            new TypeReference<BaseBeanList<StockHolderBean>>() {
                            });
                    stockHolderBeans.addAll(beanList.getData());
                    if (stockHolderBeans.size() > 0) {
                        stockHolder_layout.setVisibility(View.VISIBLE);
                        stockLineView.setMarginTop(AppDevice.dp2px(getActivity(), 25));
                        stockLineView.setMarginBottom(AppDevice.dp2px(getActivity(), 33));
                        stockLineView.setMarginLeft(AppDevice.dp2px(getActivity(), 40));
                        stockLineView.setMarginRight(AppDevice.dp2px(getActivity(), 10));
                        stockLineView.setTextSize(AppDevice.dp2px(getActivity(), 8));
                        stockLineView.setMarginBar(AppDevice.dp2px(getActivity(), 3));
                        stockLineView.setLineSize(AppDevice.dp2px(getActivity(), 1));
                        stockLineView.setChartData(stockHolderBeans);
                        KLog.e(stockHolderBeans);
                    }
                }
            }

            @Override
            public void onFailure(String msg) {

            }
        };
        new GGOKHTTP(param, GGOKHTTP.GET_TEN_TRADABLE_STOCK_HOLDERS, ggHttpInterface).startGet();
    }

}
