package cn.gogoal.im.fragment.stock;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.hply.alilayout.DelegateAdapter;
import com.hply.alilayout.VirtualLayoutManager;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.market.BigDataAdapter;
import cn.gogoal.im.adapter.market.HostIndustryGridAdapter;
import cn.gogoal.im.adapter.market.MarketViewPaerAdapter;
import cn.gogoal.im.adapter.market.RankListAdapter;
import cn.gogoal.im.adapter.market.TitleAdapter;
import cn.gogoal.im.base.AppManager;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.bean.stock.HangqingBean;
import cn.gogoal.im.bean.stock.Market;
import cn.gogoal.im.bean.stock.StockMarketBean;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.SPTools;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.ui.view.XLayout;
import cn.gogoal.im.ui.widget.refresh.RefreshLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

import static cn.gogoal.im.R.id.rv_market;
import static cn.gogoal.im.common.AppConst.REFRESH_TYPE_PARENT_BUTTON;
import static cn.gogoal.im.common.AppConst.REFRESH_TYPE_RELOAD;
import static cn.gogoal.im.common.AppConst.REFRESH_TYPE_SWIPEREFRESH;

/**
 * author wangjd on 2017/6/14 0014.
 * Staff_id 1375
 * phone 18930640263
 * description :基于阿里vlayout的行情
 */
public class MarketFragment2 extends BaseFragment {

    @BindView(rv_market)
    RecyclerView rvMarket;

    @BindView(R.id.swiperefreshlayout)
    RefreshLayout refreshLayout;

    private DelegateAdapter delegateAdapter;

    @BindView(R.id.xLayout)
    XLayout xLayout;

    private LinkedList<DelegateAdapter.Adapter> adapters;

    @Override
    public int bindLayout() {
        return R.layout.fragment_hushen;
    }

    @Override
    public void doBusiness(final Context mContext) {
        final VirtualLayoutManager layoutManager = new VirtualLayoutManager(mContext);
        rvMarket.setLayoutManager(layoutManager);
        layoutManager.setAutoMeasureEnabled(true);

//        不同子adapter之间的类型不共享
        delegateAdapter = new DelegateAdapter(layoutManager, false);
        adapters = new LinkedList<>();

        rvMarket.setAdapter(delegateAdapter);
        rvMarket.setNestedScrollingEnabled(false);
        rvMarket.setBackgroundColor(Color.WHITE);

        getMarketInformation(AppConst.REFRESH_TYPE_FIRST);

        refreshLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getMarketInformation(AppConst.REFRESH_TYPE_SWIPEREFRESH);
                refreshLayout.refreshComplete();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        getMarketInformation(AppConst.REFRESH_TYPE_RESUME);
    }

    /**
     * 获取[大盘]、[热门行业]、[涨跌振换]列表
     */
    public void getMarketInformation(final int refreshType) {

        AppManager.getInstance().sendMessage("market_start_animation_refresh");

        if (refreshType == REFRESH_TYPE_RELOAD) {
            xLayout.setStatus(XLayout.Loading);//loading
        }

        final Map<String, String> param = new HashMap<>();
        param.put("fullcode", "sh000001;sz399001;sz399006;sh000300;sz399005;sh000016");
        param.put("category_type", "1");
        GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                if (JSONObject.parseObject(responseInfo).getIntValue("code") == 0) {
                    SPTools.saveString("MARKET_RESPONSEINFO_DATA", responseInfo);//缓存
                    reconstructData(responseInfo, refreshType);
                } else {
                    xLayout.setStatus(XLayout.Error);
                    String errorMsg = JSONObject.parseObject(responseInfo).getString("message");
                    xLayout.setEmptyText(errorMsg);
                    if (refreshType == REFRESH_TYPE_PARENT_BUTTON || refreshType == REFRESH_TYPE_SWIPEREFRESH) {
                        UIHelper.toast(getContext(), "行情数据更新出错\r\n" + errorMsg, Toast.LENGTH_LONG);
                    }
                }
                AppManager.getInstance().sendMessage("market_stop_animation_refresh");
            }

            @Override
            public void onFailure(String msg) {
                KLog.e(msg);

                UIHelper.toastError(getActivity(), msg, xLayout);

                if (xLayout != null) {
                    xLayout.setEmptyText(msg);
                    xLayout.setOnReloadListener(new XLayout.OnReloadListener() {
                        @Override
                        public void onReload(View v) {
                            getMarketInformation(REFRESH_TYPE_RELOAD);
                        }
                    });
                }
                AppManager.getInstance().sendMessage("market_stop_animation_refresh");
            }
        };
        new GGOKHTTP(param, GGOKHTTP.APP_HQ_INFORMATION, ggHttpInterface).startGet();
    }

    private void reconstructData(String responseInfo, int refreshType) {
        adapters.clear();
        Market marketData = JSONObject.parseObject(responseInfo, StockMarketBean.class).getData();
        List<HangqingBean> hangqingData = marketData.getHangqing();

        List<HangqingBean> newList=new ArrayList<>();
        for (HangqingBean bean : hangqingData) {
            switch (bean.getFullcode()) {
                case "sh000001":
                    bean.setIndex(0);
                    break;
                case "sz399001":
                    bean.setIndex(1);
                    break;
                case "sz399006":
                    bean.setIndex(2);
                    break;
                case "sh000300":
                    bean.setIndex(3);
                    break;
                case "sz399005":
                    bean.setIndex(4);
                    break;
                case "sh000016":
                    bean.setIndex(5);
                    break;
            }
            newList.add(bean);
        }

        Collections.sort(newList, new Comparator<HangqingBean>() {
            @Override
            public int compare(HangqingBean o1, HangqingBean o2) {
                return Integer.compare(o1.getIndex(),o2.getIndex());
            }
        });

        //滑动指数
        adapters.add(new MarketViewPaerAdapter(newList));

        //热门行业
        adapters.add(new TitleAdapter(getContext(), TitleAdapter.RANK_LIST_TITLE_HOT_INDUSTRY));
        HostIndustryGridAdapter hostIndustryGridAdapter = new
                HostIndustryGridAdapter(getContext(), marketData.getHostIndustrylist());
        adapters.add(hostIndustryGridAdapter);
        hostIndustryGridAdapter.notifyDataSetChanged();

        //大数据选股
        adapters.add(new TitleAdapter(getContext(), TitleAdapter.RANK_LIST_TITLE_BIG_DATA_CHOOSE_STOCK));
        adapters.add(new BigDataAdapter());

        //涨
        adapters.add(new TitleAdapter(getContext(),RankListAdapter.RANK_TYPE_INCREASE_LIST));
        RankListAdapter rankListAdapter0 = new RankListAdapter(marketData.getStockRanklist().getIncrease_list(),
                RankListAdapter.RANK_TYPE_INCREASE_LIST);
        adapters.add(rankListAdapter0);
        rankListAdapter0.notifyDataSetChanged();

        //跌
        adapters.add(new TitleAdapter(getContext(),RankListAdapter.RANK_TYPE_DOWN_LIST));
        adapters.add(new RankListAdapter(marketData.getStockRanklist().getDown_list(),
                RankListAdapter.RANK_TYPE_DOWN_LIST));

        //换
        adapters.add(new TitleAdapter(getContext(),RankListAdapter.RANK_TYPE_CHANGE_LIST));
        adapters.add(new RankListAdapter(marketData.getStockRanklist().getChange_list(),
                RankListAdapter.RANK_TYPE_CHANGE_LIST));

        //振
        adapters.add(new TitleAdapter(getContext(),RankListAdapter.RANK_TYPE_AMPLITUDE_LIST));
        adapters.add(new RankListAdapter(marketData.getStockRanklist().getAmplitude_list(),
                RankListAdapter.RANK_TYPE_AMPLITUDE_LIST));

        delegateAdapter.setAdapters(adapters);

        rvMarket.scrollBy(0,5);
    }
}
