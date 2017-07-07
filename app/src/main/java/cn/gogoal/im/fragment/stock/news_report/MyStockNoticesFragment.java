package cn.gogoal.im.fragment.stock.news_report;

import android.content.Context;
import android.os.Bundle;
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
import cn.gogoal.im.fragment.stock.news_report.bean.MyStockNoticeBean;
import cn.gogoal.im.ui.DashlineItemDivider;
import cn.gogoal.im.ui.view.XLayout;
import cn.gogoal.im.ui.widget.refresh.RefreshLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * author wangjd on 2017/6/27 0027.
 * Staff_id 1375
 * phone 18930640263
 * description :我的自选股、公告
 */
public class MyStockNoticesFragment extends BaseFragment {

    @BindView(R.id.recyclerView)
    RecyclerView rvNotices;

    @BindView(R.id.swiperefreshlayout)
    RefreshLayout swiperefreshlayout;

    @BindView(R.id.xLayout)
    XLayout xLayout;

    private int defaultPage = 1;

    private MyStockNoticeAdapter noticeAdapter;
    private List<MyStockNoticeBean.MyStockNotice> noticeDatas;

    private int groupId;

    public static MyStockNoticesFragment newInstance(int groupId) {
        MyStockNoticesFragment fragment = new MyStockNoticesFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("group_id", groupId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int bindLayout() {
        return R.layout.layout_normal_list_with_refresh;
    }

    @Override
    public void doBusiness(Context mContext) {
        groupId = getArguments().getInt("group_id");

        noticeDatas = new ArrayList<>();
        noticeAdapter = new MyStockNoticeAdapter(noticeDatas);
        rvNotices.addItemDecoration(new DashlineItemDivider());
        rvNotices.setLayoutManager(new LinearLayoutManager(mContext));
        rvNotices.setAdapter(noticeAdapter);
        xLayout.setEmptyText("暂无公告数据\n请添加自选股后重试!");

        getStockNews(AppConst.REFRESH_TYPE_FIRST);

        swiperefreshlayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                defaultPage = 1;
                getStockNews(AppConst.REFRESH_TYPE_SWIPEREFRESH);
                swiperefreshlayout.refreshComplete();
            }
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
//                return ((LinearLayoutManager) rvMyStock.getLayoutManager()).findFirstCompletelyVisibleItemPosition() == 0 ;
                return !rvNotices.canScrollVertically(-1);
            }
        });

        noticeAdapter.setOnLoadMoreListener(new CommonAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (defaultPage <= 50) {
                    defaultPage++;
                    noticeAdapter.loadMoreEnd(false);
                    getStockNews(AppConst.REFRESH_TYPE_LOAD_MORE);
                } else {
                    noticeAdapter.loadMoreEnd(true);
                    noticeAdapter.setEnableLoadMore(false);
                    UIHelper.toast(getActivity(), R.string.nomoredata_hint);

                }
                noticeAdapter.loadMoreComplete();
            }
        }, rvNotices);
    }

    /**
     * 获取公告数据
     */
    private void getStockNews(final int refreshType) {
        if (refreshType == AppConst.REFRESH_TYPE_FIRST ||
                refreshType == AppConst.REFRESH_TYPE_RELOAD) {
            xLayout.setStatus(XLayout.Loading);
        }
        noticeAdapter.setEnableLoadMore(false);

        HashMap<String, String> params = UserUtils.getTokenParams();
        if (groupId == 0) {
            xLayout.setStatus(XLayout.Error);
            return;
        }
        params.put("group_id", String.valueOf(groupId));
        params.put("page", String.valueOf(defaultPage));
        params.put("rows", "15");
        params.put("get_hq", "true");
        params.put("source", String.valueOf(StockNewsType.STOCK_INFOMATION_SOURCE_NOTICES));

        new GGOKHTTP(params, GGOKHTTP.MY_STOCK_NEWS, new GGOKHTTP.GGHttpInterface() {
            @Override
            public void onSuccess(String responseInfo) {
                int code = JSONObject.parseObject(responseInfo).getIntValue("code");
                if (code == 0) {
                    if (refreshType == AppConst.REFRESH_TYPE_SWIPEREFRESH) {
                        noticeDatas.clear();
                    }
                    List<MyStockNoticeBean.MyStockNotice> myStockNewses =
                            JSONObject.parseObject(responseInfo, MyStockNoticeBean.class).getData();

                    noticeDatas.addAll(myStockNewses);

                    noticeAdapter.notifyDataSetChanged();
                    noticeAdapter.setEnableLoadMore(true);
                    noticeAdapter.loadMoreComplete();
                    xLayout.setStatus(XLayout.Success);

                } else if (code == 1001) {
                    xLayout.setStatus(XLayout.Empty);
                } else {
                    xLayout.setStatus(XLayout.Error);
                }
            }

            @Override
            public void onFailure(String msg) {
                if (xLayout!=null) {
                    xLayout.setStatus(XLayout.Error);
                }
            }
        }).startGet();


    }

    //适配器
    private class MyStockNoticeAdapter extends
            CommonAdapter<MyStockNoticeBean.MyStockNotice, BaseViewHolder> {

        private MyStockNoticeAdapter(List<MyStockNoticeBean.MyStockNotice> data) {
            super(R.layout.item_mystock_news, data);
        }

        @Override
        protected void convert(BaseViewHolder holder, final MyStockNoticeBean.MyStockNotice data, int position) {
            holder.setText(R.id.tv_mystock_news_title,data.getTitle());
            holder.setText(R.id.tv_mystock_news_stockInfo,
                    data.getStock_name() + " (" + data.getStock_code() + ")");
            holder.setText(R.id.tv_mystock_news_date,
                    CalendarUtils.getStringDate("yyyy-MM-dd", data.getDate()));
            UIHelper.setRippBg(holder.itemView);

            holder.setText(R.id.tv_mystock_news_source,data.getOrigin());

            holder.setText(R.id.tv_mystock_news_stockRate,
                    StringUtils.save2Significand(data.getPrice()) + "\u3000\u3000" +
                            StockUtils.plusMinus(data.getChange_rate(), true));

            holder.setTextColor(R.id.tv_mystock_news_stockRate,
                    getResColor(StockUtils.getStockRateColor(
                            data.getChange_rate())));


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NormalIntentUtils.go2PdfDisplayActivity(
                            v.getContext(),
                            data.getOrigin_link(),
                            "");
                }
            });
        }
    }
}
