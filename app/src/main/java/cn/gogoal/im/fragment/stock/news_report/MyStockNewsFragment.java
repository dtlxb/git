package cn.gogoal.im.fragment.stock.news_report;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.bean.StockNewsType;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.CalendarUtils;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.NormalIntentUtils;
import cn.gogoal.im.common.StockUtils;
import cn.gogoal.im.common.StringUtils;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.fragment.stock.news_report.bean.MyStockNewsBean;
import cn.gogoal.im.ui.DashlineItemDivider;
import cn.gogoal.im.ui.view.XLayout;

/**
 * author wangjd on 2017/6/27 0027.
 * Staff_id 1375
 * phone 18930640263
 * description :我的自选股、新闻
 */
public class MyStockNewsFragment extends BaseFragment {
    @BindView(R.id.recyclerView)
    RecyclerView rvNews;

    @BindView(R.id.swiperefreshlayout)
    SwipeRefreshLayout swiperefreshlayout;

    @BindView(R.id.xLayout)
    XLayout xLayout;

    private int defaultPage = 1;

    private MyStockNewsAdapter newsAdapter;
    private List<MyStockNewsBean.MyStockNews> stockNewsDatas;

    private int groupId;

    @Override
    public int bindLayout() {
        return R.layout.layout_normal_list_with_refresh;
    }

    public static MyStockNewsFragment newInstance(int groupId) {
        MyStockNewsFragment fragment = new MyStockNewsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("group_id", groupId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void doBusiness(Context mContext) {
        groupId = getArguments().getInt("group_id");
        stockNewsDatas = new ArrayList<>();
        newsAdapter = new MyStockNewsAdapter(stockNewsDatas);
        rvNews.addItemDecoration(new DashlineItemDivider());
        rvNews.setLayoutManager(new LinearLayoutManager(mContext));
        BaseActivity.iniRefresh(swiperefreshlayout);
        rvNews.setAdapter(newsAdapter);

        getStockNews(AppConst.REFRESH_TYPE_FIRST);

        swiperefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                defaultPage = 1;
                getStockNews(AppConst.REFRESH_TYPE_SWIPEREFRESH);
                swiperefreshlayout.setRefreshing(false);
            }
        });

        newsAdapter.setOnLoadMoreListener(new CommonAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (defaultPage <= 50) {
                    defaultPage++;
                    newsAdapter.loadMoreEnd(false);
                    getStockNews(AppConst.REFRESH_TYPE_LOAD_MORE);
                } else {
                    newsAdapter.loadMoreEnd(true);
                    newsAdapter.setEnableLoadMore(false);
                    UIHelper.toast(getActivity(), R.string.nomoredata_hint);

                }
                newsAdapter.loadMoreComplete();
            }
        }, rvNews);
    }

    private void getStockNews(final int refreshType) {
        if (refreshType == AppConst.REFRESH_TYPE_FIRST ||
                refreshType == AppConst.REFRESH_TYPE_RELOAD) {
            xLayout.setStatus(XLayout.Loading);
        }
        newsAdapter.setEnableLoadMore(false);

        HashMap<String, String> params = UserUtils.getTokenParams();
        if (groupId == 0) {
            xLayout.setStatus(XLayout.Error);
            return;
        }
        params.put("group_id", String.valueOf(groupId));
        params.put("page", String.valueOf(defaultPage));
        params.put("rows", "15");
        params.put("source", String.valueOf(StockNewsType.STOCK_INFOMATION_SOURCE_NEWS));

        new GGOKHTTP(params, GGOKHTTP.MY_STOCK_NEWS, new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                int code = JSONObject.parseObject(responseInfo).getIntValue("code");
                if (code == 0) {
                    if (refreshType == AppConst.REFRESH_TYPE_SWIPEREFRESH) {
                        stockNewsDatas.clear();
                    }
                    List<MyStockNewsBean.MyStockNews> myStockNewses =
                            JSONObject.parseObject(responseInfo, MyStockNewsBean.class).getData();

                    stockNewsDatas.addAll(myStockNewses);

                    newsAdapter.notifyDataSetChanged();
                    newsAdapter.setEnableLoadMore(true);
                    newsAdapter.loadMoreComplete();
                    xLayout.setStatus(XLayout.Success);

                } else if (code == 1001) {
                    xLayout.setStatus(XLayout.Empty);
                } else {
                    xLayout.setStatus(XLayout.Error);
                }
            }

            @Override
            public void onFailure(String msg) {
                if (xLayout!=null)
                xLayout.setStatus(XLayout.Error);
            }
        }).startGet();


    }

    private class MyStockNewsAdapter extends CommonAdapter<MyStockNewsBean.MyStockNews, BaseViewHolder> {

        private MyStockNewsAdapter(List<MyStockNewsBean.MyStockNews> data) {
            super(R.layout.item_mystock_news, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, final MyStockNewsBean.MyStockNews data, int position) {
            holder.setText(R.id.tv_mystock_news_title,data.getTitle());
            holder.setText(R.id.tv_mystock_news_stockInfo,
                    data.getStock_name() + " (" + data.getStock_code() + ")");
            holder.setText(R.id.tv_mystock_news_date, CalendarUtils.getStringDate("yyyy-MM-dd HH:mm", data.getDate()));
            UIHelper.setRippBg(holder.itemView);

            holder.setText(R.id.tv_mystock_news_stockRate,
                    StringUtils.save2Significand(data.getPrice()) + "\u3000\u3000" +
                            StockUtils.plusMinus(data.getChange_rate(), true));

            holder.setTextColor(R.id.tv_mystock_news_stockRate,
                    getResColor(StockUtils.getStockRateColor(
                            data.getChange_rate())));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NormalIntentUtils.go2WebActivity(
                            v.getContext(),
                            AppConst.WEB_NEWS + data.getOrigin_id() + "?source=7",
                            null,
                            true);
                }
            });
        }
    }
}
