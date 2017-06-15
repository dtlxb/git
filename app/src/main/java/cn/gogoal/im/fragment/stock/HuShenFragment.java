package cn.gogoal.im.fragment.stock;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.MarketAdapter;
import cn.gogoal.im.base.AppManager;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.bean.stock.HangqingBean;
import cn.gogoal.im.bean.stock.HostIndustrylistBean;
import cn.gogoal.im.bean.stock.Market;
import cn.gogoal.im.bean.stock.MarkteBean;
import cn.gogoal.im.bean.stock.StockMarketBean;
import cn.gogoal.im.bean.stock.stockRanklist.StockRankBean;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.SPTools;
import cn.gogoal.im.common.StringUtils;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.ui.view.XLayout;
import cn.gogoal.im.ui.widget.VpSwipeRefreshLayout;

import static cn.gogoal.im.common.AppConst.REFRESH_TYPE_PARENT_BUTTON;
import static cn.gogoal.im.common.AppConst.REFRESH_TYPE_RELOAD;
import static cn.gogoal.im.common.AppConst.REFRESH_TYPE_SWIPEREFRESH;


/**
 * author wangjd on 2017/4/5 0005.
 * Staff_id 1375
 * phone 18930640263
 * description :沪深.
 */
public class HuShenFragment extends BaseFragment {

    @BindView(R.id.xLayout)
    XLayout xLayout;

    @BindView(R.id.rv_market)
    RecyclerView rvMarket;

    @BindView(R.id.swiperefreshlayout)
    VpSwipeRefreshLayout refreshLayout;

    private MarketAdapter adapter;

    private ArrayList<MarkteBean> markteList = new ArrayList<>();

    @Override
    public int bindLayout() {
        return R.layout.fragment_hushen;
    }

    @Override
    public void doBusiness(Context mContext) {
        BaseActivity.initRecycleView(rvMarket, null);
        BaseActivity.iniRefresh(refreshLayout);

        adapter = new MarketAdapter(getActivity(), markteList);

        rvMarket.setAdapter(adapter);

        getMarketInformation(AppConst.REFRESH_TYPE_FIRST);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMarketInformation(AppConst.REFRESH_TYPE_SWIPEREFRESH);
                refreshLayout.setRefreshing(false);
            }
        });
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
                UIHelper.toastError(getActivity(), msg, xLayout);

                if (xLayout != null) {
                    xLayout.setEmptyText(msg);
                    xLayout.setOnReloadListener(new XLayout.OnReloadListener() {
                        @Override
                        public void onReload(View v) {
                            getMarketInformation(AppConst.REFRESH_TYPE_RELOAD);
                        }
                    });
                }
                AppManager.getInstance().sendMessage("market_stop_animation_refresh");

            }
        };
        new GGOKHTTP(param, GGOKHTTP.APP_HQ_INFORMATION, ggHttpInterface).startGet();
    }

    private void reconstructData(String responseInfo, final int refreshType) {
        markteList.clear();
        Market marketData = JSONObject.parseObject(responseInfo, StockMarketBean.class).getData();
        //大盘
        List<MarkteBean.MarketItemData> listMarket = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            HangqingBean hangqingBean = marketData.getHangqing().get(i);
            MarkteBean.MarketItemData itemData = new MarkteBean.MarketItemData(
                    hangqingBean.getName(),
                    StringUtils.pareseStringDouble(hangqingBean.getPrice()),
                    hangqingBean.getPrice_change(),
                    hangqingBean.getPrice_change_rate(), "", null, hangqingBean.getFullcode(),
                    StringUtils.pareseStringDouble(hangqingBean.getPrice_change()));
//            "sh000001;sz399001;sz399006;sh000300;sz399005;sh000016"
            switch (hangqingBean.getFullcode()){
                case "sh000001":
                    itemData.setIndex(0);
                    break;
                case "sz399001":
                    itemData.setIndex(1);
                    break;
                case "sz399006":
                    itemData.setIndex(2);
                    break;
                case "sh000300":
                    itemData.setIndex(3);
                    break;
                case "sz399005":
                    itemData.setIndex(4);
                    break;
                case "sh000016":
                    itemData.setIndex(5);
                    break;
            }
            listMarket.add(itemData);
        }

        markteList.add(new MarkteBean("大盘数据", listMarket));

        //热门行业
        List<MarkteBean.MarketItemData> listHotIndestry = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            HostIndustrylistBean industrylistBean = marketData.getHostIndustrylist().get(i);
            MarkteBean.MarketItemData itemData = new MarkteBean.MarketItemData(
                    industrylistBean.getIndustry_name(),
                    StringUtils.pareseStringDouble(industrylistBean.getCurrent_price()),
                    "",
                    industrylistBean.getRate(),
                    industrylistBean.getIndustry_rate(),
                    industrylistBean.getStock_name(),
                    industrylistBean.getStock_code(),
                    StringUtils.pareseStringDouble(industrylistBean.getIndustry_rate())
            );
            listHotIndestry.add(itemData);
        }
        markteList.add(new MarkteBean("热门行业", listHotIndestry));

        //[涨跌振换]列表
        markteList.add(new MarkteBean("涨幅榜", addRankList(marketData.getStockRanklist().getIncrease_list())));
        markteList.add(new MarkteBean("跌幅榜", addRankList(marketData.getStockRanklist().getDown_list())));
        markteList.add(new MarkteBean("换手率", addRankList(marketData.getStockRanklist().getChange_list())));
        markteList.add(new MarkteBean("振幅榜", addRankList(marketData.getStockRanklist().getAmplitude_list())));

        xLayout.setStatus(XLayout.Success);

        adapter.notifyDataSetChanged();

        if (refreshType == REFRESH_TYPE_PARENT_BUTTON || refreshType == REFRESH_TYPE_SWIPEREFRESH) {
            UIHelper.toast(getContext(), "行情" + getString(R.string.str_refresh_ok));
        }
    }

    private List<MarkteBean.MarketItemData> addRankList(List<StockRankBean> list) {
        List<MarkteBean.MarketItemData> increase = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            MarkteBean.MarketItemData itemData = new MarkteBean.MarketItemData(
                    null,
                    StringUtils.pareseStringDouble(list.get(i).getCurrent_price()),
                    "",
                    list.get(i).getRate(),
                    "",
                    list.get(i).getStock_name(),
                    list.get(i).getStock_code(),
                    StringUtils.pareseStringDouble(list.get(i).getRate())
            );
            increase.add(itemData);
        }
        return increase;
    }

}
