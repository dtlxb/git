package cn.gogoal.im.fragment.stock;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.activity.stock.StockNewsAllListActivity;
import cn.gogoal.im.adapter.NewsAdapter;
import cn.gogoal.im.adapter.ResearchAdapter;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.bean.StockNewsType;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.ui.NormalItemDecoration;
import cn.gogoal.im.ui.view.XLayout;
import hply.com.niugu.bean.StockDetailNewsBean;
import hply.com.niugu.bean.StockDetailNewsData;
import hply.com.niugu.bean.StockDetailResearchBean;
import hply.com.niugu.bean.StockDetailResearchData;


/**
 * author wangjd on 2017/5/3 0003.
 * Staff_id 1375
 * phone 18930640263
 * description :个股页，简短新闻、研报、公告
 */
public class StockNewsMinFragment extends BaseFragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.xLayout)
    XLayout xLayout;

    /*新闻、公告*/
    private List<StockDetailNewsData> dataListsNews;
    private NewsAdapter newsAdapter;

    /*研报*/
    private ResearchAdapter researchadapter;
    private List<StockDetailResearchData> dataListsResearch;

    //点击查看更多
    private View footView = null;

    private String stockCode;
    private String stockName;
    private int parentPosition;

    private StockNewsType stockNewsType;//实现反序列化接口，类型

    public static StockNewsMinFragment getInstance(String stockCode, String stockName, int position) {
        StockNewsMinFragment snf = new StockNewsMinFragment();
        Bundle b = new Bundle();
        b.putInt("position", position);

        StockNewsType stockNewsType = null;
        switch (position) {
            case 0:
                stockNewsType = new StockNewsType(7, "新闻", AppConst.SOURCE_TYPE_NEWS);
                break;
            case 1:
                stockNewsType = new StockNewsType(3, "公告", AppConst.SOURCE_TYPE_GONGGAO);
                break;
            case 2:
                stockNewsType = new StockNewsType(9, "研报", AppConst.SOURCE_TYPE_YANBAO);
                break;
        }
        b.putParcelable("stock_news_type", stockNewsType);
        b.putString("stock_code", stockCode);
        b.putString("stock_name", stockName);
        snf.setArguments(b);
        return snf;
    }

    @Override
    public int bindLayout() {
        return R.layout.layout_normal_list_without_refresh;
    }

    @Override
    public void doBusiness(Context mContext) {
        stockNewsType = getArguments().getParcelable("stock_news_type");

        stockName = getArguments().getString("stock_name");
        stockCode = getArguments().getString("stock_code");

        parentPosition = getArguments().getInt("position");
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new NormalItemDecoration(mContext));
        iniFootView();

        getDatas();

        ViewGroup.LayoutParams layoutParams = xLayout.getLayoutParams();
        layoutParams.height= AppDevice.dp2px(375);
        xLayout.setLayoutParams(layoutParams);

        xLayout.setEmptyText("暂无" + stockNewsType.getTitle() + "数据");
        xLayout.setOnReloadListener(new XLayout.OnReloadListener() {
            @Override
            public void onReload(View v) {
                getDatas();
            }
        });
    }

    private void getDatas() {
        if (parentPosition != 2) {
            dataListsNews = new ArrayList<>();
            newsAdapter = new NewsAdapter(dataListsNews, stockNewsType, true);
            recyclerView.setAdapter(newsAdapter);
            getNews(stockNewsType.getSource());
        } else {
            dataListsResearch = new ArrayList<>();
            researchadapter = new ResearchAdapter(dataListsResearch, stockNewsType.getNewsSource(), true);
            recyclerView.setAdapter(researchadapter);
            getYanBao();
        }
    }

    //初始化底部"查看更多按钮"
    private void iniFootView() {
        footView = LayoutInflater.from(getActivity()).inflate(
                R.layout.load_more_tv,
                new LinearLayoutCompat(getContext()),
                false);

        footView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), StockNewsAllListActivity.class);
                intent.putExtra("stockCode", stockCode);
                intent.putExtra("stockName", stockName);
                intent.putExtra("stock_news_type", stockNewsType);
                startActivity(intent);
            }
        });
    }

    void hideFootView() {
        if (parentPosition != 2) {
            newsAdapter.removeFooterView(footView);
        } else {
            researchadapter.addFooterView(footView);
        }
        footView.setVisibility(View.GONE);
    }

    void showFootView() {
        if (parentPosition != 2) {
            newsAdapter.removeAllFooterView();
            newsAdapter.addFooterView(footView);
        } else {
            researchadapter.removeAllFooterView();
            researchadapter.addFooterView(footView);
        }
        footView.setVisibility(View.VISIBLE);
    }

    /*请求新闻和公告数据*/
    private void getNews(final int type) {
        xLayout.setStatus(XLayout.Loading);
        final Map<String, String> param = new HashMap<>();
        param.put("stock_code", stockCode);
        param.put("page", "1");
        param.put("type", String.valueOf(type));
        param.put("rows", "6");

        new GGOKHTTP(param, GGOKHTTP.GET_STOCK_NEWS, new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                int code = JSONObject.parseObject(responseInfo).getIntValue("code");
                if (code == 0) {
                    dataListsNews.clear();
                    ArrayList<StockDetailNewsData> detailNewsDatas =
                            JSONObject.parseObject(responseInfo, StockDetailNewsBean.class).getData();

                    if (detailNewsDatas.size() < 6) {
                        dataListsNews.addAll(detailNewsDatas);
                        hideFootView();

                    } else {
                        dataListsNews.addAll(detailNewsDatas.subList(0, detailNewsDatas.size() - 1));
                        showFootView();
                    }

                    newsAdapter.notifyDataSetChanged();
                    if (xLayout != null)
                        xLayout.setStatus(XLayout.Success);
                } else if (code == 1001) {
                    if (xLayout != null)
                        xLayout.setStatus(XLayout.Empty);
                } else {
                    if (xLayout != null)
                        xLayout.setStatus(XLayout.Error);
                }
            }

            @Override
            public void onFailure(String msg) {
                if (xLayout != null)
                    xLayout.setStatus(XLayout.Error);

                UIHelper.toastError(getContext(), msg, xLayout);
            }
        }).startGet();
    }

    /*请求研报数据*/
    private void getYanBao() {
        xLayout.setStatus(XLayout.Loading);
        final Map<String, String> param = new HashMap<>();
        param.put("summary_auth", "1");
        param.put("stock_code", stockCode);
        param.put("token", UserUtils.getToken());
        param.put("first_class", "公司报告");
        param.put("page", "1");
        param.put("rows", "6");

        new GGOKHTTP(param, GGOKHTTP.REPORT_LIST, new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                int code = JSONObject.parseObject(responseInfo).getIntValue("code");
                if (code == 0) {
                    ArrayList<StockDetailResearchData> detailResearchDatas = JSONObject.parseObject(
                            responseInfo, StockDetailResearchBean.class).getData();

                    if (detailResearchDatas.size() < 6) {
                        hideFootView();
                        dataListsResearch.addAll(detailResearchDatas);
                    } else {
                        showFootView();
                        dataListsResearch.addAll(detailResearchDatas.subList(0, detailResearchDatas.size() - 1));
                    }
                    researchadapter.notifyDataSetChanged();
                    xLayout.setStatus(XLayout.Success);
                } else if (code == 1001) {
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
}
