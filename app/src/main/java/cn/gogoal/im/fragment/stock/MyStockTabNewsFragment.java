package cn.gogoal.im.fragment.stock;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
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
import cn.gogoal.im.base.BaseFragment;
import cn.gogoal.im.bean.stock.MyStockTabBean;
import cn.gogoal.im.common.AppConst;
import cn.gogoal.im.common.GGOKHTTP.GGOKHTTP;
import cn.gogoal.im.common.StockUtils;
import cn.gogoal.im.common.UIHelper;
import cn.gogoal.im.common.UserUtils;
import cn.gogoal.im.ui.NormalItemDecoration;
import cn.gogoal.im.ui.view.XLayout;

/**
 * Author wangjd on 2017/4/9 0009.
 * EmployeeNumber 1375
 * Phone 18930640263
 * Description :==自选股中的[新闻，公告,研报]每个Tab==
 */
public class MyStockTabNewsFragment extends BaseFragment {

    @BindView(R.id.rv_news)
    RecyclerView rvNews;

    @BindView(R.id.swiperefreshlayout)
    SwipeRefreshLayout swiperefreshlayout;

    @BindView(R.id.xLayout)
    XLayout xLayout;

    private int refreshType = AppConst.REFRESH_TYPE_AUTO;

    private int parentIndex;

    private int defaultPage = 1;

    private static final int TYPE_NEWS = 7;
    private static final int TYPE_GONGGAO = 3;

    private MyStockNewsAdapter newsAdapter;

    private List<MyStockTabBean> stockNewsDatas = new ArrayList<>();

    public static Fragment getInstance(int position) {
        MyStockTabNewsFragment fragment = new MyStockTabNewsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int bindLayout() {
        return R.layout.fragment_mystock_news_tab;
    }

    @Override
    public void doBusiness(Context mContext) {
        parentIndex = getArguments().getInt("position");
        newsAdapter = new MyStockNewsAdapter(stockNewsDatas);
        rvNews.addItemDecoration(new NormalItemDecoration(mContext));
        rvNews.setLayoutManager(new LinearLayoutManager(mContext));
        BaseActivity.iniRefresh(swiperefreshlayout);
        rvNews.setAdapter(newsAdapter);
        switch (parentIndex) {
            case 0:
                getStockNews(TYPE_NEWS, defaultPage);
                break;
            case 1:
                getStockNews(TYPE_GONGGAO, defaultPage);
                break;
            case 2:
                getYanBaoDatas(defaultPage);
                break;
        }

        swiperefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshType = AppConst.REFRESH_TYPE_SWIPEREFRESH;
                switch (parentIndex) {
                    case 0:
                        getStockNews(TYPE_NEWS, defaultPage);
                        break;
                    case 1:
                        getStockNews(TYPE_GONGGAO, defaultPage);
                        break;
                    case 2:
                        getYanBaoDatas(defaultPage);
                        break;
                }
            }
        });
    }

    /**
     * 请求[新闻][公告]
     */
    private void getStockNews(int type, final int page) {
        stockNewsDatas.clear();
        if (refreshType != AppConst.REFRESH_TYPE_SWIPEREFRESH) {
            xLayout.setStatus(XLayout.Loading);
        }
        final Map<String, String> param = new HashMap<>();
        param.put("stock_code", StockUtils.getMyStockString());
        param.put("type", type + "");
        param.put("page", page + "");
        param.put("rows", "10");

        new GGOKHTTP(param, GGOKHTTP.GET_STOCK_NEWS, new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                int code = JSONObject.parseObject(responseInfo).getIntValue("code");
                if (code == 0) {
                    JSONArray jsonArray = JSONObject.parseObject(responseInfo).getJSONArray("data");
                    for (int i=0;i<jsonArray.size();i++){
                        JSONObject object= (JSONObject) jsonArray.get(i);
                        stockNewsDatas.add(new MyStockTabBean(object.getString("title"),
                                ((JSONObject)object.getJSONArray("stock").get(0)).getString("stock_code"),
                                ((JSONObject)object.getJSONArray("stock").get(0)).getString("stock_name"),
                                object.getString("date"),String.valueOf(object.getIntValue("origin_id"))));
                    }
                    newsAdapter.notifyDataSetChanged();
                    swiperefreshlayout.setRefreshing(false);
                    xLayout.setStatus(XLayout.Success);

                    if (refreshType==AppConst.REFRESH_TYPE_SWIPEREFRESH) {
                        UIHelper.toast(getActivity(),getString(R.string.str_refresh_ok));
                    }
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

    private void getYanBaoDatas(final int page) {
        final Map<String, String> param = new HashMap<>();
        param.put("summary_auth", "1");
        param.put("stock_code",StockUtils.getMyStockString());
        param.put("token", UserUtils.getToken());
        param.put("first_class", "公司报告");
        param.put("page", page + "");
        param.put("rows", "10");

        final GGOKHTTP.GGHttpInterface ggHttpInterface = new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                int code=JSONObject.parseObject(responseInfo).getIntValue("code");
                if (code==0){
                    JSONArray array = JSONObject.parseObject(responseInfo).getJSONArray("data");
                    for (int i=0;i<array.size();i++){
                        JSONObject object= (JSONObject) array.get(i);
                          stockNewsDatas.add(new MyStockTabBean(object.getString("report_title"),
                                  object.getString("stock_code"),
                                  object.getString("stock_name"),
                                  object.getString("create_date"),
                                  object.getString("guid")));
                    }
                    newsAdapter.notifyDataSetChanged();
                    swiperefreshlayout.setRefreshing(false);
                    xLayout.setStatus(XLayout.Success);
                    if (refreshType==AppConst.REFRESH_TYPE_SWIPEREFRESH) {
                        UIHelper.toast(getActivity(),"更新数据成功");
                    }
                }else if (code==1001){

                }else {

                }
            }
            @Override
            public void onFailure(String msg) {
                UIHelper.toastError(getActivity(),msg,xLayout);
            }
        };
        new GGOKHTTP(param, GGOKHTTP.REPORT_LIST, ggHttpInterface).startGet();
    }

    private class MyStockNewsAdapter extends CommonAdapter<MyStockTabBean,BaseViewHolder> {

        private MyStockNewsAdapter(List<MyStockTabBean> datas) {
            super(getActivity(), R.layout.item_mystock_news, datas);
        }

        @Override
        protected void convert(BaseViewHolder holder, MyStockTabBean data, int position) {
            TextView tvNewsTitle = holder.getView(R.id.tv_mystock_news_title);
            if (parentIndex != 2) {
                tvNewsTitle.setMaxLines(1);
                tvNewsTitle.setEllipsize(TextUtils.TruncateAt.END);
            } else {
                tvNewsTitle.setMaxLines(2);
                tvNewsTitle.setEllipsize(TextUtils.TruncateAt.END);
            }
            tvNewsTitle.setText(data.getNewsTitle());
            holder.setText(R.id.tv_mystock_news_stockInfo,
                    data.getStockName() + " (" + data.getStockCode() + ")");
            holder.setText(R.id.tv_mystock_news_date, data.getDate());
            UIHelper.setRippBg(holder.itemView);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO 跳转 新闻页
                }
            });
        }
    }
}
