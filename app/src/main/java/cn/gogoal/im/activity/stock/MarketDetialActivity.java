package cn.gogoal.im.activity.stock;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.bean.stock.HotIndustryBean;
import cn.gogoal.im.bean.stock.RankListStockBean;
import cn.gogoal.im.bean.stock.StockMarketBean;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.StockUtils;
import cn.gogoal.im.common.StringUtils;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.ui.view.XLayout;

import static cn.gogoal.im.common.AppConst.REFRESH_TYPE_AUTO;
import static cn.gogoal.im.common.AppConst.REFRESH_TYPE_SWIPEREFRESH;

/**
 * author wangjd on 2017/4/6 0006.
 * Staff_id 1375
 * phone 18930640263
 * description :行情各模块详情.
 */
public class MarketDetialActivity extends BaseActivity {

    public static final int MODULE_TYPE_TITLE_HOT_INDUSTRY = 0x6001;

    public static final int MODULE_TYPE_HOT_INDUSTRY = 0x6002;

    public static final int MODULE_TYPE_TTILE_RANK_LIST = 0x6003;

    private int refreshType = REFRESH_TYPE_AUTO;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.swiperefreshlayout)
    SwipeRefreshLayout swiperefreshlayout;

    @BindView(R.id.xLayout)
    XLayout xLayout;

    @BindView(R.id.tv_header_group)
    TextView tvHeaderGroup;

    @BindView(R.id.tv_header_value)
    TextView tvHeaderValue;

    @BindView(R.id.tv_header_type)
    TextView tvHeaderType;

    private int moduleType;

    private RankAdapter rankAdapter;

    private List<StockMarketBean.DataBean.StockRanklistBean.StockRankBean> rankDatas = new ArrayList<>();

    private IndustryAdapter hotIndustryAdapter;

    private List<HotIndustryBean.DataBean> hotIndustryDatas = new ArrayList<>();

    private int listType;//[涨跌换振]

    @Override
    public int bindLayout() {
        return R.layout.activity_market_module_detail;
    }

    @Override
    public void doBusiness(Context mContext) {
        initRecycleView(recyclerView, R.drawable.shape_divider_1px);
        iniRefresh(swiperefreshlayout);

        setMyTitle(getIntent().getStringExtra("MODULE_TITLE"), true);

        moduleType = getIntent().getIntExtra("MODULE_TYPE", 0);

        switch (moduleType) {
            case MODULE_TYPE_TITLE_HOT_INDUSTRY:
                getHotIndustryList(null);
                hotIndustryAdapter = new IndustryAdapter(hotIndustryDatas);
                recyclerView.setAdapter(hotIndustryAdapter);
                break;
            case MODULE_TYPE_HOT_INDUSTRY:
                getHotIndustryList(getIntent().getStringExtra("INDUSTRY_NAME"));
                hotIndustryAdapter = new IndustryAdapter(hotIndustryDatas);
                recyclerView.setAdapter(hotIndustryAdapter);
                break;
            case MODULE_TYPE_TTILE_RANK_LIST:
                listType = getIntent().getIntExtra("RANK_LIST_TYPE", -1);
                getRanKList();
                rankAdapter = new RankAdapter(rankDatas);
                recyclerView.setAdapter(rankAdapter);
                break;
        }

        iniHead();//详情头部

        swiperefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshType = REFRESH_TYPE_SWIPEREFRESH;
                switch (moduleType) {
                    case MODULE_TYPE_TITLE_HOT_INDUSTRY:
                        getHotIndustryList(null);
                        break;
                    case MODULE_TYPE_HOT_INDUSTRY:
                        getHotIndustryList(getIntent().getStringExtra("INDUSTRY_NAME"));
                        break;
                    case MODULE_TYPE_TTILE_RANK_LIST:
                        listType = getIntent().getIntExtra("RANK_LIST_TYPE", -1);
                        getRanKList();
                        break;
                }
                swiperefreshlayout.setRefreshing(false);
            }
        });

    }

    private void iniHead() {
        if (moduleType == MODULE_TYPE_TITLE_HOT_INDUSTRY) {
            tvHeaderGroup.setText("行业名称");
            tvHeaderValue.setText("涨跌幅");
            tvHeaderType.setText("领涨股");
        } else if (moduleType == MODULE_TYPE_HOT_INDUSTRY) {
            tvHeaderGroup.setText("股票名称");
            tvHeaderValue.setText("股价");
            tvHeaderType.setText("涨跌幅");
        } else {
            tvHeaderGroup.setText("股票名称");
            tvHeaderValue.setText("股价");
            switch (listType) {
                case 0:
                    tvHeaderType.setText("涨幅");
                    break;
                case 1:
                    tvHeaderType.setText("跌幅");
                    break;
                case 2:
                    tvHeaderType.setText("换手率");
                    break;
                case 3:
                    tvHeaderType.setText("振幅榜");
                    break;
            }
        }
    }

    /**
     * 获取 热门行业、行业子模块
     */
    private void getHotIndustryList(String industryName) {
        if (refreshType!=REFRESH_TYPE_SWIPEREFRESH) {
            xLayout.setStatus(XLayout.Loading);
        }

        hotIndustryDatas.clear();

        Map<String, String> param = new HashMap<String, String>();
        param.put("category_type", "1");

        if (moduleType == MODULE_TYPE_HOT_INDUSTRY) {
            param.put("industry_name", industryName + "");
        }

        new GGOKHTTP(param,
                moduleType == MODULE_TYPE_TITLE_HOT_INDUSTRY ?
                        GGOKHTTP.GET_HOT_INDUSTRY : GGOKHTTP.GET_HOT_INDUSTRY_DETAIL_LIST, new GGOKHTTP.GGHttpInterface() {

            @Override
            public void onSuccess(String responseInfo) {
//                KLog.e(responseInfo);
                int responseCode = JSONObject.parseObject(responseInfo).getIntValue("code");

                if (responseCode == 0) {
                    List<HotIndustryBean.DataBean> datas = JSONObject.parseObject(responseInfo, HotIndustryBean.class).getData();
                    hotIndustryDatas.addAll(datas);
                    hotIndustryAdapter.notifyDataSetChanged();
                    if (refreshType==REFRESH_TYPE_SWIPEREFRESH){
                        UIHelper.toast(getActivity(),"更新数据成功");
                    }
                    xLayout.setStatus(XLayout.Success);
                } else if (responseCode == 1001) {
                    xLayout.setStatus(XLayout.Empty);
                } else {
                    xLayout.setStatus(XLayout.Error);
                }
            }

            @Override
            public void onFailure(String msg) {
                UIHelper.toastError(getActivity(), msg, xLayout);
            }
        }).startGet();
    }

    /**
     * 获取[涨跌换振]数据
     */
    private void getRanKList() {
        if (refreshType!=REFRESH_TYPE_SWIPEREFRESH) {
            xLayout.setStatus(XLayout.Loading);
        }
        Map<String, String> param = new HashMap<>();
        param.put("type", String.valueOf(listType));
//        param.put("industry_name", industryName + "");

        new GGOKHTTP(param, GGOKHTTP.STOCK_RANK_LIST, new GGOKHTTP.GGHttpInterface() {

            @Override
            public void onSuccess(String responseInfo) {

                int responseCode = JSONObject.parseObject(responseInfo).getIntValue("code");
                if (responseCode == 0) {
                    rankDatas.clear();
                    RankListStockBean rankListStockBean = JSONObject.parseObject(responseInfo, RankListStockBean.class);
                    switch (listType) {
                        case 0:
                            rankDatas.addAll(rankListStockBean.getData().getIncrease_list());
                            break;
                        case 1:
                            rankDatas.addAll(rankListStockBean.getData().getDown_list());
                            break;
                        case 2:
                            rankDatas.addAll(rankListStockBean.getData().getChange_list());
                            break;
                        case 3:
                            rankDatas.addAll(rankListStockBean.getData().getAmplitude_list());
                            break;
                    }

                    rankAdapter.notifyDataSetChanged();

                    xLayout.setStatus(XLayout.Success);
                } else if (responseCode == 1001) {
                    xLayout.setStatus(XLayout.Empty);
                } else {
                    xLayout.setStatus(XLayout.Error);
                }
            }

            @Override
            public void onFailure(String msg) {
                UIHelper.toastError(getActivity(), msg, xLayout);
            }
        }).startGet();
    }

    private class RankAdapter extends CommonAdapter<StockMarketBean.DataBean.StockRanklistBean.StockRankBean
            ,BaseViewHolder> {

        RankAdapter(List<StockMarketBean.DataBean.StockRanklistBean.StockRankBean> datas) {
            super(getActivity(), R.layout.item_stock_rank_list, datas);
        }

        @Override
        protected void convert(BaseViewHolder holder,
                               StockMarketBean.DataBean.StockRanklistBean.StockRankBean data, int position) {

            holder.setText(R.id.tv_stock_ranklist_stockName, data.getStock_name());
            holder.setText(R.id.tv_stock_ranklist_stockCode, data.getStock_code());
            holder.setText(R.id.tv_stock_ranklist_currentPrice, StringUtils.saveSignificand(data.getCurrent_price(), 2));


            switch (listType) {
                case 0:
                case 1:
                    holder.setText(R.id.tv_stock_ranklist_rate, StockUtils.plusMinus(data.getRate()));
                    holder.setTextResColor(R.id.tv_stock_ranklist_rate, StockUtils.getStockRateColor(data.getRate()));
                    break;
                case 2://换手率
                    holder.setText(R.id.tv_stock_ranklist_rate, StringUtils.saveSignificand(data.getRate() * 100, 2) + "%");
                    break;
                case 3://振幅榜
                    holder.setText(R.id.tv_stock_ranklist_rate, StringUtils.saveSignificand(data.getRate(), 2) + "%");
                    break;
            }
        }
    }

    /**
     * 热门行业大模块及子模块列表适配器
     */
    private class IndustryAdapter extends CommonAdapter<HotIndustryBean.DataBean,BaseViewHolder> {

        private List<HotIndustryBean.DataBean> datas;

        private IndustryAdapter(List<HotIndustryBean.DataBean> datas) {
            super(getActivity(), R.layout.item_stock_rank_list, datas);
            this.datas = datas;
        }

        @Override
        protected void convert(BaseViewHolder holder, final HotIndustryBean.DataBean data, int position) {

            if (moduleType == MODULE_TYPE_TITLE_HOT_INDUSTRY) {
                holder.setText(R.id.tv_stock_ranklist_stockName, data.getIndustry_name());
                holder.getView(R.id.tv_stock_ranklist_stockCode).setVisibility(View.GONE);

                TextView rateView = holder.getView(R.id.tv_stock_ranklist_currentPrice);

                rateView.setText(StockUtils.plusMinus(data.getIndustry_rate()));// TODO: 2017/4/7 0007

                holder.setText(R.id.tv_stock_ranklist_rate, data.getStock_name());

                rateView.setTextColor(getResColor(StockUtils.getStockRateColor(data.getIndustry_rate())));

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(v.getContext(), MarketDetialActivity.class);
                        intent.putExtra("MODULE_TITLE", data.getIndustry_name());
                        intent.putExtra("MODULE_TYPE", MarketDetialActivity.MODULE_TYPE_HOT_INDUSTRY);
                        intent.putExtra("INDUSTRY_NAME", data.getIndustry_name());
                        v.getContext().startActivity(intent);
                    }
                });

            } else {

                TextView rateView = holder.getView(R.id.tv_stock_ranklist_rate);

                holder.setText(R.id.tv_stock_ranklist_stockName, data.getStock_name());
                holder.setText(R.id.tv_stock_ranklist_stockCode, data.getStock_code());

                if (data.getStock_type() == 1) { //股票状态正常
                    holder.setText(R.id.tv_stock_ranklist_currentPrice, StringUtils.saveSignificand(data.getCurrent_price(), 2));
                    rateView.setText(StockUtils.plusMinus(data.getRate()));

                    rateView.setTextColor(getResColor(StockUtils.getStockRateColor(data.getRate())));

                    holder.setTextResColor(R.id.tv_stock_ranklist_currentPrice, R.color.textColor_333333);
                } else { //股票退市，停牌等异常
                    holder.setText(R.id.tv_stock_ranklist_currentPrice, "--");//价格
                    rateView.setText("--");                                     //涨跌幅
                    rateView.setTextColor(getResColor(R.color.stock_gray));
                    holder.setTextResColor(R.id.tv_stock_ranklist_currentPrice, R.color.stock_gray);
                }

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //TODO 个股
                    }
                });
            }
        }
    }

}
