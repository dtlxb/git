package cn.gogoal.im.activity.stock;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindArray;
import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.base.BaseActivity;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.bean.StockNewsType;
import cn.gogoal.im.bean.stock.MyStockTabNewsBean;
import cn.gogoal.im.bean.stock.Stock;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.CalendarUtils;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.NormalIntentUtils;
import cn.gogoal.im.common.StockUtils;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.ui.DashlineItemDivider;
import cn.gogoal.im.ui.view.XLayout;


/**
 * 作者 wangjd on 2017/3/5 0005 16:27.
 * 联系方式 18930640263 ;
 * <p>
 * 简介:自选股中的[新闻，公告,研报]
 */

public class MyStockNewsActivity extends BaseActivity {
    @BindArray(R.array.mystock_news_title)
    String[] newsTitle;

    @Override
    public int bindLayout() {
        return R.layout.activity_mystock_news;
    }

    @Override
    public void doBusiness(Context mContext) {
        //展示tab的index====0=新闻；1=公告；2=研报

        int index = getIntent().getIntExtra("showTabIndex", 0);

        setMyTitle("自选股" + newsTitle[index], true);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.layout_news_content, MyStockTabNewsFragment.getInstance(index));
        transaction.commit();
    }

    public static class MyStockTabNewsFragment extends BaseFragment {

        @BindView(R.id.recyclerView)
        RecyclerView rvNews;

        @BindView(R.id.swiperefreshlayout)
        SwipeRefreshLayout swiperefreshlayout;

        @BindView(R.id.xLayout)
        XLayout xLayout;

        private int defaultPage = 1;

        private MyStockNewsAdapter newsAdapter;

        private List<MyStockTabNewsBean> stockNewsDatas = new ArrayList<>();

        private StockNewsType stockNewsType;

        public static Fragment getInstance(int position) {
            MyStockTabNewsFragment fragment = new MyStockTabNewsFragment();
            Bundle bundle = new Bundle();
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
            bundle.putParcelable("stock_news_type", stockNewsType);
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int bindLayout() {
            return R.layout.layout_normal_list_with_refresh;
        }

        @Override
        public void doBusiness(Context mContext) {
            stockNewsType = getArguments().getParcelable("stock_news_type");

            newsAdapter = new MyStockNewsAdapter(stockNewsDatas, stockNewsType.getNewsSource());
            rvNews.addItemDecoration(new DashlineItemDivider());
            rvNews.setLayoutManager(new LinearLayoutManager(mContext));
            BaseActivity.iniRefresh(swiperefreshlayout);
            rvNews.setAdapter(newsAdapter);

            getStockNews(AppConst.REFRESH_TYPE_FIRST);

            swiperefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    getStockNews(AppConst.REFRESH_TYPE_SWIPEREFRESH);
                    defaultPage = 1;
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

        /**
         * 请求[新闻][公告]
         */
        private void getStockNews(final int refreshType) {
            newsAdapter.setEnableLoadMore(false);
            if (refreshType == AppConst.REFRESH_TYPE_FIRST) {
                xLayout.setStatus(XLayout.Loading);
            }
            new GGOKHTTP(getParams(), stockNewsType.getNewsSource() == AppConst.SOURCE_TYPE_YANBAO ?
                    GGOKHTTP.REPORT_LIST : GGOKHTTP.GET_STOCK_NEWS, new GGOKHTTP.GGHttpInterface() {

                @Override
                public void onSuccess(String responseInfo) {

                    KLog.json(responseInfo);

                    KLog.e(stockNewsType.getTitle(), responseInfo);

                    int code = JSONObject.parseObject(responseInfo).getIntValue("code");
                    if (code == 0) {
                        if (refreshType==AppConst.REFRESH_TYPE_SWIPEREFRESH){
                            stockNewsDatas.clear();
                        }
                        JSONArray jsonArray = JSONObject.parseObject(responseInfo).getJSONArray("data");
                        for (int i = 0; i < jsonArray.size(); i++) {
                            JSONObject object = (JSONObject) jsonArray.get(i);

                            if (stockNewsType.getNewsSource() == AppConst.SOURCE_TYPE_YANBAO) {
                                Stock stock = new Stock(object.getString("stock_code"),
                                        object.getString("stock_name"));
                                stock.setCurrent_price(object.getString("stock_price"));
                                stock.setStock_rate(object.getString("stock_rate"));

                                stockNewsDatas.add(new MyStockTabNewsBean(
                                        stock, object.getString("report_title"),
                                        object.getString("create_date"),
                                        object.getString("guid")));
                            } else {
                                String originLink;
                                try {
                                    originLink = object.getString("origin_link");
                                } catch (Exception e) {
                                    originLink = "";
                                }
                                Stock stock = new Stock(
                                        ((JSONObject) object.getJSONArray("stock").get(0)).getString("stock_code"),
                                        ((JSONObject) object.getJSONArray("stock").get(0)).getString("stock_name"));
                                stock.setCurrent_price(((JSONObject) object.getJSONArray("stock").get(0)).getString("stock_price"));
                                stock.setStock_rate(((JSONObject) object.getJSONArray("stock").get(0)).getString("stock_rate"));

                                stockNewsDatas.add(new MyStockTabNewsBean(
                                        stock, object.getString("title"),
                                        object.getString("date"),
                                        String.valueOf(object.getIntValue("origin_id")),
                                        originLink));
                            }
                        }
                        newsAdapter.notifyDataSetChanged();
                        newsAdapter.setEnableLoadMore(true);
                        newsAdapter.loadMoreComplete();
                        xLayout.setStatus(XLayout.Success);

                        if (refreshType == AppConst.REFRESH_TYPE_SWIPEREFRESH) {
                            UIHelper.toast(getActivity(), getString(R.string.str_refresh_ok));
                        }
                    } else {
                        xLayout.setStatus(XLayout.Error);
                    }
                }

                @Override
                public void onFailure(String msg) {
                    UIHelper.toastError(getActivity(), msg, xLayout);
                    xLayout.setOnReloadListener(new XLayout.OnReloadListener() {
                        @Override
                        public void onReload(View v) {
                            getStockNews(AppConst.REFRESH_TYPE_RELOAD);
                        }
                    });
                }
            }).startGet();
        }

        private class MyStockNewsAdapter extends CommonAdapter<MyStockTabNewsBean, BaseViewHolder> {

            private int newsSource;

            private MyStockNewsAdapter(List<MyStockTabNewsBean> datas, int newsSource) {
                super(R.layout.item_mystock_news, datas);
                this.newsSource = newsSource;
            }

            @Override
            protected void convert(BaseViewHolder holder, final MyStockTabNewsBean data, int position) {
                TextView tvNewsTitle = holder.getView(R.id.tv_mystock_news_title);
                tvNewsTitle.setText(data.getNewsTitle());
                holder.setText(R.id.tv_mystock_news_stockInfo,
                        data.getStock().getStock_name() + " (" + data.getStock().getStock_code() + ")");
                holder.setText(R.id.tv_mystock_news_date, CalendarUtils.getStringDate("yyyy-MM-dd HH:mm", data.getDate()));
                UIHelper.setRippBg(holder.itemView);

                holder.setText(R.id.tv_mystock_news_stockRate, data.getStock().getCurrent_price() + "\u3000\u3000" +
                        data.getStock().getStock_rate()+"%");

                holder.setTextColor(R.id.tv_mystock_news_stockRate,
                        getResColor(StockUtils.getStockRateColor(
                                data.getStock().getStock_rate())));

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (newsSource != AppConst.SOURCE_TYPE_GONGGAO) {
                            NormalIntentUtils.go2WebActivity(
                                    v.getContext(),
                                    AppConst.WEB_NEWS + data.getNewsId() + "?source=" + stockNewsType.getNewsSource(),
                                    null,
                                    stockNewsType.getNewsSource() == AppConst.SOURCE_TYPE_GONGGAO);
                        } else {
                            NormalIntentUtils.go2PdfDisplayActivity(
                                    v.getContext(),
                                    data.getOrigin_link(),
                                    ""
                            );
                        }
                    }
                });

            }
        }

        public HashMap<String, String> getParams() {
            final HashMap<String, String> param = new HashMap<>();
            if (stockNewsType.getNewsSource() == AppConst.SOURCE_TYPE_YANBAO) {
                param.clear();
                param.put("summary_auth", "1");
                param.put("stock_code", StockUtils.getMyStockString());
                param.put("token", UserUtils.getToken());
                param.put("first_class", "公司报告");
                param.put("page", defaultPage + "");
                param.put("rows", "10");
            } else {
                param.clear();
                param.put("stock_code", StockUtils.getMyStockString());
                param.put("type", String.valueOf(stockNewsType.getNewsType()));
                param.put("page", String.valueOf(defaultPage));
                param.put("rows", "10");
            }
            return param;
        }
    }

}
