package cn.gogoal.im.fragment.stock;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import cn.gogoal.im.R;
import cn.gogoal.im.activity.stock.MyStockNewsActivity;
import cn.gogoal.im.adapter.baseAdapter.BaseViewHolder;
import cn.gogoal.im.adapter.baseAdapter.CommonAdapter;
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.bean.stock.MyStockTabNewsBean;
import cn.gogoal.im.common.AppDevice;
import cn.gogoal.im.common.CalendarUtils;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.StockUtils;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.ui.NormalItemDecoration;
import cn.gogoal.im.ui.view.XLayout;

import static cn.gogoal.im.R.id.tv_mystock_news_date;
import static cn.gogoal.im.R.id.tv_mystock_news_title;

/**
 * author wangjd on 2017/4/12 0012.
 * Staff_id 1375
 * phone 18930640263
 * description :个股详情中的 新闻、公共、研报 单个Tab Fragment
 */
public class StockDetailNewTab extends BaseFragment {

    @BindView(R.id.rv_news)
    RecyclerView rvNews;

    @BindView(R.id.xLayout)
    XLayout xLayout;

    private int parentIndex;
    private StockDetailNewsAdapter newsAdapter;
    private List<MyStockTabNewsBean> stockNewsDatas = new ArrayList<>();

    public static Fragment getInstance(int position) {
        StockDetailNewTab fragment = new StockDetailNewTab();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_stock_detail_newtab;
    }

    @Override
    public void doBusiness(Context mContext) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext) {
            @Override
            public boolean canScrollVertically() {
                return true;
            }
        };

        layoutManager.setSmoothScrollbarEnabled(true);
        layoutManager.setAutoMeasureEnabled(true);

        rvNews.setHasFixedSize(true);
        rvNews.setNestedScrollingEnabled(false);
        parentIndex = getArguments().getInt("position");
        newsAdapter = new StockDetailNewsAdapter(stockNewsDatas);
        rvNews.addItemDecoration(new NormalItemDecoration(mContext));
        rvNews.setLayoutManager(layoutManager);
        rvNews.setAdapter(newsAdapter);
        switch (parentIndex) {
            case 0:
                getStockNews(7);
                break;
            case 1:
                getStockNews(3);
                break;
            case 2:
                getYanBaoDatas();
                break;
        }

        TextView tvFoot = new TextView(mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                AppDevice.dp2px(getContext(), 55));
        tvFoot.setGravity(Gravity.CENTER);
        tvFoot.setText("点击查看更多");
        tvFoot.setLayoutParams(params);
        tvFoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MyStockNewsActivity.class);
                intent.putExtra("showTabIndex",parentIndex);
                intent.putExtra("news_title",getString(R.string.title_stock_news));
                startActivity(intent);
            }
        });
        newsAdapter.addFooterView(tvFoot);
    }

    /**
     * 请求[新闻][公告]
     */
    private void getStockNews(final int type) {
        stockNewsDatas.clear();
        xLayout.setStatus(XLayout.Loading);
        final Map<String, String> param = new HashMap<>();
        param.put("stock_code", StockUtils.getMyStockString());
        param.put("type", type + "");
        param.put("page", "1");
        param.put("rows", "4");

        new GGOKHTTP(param, GGOKHTTP.GET_STOCK_NEWS, new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                int code = JSONObject.parseObject(responseInfo).getIntValue("code");
                if (code == 0) {
                    JSONArray jsonArray = JSONObject.parseObject(responseInfo).getJSONArray("data");
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JSONObject object = (JSONObject) jsonArray.get(i);
                        stockNewsDatas.add(new MyStockTabNewsBean(object.getString("title"),
                                ((JSONObject) object.getJSONArray("stock").get(0)).getString("stock_code"),
                                ((JSONObject) object.getJSONArray("stock").get(0)).getString("stock_name"),
                                object.getString("date"), String.valueOf(object.getIntValue("origin_id"))));
                    }
                    newsAdapter.notifyDataSetChanged();

                    xLayout.setStatus(XLayout.Success);


                } else if (code == 1001) {
                    xLayout.setStatus(XLayout.Error);
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

    private void getYanBaoDatas() {
        final Map<String, String> param = new HashMap<>();
        param.put("summary_auth", "1");
        param.put("stock_code", StockUtils.getMyStockString());
        param.put("token", UserUtils.getToken());
        param.put("first_class", "公司报告");
        param.put("page", "1");
        param.put("rows", "4");

        final GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                int code = JSONObject.parseObject(responseInfo).getIntValue("code");
                if (code == 0) {
                    JSONArray array = JSONObject.parseObject(responseInfo).getJSONArray("data");
                    for (int i = 0; i < array.size(); i++) {
                        JSONObject object = (JSONObject) array.get(i);
                        stockNewsDatas.add(new MyStockTabNewsBean(object.getString("report_title"),
                                object.getString("stock_code"),
                                object.getString("stock_name"),
                                object.getString("create_date"),
                                object.getString("guid")));
                    }
                    newsAdapter.notifyDataSetChanged();
                    xLayout.setStatus(XLayout.Success);
                } else if (code == 1001) {

                } else {

                }
            }

            @Override
            public void onFailure(String msg) {
                UIHelper.toastError(getActivity(), msg, xLayout);
            }
        };
        new GGOKHTTP(param, GGOKHTTP.REPORT_LIST, ggHttpInterface).startGet();
    }

    private class StockDetailNewsAdapter extends CommonAdapter<MyStockTabNewsBean, BaseViewHolder> {

        private StockDetailNewsAdapter(List<MyStockTabNewsBean> data) {
            super(R.layout.item_mystock_news, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, MyStockTabNewsBean data, int position) {
            holder.getView(R.id.tv_mystock_news_stockInfo).setVisibility(View.GONE);
            holder.setText(tv_mystock_news_date, CalendarUtils.getStringDate("yyyy-MM-dd HH:mm",data.getDate()));
            holder.setText(tv_mystock_news_title, data.getNewsTitle());
        }
    }
}
