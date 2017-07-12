package cn.gogoal.im.fragment.stock;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.gogoal.im.R;
import cn.gogoal.im.activity.stock.BigDataAcyivity;
import cn.gogoal.im.activity.stock.RankListDetialActivity;
import cn.gogoal.im.adapter.market.rebuild.HostIndustryGridAdapter;
import cn.gogoal.im.adapter.market.rebuild.MarketViewPagerAdapter;
import cn.gogoal.im.adapter.market.rebuild.RankData;
import cn.gogoal.im.adapter.market.rebuild.RankLiastAdapter;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.bean.stock.HangqingBean;
import cn.gogoal.im.bean.stock.HostIndustrylistBean;
import cn.gogoal.im.bean.stock.Market;
import cn.gogoal.im.bean.stock.StockMarketBean;
import cn.gogoal.im.bean.stock.stockRanklist.StockRankData;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.ui.XDividerItemDecoration;
import cn.gogoal.im.ui.view.XLayout;
import cn.gogoal.im.ui.widget.CatchLayoutManager;
import cn.gogoal.im.ui.widget.DisallowParentTouchViewPager;
import cn.gogoal.im.ui.widget.refresh.RefreshLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

import static cn.gogoal.im.common.AppConst.REFRESH_TYPE_PARENT_BUTTON;
import static cn.gogoal.im.common.AppConst.REFRESH_TYPE_RELOAD;
import static cn.gogoal.im.common.AppConst.REFRESH_TYPE_SWIPEREFRESH;

/**
 * author wangjd on 2017/7/11 0011.
 * Staff_id 1375
 * phone 18930640263
 * description :${rebuild market }.
 */
public class MarketFragment extends BaseFragment {

    @BindView(R.id.xLayout)
    XLayout xLayout;

    @BindView(R.id.vp_market_zhishu)
    DisallowParentTouchViewPager vpMarketZhishu;

    @BindView(R.id.rv_market_hot_industry)
    RecyclerView rvMarketHotIndustry;

    @BindView(R.id.rv_rankList)
    RecyclerView rvRankList;

    @BindView(R.id.refreshLayout)
    RefreshLayout refreshLayout;

    //热门
    private HostIndustryGridAdapter hostIndustryGridAdapter;
    private List<HostIndustrylistBean> hotIndustryDatas;

    private List<RankData> rankDataList;
    private RankLiastAdapter rankLiastAdapter;

    @Override
    public int bindLayout() {
        return R.layout.fragment_hushen;
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        view.findViewById(R.id.include_title_big_data)
                .findViewById(R.id.flag_tv_more).setVisibility(View.INVISIBLE);
        TextView textViewTitle = (TextView) view.findViewById(R.id.include_title_big_data)
                .findViewById(R.id.tv_stock_ranklist_title);
        textViewTitle.setText(getString(R.string.title_big_data));
    }

    @Override
    public void doBusiness(Context mContext) {
        vpMarketZhishu.setNestParent(refreshLayout);
        rvMarketHotIndustry.setNestedScrollingEnabled(false);
        rvMarketHotIndustry.addItemDecoration(new GridMarketDivider(mContext));
        rvRankList.setNestedScrollingEnabled(false);

        //热门
        hotIndustryDatas = new ArrayList<>();
        hostIndustryGridAdapter = new HostIndustryGridAdapter(hotIndustryDatas);
        rvMarketHotIndustry.setAdapter(hostIndustryGridAdapter);

        //liebiao
        rankDataList = new ArrayList<>();
        rvRankList.setLayoutManager(new CatchLayoutManager(mContext));
        rvRankList.setNestedScrollingEnabled(false);
        rankLiastAdapter = new RankLiastAdapter(rankDataList);
        rvRankList.setAdapter(rankLiastAdapter);

        getMarketInformation(AppConst.REFRESH_TYPE_FIRST);

        refreshLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getMarketInformation(AppConst.REFRESH_TYPE_SWIPEREFRESH);
                refreshLayout.refreshComplete();
            }

        });

        xLayout.setOnReloadListener(new XLayout.OnReloadListener() {
            @Override
            public void onReload(View v) {
                getMarketInformation(AppConst.REFRESH_TYPE_RELOAD);
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
                    reconstructData(responseInfo, refreshType);
                } else {
                    xLayout.setStatus(XLayout.Error);
                    String errorMsg = JSONObject.parseObject(responseInfo).getString("message");
                    xLayout.setEmptyText(errorMsg);
                    if (refreshType == REFRESH_TYPE_PARENT_BUTTON || refreshType == REFRESH_TYPE_SWIPEREFRESH) {
                        UIHelper.toast(getContext(), "行情数据更新出错\r\n" + errorMsg, Toast.LENGTH_LONG);
                    }
                }
            }

            @Override
            public void onFailure(String msg) {
                KLog.e(msg);

                UIHelper.toastError(getActivity(), msg, xLayout);

            }
        };
        new GGOKHTTP(param, GGOKHTTP.APP_HQ_INFORMATION, ggHttpInterface).startGet();
    }

    private void reconstructData(String responseInfo, int refreshType) {
        Market marketData = JSONObject.parseObject(responseInfo, StockMarketBean.class).getData();
        List<HangqingBean> hangqingData = marketData.getHangqing();

        List<HangqingBean> newList = new ArrayList<>();
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
                return Integer.compare(o1.getIndex(), o2.getIndex());
            }
        });

        vpMarketZhishu.setAdapter(new MarketViewPagerAdapter(newList));

        if (refreshType == AppConst.REFRESH_TYPE_FIRST) {
            hotIndustryDatas.clear();
            hotIndustryDatas.addAll(marketData.getHostIndustrylist());
            hostIndustryGridAdapter.notifyDataSetChanged();
        }

        rankDataList.clear();
        //涨
        rankDataList.add(new RankData(RankData.TYPE_ITEM_TITLE, "涨幅榜"));
        List<StockRankData> increaseList = marketData.getStockRanklist().getIncrease_list();
        for (StockRankData data : increaseList) {
            rankDataList.add(new RankData(RankData.RANK_TYPE_INCREASE_LIST, data));
        }
        //跌
        rankDataList.add(new RankData(RankData.TYPE_ITEM_TITLE, "跌幅榜"));
        List<StockRankData> downList = marketData.getStockRanklist().getDown_list();
        for (StockRankData data : downList) {
            rankDataList.add(new RankData(RankData.RANK_TYPE_INCREASE_LIST, data));
        }
        //换
        rankDataList.add(new RankData(RankData.TYPE_ITEM_TITLE, "换手率"));
        List<StockRankData> changeList = marketData.getStockRanklist().getChange_list();
        for (StockRankData data : changeList) {
            rankDataList.add(new RankData(RankData.RANK_TYPE_INCREASE_LIST, data));
        }
        //振
        rankDataList.add(new RankData(RankData.TYPE_ITEM_TITLE, "振幅榜"));
        List<StockRankData> amplitudeList = marketData.getStockRanklist().getAmplitude_list();
        for (StockRankData data : amplitudeList) {
            rankDataList.add(new RankData(RankData.RANK_TYPE_INCREASE_LIST, data));
        }
        rankLiastAdapter.notifyDataSetChanged();

    }

    @OnClick({R.id.btn_item_stock_market_event,
            R.id.btn_item_stock_market_subject, R.id.include_title_hot_industry})
    void click(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.include_title_hot_industry:
                intent.setClass(view.getContext(), RankListDetialActivity.class);
                intent.putExtra("MODULE_TITLE", "热门行业");
                intent.putExtra("MODULE_TYPE", RankListDetialActivity.MODULE_TYPE_HOT_INDUSTRY_TITLE);
                startActivity(intent);
                break;
            case R.id.btn_item_stock_market_event://事件选股
                intent.setClass(view.getContext(), BigDataAcyivity.class);
                intent.putExtra("big_index", 0);
                startActivity(intent);
                break;
            case R.id.btn_item_stock_market_subject://主题选股
                intent.setClass(view.getContext(), BigDataAcyivity.class);
                intent.putExtra("big_index", 1);
                view.getContext().startActivity(intent);
                break;
        }

    }

    //分割线处理
    private class GridMarketDivider extends XDividerItemDecoration {

        private GridMarketDivider(Context context) {
            super(context, 0.5f, ContextCompat.getColor(context, R.color.colorDivider_d9d9d9));
        }

        @Override
        public boolean[] getItemSidesIsHaveOffsets(int itemPosition) {
            boolean[] showDivider = new boolean[4];
            showDivider[2] = true;
            showDivider[3] = true;
            return showDivider;
        }
    }
}
