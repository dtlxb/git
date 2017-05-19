package cn.gogoal.im.activity.stock;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.NewsAdapter;
import cn.gogoal.im.adapter.ResearchAdapter;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.bean.StockNewsType;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.ui.view.XLayout;
import hply.com.niugu.bean.StockDetailNewsBean;
import hply.com.niugu.bean.StockDetailNewsData;
import hply.com.niugu.bean.StockDetailResearchBean;
import hply.com.niugu.bean.StockDetailResearchData;

/**
 * Author wangjd on 2017/5/3 0003.
 * EmployeeNumber 1375
 * Phone 18930640263
 * Description :个股 新闻、公告、研报全部列表
 */
public class StockNewsAllListActivity extends BaseActivity {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.xLayout)
    XLayout xLayout;

    private String stockName;
    private String stockCode;

    private int page = 1;

    /*新闻、公告*/
    private List<StockDetailNewsData> dataListsNews;
    private NewsAdapter newsAdapter;

    /*研报*/
    private ResearchAdapter researchadapter;
    private List<StockDetailResearchData> dataListsResearch;

    private StockNewsType stockNewsType;

    @Override
    public int bindLayout() {
        return R.layout.layout_normal_list_without_refresh;
    }

    @Override
    public void doBusiness(Context mContext) {
        stockName = getIntent().getStringExtra("stockName");
        stockCode = getIntent().getStringExtra("stockCode");

        BaseActivity.initRecycleView(recyclerView, 0);
        stockNewsType = getIntent().getParcelableExtra("stock_news_type");

        xLayout.setEmptyText("暂无相关" + stockNewsType.getTitle());

        setMyTitle(stockName + "-" + stockNewsType.getTitle(), true);

        if (stockNewsType.getNewsSource() == AppConst.SOURCE_TYPE_YANBAO) {
            dataListsResearch = new ArrayList<>();
            researchadapter = new ResearchAdapter(dataListsResearch, stockNewsType.getNewsSource(),false);
            recyclerView.setAdapter(researchadapter);
            getYanBao(false);
            researchadapter.setOnLoadMoreListener(new CommonAdapter.RequestLoadMoreListener() {
                @Override
                public void onLoadMoreRequested() {
                    if (page <= 50) {
                        page++;
                        getYanBao(true);
                    } else {
                        researchadapter.loadMoreEnd(true);
                        researchadapter.setEnableLoadMore(false);
                        UIHelper.toast(getActivity(),"没有更多数据");

                    }
                    researchadapter.loadMoreComplete();
                }
            }, recyclerView);

        } else {
            dataListsNews = new ArrayList<>();
            newsAdapter = new NewsAdapter(dataListsNews, stockNewsType,false);
            recyclerView.setAdapter(newsAdapter);
            getNews(false);
            newsAdapter.setOnLoadMoreListener(new CommonAdapter.RequestLoadMoreListener() {
                @Override
                public void onLoadMoreRequested() {
                    if (page <= 50) {
                        page++;
                        newsAdapter.loadMoreEnd(false);
                        getNews(true);
                    } else {
                        newsAdapter.loadMoreEnd(true);
                        newsAdapter.setEnableLoadMore(false);
                        UIHelper.toast(getActivity(),"没有更多数据");

                    }
                    newsAdapter.loadMoreComplete();
                }
            }, recyclerView);
        }

    }

    /*请求新闻和公告数据*/
    private void getNews(final boolean loadMore) {
        newsAdapter.setEnableLoadMore(false);
        if (!loadMore) {
            xLayout.setStatus(XLayout.Loading);
        }
        final Map<String, String> param = new HashMap<>();
        param.put("stock_code", stockCode);
        param.put("page", String.valueOf(page));
        param.put("type", String.valueOf(stockNewsType.getNewsType()));
        param.put("rows", "20");

        new GGOKHTTP(param, GGOKHTTP.GET_STOCK_NEWS, new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                if (JSONObject.parseObject(responseInfo).getIntValue("code") == 0) {
                    if (!loadMore) {
                        dataListsNews.clear();
                    }
                    dataListsNews.addAll(JSONObject.parseObject(responseInfo, StockDetailNewsBean.class).getData());

                    newsAdapter.notifyDataSetChanged();
                    newsAdapter.setEnableLoadMore(true);
                    newsAdapter.loadMoreComplete();

                    xLayout.setStatus(XLayout.Success);
                } else {
                    xLayout.setStatus(XLayout.Empty);
                }
            }

            @Override
            public void onFailure(String msg) {
                UIHelper.toastError(getActivity(), msg, xLayout);
            }
        }).startGet();
    }

    /*请求研报数据*/
    private void getYanBao(final boolean loadMore) {
        if (!loadMore) {
            xLayout.setStatus(XLayout.Loading);
        }
        researchadapter.setEnableLoadMore(false);
        final Map<String, String> param = new HashMap<>();
        param.put("summary_auth", "1");
        param.put("stock_code", stockCode);
        param.put("token", UserUtils.getToken());
        param.put("first_class", "公司报告");
        param.put("page", "1");
        param.put("rows", "20");

        new GGOKHTTP(param, GGOKHTTP.REPORT_LIST, new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                if (JSONObject.parseObject(responseInfo).getIntValue("code") == 0) {
                    if (!loadMore) {
                        dataListsResearch.clear();
                    }
                    dataListsResearch.addAll(
                            JSONObject.parseObject(
                                    responseInfo, StockDetailResearchBean.class).getData());
                    researchadapter.notifyDataSetChanged();
                    researchadapter.setEnableLoadMore(true);
                    researchadapter.loadMoreComplete();
                    xLayout.setStatus(XLayout.Success);
                } else {
                    xLayout.setStatus(XLayout.Empty);
                }
            }

            @Override
            public void onFailure(String msg) {
                UIHelper.toastError(getActivity(), msg, xLayout);
            }
        }).startGet();
    }

}
